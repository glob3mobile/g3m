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
  
  for(std::map<std::string, GPUAttributeValue*> ::const_iterator it = _attributesValues.begin();
      it != _attributesValues.end();
      it++){
    
    std::string name = it->first;
    GPUAttributeValue* v = it->second;
    
    const int type = v->getType();
    const int size = v->getAttributeSize();
    if (type == GLType::glFloat() && size == 1){
      GPUAttributeVec1Float* a = prog.getGPUAttributeVec1Float(name);
      if (a == NULL){
        ILogger::instance()->logError("ATTRIBUTE NOT FOUND " + name);
      } else{
        a->set(v);
      }
      continue;
    }
    
    if (type == GLType::glFloat() && size == 2){
      GPUAttributeVec2Float* a = prog.getGPUAttributeVec2Float(name);
      if (a == NULL){
        ILogger::instance()->logError("ATTRIBUTE NOT FOUND " + name);
      } else{
        a->set(v);
      }
      continue;
    }
    
    if (type == GLType::glFloat() && size == 3){
      GPUAttributeVec3Float* a = prog.getGPUAttributeVec3Float(name);
      if (a == NULL){
        ILogger::instance()->logError("ATTRIBUTE NOT FOUND " + name);
      } else{
        a->set(v);
      }
      continue;
    }
    
    if (type == GLType::glFloat() && size == 4){
      GPUAttributeVec4Float* a = prog.getGPUAttributeVec4Float(name);
      if (a == NULL){
        ILogger::instance()->logError("ATTRIBUTE NOT FOUND " + name);
      } else{
        a->set(v);
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

void GPUProgramState::setUniformValue(const std::string& name, GPUUniformValue* v){
  std::map<std::string, GPUUniformValue*> ::iterator it = _uniformValues.find(name);
  if (it != _uniformValues.end()){
    delete it->second;
  }
  _uniformValues[name] = v;
}

void GPUProgramState::setAttributeValue(const std::string& name, GPUAttributeValue* v){
  std::map<std::string, GPUAttributeValue*> ::iterator it = _attributesValues.find(name);
  if (it != _attributesValues.end()){
    delete it->second;
  }
  _attributesValues[name] = v;
}

void GPUProgramState::setAttributeValue(const std::string& name,
                                        IFloatBuffer* buffer, int attributeSize,
                                        int arrayElementSize, int index, bool normalized, int stride){
  switch (attributeSize) {
    case 1:
      setAttributeValue(name, new GPUAttributeValueVec1Float(buffer, arrayElementSize, index, stride, normalized) );
      break;
      
    case 2:
      setAttributeValue(name, new GPUAttributeValueVec2Float(buffer, arrayElementSize, index, stride, normalized) );
      break;
      
    case 3:
      setAttributeValue(name, new GPUAttributeValueVec3Float(buffer, arrayElementSize, index, stride, normalized) );
      break;
      
    case 4:
      setAttributeValue(name, new GPUAttributeValueVec4Float(buffer, arrayElementSize, index, stride, normalized) );
      break;
      
    default:
      ILogger::instance()->logError("Invalid size for Attribute.");
      break;
  }
}

MutableMatrix44D GPUProgramState::getAccumulatedMatrixFromParent(const std::string name){
  
  MutableMatrix44D m;
  if (_parentState == NULL){
    MutableMatrix44D m = MutableMatrix44D::identity();
  } else{
    std::map<std::string, GPUUniformValue*> ::const_iterator it = _parentState->_uniformValues.find(name);
    if (it != _uniformValues.end()){
      GPUUniformValue* uv = it->second;
      if (uv->getType() == GLType::glMatrix4Float()){
        GPUUniformValueMatrix4Float *uvm = (GPUUniformValueMatrix4Float *) uv;
        m = m.multiply(uvm->getValue());
      }
    }
  }

  return m;
}
