//
//  GPUProgram.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//

#ifndef __G3MiOSSDK__GPUProgram__
#define __G3MiOSSDK__GPUProgram__

#include <iostream.h>
#include <string.h>
#include <map>

#include "Attribute.hpp"
#include "Uniform.hpp"

#include "G3MError.hpp"

class IFloatBuffer;

class GL;

class GPUProgram{
  
  INativeGL* _nativeGL;
  int _programID;
  bool _programCreated;
  std::map<std::string, Attribute*> _attributes;
  std::map<std::string, Uniform*> _uniforms;
  std::string _name;
  
  bool compileShader(int shader, const std::string& source) const;
  bool linkProgram() const;
  void deleteShader(int shader) const;
  
  void getVariables();
  
  GPUProgram(){}
  
  Uniform* getUniform(const std::string name) const;
  
public:
  
  static GPUProgram* createProgram(GL* gl, const std::string name, const std::string& vertexSource,
                                               const std::string& fragmentSource);
  
  ~GPUProgram();
  
  std::string getName() const { return _name;}
  
  int getProgramID() const{ return _programID;}
  bool isCreated() const{ return _programCreated;}
  void deleteProgram(int p);
  
  Attribute* getAttribute(const std::string name) const;
  
  UniformBool* getUniformBool(const std::string name) const;
  UniformVec2Float* getUniformVec2Float(const std::string name) const;
  UniformVec4Float* getUniformVec4Float(const std::string name) const;
  UniformFloat* getUniformFloat(const std::string name) const;
  UniformMatrix4Float* getUniformMatrix4Float(const std::string name) const;
  
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
