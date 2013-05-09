//
//  GPUProgramState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//

#include "GPUProgramState.hpp"

void GPUProgramState::setValuesOntoGPUProgram(GPUProgram& prog) const
{
  if (_parentState != NULL){
    _parentState->setValuesOntoGPUProgram(prog);
  }
  
  for(std::map<std::string, GPUUniformValue*> ::const_iterator it = _uniformValues.begin();
      it != _uniformValues.end();
      it++){
    
    std::string name = it->first;
    GPUUniformValue* v = it->second;
    
    GPUUniform* u = NULL;
    const int type = v->getType();
    if (type == GLType::glBool()){
      u = prog.getGPUUniformBool(name);
    } else {
      if (type == GLType::glVec2Float()){
        u = prog.getGPUUniformVec2Float(name);
      } else{
        if (type == GLType::glVec4Float()){
          u = prog.getGPUUniformVec4Float(name);
        } else{
          if (type == GLType::glFloat()){
            u = prog.getGPUUniformFloat(name);
          } else
            if (type == GLType::glMatrix4Float()){
              u = prog.getGPUUniformMatrix4Float(name);
            }
        }
      }
    }
    
    
    if (u == NULL){
      ILogger::instance()->logError("UNIFORM " + name + " NOT FOUND");
    } else{
      u->set(v);
    }
  }
  
  for(std::map<std::string, bool> ::const_iterator it = _attributesEnabled.begin();
      it != _attributesEnabled.end();
      it++){
    std::string name = it->first;
    GPUAttribute* a = prog.getGPUAttribute(name);
    if (a == NULL){
      ILogger::instance()->logError("ATTRIBUTE NOT FOUND " + name + ". COULDN'T CHANGE ENABLED STATE.");
    } else{
      a->setEnable(it->second);
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
      GPUAttribute* a = prog.getGPUAttributeVec3Float(name);
      if (a == NULL){
        a = prog.getGPUAttributeVec4Float(name); //A VEC3 COLUD BE STORED IN A VEC4 ATTRIBUTE
      }
      
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
}

void GPUProgramState::applyChanges(GL* gl, GPUProgram& prog) const{
  setValuesOntoGPUProgram(prog);
  
  prog.applyChanges(gl); //Applying changes on GPU
}

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

MutableMatrix44D* GPUProgramState::getMatrixValue(const std::string name) const{
  
  std::map<std::string, GPUUniformValue*> ::const_iterator it = _uniformValues.find(name);
  if (it != _uniformValues.end()){
    GPUUniformValue* uv = it->second;
    if (uv->getType() == GLType::glMatrix4Float()){
      GPUUniformValueMatrix4Float *uvm = (GPUUniformValueMatrix4Float *) uv;
      return new MutableMatrix44D(uvm->getValue());
    }
  }
  
  return NULL;
}

void GPUProgramState::setUniformValue(const std::string& name, bool b){
  setUniformValue(name, new GPUUniformValueBool(b) );
}

void GPUProgramState::setUniformValue(const std::string& name, float f){
  setUniformValue(name, new GPUUniformValueFloat(f));
}

void GPUProgramState::setUniformValue(const std::string& name, const Vector2D& v){
  setUniformValue(name, new GPUUniformValueVec2Float(v._x, v._y));
}

void GPUProgramState::setUniformValue(const std::string& name, double x, double y, double z, double w){
  setUniformValue(name, new GPUUniformValueVec4Float(x,y,z,w));
}

void GPUProgramState::setUniformValue(const std::string& name, const MutableMatrix44D& m){
  setUniformValue(name, new GPUUniformValueMatrix4Float(m));
}

void GPUProgramState::multiplyUniformValue(const std::string& name, const MutableMatrix44D& m){
  
  
  MutableMatrix44D previousM;
  MutableMatrix44D* thisM = getMatrixValue(name);
  if (thisM != NULL){
    previousM = *thisM;
    delete thisM;
  } else{
    MutableMatrix44D* parentM = NULL;
    const GPUProgramState* parent = _parentState;
    while (parent != NULL && parentM == NULL) {
      parentM = parent->getMatrixValue(name);
      parent = parent->_parentState;
    }
    
    if (parentM != NULL){
      previousM = *parentM;
      delete parentM;
    } else{
      ILogger::instance()->logError("Multiplying matrix uniform without a previous value.");
    }
  }
  
  setUniformValue(name, new GPUUniformValueMatrix4Float(previousM.multiply(m) ));
}

void GPUProgramState::setAttributeEnabled(const std::string& name, bool enabled){
  _attributesEnabled[name] = enabled;
}

std::string GPUProgramState::description() const{
  std::string desc = "PROGRAM STATE\n==========\n";
  
  for(std::map<std::string, GPUUniformValue*> ::const_iterator it = _uniformValues.begin();
      it != _uniformValues.end();
      it++){
    
    std::string name = it->first;
    GPUUniformValue* v = it->second;
    
    desc += "Uniform " + name + ":\n";
    desc += v->description() + "\n";
  }
  
  for(std::map<std::string, bool> ::const_iterator it = _attributesEnabled.begin();
      it != _attributesEnabled.end();
      it++){
    std::string name = it->first;
    desc += "Attribute " + name;
    if (it->second) desc += " ENABLED\n";
    else desc += " ENABLED\n";
  }
  
  for(std::map<std::string, GPUAttributeValue*> ::const_iterator it = _attributesValues.begin();
      it != _attributesValues.end();
      it++){
    
    std::string name = it->first;
    GPUAttributeValue* v = it->second;
    
    desc += "Attribute " + name + ":\n";
    desc += v->description() + "\n";
  }
  
  if (_parentState != NULL){
    desc = "PARENT " + _parentState->description() + desc;
  }
  
  return desc;
}

std::vector<std::string> GPUProgramState::getUniformsNames() const{
  std::vector<std::string> us;
  
  const GPUProgramState* state = this;
  while (state != NULL) {
    
    for(std::map<std::string, GPUUniformValue*> ::const_iterator it = state->_uniformValues.begin();
        it != state->_uniformValues.end();
        it++){
      us.push_back(it->first);
    }
    
    state = state->_parentState;
    
  }
  
  return us;
}
