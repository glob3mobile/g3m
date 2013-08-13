package org.glob3.mobile.generated; 
//
//  JSONBaseObject.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  JSONBaseObject.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 17/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//class JSONObject;
//class JSONArray;
//class JSONBoolean;
//class JSONNumber;
//class JSONString;
//class JSONNull;
//class JSONVisitor;



public abstract class JSONBaseObject extends Disposable
{


  public static JSONBaseObject deepCopy(JSONBaseObject object)
  {
    return (object == null) ? null : object.deepCopy();
  }

  public void dispose()
  {
    JAVA_POST_DISPOSE
  }

  public JSONObject asObject()
  {
    ILogger.instance().logError("The requested Object is not of type JSONObject!");
    return null;
  }
  public JSONArray asArray()
  {
    ILogger.instance().logError("The requested Object is not of type JSONArray!");
    return null;
  }
  public JSONBoolean asBoolean()
  {
    ILogger.instance().logError("The requested Object is not of type JSONBoolean!");
    return null;
  }
  public JSONNumber asNumber()
  {
    ILogger.instance().logError("The requested Object is not of type JSONNumber!");
    return null;
  }
  public JSONString asString()
  {
    ILogger.instance().logError("The requested Object is not of type JSONString!");
    return null;
  }

  public JSONNull asNull()
  {
    return null;
  }


  public abstract JSONBaseObject deepCopy();

  public abstract String description();

  public abstract void acceptVisitor(JSONVisitor visitor);

}