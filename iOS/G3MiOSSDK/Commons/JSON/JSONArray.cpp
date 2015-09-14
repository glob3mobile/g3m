//
//  JSONArray.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "JSONArray.hpp"
#include "JSONBaseObject.hpp"
#include "IStringBuilder.hpp"
#include "JSONBoolean.hpp"
#include "JSONNumber.hpp"
#include "JSONDouble.hpp"
#include "JSONFloat.hpp"
#include "JSONInteger.hpp"
#include "JSONLong.hpp"
#include "JSONString.hpp"
#include "JSONVisitor.hpp"

JSONArray::~JSONArray() {
  for (int i = 0; i < _entries.size(); i++) {
    delete _entries[i];
  }
  _entries.clear();

#ifdef JAVA_CODE
  super.dispose();
#endif

}

const JSONBaseObject* JSONArray::get(const size_t index) const {
  return _entries[index];
}

void JSONArray::add(JSONBaseObject* object) {
  _entries.push_back(object);
}

void JSONArray::add(const std::string& value) {
  _entries.push_back(new JSONString(value));
}

void JSONArray::add(double value) {
  _entries.push_back(new JSONDouble(value));
}

void JSONArray::add(float value) {
  _entries.push_back(new JSONFloat(value));
}

void JSONArray::add(int value) {
  _entries.push_back(new JSONInteger(value));
}

void JSONArray::add(long long value) {
  _entries.push_back(new JSONLong(value));
}

void JSONArray::add(bool value) {
  _entries.push_back(new JSONBoolean(value));
}

size_t JSONArray::size() const {
  return _entries.size();
}

const JSONObject* JSONArray::getAsObject(const size_t index) const {
  const JSONBaseObject* object = get(index);
  return (object == NULL) ? NULL : object->asObject();
}

const JSONArray* JSONArray::getAsArray(const size_t index) const {
  const JSONBaseObject* object = get(index);
  return (object == NULL) ? NULL : object->asArray();
}

const JSONBoolean* JSONArray::getAsBoolean(const size_t index) const {
  const JSONBaseObject* object = get(index);
  return (object == NULL) ? NULL : object->asBoolean();
}

const JSONNumber* JSONArray::getAsNumber(const size_t index) const {
  const JSONBaseObject* object = get(index);
  return (object == NULL) ? NULL : object->asNumber();
}

const JSONString* JSONArray::getAsString(const size_t index) const {
  const JSONBaseObject* object = get(index);
  return (object == NULL) ? NULL : object->asString();
}

const std::string JSONArray::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  const size_t size = this->size();

  isb->addString("[");

  if (size > 0) {
    isb->addString((this->get(0) == NULL) ? "null" : this->get(0)->description());

    if (size <= 10) {
      for (size_t i = 1; i < size; i++) {
        isb->addString(", ");
        isb->addString((this->get(i) == NULL) ? "null" : this->get(i)->description());
      }
    }
    else {
      for (size_t i = 1; i < 10; i++) {
        isb->addString(", ");
        isb->addString((this->get(i) == NULL) ? "null" : this->get(i)->description());
      }
      isb->addString(", ...");
      isb->addString(" size=");
      isb->addLong(size);

    }
  }

  isb->addString("]");

  const std::string s = isb->getString();
  delete isb;
  return s;
}

JSONArray* JSONArray::deepCopy() const {
  JSONArray* result = new JSONArray();

  const size_t size = this->size();
  for (size_t i = 0; i < size; i++) {
    result->add( JSONBaseObject::deepCopy( get(i) ) );
  }

  return result;
}

bool JSONArray::getAsBoolean(const size_t index,
                             bool defaultValue) const {
  const JSONBoolean* jsBool = getAsBoolean(index);
  return (jsBool == NULL) ? defaultValue : jsBool->value();
}

double JSONArray::getAsNumber(const size_t index,
                              double defaultValue) const {
  const JSONNumber* jsNumber = getAsNumber(index);
  return (jsNumber == NULL) ? defaultValue : jsNumber->value();
}

const std::string JSONArray::getAsString(const size_t index,
                                         const std::string& defaultValue) const {
  const JSONString* jsString = getAsString(index);
  return (jsString == NULL) ? defaultValue : jsString->value();
}

void JSONArray::acceptVisitor(JSONVisitor* visitor) const {
  visitor->visitArrayBeforeChildren(this);

  const size_t size = this->size();
  for (size_t i = 0; i < size; i++) {
    if (i != 0) {
      visitor->visitArrayInBetweenChildren(this);
    }
    visitor->visitArrayBeforeChild(this, i);
    if(get(i)!= NULL) {
        get(i)->acceptVisitor(visitor);
    }
  }

  visitor->visitArrayAfterChildren(this);
}


std::vector<std::string> JSONArray::asStringVector() const {
  std::vector<std::string> result;
  const size_t size = this->size();
  for (size_t i = 0; i < size; i++) {
    result.push_back( getAsString(i)->value() );
  }
  return result;
}

const std::string JSONArray::toString() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  const size_t size = this->size();

  isb->addString("[");

  if (size > 0) {
    isb->addString((this->get(0) == NULL) ? "null" : this->get(0)->toString());

    if (size <= 10) {
      for (size_t i = 1; i < size; i++) {
        isb->addString(", ");
        isb->addString((this->get(i) == NULL) ? "null" : this->get(i)->toString());
      }
    }
    else {
      for (size_t i = 1; i < 10; i++) {
        isb->addString(", ");
        isb->addString((this->get(i) == NULL) ? "null" : this->get(i)->toString());
      }
      isb->addString(", ...");
      isb->addString(" size=");
      isb->addLong(size);
    }
  }

  isb->addString("]");

  const std::string s = isb->getString();
  delete isb;
  return s;
}
