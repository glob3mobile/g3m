//
//  GPUProgram_D3D.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 21/07/14.
//
//
#include <fstream>
#include <D3D11Shader.h>
#include <D3Dcompiler.h>

#include "GPUProgram_D3D.hpp"
#include "GLUniformID_win8.hpp"
#include "ILogger.hpp"
#include "GPUUniform.hpp"
#include "GPUAttribute.hpp""


GPUProgram_D3D::GPUProgram_D3D(GL* gl,
	const std::string& name,
	NativeGL_win8* ngl){

	m["BINORMAL"] = FLOAT4;
	m["BLENDWEIGHT"] = FLOAT1;
	m["COLOR"] = FLOAT4;
	m["NORMAL"] = FLOAT4;
	m["POSITION"] = FLOAT4;
	m["POSITIONT"] = FLOAT4;
	m["PSIZE"] = FLOAT1;
	m["TANGENT"] = FLOAT4;
	m["TEXCOORD"] = FLOAT4;

	_name = name;
	_gl = gl;
	_ngl = ngl;

	HRESULT hr;
	//Load pre-compiled vertex-Shader file
	std::string vertexShaderName = _name + "V.cso";
	_vsData = loadShaderFile(vertexShaderName);
	hr=_ngl->getDevice()->CreateVertexShader(_vsData->Data, _vsData->Length, nullptr, &_vshader);
	if (FAILED(hr)){
		ILogger::instance()->logError("Error while creating Vertex shader " + vertexShaderName);
		std::string errMsg("Error while creating Vertex shader");
		throw std::exception(errMsg.c_str());
	}

	//Load pre-compiled pixel shader file
	std::string pixelShaderName = name + "P.cso";
	_psData = loadShaderFile(pixelShaderName);
	hr=_ngl->getDevice()->CreatePixelShader(_psData->Data, _psData->Length, nullptr, &_pshader);
	if (FAILED(hr)){
		ILogger::instance()->logError("Error while creating Pixel shader " + pixelShaderName);
		std::string errMsg("Error while creating Pixel shader");
		throw std::exception(errMsg.c_str());
	}

	getVariables(gl);


}

