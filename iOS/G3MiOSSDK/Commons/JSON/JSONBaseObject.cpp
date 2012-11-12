//
//  JSONBaseObject.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "JSONBaseObject.hpp"
#include "JSONObject.hpp"
#include "JSONArray.hpp"
#include "JSONNumber.hpp"

JSONObject* JSONBaseObject::asObject() {
  ILogger::instance()->logError("The requested Object is not of type JSONObject!");
  return NULL;
}

JSONArray* JSONBaseObject::asArray() {
  ILogger::instance()->logError("The requested Object is not of type JSONArray!");
  return NULL;
}

JSONNumber* JSONBaseObject::asNumber() {
  ILogger::instance()->logError("The requested Object is not of type JSONNumber!");
  return NULL;
}

JSONString* JSONBaseObject::asString() {
  ILogger::instance()->logError("The requested Object is not of type JSONString!");
  return NULL;
}

JSONBoolean* JSONBaseObject::asBoolean() {
  ILogger::instance()->logError("The requested Object is not of type JSONBoolean!");
  return NULL;
}
