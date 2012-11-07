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


JSONArray::~JSONArray() {
  for (int i = 0; i < _entries.size(); i++) {
    delete _entries[i];
  }
  _entries.clear();
}

JSONBaseObject* JSONArray::get(const int index) const {
  return _entries[index];
}

void JSONArray::add(JSONBaseObject* object) {
  _entries.push_back(object);
}

int JSONArray::size() const {
  return _entries.size();
}

JSONObject* JSONArray::getAsObject(const int index) const {
  return get(index)->asObject();
}

JSONArray* JSONArray::getAsArray(const int index) const {
  return get(index)->asArray();
}

JSONBoolean* JSONArray::getAsBoolean(const int index) const {
  return get(index)->asBoolean();
}

JSONNumber* JSONArray::getAsNumber(const int index) const {
  return get(index)->asNumber();
}

JSONString* JSONArray::getAsString(const int index) const {
  return get(index)->asString();
}


const std::string JSONArray::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();

  int size = this->size();

  isb->addString("[size=");
  isb->addInt(size);

  if (size > 0) {
    isb->addString("/");

    isb->addString(this->get(0)->description());

    if (size <= 10) {
      for (int i = 1; i < size; i++) {
        isb->addString(",");
        isb->addString(this->get(i)->description());
      }
    }
    else {
      for (int i = 1; i < 10; i++) {
        isb->addString(",");
        isb->addString(this->get(i)->description());
      }
      isb->addString(",...");
    }
  }

  isb->addString("]");

  const std::string s = isb->getString();
  delete isb;
  return s;
}
