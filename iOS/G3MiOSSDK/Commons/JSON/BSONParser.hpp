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

class BSONParser {
private:
  BSONParser() {

  }

  static JSONBaseObject* parseValue(const unsigned char type,
                                    ByteBufferIterator* iterator);


  static JSONString*  parseString(ByteBufferIterator* iterator);
  static JSONArray*   parseArray(ByteBufferIterator* iterator);
  static JSONNumber*  parseDouble(ByteBufferIterator* iterator);
  static JSONNumber*  parseInt(ByteBufferIterator* iterator);

public:

  static JSONBaseObject* parse(IByteBuffer* buffer);

};

#endif
