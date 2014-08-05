//
//  IGPUProgram.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 21/07/14.
//
//

#ifndef __G3MiOSSDK__IGPUProgram__
#define __G3MiOSSDK__IGPUProgram__

#include <string>

class GPUAttribute;
class GPUUniform;
class GL;

class GPUUniformBool;
class GPUUniformVec2Float;
class GPUUniformVec4Float;
class GPUUniformFloat;
class GPUUniformMatrix4Float;
class GPUAttributeVec1Float;
class GPUAttributeVec2Float;
class GPUAttributeVec3Float;
class GPUAttributeVec4Float;
class GPUVariable;
class GPUUniformValue;
class GPUAttributeValue;


enum ShaderType {
	VERTEX_SHADER,
	FRAGMENT_SHADER
};

class IGPUProgram{

protected:
	int _programID;

	IGPUProgram() :
		_createdAttributes(NULL),
		_createdUniforms(NULL),
		_nUniforms(0),
		_nAttributes(0),
		_uniformsCode(0),
		_attributesCode(0),
		_gl(NULL),
		_nReferences(0)
	{
	}
	GPUUniform* _uniforms[32];
	GPUAttribute* _attributes[32];
	int _nAttributes;
	int _nUniforms;
	GPUUniform** _createdUniforms;
	GPUAttribute** _createdAttributes;

	int _uniformsCode;
	int _attributesCode;

	std::string _name;

	GL* _gl;

	int _nReferences; //Number of items that reference this Program

	virtual void getVariables(GL* gl) = 0;

public:
	virtual ~IGPUProgram(){};
	int getProgramID() const{ return _programID; }

	virtual void onUsed(){}; //previous implementation did nothing...
	virtual void onUnused(GL* gl) = 0;
	virtual void applyChanges(GL* gl) = 0;
	virtual void deleteShader(GL* gl, int shader) const = 0;
	virtual void deleteProgram(GL* gl, const IGPUProgram* p) = 0;

	std::string getName() const { return _name; }
	int getGPUAttributesNumber() const { return _nAttributes; }
	int getGPUUniformsNumber() const { return _nUniforms; }

	GPUUniform* getGPUUniform(const std::string name) const;
	GPUAttribute* getGPUAttribute(const std::string name) const;

	GPUUniformBool* getGPUUniformBool(const std::string name) const;
	GPUUniformVec2Float* getGPUUniformVec2Float(const std::string name) const;
	GPUUniformVec4Float* getGPUUniformVec4Float(const std::string name) const;
	GPUUniformFloat* getGPUUniformFloat(const std::string name) const;
	GPUUniformMatrix4Float* getGPUUniformMatrix4Float(const std::string name) const;

	GPUAttribute* getGPUAttributeVecXFloat(const std::string name, int x) const;
	GPUAttributeVec1Float* getGPUAttributeVec1Float(const std::string name) const;
	GPUAttributeVec2Float* getGPUAttributeVec2Float(const std::string name) const;
	GPUAttributeVec3Float* getGPUAttributeVec3Float(const std::string name) const;
	GPUAttributeVec4Float* getGPUAttributeVec4Float(const std::string name) const;

	GPUUniform* getUniformOfType(const std::string& name, int type) const;

	GPUUniform* getGPUUniform(int key) const;
	GPUAttribute* getGPUAttribute(int key) const;
	GPUAttribute* getGPUAttributeVecXFloat(int key, int x) const;

	int getAttributesCode() const{ return _attributesCode; }
	int getUniformsCode() const{ return _uniformsCode; }
	void setGPUUniformValue(int key, GPUUniformValue* v);
	void setGPUAttributeValue(int key, GPUAttributeValue* v);

	void addReference() { ++_nReferences; }
	void removeReference() { --_nReferences; }
	int getNReferences() const { return _nReferences; }


};

#endif