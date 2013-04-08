//
//  GPUProgramState.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//

#ifndef __G3MiOSSDK__GPUProgramState__
#define __G3MiOSSDK__GPUProgramState__

#include <iostream>
#include <map>
#include <string>

#include "GPUUniform.hpp"
#include "GL.hpp"
#include "GPUProgram.hpp"

class GPUProgramState{
  
  std::map<std::string, GPUUniformValue*> _uniformValues;
  
  const GPUProgramState* _parentState;
  
  void setValueToUniform(const std::string& name, GPUUniformValue* v){
    std::map<std::string, GPUUniformValue*> ::iterator it = _uniformValues.find(name);
    if (it != _uniformValues.end()){
      delete it->second;
    }
    it->second = v;
  }
  
public:
  
  GPUProgramState(GPUProgramState* parentState):_parentState(parentState){}
  
  ~GPUProgramState(){
    int VALUES_ARE_STORED_AT_UNIFORMS; //DO NOT DELETE
  }

  
  void setValueToUniform(const std::string& name, bool b){
    setValueToUniform(name, new GPUUniformValueBool(b));
  }
  
  void setValueToUniform(const std::string& name, float f){
    setValueToUniform(name, new GPUUniformValueFloat(f));
  }
  
  void setValueToUniform(const std::string& name, const Vector2D& v){
    setValueToUniform(name, new GPUUniformValueVec2Float(v._x, v._y));
  }
  
  void setValueToUniform(const std::string& name, double x, double y, double z, double w){
    setValueToUniform(name, new GPUUniformValueVec4Float(x,y,z,w));
  }
  
  void setValueToUniform(const std::string& name, const MutableMatrix44D& m){
    setValueToUniform(name, new GPUUniformValueMatrix4Float(m));
  }
  
  void applyChanges(GL* gl, const GPUProgram& prog) const{
    
    if (_parentState != NULL){
      _parentState->applyChanges(gl, prog);
    }
    
    for(std::map<std::string, GPUUniformValue*> ::const_iterator it = _uniformValues.begin();
        it != _uniformValues.end();
        it++){
      
      std::string name = it->first;
      GPUUniformValue* v = it->second;
      
      const int type = v->getType();
      if (type == GLType::glBool()){
        GPUUniformBool* u = prog.getGPUUniformBool(name);
        if (u == NULL){
          throw new G3MError("UNIFORM NOT FOUND");
        } else{
          u->set(v);
        }
        return;
      }
      if (type == GLType::glVec2Float()){
        GPUUniformVec2Float* u = prog.getGPUUniformVec2Float(name);
        if (u == NULL){
          throw new G3MError("UNIFORM NOT FOUND");
        } else{
          u->set(v);
        }
        return;
      }
      if (type == GLType::glVec4Float()){
        GPUUniformVec4Float* u = prog.getGPUUniformVec4Float(name);
        if (u == NULL){
          throw new G3MError("UNIFORM NOT FOUND");
        } else{
          u->set(v);
        }
        return;
      }
      if (type == GLType::glFloat()){
        GPUUniformFloat* u = prog.getGPUUniformFloat(name);
        if (u == NULL){
          throw new G3MError("UNIFORM NOT FOUND");
        } else{
          u->set(v);
        }
        return;
      }
      if (type == GLType::glMatrix4Float()){
        GPUUniformMatrix4Float* u = prog.getGPUUniformMatrix4Float(name);
        if (u == NULL){
          throw new G3MError("UNIFORM NOT FOUND");
        } else{
          u->set(v);
        }
        return;
      }
    }
  }
  
  GPUUniformValue* getUniformValue(const std::string name) const{
    std::map<std::string, GPUUniformValue*> ::const_iterator it = _uniformValues.find(name);
    if (it != _uniformValues.end()){
      return it->second;
    } else{
      return NULL;
    }
  }
};

#endif /* defined(__G3MiOSSDK__GPUProgramState__) */
