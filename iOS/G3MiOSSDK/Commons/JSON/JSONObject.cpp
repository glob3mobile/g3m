//
//  JSONObject.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 01/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "JSONObject.hpp"
#include "ILogger.hpp"
#include "IStringBuilder.hpp"

#include "JSONString.hpp"
#include "JSONBoolean.hpp"
#include "JSONNumber.hpp"
#include "JSONDouble.hpp"
#include "JSONFloat.hpp"
#include "JSONLong.hpp"
#include "JSONInteger.hpp"
#include "JSONVisitor.hpp"

JSONObject::~JSONObject() {
#ifdef C_CODE
  for (std::map<std::string, JSONBaseObject*>::iterator it=_entries.begin(); it!=_entries.end(); it++) {
    delete it->second;
  }
#endif
  _entries.clear();

#ifdef JAVA_CODE
  super.dispose();
#endif

}

const JSONBaseObject* JSONObject::get(const std::string& key) const {
#ifdef C_CODE
  std::map<std::string, JSONBaseObject*>::const_iterator it = _entries.find(key);
  if (it != _entries.end()) {
    return _entries.at(key);
  }
  //ILogger::instance()->logError("The JSONObject does not contain the key \"" + key + "\"");
  return NULL;
#endif

#ifdef JAVA_CODE
  return _entries.get(key);
#endif
}

void JSONObject::put(const std::string& key,
                     JSONBaseObject* object) {
  _entries[key] = object;
}

void JSONObject::put(const std::string& key,
                     const std::string& value) {
  _entries[key] = new JSONString(value);
}

void JSONObject::put(const std::string& key,
                     double value) {
  _entries[key] = new JSONDouble(value);
}

void JSONObject::put(const std::string& key,
                     float value) {
  _entries[key] = new JSONFloat(value);
}

void JSONObject::put(const std::string& key,
                     int value) {
  _entries[key] = new JSONInteger(value);
}

void JSONObject::put(const std::string& key,
                     long long value) {
  _entries[key] = new JSONLong(value);
}

void JSONObject::put(const std::string& key,
                     bool value) {
  _entries[key] = new JSONBoolean(value);
}

int JSONObject::size() const {
  return _entries.size();
}

const JSONObject* JSONObject::getAsObject(const std::string& key) const {
  const JSONBaseObject* object = get(key);
  return (object == NULL) ? NULL : object->asObject();
}

const JSONArray* JSONObject::getAsArray(const std::string& key) const {
  const JSONBaseObject* object = get(key);
  return (object == NULL) ? NULL : object->asArray();
}

const JSONBoolean* JSONObject::getAsBoolean(const std::string& key) const {
  const JSONBaseObject* object = get(key);
  return (object == NULL) ? NULL : object->asBoolean();
}

const JSONNumber* JSONObject::getAsNumber(const std::string& key) const {
  const JSONBaseObject* object = get(key);
  return (object == NULL) ? NULL : object->asNumber();
}

const JSONString* JSONObject::getAsString(const std::string& key) const {
  const JSONBaseObject* object = get(key);
  return (object == NULL) ? NULL : object->asString();
}

bool JSONObject::getAsBoolean(const std::string& key,
                              bool defaultValue) const {
  const JSONBaseObject* jsValue = get(key);
  if ((jsValue == NULL) ||
      (jsValue->asNull() != NULL)) {
    return defaultValue;
  }

  const JSONBoolean* jsBool = jsValue->asBoolean();
  return (jsBool == NULL) ? defaultValue : jsBool->value();
}

double JSONObject::getAsNumber(const std::string& key,
                               double defaultValue) const {
  const JSONBaseObject* jsValue = get(key);
  if ((jsValue == NULL) ||
      (jsValue->asNull() != NULL)) {
    return defaultValue;
  }

  const JSONNumber* jsNumber = jsValue->asNumber();
  return (jsNumber == NULL) ? defaultValue : jsNumber->value();
}

const std::string JSONObject::getAsString(const std::string& key,
                                          const std::string& defaultValue) const {
  const JSONBaseObject* jsValue = get(key);
  if ((jsValue == NULL) ||
      (jsValue->asNull() != NULL)) {
    return defaultValue;
  }

  const JSONString* jsString = jsValue->asString();
  return (jsString == NULL) ? defaultValue : jsString->value();
}


std::vector<std::string> JSONObject::keys() const {
#ifdef C_CODE
  std::vector<std::string> result;

  std::map<std::string, JSONBaseObject*>::const_iterator it = _entries.begin();
  while (it != _entries.end()) {
    result.push_back(it->first);
    it++;
  }

  return result;
#endif
#ifdef JAVA_CODE
  return new java.util.ArrayList<String>(_entries.keySet());
#endif
}

void JSONObject::putKeyAndValueDescription(const std::string& key,
                                           IStringBuilder* isb) const {
  isb->addString("\"");
  isb->addString(key);
  isb->addString("\":");
    isb->addString((get(key) == NULL) ? "null" : get(key)->description());
}

const std::string JSONObject::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString("{");

  std::vector<std::string> keys = this->keys();

  int keysCount = keys.size();
  if (keysCount > 0) {
    putKeyAndValueDescription(keys[0], isb);
    for (int i = 1; i < keysCount; i++) {
      isb->addString(", ");
      putKeyAndValueDescription(keys[i], isb);
    }
  }

  isb->addString("}");

  const std::string s = isb->getString();
  delete isb;
  return s;
}

JSONObject* JSONObject::deepCopy() const {
  JSONObject* result = new JSONObject();

  std::vector<std::string> keys = this->keys();

  int keysCount = keys.size();
  for (int i = 0; i < keysCount; i++) {
    std::string key = keys[i];
    result->put(key, JSONBaseObject::deepCopy( get(key) ) );
  }

  return result;
}

void JSONObject::acceptVisitor(JSONVisitor* visitor) const {
  visitor->visitObjectBeforeChildren(this);

  std::vector<std::string> keys = this->keys();

  int keysCount = keys.size();
  for (int i = 0; i < keysCount; i++) {
    if (i != 0) {
      visitor->visitObjectInBetweenChildren(this);
    }
    std::string key = keys[i];
    visitor->visitObjectBeforeChild(this, key);
    const JSONBaseObject* child = get(key);
    if(child != NULL) {
        child->acceptVisitor(visitor);
    }
  }

  visitor->visitObjectAfterChildren(this);
}
