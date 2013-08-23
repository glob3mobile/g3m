//
//  GPUUniform.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/05/13.
//
//

#include "GPUUniform.hpp"

//void GPUUniformValue::setValueToLinkedUniform() const {
//  if (_uniform == NULL) {
//    ILogger::instance()->logError("Uniform value unlinked");
//  }
//  else {
//    //_uniform->set((GPUUniformValue*)this);
//    _uniform->set(this);
//  }
//}

void GPUUniform::unset() {
  if (_value != NULL) {
    _value->_release();
    _value = NULL;
  }
  _dirty = false;
}

void GPUUniform::applyChanges(GL* gl) {
  if (_dirty) {
    _value->setUniform(gl, _id);
    _dirty = false;
  }
  else {
    if (_value == NULL) {
      ILogger::instance()->logError("Uniform " + _name + " was not set.");
    }
  }
}

//GPUUniformValue* GPUUniformValueMatrix4FloatTransform::copyOrCreate(GPUUniformValue* value) const {
//  if (value == NULL) {
//    return new GPUUniformValueMatrix4FloatTransform(_m, _isTransform);
//  } else{
//    GPUUniformValueMatrix4FloatTransform* valueM = (GPUUniformValueMatrix4FloatTransform*)value;
//#ifdef C_CODE
//    if (_isTransform) {
//      valueM->_m.copyValueOfMultiplication(valueM->_m, _m);
//    } else {
//      valueM->_m.copyValue(_m);
//    }
//#endif
//#ifdef JAVA_CODE
//    if (_isTransform) {
//      valueM._m = valueM._m.multiply(_m);
//    } else {
//      valueM._m = new MutableMatrix44D(_m);
//    }
//#endif
//    return value;
//  }
//}

//GPUUniformValue* GPUUniformValueMatrix4Float::copyOrCreate(GPUUniformValue* value) const{
//  delete value;
//  return new GPUUniformValueMatrix4Float(*_m);
//}
