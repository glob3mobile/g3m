package org.glob3.mobile.generated;//
//  JSONVisitor.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONBoolean;
//class JSONNumber;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONString;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONArray;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONDouble;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONFloat;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONInteger;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONLong;



public abstract class JSONVisitor
{
  public void dispose()
  {
  }

//  virtual void visitNumber (const JSONNumber*  value) = 0;
  public abstract void visitDouble (JSONDouble value);
  public abstract void visitFloat (JSONFloat value);
  public abstract void visitInteger(JSONInteger value);
  public abstract void visitLong (JSONLong value);

  public abstract void visitBoolean(JSONBoolean value);
  public abstract void visitString(JSONString value);

  public abstract void visitNull();

  public abstract void visitArrayBeforeChildren(JSONArray value);
  public abstract void visitArrayInBetweenChildren(JSONArray value);
  public abstract void visitArrayBeforeChild(JSONArray value, int i);
  public abstract void visitArrayAfterChildren(JSONArray value);

  public abstract void visitObjectBeforeChildren(JSONObject value);
  public abstract void visitObjectInBetweenChildren(JSONObject value);
  public abstract void visitObjectBeforeChild(JSONObject value, String key);
  public abstract void visitObjectAfterChildren(JSONObject value);

}