GPUProgram_D3D::~GPUProgram_D3D(){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void GPUProgram_D3D::getVariables(GL* gl){
	getShaderUniforms(gl);
	getShaderAttributes(gl);
}

void GPUProgram_D3D::getShaderAttributes(GL* gl){
	for (int i = 0; i < 32; i++) {
		_attributes[i] = NULL;
	}

	//Vertex shader attributes
	_attributesCode = 0;
	HRESULT hr = S_OK;
	ID3D11ShaderReflection* vsReflector = NULL;
	hr = D3DReflect(_vsData->Data, _vsData->Length, IID_ID3D11ShaderReflection, (void**)&vsReflector);
	if (FAILED(hr)){
		ILogger::instance()->logError("Error while calling D3DReflect");
		std::string errMsg("Error while calling D3DReflect");
		throw std::exception(errMsg.c_str());
	}
	D3D11_SHADER_DESC vsdesc;
	vsReflector->GetDesc(&vsdesc);

	int _nAttributes = vsdesc.InputParameters;
	int counter = 0;
	GPUAttribute** _createdAttributes = new GPUAttribute*[_nAttributes];
	ILogger::instance()->logInfo("--Vertex Shader has %i Attributes.", _nAttributes);
	for (int i = 0; i < _nAttributes; i++) {
		D3D11_SIGNATURE_PARAMETER_DESC input_desc;  
		vsReflector->GetInputParameterDesc(i, &input_desc);
		std::string iname = input_desc.SemanticName;
		ILogger::instance()->logInfo("InputAttributeName: " + iname);

		GPUAttribute* a = createAttribute(iname);
		if (a != NULL) {
			_attributes[a->getIndex()] = a;
			const int code = GPUVariable::getAttributeCode(a->_key);
			_attributesCode = _attributesCode | code;
		}

		_createdAttributes[counter++] = a;
	}


	//Pixel Shader attributes
	/*ID3D11ShaderReflection* psReflector = NULL;
	hr = D3DReflect(_psData->Data, _psData->Length, IID_ID3D11ShaderReflection, (void**)&psReflector);
	if (FAILED(hr)){
		ILogger::instance()->logError("Error while calling D3DReflect");
		std::string errMsg("Error while calling D3DReflect");
		throw std::exception(errMsg.c_str());
	}
	D3D11_SHADER_DESC psdesc;
	psReflector->GetDesc(&psdesc);
	int _npsAttributes = psdesc.InputParameters;
	counter = 0;
	GPUAttribute** _createdPSAttributes = new GPUAttribute*[_npsAttributes];
	ILogger::instance()->logInfo("--Pixel Shader has %i Attributes.", _npsAttributes);
	for (int i = 0; i < _npsAttributes; i++) {
		D3D11_SIGNATURE_PARAMETER_DESC input_desc;
		psReflector->GetInputParameterDesc(i, &input_desc);
		std::string name = input_desc.SemanticName;
		ILogger::instance()->logInfo("InputAttributeName: " + name);
	}*/

}

void GPUProgram_D3D::getShaderUniforms(GL* gl){
	
	for (int i = 0; i < 32; i++) {
		_uniforms[i] = NULL;
	}

	//Uniforms -> constant buffers
	//1. Vertex Shader Uniforms
	_uniformsCode = 0;
	HRESULT hr = S_OK;
	ID3D11ShaderReflection* vsReflector = NULL;
	hr = D3DReflect(_vsData->Data, _vsData->Length, IID_ID3D11ShaderReflection, (void**)&vsReflector);
	if (FAILED(hr)){
		ILogger::instance()->logError("Error while calling D3DReflect");
		std::string errMsg("Error while calling D3DReflect");
		throw std::exception(errMsg.c_str());
	}
	D3D11_SHADER_DESC vsdesc;
	vsReflector->GetDesc(&vsdesc);

	int _nvsUniforms = vsdesc.ConstantBuffers;
	int counter = 0;
	GPUUniform** _createdVSUniforms = new GPUUniform*[_nvsUniforms];
	ILogger::instance()->logInfo("--Vertex Shader has %i Uniforms.", _nvsUniforms);
	for (int i = 0; i < _nvsUniforms; i++) {
		//get the cbuffer i
		ID3D11ShaderReflectionConstantBuffer* constBuffer = vsReflector->GetConstantBufferByIndex(i);
		//get its description
		D3D11_SHADER_BUFFER_DESC cbufferdesc;
		constBuffer->GetDesc(&cbufferdesc);
		std::string name = cbufferdesc.Name;
		ILogger::instance()->logInfo("Name of the constant buffer: " + name);
		for (UINT j = 0; j < cbufferdesc.Variables; j++){

			//Extract variable (and corresponding _desc) from cBuffer
			ID3D11ShaderReflectionVariable* cbufferVariable = constBuffer->GetVariableByIndex(j);
			D3D11_SHADER_VARIABLE_DESC cbufferVariable_desc;
			cbufferVariable->GetDesc(&cbufferVariable_desc);


			ID3D11ShaderReflectionType* pType = cbufferVariable->GetType();
			D3D11_SHADER_TYPE_DESC type_desc;   
			pType->GetDesc(&type_desc);

			//get the name from the variableDesc
			std::string vname = cbufferVariable_desc.Name;
			ILogger::instance()->logInfo("Name of the variable: " + vname);
			int vsize = cbufferVariable_desc.Size;
			ILogger::instance()->logInfo("Size of the variable: %i", vsize);

			//get the type and class
			int type = type_desc.Type;
			ILogger::instance()->logInfo("Type of the variable: %i", type);
			int claas = type_desc.Class;
			ILogger::instance()->logInfo("Class of the variable: %i", claas);
			int members = type_desc.Members;
			ILogger::instance()->logInfo("Members of the variable: %i", members);

			//GPUUniform* u = createUniform(name, vsize, type_desc.Class, type_desc.Type,  i);
			GPUUniform* u = createUniform(vname, vsize, type_desc.Class, type_desc.Type, i);
			if (u != NULL) {
				_uniforms[u->getIndex()] = u;

				const int code = GPUVariable::getUniformCode(u->_key);
				_uniformsCode = _uniformsCode | code;
			}
			_createdVSUniforms[counter++] = u; //Adding to created uniforms array
		}
	}

	//2. Pixel Shader Uniforms
	ID3D11ShaderReflection* psReflector = NULL;
	hr = D3DReflect(_psData->Data, _psData->Length, IID_ID3D11ShaderReflection, (void**)&psReflector);
	if (FAILED(hr)){
		ILogger::instance()->logError("Error while calling D3DReflect");
		std::string errMsg("Error while calling D3DReflect");
		throw std::exception(errMsg.c_str());
	}
	D3D11_SHADER_DESC psdesc;
	psReflector->GetDesc(&psdesc);

	int _npsUniforms = psdesc.ConstantBuffers;
	counter = 0;
	GPUUniform** _createdPSUniforms = new GPUUniform*[_npsUniforms];
	ILogger::instance()->logInfo("--Pixel Shader has %i Uniforms.", _npsUniforms);
	for (int i = 0; i < _npsUniforms; i++) {
		//get the cbuffer i
		ID3D11ShaderReflectionConstantBuffer* constBuffer = psReflector->GetConstantBufferByIndex(i);
		//get its description
		D3D11_SHADER_BUFFER_DESC cbufferdesc;
		constBuffer->GetDesc(&cbufferdesc);
		std::string name = cbufferdesc.Name;
		ILogger::instance()->logInfo("Name of the constant buffer: " + name);
		for (UINT j = 0; j < cbufferdesc.Variables; j++){

			//Extract variable (and corresponding _desc) from cBuffer
			ID3D11ShaderReflectionVariable* cbufferVariable = constBuffer->GetVariableByIndex(j);
			D3D11_SHADER_VARIABLE_DESC cbufferVariable_desc;
			cbufferVariable->GetDesc(&cbufferVariable_desc);


			ID3D11ShaderReflectionType* pType = cbufferVariable->GetType();
			D3D11_SHADER_TYPE_DESC type_desc;
			pType->GetDesc(&type_desc);

			//get the name from the variableDesc
			std::string vname = cbufferVariable_desc.Name;
			ILogger::instance()->logInfo("Name of the variable: " + vname);
			int vsize = cbufferVariable_desc.Size;
			ILogger::instance()->logInfo("Size of the variable: %i", vsize);

			//get the type and class
			int type = type_desc.Type;
			ILogger::instance()->logInfo("Type of the variable: %i", type);
			int claas = type_desc.Class;
			ILogger::instance()->logInfo("Class of the variable: %i", claas);
			int members = type_desc.Members;
			ILogger::instance()->logInfo("Members of the variable: %i", members);

			GPUUniform* u = createUniform(vname, vsize, type_desc.Class, type_desc.Type, i);
			if (u != NULL) {
				_uniforms[u->getIndex()] = u;

				const int code = GPUVariable::getUniformCode(u->_key);
				_uniformsCode = _uniformsCode | code;
			}
			_createdPSUniforms[counter++] = u; //Adding to created uniforms array
		}
	}
	//3. Combine all the uniforms
	_nUniforms = _nvsUniforms + _npsUniforms;
	_createdUniforms = new GPUUniform*[_nUniforms];
	for (int i = 0; i < _nvsUniforms; i++){
		_createdUniforms[i] = _createdVSUniforms[i];
	}
	for (int i = 0; i < _npsUniforms; i++){
		_createdUniforms[i + _nvsUniforms] = _createdPSUniforms[i];
	}
	delete _createdPSUniforms;
	delete _createdVSUniforms;
}


void GPUProgram_D3D::onUnused(GL* gl){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void GPUProgram_D3D::applyChanges(GL* gl){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void GPUProgram_D3D::deleteShader(GL* gl, int shader) const{
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

void GPUProgram_D3D::deleteProgram(GL* gl, const IGPUProgram* p){
	std::string errMsg("TODO: Implementation");
	throw std::exception(errMsg.c_str());
}

GPUUniform* GPUProgram_D3D::createUniform(std::string name, int size, int klasse, int type, int id){
	if ((klasse == 3 | klasse == 4) && type == 3){
		return new GPUUniformMatrix4Float(name, new GLUniformID_win8(id));
	}
	else if (klasse == 1 && type == 3 && size == 16){
		return new GPUUniformVec4Float(name, new GLUniformID_win8(id));
	}
	else if (klasse == 0 && type == 3){
		return new GPUUniformFloat(name, new GLUniformID_win8(id));
	}

	else if (klasse == 1 && type == 3 && size == 8){
		return new GPUUniformVec2Float(name, new GLUniformID_win8(id));
	}
	else if (klasse == 1 && type == 3 && size == 12){
		return new GPUUniformVec3Float(name, new GLUniformID_win8(id));
	}
	else if (klasse == 0 && type == 1){
		return new GPUUniformBool(name, new GLUniformID_win8(id));
	}
	/*
	else if (){
	return new GPUUniformSampler2D(name, new GLUniformID_win8(id));
	}*/
	else{
		ILogger::instance()->logError("Unknown uniform type : " + name);
		return NULL;
	}
}

GPUAttribute* GPUProgram_D3D::createAttribute(std::string name){
	int type = -1;
	for (std::map<std::string, AttributeType>::iterator it = m.begin(); it != m.end(); ++it) {
		type = it->second;
	}

	if (type == -1 || type == 0){
		return NULL;
	}
	else if (type == 1){
		return new GPUAttributeVec1Float(name, type);
	}
	else if (type == 2){
		return new GPUAttributeVec2Float(name, type);
	}
	else if (type == 3){
		return new GPUAttributeVec3Float(name, type);
	}
	else if (type == 4){
		return new GPUAttributeVec4Float(name, type);
	}
}

Platform::Array<byte>^ GPUProgram_D3D::loadShaderFile(std::string File)
{
	Platform::Array<byte>^ FileData = nullptr;

	// open the file
	std::ifstream VertexFile(File, std::ios::in | std::ios::binary | std::ios::ate);

	// if open was successful
	if (VertexFile.is_open())
	{
		// find the length of the file
		int Length = (int)VertexFile.tellg();

		// collect the file data
		FileData = ref new Platform::Array<byte>(Length);
		VertexFile.seekg(0, std::ios::beg);
		VertexFile.read(reinterpret_cast<char*>(FileData->Data), Length);
		VertexFile.close();
	}
	else{
		ILogger::instance()->logError("Could not load shader file " + File);
		std::string errMsg("Error while loading shader file " + File);
		throw std::exception(errMsg.c_str());
	}


	return FileData;
}