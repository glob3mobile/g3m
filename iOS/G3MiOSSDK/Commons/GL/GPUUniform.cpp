//
//  GPUUniform.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/05/13.
//
//

#include "GPUUniform.hpp"

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
