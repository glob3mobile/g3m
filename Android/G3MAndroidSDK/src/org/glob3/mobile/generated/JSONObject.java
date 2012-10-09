package org.glob3.mobile.generated; 
//
//  JSONObject.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 01/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  JSONObject.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 01/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public class JSONObject extends JSONBaseObject
{
  private java.util.HashMap<String, JSONBaseObject> _entries = new java.util.HashMap<String, JSONBaseObject>();

  public void dispose()
  {
  }
  public final JSONObject getObject()
  {
	return this;
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  JSONBaseObject getObjectForKey(String key);

  public final void putObject(String key, JSONBaseObject object)
  {
	_entries.put(key, object);
  }

}