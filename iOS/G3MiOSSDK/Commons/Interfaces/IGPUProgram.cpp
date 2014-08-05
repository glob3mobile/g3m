//
//  IGPUProgram.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 21/07/14.
//
//

#include "IGPUProgram.hpp"
#include "GPUUniform.hpp"
#include "GPUAttribute.hpp"
#include "GL.hpp"



//Getters for uniforms
GPUUniform* IGPUProgram::getGPUUniform(const std::string name) const{
#ifdef C_CODE
	const int key = GPUVariable::getUniformKey(name);
#endif
#ifdef JAVA_CODE
	final int key = GPUVariable.getUniformKey(name).getValue();
#endif
	return _uniforms[key];
}

GPUUniformBool* IGPUProgram::getGPUUniformBool(const std::string name) const {
	GPUUniform* u = getGPUUniform(name);
	if (u != NULL && u->_type == GLType::glBool()) {
		return (GPUUniformBool*)u;
	}
	return NULL;
}

GPUUniformVec2Float* IGPUProgram::getGPUUniformVec2Float(const std::string name) const {
	GPUUniform* u = getGPUUniform(name);
	if (u != NULL && u->_type == GLType::glVec2Float()) {
		return (GPUUniformVec2Float*)u;
	}
	return NULL;
}

GPUUniformVec4Float* IGPUProgram::getGPUUniformVec4Float(const std::string name) const{
	GPUUniform* u = getGPUUniform(name);
	if (u != NULL && u->_type == GLType::glVec4Float()) {
		return (GPUUniformVec4Float*)u;
	}
	return NULL;
}

GPUUniformFloat* IGPUProgram::getGPUUniformFloat(const std::string name) const{
	GPUUniform* u = getGPUUniform(name);
	if (u != NULL && u->_type == GLType::glFloat()) {
		return (GPUUniformFloat*)u;
	}
	return NULL;
}

GPUUniformMatrix4Float* IGPUProgram::getGPUUniformMatrix4Float(const std::string name) const{
	GPUUniform* u = getGPUUniform(name);
	if (u != NULL && u->_type == GLType::glMatrix4Float()) {
		return (GPUUniformMatrix4Float*)u;
	}
	return NULL;

}


//getters for Attributes
GPUAttribute* IGPUProgram::getGPUAttribute(const std::string name) const{
#ifdef C_CODE
	const int key = GPUVariable::getAttributeKey(name);
#endif
#ifdef JAVA_CODE
	final int key = GPUVariable.getAttributeKey(name).getValue();
#endif
	return _attributes[key];
}

GPUAttribute* IGPUProgram::getGPUAttributeVecXFloat(const std::string name, int x) const{
	switch (x) {
	case 1:
		return getGPUAttributeVec1Float(name);
	case 2:
		return getGPUAttributeVec2Float(name);
	case 3:
		return getGPUAttributeVec3Float(name);
	case 4:
		return getGPUAttributeVec4Float(name);
	default:
		return NULL;
	}
}

GPUAttributeVec1Float* IGPUProgram::getGPUAttributeVec1Float(const std::string name) const{
	GPUAttributeVec1Float* a = (GPUAttributeVec1Float*)getGPUAttribute(name);
	if (a != NULL && a->_size == 1 && a->_type == GLType::glFloat()) {
		return (GPUAttributeVec1Float*)a;
	}
	return NULL;

}

GPUAttributeVec2Float* IGPUProgram::getGPUAttributeVec2Float(const std::string name) const{
	GPUAttributeVec2Float* a = (GPUAttributeVec2Float*)getGPUAttribute(name);
	if (a != NULL && a->_size == 2 && a->_type == GLType::glFloat()) {
		return (GPUAttributeVec2Float*)a;
	}
	return NULL;

}

GPUAttributeVec3Float* IGPUProgram::getGPUAttributeVec3Float(const std::string name) const{
	GPUAttributeVec3Float* a = (GPUAttributeVec3Float*)getGPUAttribute(name);
	if (a != NULL && a->_size == 3 && a->_type == GLType::glFloat()) {
		return (GPUAttributeVec3Float*)a;
	}
	return NULL;

}

GPUAttributeVec4Float* IGPUProgram::getGPUAttributeVec4Float(const std::string name) const{
	GPUAttributeVec4Float* a = (GPUAttributeVec4Float*)getGPUAttribute(name);
	if (a != NULL && a->_size == 4 && a->_type == GLType::glFloat()) {
		return (GPUAttributeVec4Float*)a;
	}
	return NULL;

}


GPUUniform* IGPUProgram::getUniformOfType(const std::string& name, int type) const{
	GPUUniform* u = NULL;
	if (type == GLType::glBool()) {
		u = getGPUUniformBool(name);
	}
	else {
		if (type == GLType::glVec2Float()) {
			u = getGPUUniformVec2Float(name);
		}
		else{
			if (type == GLType::glVec4Float()) {
				u = getGPUUniformVec4Float(name);
			}
			else{
				if (type == GLType::glFloat()) {
					u = getGPUUniformFloat(name);
				}
				else
					if (type == GLType::glMatrix4Float()) {
					u = getGPUUniformMatrix4Float(name);
					}
			}
		}
	}
	return u;
}

GPUUniform* IGPUProgram::getGPUUniform(int key) const{
	return _uniforms[key];
}

GPUAttribute* IGPUProgram::getGPUAttribute(int key) const{
	return _attributes[key];
}

GPUAttribute* IGPUProgram::getGPUAttributeVecXFloat(int key, int x) const{
	GPUAttribute* a = getGPUAttribute(key);
	if (a->_type == GLType::glFloat() && a->_size == x) {
		return a;
	}
	return NULL;
}

void IGPUProgram::setGPUUniformValue(int key, GPUUniformValue* v) {
	GPUUniform* u = _uniforms[key];
	if (u == NULL) {
		ILogger::instance()->logError("Uniform [key=%d] not found", key);
		return;
	}
	u->set(v);
}

void IGPUProgram::setGPUAttributeValue(int key, GPUAttributeValue* v) {
	GPUAttribute* a = _attributes[key];
	if (a == NULL) {
		ILogger::instance()->logError("Attribute [key=%d] not found", key);
		return;
	}
	a->set(v);
}