//
//  GPUProgram.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//

#ifndef __G3MiOSSDK__GPUProgram__
#define __G3MiOSSDK__GPUProgram__

#include <iostream>
#include <string>
#include <map>

#include "GPUAttribute.hpp"

#include "GPUUniform.hpp"

class IFloatBuffer;

class GL;

class GPUProgram{
  
  //INativeGL* _nativeGL;
  int _programID;
  bool _programCreated;
  std::map<std::string, GPUAttribute*> _attributes;
  std::map<std::string, GPUUniform*> _uniforms;
  std::string _name;
  
  bool compileShader(GL* gl, int shader, const std::string& source) const;
  bool linkProgram(GL* gl) const;
  void deleteShader(GL* gl, int shader) const;
  
  void getVariables(GL* gl);
  
  GPUProgram(){}

  
public:
  
  static GPUProgram* createProgram(GL* gl, const std::string name, const std::string& vertexSource,
                                               const std::string& fragmentSource);
  
  ~GPUProgram();
  
  std::string getName() const { return _name;}
  
  int getProgramID() const{ return _programID;}
  bool isCreated() const{ return _programCreated;}
  void deleteProgram(GL* gl, int p);
  
  int getGPUAttributesNumber() const { return _attributes.size();}
  int getGPUUniformsNumber() const { return _uniforms.size();}
  
  
  GPUUniform* getGPUUniform(const std::string name) const;
  GPUAttribute* getGPUAttribute(const std::string name) const;
  
  GPUUniformBool* getGPUUniformBool(const std::string name) const;
  GPUUniformVec2Float* getGPUUniformVec2Float(const std::string name) const;
  GPUUniformVec4Float* getGPUUniformVec4Float(const std::string name) const;
  GPUUniformFloat* getGPUUniformFloat(const std::string name) const;
  GPUUniformMatrix4Float* getGPUUniformMatrix4Float(const std::string name) const;

  
  GPUAttributeVec1Float* getGPUAttributeVec1Float(const std::string name) const;
  GPUAttributeVec2Float* getGPUAttributeVec2Float(const std::string name) const;
  GPUAttributeVec3Float* getGPUAttributeVec3Float(const std::string name) const;
  GPUAttributeVec4Float* getGPUAttributeVec4Float(const std::string name) const;
  
  void onUsed();
  void onUnused();
  void applyChanges(GL* gl);
/*
  void setUniform(GL* gl, const std::string& name, const Vector2D& v) const{
    Uniform* u = getUniform(name);
    if (u != NULL && u->getType() == GLType::glVec2Float()) {
      ((UniformVec2Float*)u)->set(gl, v);
    } else{
      throw G3MError("Error setting Uniform " + name);
    }
  }
  
  void setUniform(GL* gl, const std::string& name, double x, double y, double z, double w) const{
    Uniform* u = getUniform(name);
    if (u != NULL && u->getType() == GLType::glVec4Float()) {
      ((UniformVec4Float*)u)->set(gl, x,y,z,w);
    } else{
      throw G3MError("Error setting Uniform " + name);
    }
  }
  
  void setUniform(GL* gl, const std::string& name, bool b) const{
    Uniform* u = getUniform(name);
    if (u != NULL && u->getType() == GLType::glBool()) {
      ((UniformBool*)u)->set(gl, b);
    } else{
      throw G3MError("Error setting Uniform " + name);
    }
  }
  
  void setUniform(GL* gl, const std::string& name, float f) const{
    Uniform* u = getUniform(name);
    if (u != NULL && u->getType() == GLType::glFloat()) {
      ((UniformFloat*)u)->set(gl, f);
    } else{
      throw G3MError("Error setting Uniform " + name);
    }
  }
  
  void setUniform(GL* gl, const std::string& name, const MutableMatrix44D& m) const{
    Uniform* u = getUniform(name);
    if (u != NULL && u->getType() == GLType::glMatrix4Float()) {
      ((UniformMatrix4Float*)u)->set(gl, m);
    } else{
      throw G3MError("Error setting Uniform " + name);
    }
  }
  */
  
  
};

#endif /* defined(__G3MiOSSDK__GPUProgram__) */
