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
#include "JSONNumber.hpp"

JSONBaseObject* BSONParser::parse(IByteBuffer* buffer) {

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
    JSONBaseObject* value = parseValue(type, &iterator);
    if (value != NULL) {
      result->put(key, value);
    }
  }

  return result;
}

JSONBaseObject* BSONParser::parseValue(const unsigned char type,
                                       ByteBufferIterator* iterator) {
  switch (type) {
    case 0x02: {
      return parseString(iterator);
    }
    case 0x04: {
      return parseArray(iterator);
    }
    case 0x01: {
      return parseDouble(iterator);
    }
    case 0x10: {
      return parseInt(iterator);
    }
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

JSONArray* BSONParser::parseArray(ByteBufferIterator* iterator) {
  const int arraySize = iterator->nextInt32();

  JSONArray* result = new JSONArray();
  while (iterator->hasNext()) {
    const unsigned char type = iterator->nextUInt8();
    if (type == 0) {
      break;
    }
    
    const std::string key = iterator->nextZeroTerminatedString();
    JSONBaseObject* value = parseValue(type, iterator);
    if (value != NULL) {
      result->add(value);
    }
  }

  return result;
}

JSONNumber* BSONParser::parseDouble(ByteBufferIterator* iterator) {
  const double d = iterator->nextDouble();
  return new JSONNumber(d);
}

JSONNumber* BSONParser::parseInt(ByteBufferIterator* iterator) {
  const int i = iterator->nextInt32();
  return new JSONNumber(i);
}
