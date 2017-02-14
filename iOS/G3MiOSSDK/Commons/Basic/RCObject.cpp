//
//  RCObject.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 17/08/12.
//
//

#include "RCObject.hpp"
#include "ErrorHandling.hpp"


RCObject::~RCObject() {
  if (_referenceCounter != 0) {
    THROW_EXCEPTION("Deleted RCObject with unreleased references!");
  }
}
