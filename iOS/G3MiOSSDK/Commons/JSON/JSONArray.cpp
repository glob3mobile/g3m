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

const JSONBaseObject* JSONArray::get(const int index) const {
  return _entries[index];
}

void JSONArray::add(JSONBaseObject* object) {
  _entries.push_back(object);
}

int JSONArray::size() const {
  return _entries.size();
}

const JSONObject* JSONArray::getAsObject(const int index) const {
  const JSONBaseObject* object = get(index);
  return (object == NULL) ? NULL : object->asObject();
}

const JSONArray* JSONArray::getAsArray(const int index) const {
  const JSONBaseObject* object = get(index);
  return (object == NULL) ? NULL : object->asArray();
}

const JSONBoolean* JSONArray::getAsBoolean(const int index) const {
  const JSONBaseObject* object = get(index);
  return (object == NULL) ? NULL : object->asBoolean();
}

const JSONNumber* JSONArray::getAsNumber(const int index) const {
  const JSONBaseObject* object = get(index);
  return (object == NULL) ? NULL : object->asNumber();
}

const JSONString* JSONArray::getAsString(const int index) const {
  const JSONBaseObject* object = get(index);
  return (object == NULL) ? NULL : object->asString();
}

const std::string JSONArray::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  int size = this->size();

  isb->addString("[");
  //  isb->addString("[size=");
  //  isb->addInt(size);

  if (size > 0) {
    //isb->addString(" ");

      isb->addString((this->get(0) == NULL) ? "null" : this->get(0)->description());

    if (size <= 10) {
      for (int i = 1; i < size; i++) {
        isb->addString(", ");
        isb->addString((this->get(i) == NULL) ? "null" : this->get(i)->description());
      }
    }
    else {
      for (int i = 1; i < 10; i++) {
        isb->addString(", ");
        isb->addString((this->get(i) == NULL) ? "null" : this->get(i)->description());
      }
      isb->addString(", ...");
      isb->addString(" size=");
      isb->addInt(size);

    }
  }

  isb->addString("]");

  const std::string s = isb->getString();
  delete isb;
  return s;
}

JSONArray* JSONArray::deepCopy() const {
  JSONArray* result = new JSONArray();

  const int size = this->size();
  for (int i = 0; i < size; i++) {
    result->add( JSONBaseObject::deepCopy( get(i) ) );
  }

  return result;
}

bool JSONArray::getAsBoolean(const int index,
                             bool defaultValue) const {
  const JSONBoolean* jsBool = getAsBoolean(index);
  return (jsBool == NULL) ? defaultValue : jsBool->value();
}

double JSONArray::getAsNumber(const int index,
                              double defaultValue) const {
  const JSONNumber* jsNumber = getAsNumber(index);
  return (jsNumber == NULL) ? defaultValue : jsNumber->value();
}

const std::string JSONArray::getAsString(const int index,
                                         const std::string& defaultValue) const {
  const JSONString* jsString = getAsString(index);
  return (jsString == NULL) ? defaultValue : jsString->value();
}

void JSONArray::acceptVisitor(JSONVisitor* visitor) const {
  visitor->visitArrayBeforeChildren(this);

  const int size = this->size();
  for (int i = 0; i < size; i++) {
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
