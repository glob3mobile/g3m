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
	_entries.clear();
  
  }
  public final JSONObject getObject()
  {
	return this;
  }

  public final JSONBaseObject getObjectForKey(String key)
  {
  
	return _entries.get(key);
  }

  public final void putObject(String key, JSONBaseObject object)
  {
	_entries.put(key, object);
  }

  public final int getSize()
  {
	return _entries.size();
  }

}