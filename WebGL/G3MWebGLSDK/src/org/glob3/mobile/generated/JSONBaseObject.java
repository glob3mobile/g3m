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


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONArray;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONBoolean;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONNumber;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONString;


public abstract class JSONBaseObject
{

  public void dispose()
  {
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const String description() const = 0;
  public abstract String description();

}