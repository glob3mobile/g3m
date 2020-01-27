//
//  JSONGenerator.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/3/13.
//
//

#ifndef __G3M__JSONGenerator__
#define __G3M__JSONGenerator__

#include "JSONVisitor.hpp"
class JSONBaseObject;
class IStringBuilder;

class JSONGenerator : public JSONVisitor {
private:
  IStringBuilder* _isb;

  JSONGenerator(const int floatPrecision);

  const std::string getString();

public:
  virtual ~JSONGenerator();

  static const std::string generate(const JSONBaseObject* value,
                                    const int floatPrecision);

  void visitDouble (const JSONDouble*  value);
  void visitFloat  (const JSONFloat*   value);
  void visitInteger(const JSONInteger* value);
  void visitLong   (const JSONLong*    value);

  void visitBoolean(const JSONBoolean* value);
  void visitString(const JSONString* value);

  void visitNull();

  void visitArrayBeforeChildren(const JSONArray* value);
  void visitArrayInBetweenChildren(const JSONArray* value);
  void visitArrayBeforeChild(const JSONArray* value,
                             size_t i);
  void visitArrayAfterChildren(const JSONArray* value);

  void visitObjectBeforeChildren(const JSONObject* value);
  void visitObjectInBetweenChildren(const JSONObject* value);
  void visitObjectBeforeChild(const JSONObject* value,
                              const std::string& key);
  void visitObjectAfterChildren(const JSONObject* value);
  
};

#endif
