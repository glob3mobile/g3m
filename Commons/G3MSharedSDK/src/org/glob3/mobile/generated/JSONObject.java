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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IStringBuilder;

public class JSONObject extends JSONBaseObject
{
  private java.util.HashMap<String, JSONBaseObject> _entries = new java.util.HashMap<String, JSONBaseObject>();


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void putKeyAndValueDescription(const String& key, IStringBuilder *isb) const
  private void putKeyAndValueDescription(String key, IStringBuilder isb)
  {
	isb.addString("\"");
	isb.addString(key);
	isb.addString("\"=");
	isb.addString(get(key).description());
  }

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
	JSONBaseObject object = get(key);
	return (object == null) ? null : object.asObject();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONArray* getAsArray(const String& key) const
  public final JSONArray getAsArray(String key)
  {
	JSONBaseObject object = get(key);
	return (object == null) ? null : object.asArray();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONBoolean* getAsBoolean(const String& key) const
  public final JSONBoolean getAsBoolean(String key)
  {
	JSONBaseObject object = get(key);
	return (object == null) ? null : object.asBoolean();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONNumber* getAsNumber(const String& key) const
  public final JSONNumber getAsNumber(String key)
  {
	JSONBaseObject object = get(key);
	return (object == null) ? null : object.asNumber();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONString* getAsString(const String& key) const
  public final JSONString getAsString(String key)
  {
	JSONBaseObject object = get(key);
	return (object == null) ? null : object.asString();
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<String> keys() const
  public final java.util.ArrayList<String> keys()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	return new java.util.ArrayList<String>(_entries.keySet());
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
  
	isb.addString("{");
  
	java.util.ArrayList<String> keys = this.keys();
  
	int keysCount = keys.size();
	if (keysCount > 0)
	{
	  putKeyAndValueDescription(keys.get(0), isb);
	  for (int i = 1; i < keysCount; i++)
	  {
		isb.addString(", ");
		putKeyAndValueDescription(keys.get(i), isb);
	  }
	}
  
	isb.addString("}");
  
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

}