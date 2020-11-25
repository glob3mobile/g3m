package org.glob3.mobile.generated;
//
//  JSONBaseObject.cpp
//  G3M
//
//  Created by Oliver Koehler on 02/10/12.
//

//
//  JSONBaseObject.hpp
//  G3M
//
//  Created by Oliver Koehler on 17/09/12.
//



//class JSONObject;
//class JSONArray;
//class JSONBoolean;
//class JSONNumber;
//class JSONString;
//class JSONNull;
//class JSONVisitor;


public abstract class JSONBaseObject
{


  public static JSONBaseObject deepCopy(JSONBaseObject object)
  {
    return (object == null) ? null : object.deepCopy();
  }

  public static String toString(JSONBaseObject object)
  {
    return (object == null) ? "null" : object.toString();
  }

  public void dispose()
  {
  }

  public JSONObject asObject()
  {
    //ILogger::instance()->logError("The requested Object is not of type JSONObject!");
    return null;
  }
  public JSONArray asArray()
  {
    //ILogger::instance()->logError("The requested Object is not of type JSONArray!");
    return null;
  }
  public JSONBoolean asBoolean()
  {
    //ILogger::instance()->logError("The requested Object is not of type JSONBoolean!");
    return null;
  }
  public JSONNumber asNumber()
  {
    //ILogger::instance()->logError("The requested Object is not of type JSONNumber!");
    return null;
  }
  public JSONString asString()
  {
    //ILogger::instance()->logError("The requested Object is not of type JSONString!");
    return null;
  }
  public JSONNull asNull()
  {
    //ILogger::instance()->logError("The requested Object is not of type JSONNull!");
    return null;
  }

  public abstract JSONBaseObject deepCopy();

  public abstract String description();

  public abstract void acceptVisitor(JSONVisitor visitor);

  public abstract String toString();

}