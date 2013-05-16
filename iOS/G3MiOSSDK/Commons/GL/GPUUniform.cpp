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
    ILogger::instance()->logError("Uniform unlinked");
  } else{
    _uniform->set((GPUUniformValue*)this);
//    _uniform->applyChanges(gl);
    
    //    setUniform(gl, _uniform->getID());
  }
}
