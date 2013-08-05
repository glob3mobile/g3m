//
//  BSONGenerator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/13.
//
//

#ifndef __G3MiOSSDK__BSONGenerator__
#define __G3MiOSSDK__BSONGenerator__

#include "JSONVisitor.hpp"
#include "IByteBuffer.hpp"

class JSONBaseObject;
class ByteBufferBuilder;

#include <vector>

class BSONGenerator : public JSONVisitor {
private:
  ByteBufferBuilder* _builder;

  BSONGenerator();

  IByteBuffer* createBuffer();

  std::string _currentKey;

  std::vector<unsigned int> _positionsStack;

  void addCurrentKey();

public:
  virtual ~BSONGenerator();

  static IByteBuffer* generate(const JSONBaseObject* value);

  void visitBoolean(const JSONBoolean* value);
//  void visitNumber(const JSONNumber* value);
  void visitDouble (const JSONDouble*  value);
  void visitFloat  (const JSONFloat*   value);
  void visitInteger(const JSONInteger* value);
  void visitLong   (const JSONLong*    value);

  void visitString(const JSONString* value);

  void visitNull();

  void visitArrayBeforeChildren(const JSONArray* value);
  void visitArrayInBetweenChildren(const JSONArray* value);
  void visitArrayBeforeChild(const JSONArray* value,
                             int i);
  void visitArrayAfterChildren(const JSONArray* value);

  void visitObjectBeforeChildren(const JSONObject* value);
  void visitObjectInBetweenChildren(const JSONObject* value);
  void visitObjectBeforeChild(const JSONObject* value,
                              const std::string& key);
  void visitObjectAfterChildren(const JSONObject* value);

};

#endif
