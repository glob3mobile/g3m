//
//  JSONGenerator.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/13.
//
//

#include "JSONGenerator.hpp"

#include "JSONBaseObject.hpp"
#include "IStringBuilder.hpp"
#include "JSONBoolean.hpp"
#include "JSONString.hpp"
#include "JSONDouble.hpp"
#include "JSONFloat.hpp"
#include "JSONInteger.hpp"
#include "JSONLong.hpp"

std::string JSONGenerator::generate(const JSONBaseObject* value) {
  JSONGenerator* generator = new JSONGenerator();
  value->acceptVisitor(generator);

  std::string result = generator->getString();

  delete generator;
  return result;
}

JSONGenerator::JSONGenerator() {
  _isb = IStringBuilder::newStringBuilder();
}

JSONGenerator::~JSONGenerator() {
  delete _isb;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

std::string JSONGenerator::getString() {
  return _isb->getString();
}

void JSONGenerator::visitBoolean(const JSONBoolean* value) {
  if (value->value()) {
    _isb->addString("true");
  }
  else {
    _isb->addString("false");
  }
}

void JSONGenerator::visitDouble(const JSONDouble* value) {
  const double doubleValue = value->doubleValue();
  const long long longValue = (long long) doubleValue;
  if (longValue == doubleValue) {
    _isb->addLong(longValue);
  }
  else {
    _isb->addDouble(doubleValue);
  }
}

void JSONGenerator::visitFloat(const JSONFloat*   value) {
  const float floatValue = value->floatValue();
  const int intValue = (int) floatValue;
  if (intValue == floatValue) {
    _isb->addInt(intValue);
  }
  else {
    _isb->addFloat(floatValue);
  }
}

void JSONGenerator::visitInteger(const JSONInteger* value) {
  _isb->addInt(value->intValue());
}

void JSONGenerator::visitLong(const JSONLong*    value) {
  _isb->addLong(value->longValue());
}

void JSONGenerator::visitString(const JSONString* value) {
  _isb->addString("\"");
  _isb->addString(value->value());
  _isb->addString("\"");
}

void JSONGenerator::visitNull() {
  _isb->addString("null");
}

void JSONGenerator::visitArrayBeforeChildren(const JSONArray* value) {
  _isb->addString("[");
}

void JSONGenerator::visitArrayInBetweenChildren(const JSONArray* value) {
  _isb->addString(",");
}

void JSONGenerator::visitArrayBeforeChild(const JSONArray* value,
                                          int i) {

}

void JSONGenerator::visitArrayAfterChildren(const JSONArray* value) {
  _isb->addString("]");
}

void JSONGenerator::visitObjectBeforeChildren(const JSONObject* value) {
  _isb->addString("{");
}

void JSONGenerator::visitObjectInBetweenChildren(const JSONObject* value) {
  _isb->addString(",");
}

void JSONGenerator::visitObjectBeforeChild(const JSONObject* value,
                                           const std::string& key) {
  _isb->addString("\"");
  _isb->addString(key);
  _isb->addString("\":");
}

void JSONGenerator::visitObjectAfterChildren(const JSONObject* value) {
  _isb->addString("}");
}
