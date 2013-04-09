//
//  GPUProgramState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//

#include "GPUProgramState.hpp"

void GPUProgramState::applyChanges(GL* gl, const GPUProgram& prog) const{
  
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

GPUUniformValue* GPUProgramState::getUniformValue(const std::string name) const{
  std::map<std::string, GPUUniformValue*> ::const_iterator it = _uniformValues.find(name);
  if (it != _uniformValues.end()){
    return it->second;
  } else{
    return NULL;
  }
}

void GPUProgramState::setValueToUniform(const std::string& name, GPUUniformValue* v){
  std::map<std::string, GPUUniformValue*> ::iterator it = _uniformValues.find(name);
  if (it != _uniformValues.end()){
    delete it->second;
  }
  _uniformValues[name] = v;
}
