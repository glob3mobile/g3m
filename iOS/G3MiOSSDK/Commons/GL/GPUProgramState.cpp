//
//  GPUProgramState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//

#include "GPUProgramState.hpp"

void GPUProgramState::applyChanges(GL* gl, GPUProgram& prog) const{
  
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
        ILogger::instance()->logError("UNIFORM NOT FOUND");
      } else{
        u->set(v);
      }
      continue;
    }
    if (type == GLType::glVec2Float()){
      GPUUniformVec2Float* u = prog.getGPUUniformVec2Float(name);
      if (u == NULL){
        ILogger::instance()->logError("UNIFORM NOT FOUND");
      } else{
        u->set(v);
      }
      continue;
    }
    if (type == GLType::glVec4Float()){
      GPUUniformVec4Float* u = prog.getGPUUniformVec4Float(name);
      if (u == NULL){
        ILogger::instance()->logError("UNIFORM NOT FOUND");
      } else{
        u->set(v);
      }
      continue;
    }
    if (type == GLType::glFloat()){
      GPUUniformFloat* u = prog.getGPUUniformFloat(name);
      if (u == NULL){
        ILogger::instance()->logError("UNIFORM NOT FOUND");
      } else{
        u->set(v);
      }
      continue;
    }
    if (type == GLType::glMatrix4Float()){
      GPUUniformMatrix4Float* u = prog.getGPUUniformMatrix4Float(name);
      if (u == NULL){
        ILogger::instance()->logError("UNIFORM NOT FOUND");
      } else{
        u->set(v);
      }
      continue;
    }
  }
  
  prog.applyChanges(gl); //Applying changes on GPU
}

//GPUUniformValue* GPUProgramState::getUniformValue(const std::string name) const{
//  std::map<std::string, GPUUniformValue*> ::const_iterator it = _uniformValues.find(name);
//  if (it != _uniformValues.end()){
//    return it->second;
//  } else{
//    return NULL;
//  }
//}

void GPUProgramState::setValueToUniform(const std::string& name, GPUUniformValue* v){
  std::map<std::string, GPUUniformValue*> ::iterator it = _uniformValues.find(name);
  if (it != _uniformValues.end()){
    delete it->second;
  }
  _uniformValues[name] = v;
}

void GPUProgramState::setValueToAttribute(const std::string& name, GPUAttributeValue* v){
  std::map<std::string, GPUAttributeValue*> ::iterator it = _attributesValues.find(name);
  if (it != _attributesValues.end()){
    delete it->second;
  }
  _attributesValues[name] = v;
}

void GPUProgramState::setValueToAttribute(const std::string& name, IFloatBuffer* buffer, int size, int index, bool normalized, int stride){
  switch (size) {
    case 1:
      setValueToAttribute(name, new GPUAttributeValueVec1Float(buffer, index, stride, normalized) );
      break;
      
    case 2:
      setValueToAttribute(name, new GPUAttributeValueVec2Float(buffer, index, stride, normalized) );
      break;
      
    case 3:
      setValueToAttribute(name, new GPUAttributeValueVec3Float(buffer, index, stride, normalized) );
      break;
      
    case 4:
      setValueToAttribute(name, new GPUAttributeValueVec4Float(buffer, index, stride, normalized) );
      break;
      
    default:
      ILogger::instance()->logError("Invalid size for Attribute.");
      break;
  }
}
