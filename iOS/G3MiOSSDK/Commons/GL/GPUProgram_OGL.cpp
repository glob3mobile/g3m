//
//  GPUProgram_OGL.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 21/07/14.
//
//

#include "GPUProgram_OGL.hpp"

#include "GL.hpp"
#include "GPUAttribute.hpp"
#include "GPUUniform.hpp"
#include "GPUProgramManager.hpp"

GPUProgram_OGL* GPUProgram_OGL::createProgram(GL* gl,
	const std::string& name,
	const std::string& vertexSource,
	const std::string& fragmentSource) {

	GPUProgram_OGL* p = new GPUProgram_OGL();

	p->_name = name;
	p->_programID = gl->createProgram();
	p->_gl = gl;

	// compile vertex shader
	int vertexShader = gl->createShader(VERTEX_SHADER);
	if (!p->compileShader(gl, vertexShader, vertexSource)) {
		ILogger::instance()->logError("GPUProgram: ERROR compiling vertex shader :\n %s\n", vertexSource.c_str());
		gl->printShaderInfoLog(vertexShader);

		p->deleteShader(gl, vertexShader);
		p->deleteProgram(gl, p);
		return NULL;
	}

	//  ILogger::instance()->logInfo("VERTEX SOURCE: \n %s", vertexSource.c_str());

	// compile fragment shader
	int fragmentShader = gl->createShader(FRAGMENT_SHADER);
	if (!p->compileShader(gl, fragmentShader, fragmentSource)) {
		ILogger::instance()->logError("GPUProgram: ERROR compiling fragment shader :\n %s\n", fragmentSource.c_str());
		gl->printShaderInfoLog(fragmentShader);

		p->deleteShader(gl, fragmentShader);
		p->deleteProgram(gl, p);
		return NULL;
	}

	//  ILogger::instance()->logInfo("FRAGMENT SOURCE: \n %s", fragmentSource.c_str());

	//gl->bindAttribLocation(p, 0, POSITION);

	// link program
	if (!p->linkProgram(gl)) {
		ILogger::instance()->logError("GPUProgram: ERROR linking graphic program\n");
		p->deleteShader(gl, vertexShader);
		p->deleteShader(gl, fragmentShader);
		p->deleteProgram(gl, p);
		ILogger::instance()->logError("GPUProgram: ERROR linking graphic program");
		return NULL;
	}

	//Mark shaders for deleting when program is deleted
	p->deleteShader(gl, vertexShader);
	p->deleteShader(gl, fragmentShader);

	p->getVariables(gl);

	if (gl->getError() != GLError::noError()) {
		ILogger::instance()->logError("Error while compiling program");
	}

	return p;
}

void GPUProgram_OGL::onUnused(GL* gl) {
	//ILogger::instance()->logInfo("GPUProgram %s unused", _name.c_str());

	for (int i = 0; i < _nUniforms; i++) {
		if (_createdUniforms[i] != NULL) { //Texture Samplers return null
			_createdUniforms[i]->unset();
		}
	}

	for (int i = 0; i < _nAttributes; i++) {
		if (_createdAttributes[i] != NULL) {
			_createdAttributes[i]->unset(gl);
		}
	}
}

/**
Must be called before drawing to apply Uniforms and Attributes new values
*/
void GPUProgram_OGL::applyChanges(GL* gl) {

	for (int i = 0; i < _nUniforms; i++) {
		GPUUniform* uniform = _createdUniforms[i];
		if (uniform != NULL) { //Texture Samplers return null
			uniform->applyChanges(gl);
		}
	}

	for (int i = 0; i < _nAttributes; i++) {
		GPUAttribute* attribute = _createdAttributes[i];
		if (attribute != NULL) {
			attribute->applyChanges(gl);
		}
	}
}

bool GPUProgram_OGL::compileShader(GL* gl, int shader, const std::string& source) const{
	bool result = gl->compileShader(shader, source);

	//#if defined(DEBUG)
	//  _nativeGL->printShaderInfoLog(shader);
	//#endif

	if (result) {
		gl->attachShader(_programID, shader);
	}
	else{
		ILogger::instance()->logError("GPUProgram: Problem encountered while compiling shader.");
	}

	return result;
}

bool GPUProgram_OGL::linkProgram(GL* gl) const {
	bool result = gl->linkProgram(_programID);
	//#if defined(DEBUG)
	//  _nativeGL->printProgramInfoLog(_programID);
	//#endif
	return result;
}

void GPUProgram_OGL::deleteShader(GL* gl, int shader) const{
	if (!gl->deleteShader(shader)) {
		ILogger::instance()->logError("GPUProgram: Problem encountered while deleting shader.");
	}
}

void GPUProgram_OGL::deleteProgram(GL* gl, const IGPUProgram* p) {
	if (!gl->deleteProgram(p)) {
		ILogger::instance()->logError("GPUProgram: Problem encountered while deleting program.");
	}
}

GPUProgram_OGL::~GPUProgram_OGL() {

	//ILogger::instance()->logInfo("Deleting program %s", _name.c_str());

	//  if (_manager != NULL) {
	//    _manager->compiledProgramDeleted(this->_name);
	//  }

	for (int i = 0; i < _nUniforms; i++) {
		delete _createdUniforms[i];
	}

	for (int i = 0; i < _nAttributes; i++) {
		delete _createdAttributes[i];
	}

	delete[] _createdAttributes;
	delete[] _createdUniforms;

	if (!_gl->deleteProgram(this)) {
		ILogger::instance()->logError("GPUProgram: Problem encountered while deleting program.");
	}
}

void GPUProgram_OGL::getVariables(GL* gl) {

	for (int i = 0; i < 32; i++) {
		_uniforms[i] = NULL;
		_attributes[i] = NULL;
	}

	//Uniforms
	_uniformsCode = 0;
	_nUniforms = gl->getProgramiv(this, GLVariable::activeUniforms());

	int counter = 0;
	_createdUniforms = new GPUUniform*[_nUniforms];

	for (int i = 0; i < _nUniforms; i++) {
		GPUUniform* u = gl->getActiveUniform(this, i);
		if (u != NULL) {
			_uniforms[u->getIndex()] = u;

			const int code = GPUVariable::getUniformCode(u->_key);
			_uniformsCode = _uniformsCode | code;
		}

		_createdUniforms[counter++] = u; //Adding to created uniforms array
	}

	//Attributes
	_attributesCode = 0;
	_nAttributes = gl->getProgramiv(this, GLVariable::activeAttributes());

	counter = 0;
	_createdAttributes = new GPUAttribute*[_nAttributes];

	for (int i = 0; i < _nAttributes; i++) {
		GPUAttribute* a = gl->getActiveAttribute(this, i);
		if (a != NULL) {
			_attributes[a->getIndex()] = a;

			const int code = GPUVariable::getAttributeCode(a->_key);
			_attributesCode = _attributesCode | code;
		}

		_createdAttributes[counter++] = a;
	}

	//ILogger::instance()->logInfo("Program with Uniforms Bitcode: %d and Attributes Bitcode: %d", _uniformsCode, _attributesCode);
}