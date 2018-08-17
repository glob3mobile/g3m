//
//  BSONParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/13.
//
//

#ifndef __G3MiOSSDK__BSONParser__
#define __G3MiOSSDK__BSONParser__

class IByteBuffer;
class ByteBufferIterator;
class JSONBaseObject;
class JSONString;
class JSONArray;
class JSONNumber;
class JSONDouble;
class JSONInteger;
class JSONLong;
class JSONBoolean;
class JSONObject;

class BSONParser {
private:
  BSONParser() {

  }

  static JSONBaseObject* parseValue(const unsigned char type,
                                    ByteBufferIterator* iterator,
                                    bool nullAsObject);


  static JSONString*  parseString(ByteBufferIterator* iterator);
  static JSONArray*   parseArray(ByteBufferIterator* iterator,
                                 bool nullAsObject);
  static JSONArray*   parseCustomizedArray(ByteBufferIterator* iterator,
                                           bool nullAsObject);
  static JSONNumber*  parseDouble(ByteBufferIterator* iterator);
  static JSONInteger* parseInt32(ByteBufferIterator* iterator);
  static JSONLong*    parseInt64(ByteBufferIterator* iterator);
  static JSONBoolean* parseBool(ByteBufferIterator* iterator);
  static JSONObject*  parseObject(ByteBufferIterator* iterator,
                                  bool nullAsObject);

public:

  static JSONBaseObject* parse(const IByteBuffer* buffer,
                               bool nullAsObject = false);

};

#endif
