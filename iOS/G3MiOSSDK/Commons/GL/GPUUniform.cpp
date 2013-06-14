//
//  GPUUniform.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/05/13.
//
//

#include "GPUUniform.hpp"

void GPUUniformValue::setValueToLinkedUniform() const{
  if (_uniform == NULL){
    ILogger::instance()->logError("Uniform value unlinked");
  } else{
    _uniform->set((GPUUniformValue*)this);
  }
}

void GPUUniform::set(GPUUniformValue* v){
  if (_type != v->getType()){ //type checking
    //      delete v;
    ILogger::instance()->logError("Attempting to set uniform " + _name + "with invalid value type.");
    return;
  }
  if (_value == NULL || !_value->isEqualsTo(v)){
    _dirty = true;
    
    if (_value != NULL){
      v->setLastGPUUniformValue(_value); //Multiply matrix when needed
      _value->copyFrom(v);
    } else{
      _value = v->deepCopy();
    }
  }
}