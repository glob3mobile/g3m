//
//  BSONParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/13.
//
//

#include "BSONParser.hpp"

#include "ByteBufferIterator.hpp"
#include "ILogger.hpp"
#include "IByteBuffer.hpp"
#include "JSONObject.hpp"
#include "JSONString.hpp"
#include "JSONArray.hpp"
#include "JSONInteger.hpp"
#include "JSONLong.hpp"
#include "JSONDouble.hpp"
#include "JSONFloat.hpp"
#include "JSONBoolean.hpp"
#include "JSONNull.hpp"

JSONBaseObject* BSONParser::parse(const IByteBuffer* buffer,
                                  bool nullAsObject) {

  ByteBufferIterator iterator(buffer);
  const int bufferSize = iterator.nextInt32();

  if (buffer->size() != bufferSize) {
    ILogger::instance()->logError("Invalid bufferSize, expected %d but got %d",
                                  bufferSize,
                                  buffer->size());
  }

  JSONObject* result = new JSONObject();

  while (iterator.hasNext()) {
    const unsigned char type = iterator.nextUInt8();
    if (type == 0) {
      break;
    }

    const std::string key = iterator.nextZeroTerminatedString();
    JSONBaseObject* value = parseValue(type, &iterator, nullAsObject);
    if (value != NULL) {
      result->put(key, value);
    }
  }

  return result;
}

JSONBaseObject* BSONParser::parseValue(const unsigned char type,
                                       ByteBufferIterator* iterator,
                                       bool nullAsObject) {
  switch (type) {
    case 0x02:
      return parseString(iterator);
    case 0x04:
      return parseArray(iterator, nullAsObject);
    case 0x01:
      return parseDouble(iterator);
    case 0x10:
      return parseInt32(iterator);
    case 0x12:
      return parseInt64(iterator);
    case 0x08:
      return parseBool(iterator);
    case 0x03:
      return parseObject(iterator, nullAsObject);
    case 0x44:
      return parseCustomizedArray(iterator, nullAsObject);
    case 0x0A:
      return nullAsObject ? new JSONNull() : NULL;
    default: {
      ILogger::instance()->logError("Unknown type %d", type);
      return NULL;
    }
  }
}

JSONString* BSONParser::parseString(ByteBufferIterator* iterator) {
  const int stringSize = iterator->nextInt32();
  const std::string str = iterator->nextZeroTerminatedString();

  if ( (stringSize - 1) == str.size() ) {
    return new JSONString(str);
  }

  ILogger::instance()->logError("Invalid stringSize, expected %d but got %d",
                                stringSize,
                                str.size());
  return NULL;
}

JSONArray* BSONParser::parseArray(ByteBufferIterator* iterator,
                                  bool nullAsObject) {
  //const int arraySize = iterator->nextInt32();
  iterator->nextInt32(); // consumes the size

  JSONArray* result = new JSONArray();
  while (iterator->hasNext()) {
    const unsigned char type = iterator->nextUInt8();
    if (type == 0) {
      break;
    }
    
    // const std::string key = iterator->nextZeroTerminatedString();
    iterator->nextZeroTerminatedString(); // consumes the key
    JSONBaseObject* value = parseValue(type, iterator, nullAsObject);
    if (value != NULL) {
      result->add(value);
    }
  }

  return result;
}

JSONArray* BSONParser::parseCustomizedArray(ByteBufferIterator* iterator,
                                            bool nullAsObject) {
  //const int arraySize = iterator->nextInt32();
  iterator->nextInt32(); // consumes the size

  JSONArray* result = new JSONArray();
  while (iterator->hasNext()) {
    const unsigned char type = iterator->nextUInt8();
    if (type == 0) {
      break;
    }

    //const std::string key = iterator->nextZeroTerminatedString();
    JSONBaseObject* value = parseValue(type, iterator, nullAsObject);
    if (value != NULL) {
      result->add(value);
    }
  }

  return result;
}


JSONNumber* BSONParser::parseDouble(ByteBufferIterator* iterator) {
  const double doubleValue = iterator->nextDouble();
  const float floatValue = (float) doubleValue;
  if (doubleValue == floatValue) {
    return new JSONFloat( floatValue );
  }
  return new JSONDouble( doubleValue );
}

JSONInteger* BSONParser::parseInt32(ByteBufferIterator* iterator) {
  return new JSONInteger( iterator->nextInt32() );
}

JSONLong* BSONParser::parseInt64(ByteBufferIterator* iterator) {
  return new JSONLong( iterator->nextInt64() );
}

JSONBoolean* BSONParser::parseBool(ByteBufferIterator* iterator) {
  unsigned char b = iterator->nextUInt8();
  if (b == 0x01) {
    return new JSONBoolean(true);
  }
  return new JSONBoolean(false);
}

JSONObject* BSONParser::parseObject(ByteBufferIterator* iterator,
                                    bool nullAsObject) {
  //const int objectSize = iterator->nextInt32();
  iterator->nextInt32(); // consumes the size

  JSONObject* result = new JSONObject();
  while (iterator->hasNext()) {
    const unsigned char type = iterator->nextUInt8();
    if (type == 0) {
      break;
    }

    const std::string key = iterator->nextZeroTerminatedString();
    JSONBaseObject* value = parseValue(type, iterator, nullAsObject);
    if (value != NULL) {
      result->put(key, value);
    }
  }

  return result;
}
