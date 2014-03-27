//
//  GPUProgram.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//

#ifndef __G3MiOSSDK__GPUProgram__
#define __G3MiOSSDK__GPUProgram__

#include <string>
#include <map>
#include <list>

class GPUAttribute;
class GPUUniform;

class GPUUniformBool;
class GPUUniformVec2Float;
class GPUUniformVec4Float;
class GPUUniformFloat;
class GPUUniformMatrix4Float;
class GPUAttributeVec1Float;
class GPUAttributeVec2Float;
class GPUAttributeVec3Float;
class GPUAttributeVec4Float;
class GPUUniformValue;
class GPUAttributeValue;
class IFloatBuffer;
class GL;
class GPUProgramManager;

enum ShaderType {
  VERTEX_SHADER,
  FRAGMENT_SHADER
};

class GPUProgram{
  int _programID;

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
  
  bool compileShader(GL* gl, int shader, const std::string& source) const;
  bool linkProgram(GL* gl) const;
  void deleteShader(GL* gl, int shader) const;
  void deleteProgram(GL* gl, const GPUProgram* p);
  
  void getVariables(GL* gl);

  GPUProgram():
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

  GPUProgram(const GPUProgram& that);

public:


  ~GPUProgram();
  
  static GPUProgram* createProgram(GL* gl,
                                   const std::string& name,
                                   const std::string& vertexSource,
                                   const std::string& fragmentSource);

  std::string getName() const { return _name;}
  
  int getProgramID() const{ return _programID;}
  
  int getGPUAttributesNumber() const { return _nAttributes;}
  int getGPUUniformsNumber() const { return _nUniforms;}
  
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
  
  void onUsed();
  void onUnused(GL* gl);
  void applyChanges(GL* gl);
  
  GPUUniform* getUniformOfType(const std::string& name, int type) const;
  
  GPUUniform* getGPUUniform(int key) const;
  GPUAttribute* getGPUAttribute(int key) const;
  GPUAttribute* getGPUAttributeVecXFloat(int key, int x) const;

  int getAttributesCode() const{ return _attributesCode;}
  int getUniformsCode() const{ return _uniformsCode;}

  void setGPUUniformValue(int key, GPUUniformValue* v);
  void setGPUAttributeValue(int key, GPUAttributeValue* v);

  void addReference() { ++_nReferences;}
  void removeReference() { --_nReferences;}
  int getNReferences() const { return _nReferences;}

};

#endif
