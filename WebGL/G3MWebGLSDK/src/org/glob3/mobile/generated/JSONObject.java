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

  public final JSONObject asObject()
  {
	return this;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONBaseObject* get(const String& key) const
  public final JSONBaseObject get(String key)
  {
  
	return _entries.get(key);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONObject* getAsObject(const String& key) const
  public final JSONObject getAsObject(String key)
  {
	return get(key).asObject();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONArray* getAsArray(const String& key) const
  public final JSONArray getAsArray(String key)
  {
	return get(key).asArray();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONBoolean* getAsBoolean(const String& key) const
  public final JSONBoolean getAsBoolean(String key)
  {
	return get(key).asBoolean();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONNumber* getAsNumber(const String& key) const
  public final JSONNumber getAsNumber(String key)
  {
	return get(key).asNumber();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONString* getAsString(const String& key) const
  public final JSONString getAsString(String key)
  {
	return get(key).asString();
  }

  public final void put(String key, JSONBaseObject object)
  {
	_entries.put(key, object);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int size() const
  public final int size()
  {
	return _entries.size();
  }

}