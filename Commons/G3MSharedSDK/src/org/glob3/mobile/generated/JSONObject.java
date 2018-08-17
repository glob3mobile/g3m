package org.glob3.mobile.generated;import java.util.*;

//
//  JSONObject.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 01/10/12.
//

//
//  JSONObject.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 01/10/12.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IStringBuilder;

public class JSONObject extends JSONBaseObject
{
  private java.util.HashMap<String, JSONBaseObject> _entries = new java.util.HashMap<String, JSONBaseObject>();


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void putKeyAndValueDescription(const String& key, IStringBuilder* isb) const
  private void putKeyAndValueDescription(String key, IStringBuilder isb)
  {
	isb.addString("\"");
	isb.addString(key);
	isb.addString("\":");
	isb.addString((get(key) == null) ? "null" : get(key).description());
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void putKeyAndValueToString(const String& key, IStringBuilder* isb) const
  private void putKeyAndValueToString(String key, IStringBuilder isb)
  {
	isb.addString("\"");
	isb.addString(key);
	isb.addString("\":");
	isb.addString((get(key) == null) ? "null" : get(key).toString());
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	for (java.util.Iterator<String, JSONBaseObject> it = _entries.iterator(); it.hasNext();)
	{
	  it.next().getValue() = null;
	}
//#endif
	_entries.clear();
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONObject* asObject() const
  public final JSONObject asObject()
  {
	return this;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONBaseObject* get(const String& key) const
  public final JSONBaseObject get(String key)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	java.util.Iterator<String, JSONBaseObject> it = _entries.find(key);
	if (it.hasNext())
	{
	  return _entries.at(key);
	}
	//ILogger::instance()->logError("The JSONObject does not contain the key \"" + key + "\"");
	return null;
//#endif
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	return _entries.get(key);
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONObject* getAsObject(const String& key) const
  public final JSONObject getAsObject(String key)
  {
	final JSONBaseObject object = get(key);
	return (object == null) ? null : object.asObject();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONArray* getAsArray(const String& key) const
  public final JSONArray getAsArray(String key)
  {
	final JSONBaseObject object = get(key);
	return (object == null) ? null : object.asArray();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONBoolean* getAsBoolean(const String& key) const
  public final JSONBoolean getAsBoolean(String key)
  {
	final JSONBaseObject object = get(key);
	return (object == null) ? null : object.asBoolean();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONNumber* getAsNumber(const String& key) const
  public final JSONNumber getAsNumber(String key)
  {
	final JSONBaseObject object = get(key);
	return (object == null) ? null : object.asNumber();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONString* getAsString(const String& key) const
  public final JSONString getAsString(String key)
  {
	final JSONBaseObject object = get(key);
	return (object == null) ? null : object.asString();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean getAsBoolean(const String& key, boolean defaultValue) const
  public final boolean getAsBoolean(String key, boolean defaultValue)
  {
	final JSONBaseObject jsValue = get(key);
	if ((jsValue == null) || (jsValue.asNull() != null))
	{
	  return defaultValue;
	}
  
	final JSONBoolean jsBool = jsValue.asBoolean();
	return (jsBool == null) ? defaultValue : jsBool.value();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double getAsNumber(const String& key, double defaultValue) const
  public final double getAsNumber(String key, double defaultValue)
  {
	final JSONBaseObject jsValue = get(key);
	if ((jsValue == null) || (jsValue.asNull() != null))
	{
	  return defaultValue;
	}
  
	final JSONNumber jsNumber = jsValue.asNumber();
	return (jsNumber == null) ? defaultValue : jsNumber.value();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getAsString(const String& key, const String& defaultValue) const
  public final String getAsString(String key, String defaultValue)
  {
	final JSONBaseObject jsValue = get(key);
	if ((jsValue == null) || (jsValue.asNull() != null))
	{
	  return defaultValue;
	}
  
	final JSONString jsString = jsValue.asString();
	return (jsString == null) ? defaultValue : jsString.value();
  }

  public final void put(String key, JSONBaseObject object)
  {
	_entries.put(key, object);
  }

  public final void put(String key, String value)
  {
	_entries.put(key, new JSONString(value));
  }

  public final void put(String key, double value)
  {
	_entries.put(key, new JSONDouble(value));
  }

  public final void put(String key, float value)
  {
	_entries.put(key, new JSONFloat(value));
  }

  public final void put(String key, int value)
  {
	_entries.put(key, new JSONInteger(value));
  }

  public final void put(String key, long value)
  {
	_entries.put(key, new JSONLong(value));
  }

  public final void put(String key, boolean value)
  {
	_entries.put(key, new JSONBoolean(value));
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
//#if C_CODE
	java.util.ArrayList<String> result = new java.util.ArrayList<String>();
  
	java.util.Iterator<String, JSONBaseObject> it = _entries.iterator();
	while (it.hasNext())
	{
	  result.add(it.next().getKey());
	}
  
	return result;
//#endif
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
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String toString() const
  public final String toString()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
  
	isb.addString("{");
  
	java.util.ArrayList<String> keys = this.keys();
  
	int keysCount = keys.size();
	if (keysCount > 0)
	{
	  putKeyAndValueToString(keys.get(0), isb);
	  for (int i = 1; i < keysCount; i++)
	  {
		isb.addString(", ");
		putKeyAndValueToString(keys.get(i), isb);
	  }
	}
  
	isb.addString("}");
  
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONObject* deepCopy() const
  public final JSONObject deepCopy()
  {
	JSONObject result = new JSONObject();
  
	java.util.ArrayList<String> keys = this.keys();
  
	int keysCount = keys.size();
	for (int i = 0; i < keysCount; i++)
	{
	  String key = keys.get(i);
	  result.put(key, JSONBaseObject.deepCopy(get(key)));
	}
  
	return result;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void acceptVisitor(JSONVisitor* visitor) const
  public final void acceptVisitor(JSONVisitor visitor)
  {
	visitor.visitObjectBeforeChildren(this);
  
	java.util.ArrayList<String> keys = this.keys();
  
	int keysCount = keys.size();
	for (int i = 0; i < keysCount; i++)
	{
	  if (i != 0)
	  {
		visitor.visitObjectInBetweenChildren(this);
	  }
	  String key = keys.get(i);
	  visitor.visitObjectBeforeChild(this, key);
	  final JSONBaseObject child = get(key);
	  if(child != null)
	  {
		child.acceptVisitor(visitor);
	  }
	}
  
	visitor.visitObjectAfterChildren(this);
  }

}
