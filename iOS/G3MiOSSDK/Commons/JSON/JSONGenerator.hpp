//
//  JSONGenerator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/13.
//
//

#ifndef __G3MiOSSDK__JSONGenerator__
#define __G3MiOSSDK__JSONGenerator__

#include "JSONVisitor.hpp"
class JSONBaseObject;
class IStringBuilder;

class JSONGenerator : public JSONVisitor {
private:
  IStringBuilder* _isb;

  JSONGenerator();

  std::string getString();

public:
  virtual ~JSONGenerator();

  static std::string generate(const JSONBaseObject* value);

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
                             int i);
  void visitArrayAfterChildren(const JSONArray* value);

  void visitObjectBeforeChildren(const JSONObject* value);
  void visitObjectInBetweenChildren(const JSONObject* value);
  void visitObjectBeforeChild(const JSONObject* value,
                              const std::string& key);
  void visitObjectAfterChildren(const JSONObject* value);
  
};

#endif
