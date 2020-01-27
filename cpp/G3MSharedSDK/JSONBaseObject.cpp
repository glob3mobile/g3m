//
//  JSONBaseObject.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//

#include "JSONBaseObject.hpp"


const JSONObject* JSONBaseObject::asObject()  const {
  //ILogger::instance()->logError("The requested Object is not of type JSONObject!");
  return NULL;
}

const JSONArray* JSONBaseObject::asArray() const {
  //ILogger::instance()->logError("The requested Object is not of type JSONArray!");
  return NULL;
}

const JSONNumber* JSONBaseObject::asNumber() const {
  //ILogger::instance()->logError("The requested Object is not of type JSONNumber!");
  return NULL;
}

const JSONString* JSONBaseObject::asString() const {
  //ILogger::instance()->logError("The requested Object is not of type JSONString!");
  return NULL;
}

const JSONBoolean* JSONBaseObject::asBoolean() const {
  //ILogger::instance()->logError("The requested Object is not of type JSONBoolean!");
  return NULL;
}

const JSONNull* JSONBaseObject::asNull() const {
  //ILogger::instance()->logError("The requested Object is not of type JSONNull!");
  return NULL;
}
