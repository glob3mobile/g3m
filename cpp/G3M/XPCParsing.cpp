//
//  XPCParsing.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/16/21.
//

#include "XPCParsing.hpp"

#include <string>

#include "JSONArray.hpp"
#include "JSONNumber.hpp"
#include "JSONString.hpp"

#include "Sector.hpp"


const Sector* XPCParsing::parseSector(const JSONArray* jsonArray) {
  const double lowerLat = jsonArray->getAsNumber(0)->value();
  const double lowerLon = jsonArray->getAsNumber(1)->value();
  const double upperLat = jsonArray->getAsNumber(2)->value();
  const double upperLon = jsonArray->getAsNumber(3)->value();

  return Sector::newFromDegrees(lowerLat, lowerLon,
                                upperLat, upperLon);
}


const std::vector<std::string>* XPCParsing::parseStrings(const JSONArray* jsonArray) {

  const size_t size = jsonArray->size();

  std::vector<std::string>* result = new std::vector<std::string>();

  for (size_t i = 0; i < size; i++) {
    std::string string = jsonArray->getAsString(i)->value();
    result->push_back(string);
  }

  return result;
}
