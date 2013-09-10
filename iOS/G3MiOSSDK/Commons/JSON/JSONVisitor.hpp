//
//  JSONVisitor.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/13.
//
//

#ifndef __G3MiOSSDK__JSONVisitor__
#define __G3MiOSSDK__JSONVisitor__

class JSONBoolean;
//class JSONNumber;
class JSONString;
class JSONArray;
class JSONObject;
class JSONDouble;
class JSONFloat;
class JSONInteger;
class JSONLong;

#include <string>


class JSONVisitor {
public:
  virtual ~JSONVisitor() {
  }

//  virtual void visitNumber (const JSONNumber*  value) = 0;
  virtual void visitDouble (const JSONDouble*  value) = 0;
  virtual void visitFloat  (const JSONFloat*   value) = 0;
  virtual void visitInteger(const JSONInteger* value) = 0;
  virtual void visitLong   (const JSONLong*    value) = 0;

  virtual void visitBoolean(const JSONBoolean* value) = 0;
  virtual void visitString(const JSONString* value) = 0;

  virtual void visitNull() = 0;

  virtual void visitArrayBeforeChildren(const JSONArray* value) = 0;
  virtual void visitArrayInBetweenChildren(const JSONArray* value) = 0;
  virtual void visitArrayBeforeChild(const JSONArray* value,
                                     int i) = 0;
  virtual void visitArrayAfterChildren(const JSONArray* value) = 0;

  virtual void visitObjectBeforeChildren(const JSONObject* value) = 0;
  virtual void visitObjectInBetweenChildren(const JSONObject* value) = 0;
  virtual void visitObjectBeforeChild(const JSONObject* value,
                                      const std::string& key) = 0;
  virtual void visitObjectAfterChildren(const JSONObject* value) = 0;
  
};

#endif
