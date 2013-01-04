//
//  BSONGenerator.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/13.
//
//

#include "BSONGenerator.hpp"

#include "JSONBaseObject.hpp"
#include "ByteBufferBuilder.hpp"
#include "JSONString.hpp"
#include "IStringBuilder.hpp"
#include "JSONNumber.hpp"
#include "JSONBoolean.hpp"

BSONGenerator::BSONGenerator() {
  _builder = new ByteBufferBuilder();
//  _builder->addUInt32(0);

  _currentKey = "";
}

BSONGenerator::~BSONGenerator() {
  delete _builder;
}

IByteBuffer* BSONGenerator::createBuffer() {
//  _builder->add(0); // bson is \0 terminated
//  _builder->setUInt32(0, _builder->size());
  return _builder->create();
}

IByteBuffer* BSONGenerator::generate(const JSONBaseObject* value) {
  BSONGenerator* generator = new BSONGenerator();
  value->acceptVisitor(generator);

  IByteBuffer* result = generator->createBuffer();

  delete generator;
  return result;
}

void BSONGenerator::visitBoolean(const JSONBoolean* value) {
  _builder->add(0x08);
  _builder->addStringZeroTerminated(_currentKey);
  if (value->value()) {
    _builder->add(0x01);
  }
  else {
    _builder->add(0x00);
  }
}

void BSONGenerator::visitNumber(const JSONNumber* value) {
  switch ( value->getType() ) {
    case int_type:
      _builder->add(0x10);
      _builder->addStringZeroTerminated(_currentKey);
      _builder->addInt32( value->intValue() );
      break;
    case float_type:
      _builder->add(0x01);
      _builder->addStringZeroTerminated(_currentKey);
      _builder->addDouble( value->floatValue() );
      break;
    case double_type:
      _builder->add(0x01);
      _builder->addStringZeroTerminated(_currentKey);
      _builder->addDouble( value->doubleValue() );
      break;

    default:
      break;
  }

}

void BSONGenerator::visitString(const JSONString* value) {
  _builder->add(0x02); // type string
  _builder->addStringZeroTerminated(_currentKey);

  const std::string str = value->value();
  _builder->addInt32(str.size() + 1 /* 1 for \0 termination */);
  _builder->addStringZeroTerminated(str);
}

void BSONGenerator::visitArrayBeforeChildren(const JSONArray* value) {
  _builder->add(0x04); // type array
  _builder->addStringZeroTerminated(_currentKey);

  _positionsStack.push_back(_builder->size()); // store current position, to update the size later
  _builder->addInt32(0); // save space for size
}

void BSONGenerator::visitArrayInBetweenChildren(const JSONArray* value) {

}

void BSONGenerator::visitArrayBeforeChild(const JSONArray* value,
                                          int i) {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addInt(i);
  _currentKey = isb->getString();
  delete isb;
}

void BSONGenerator::visitArrayAfterChildren(const JSONArray* value) {
  unsigned int sizePosition = _positionsStack.back();
  _positionsStack.pop_back();

  _builder->add(0); // bson is \0 terminated
  _builder->setInt32(sizePosition, _builder->size() - sizePosition);
}

void BSONGenerator::visitObjectBeforeChildren(const JSONObject* value) {
  if (_positionsStack.size() != 0) {
    // if positions back is not empty, it means the object is not the outer object
    _builder->add(0x03); // type document
    _builder->addStringZeroTerminated(_currentKey);
  }

  _positionsStack.push_back(_builder->size()); // store current position, to update the size later
  _builder->addInt32(0); // save space for size
}

void BSONGenerator::visitObjectInBetweenChildren(const JSONObject* value) {

}

void BSONGenerator::visitObjectBeforeChild(const JSONObject* value,
                                           const std::string& key) {
  _currentKey = key;
}

void BSONGenerator::visitObjectAfterChildren(const JSONObject* value) {
  unsigned int sizePosition = _positionsStack.back();
  _positionsStack.pop_back();

  _builder->add(0); // bson is \0 terminated
  _builder->setInt32(sizePosition, _builder->size() - sizePosition);
}
