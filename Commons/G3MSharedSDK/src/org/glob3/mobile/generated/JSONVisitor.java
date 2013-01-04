package org.glob3.mobile.generated; 
//
//  JSONVisitor.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/13.
//
//

//
//  JSONVisitor.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONBoolean;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONNumber;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONString;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONArray;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONObject;


public abstract class JSONVisitor
{
  public void dispose()
  {

  }

  public abstract void visitBoolean(JSONBoolean value);
  public abstract void visitNumber(JSONNumber value);
  public abstract void visitString(JSONString value);

  public abstract void visitArrayBeforeChildren(JSONArray value);
  public abstract void visitArrayInBetweenChildren(JSONArray value);
  public abstract void visitArrayBeforeChild(JSONArray value, int i);
  public abstract void visitArrayAfterChildren(JSONArray value);

  public abstract void visitObjectBeforeChildren(JSONObject value);
  public abstract void visitObjectInBetweenChildren(JSONObject value);
  public abstract void visitObjectBeforeChild(JSONObject value, String key);
  public abstract void visitObjectAfterChildren(JSONObject value);

}