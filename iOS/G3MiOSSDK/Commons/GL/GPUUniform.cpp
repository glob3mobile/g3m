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

GPUUniformValue* GPUUniformValueMatrix4FloatTransform::copyOrCreate(GPUUniformValue* value){
  if (value == NULL){
    return new GPUUniformValueMatrix4FloatTransform(_m, _isTransform);
  } else{
    GPUUniformValueMatrix4FloatTransform* valueM = (GPUUniformValueMatrix4FloatTransform*)value;
#ifdef C_CODE
    if (_isTransform){
      valueM->_m.copyValueOfMultiplication(valueM->_m, _m);
    } else {
      valueM->_m.copyValue(_m);
    }
#endif
#ifdef JAVA_CODE
    if (_isTransform){
      valueM._m = valueM._m.multiply(_m);
    } else {
      valueM._m = new MutableMatrix44D(_m);
    }
#endif
    return value;
  }
}