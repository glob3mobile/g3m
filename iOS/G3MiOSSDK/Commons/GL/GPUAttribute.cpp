//
//  GPUAttribute.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/05/13.
//
//

#include "GPUAttribute.hpp"

void GPUAttributeValue::setValueToLinkedAttribute() const{
  if (_attribute == NULL){
    ILogger::instance()->logError("Attribute unlinked");
  } else{
    
    _attribute->set((GPUAttributeValue*)this);
//    _attribute->applyChanges(gl);
  }
}