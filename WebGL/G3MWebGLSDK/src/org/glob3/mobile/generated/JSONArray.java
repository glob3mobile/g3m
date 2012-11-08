package org.glob3.mobile.generated; 
//
//  JSONArray.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  JSONArray.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public class JSONArray extends JSONBaseObject
{
  private java.util.ArrayList<JSONBaseObject> _entries = new java.util.ArrayList<JSONBaseObject>();

  public final JSONArray asArray()
  {
	return this;
  }

  public void dispose()
  {
	for (int i = 0; i < _entries.size(); i++)
	{
	  if (_entries.get(i) != null)
		  _entries.get(i).dispose();
	}
	_entries.clear();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONBaseObject* get(const int index) const
  public final JSONBaseObject get(int index)
  {
	return _entries.get(index);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONObject* getAsObject(const int index) const
  public final JSONObject getAsObject(int index)
  {
	return get(index).asObject();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONArray* getAsArray(const int index) const
  public final JSONArray getAsArray(int index)
  {
	return get(index).asArray();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONBoolean* getAsBoolean(const int index) const
  public final JSONBoolean getAsBoolean(int index)
  {
	return get(index).asBoolean();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONNumber* getAsNumber(const int index) const
  public final JSONNumber getAsNumber(int index)
  {
	return get(index).asNumber();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONString* getAsString(const int index) const
  public final JSONString getAsString(int index)
  {
	return get(index).asString();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int size() const
  public final int size()
  {
	return _entries.size();
  }

  public final void add(JSONBaseObject object)
  {
	_entries.add(object);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
  
	int size = this.size();
  
	isb.addString("[size=");
	isb.addInt(size);
  
	if (size > 0)
	{
	  isb.addString("/");
  
	  isb.addString(this.get(0).description());
  
	  if (size <= 10)
	  {
		for (int i = 1; i < size; i++)
		{
		  isb.addString(",");
		  isb.addString(this.get(i).description());
		}
	  }
	  else
	  {
		for (int i = 1; i < 10; i++)
		{
		  isb.addString(",");
		  isb.addString(this.get(i).description());
		}
		isb.addString(",...");
	  }
	}
  
	isb.addString("]");
  
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

}