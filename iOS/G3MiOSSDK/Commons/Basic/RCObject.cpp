//
//  RCObject.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 17/08/12.
//
//

#include "RCObject.hpp"

RCObject::~RCObject() {
  if (_referenceCounter != 0) {
    ILogger::instance()->logError("DELETING RCOBJECT WITH UNRELEASED REFERENCES!");
  }
}
