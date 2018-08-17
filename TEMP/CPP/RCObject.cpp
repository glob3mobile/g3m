//
//  RCObject.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 17/08/12.
//
//

#include "RCObject.hpp"
#include "ErrorHandling.hpp"
//#include "ILogger.hpp"

RCObject::~RCObject() {
  if (_referenceCounter != 0) {
//    ILogger::instance()->logError("DELETING RCOBJECT WITH UNRELEASED REFERENCES!");
    THROW_EXCEPTION("Deleted RCObject with unreleased references!");
  }
}
