//
//  XPCDimension.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#include "XPCDimension.hpp"

#include "JSONArray.hpp"
#include "JSONObject.hpp"
#include "JSONString.hpp"


const std::vector<XPCDimension*>* XPCDimension::fromJSON(const JSONArray* jsonArray) {
  if (jsonArray == NULL) {
    return NULL;
  }

  std::vector<XPCDimension*>* result = new std::vector<XPCDimension*>();

  const size_t size = jsonArray->size();

  for (size_t i = 0; i < size; i++) {
    const JSONObject* jsonObject = jsonArray->getAsObject(i);
    XPCDimension* dimension = XPCDimension::fromJSON(jsonObject);
    if (dimension == NULL) {

      for (size_t j = 0; j < result->size(); j++) {
        delete result->at(j);
      }
      delete result;

      return NULL;
    }
    result->push_back( dimension );
  }

  return result;
}


XPCDimension* XPCDimension::fromJSON(const JSONObject* jsonObject) {
  const std::string name = jsonObject->getAsString("name")->value();
  const std::string type = jsonObject->getAsString("type")->value();

  if ((type != "float") && (type != "int")) {
    return NULL;
  }

  return new XPCDimension(name, type);
}
