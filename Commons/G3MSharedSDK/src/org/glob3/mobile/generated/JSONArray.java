package org.glob3.mobile.generated;import java.util.*;

//
//  JSONArray.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//

//
//  JSONArray.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//




public class JSONArray extends JSONBaseObject
{
  private java.util.ArrayList<JSONBaseObject> _entries = new java.util.ArrayList<JSONBaseObject>();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONArray* asArray() const
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
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONBaseObject* get(const int index) const
  public final JSONBaseObject get(int index)
  {
	return _entries.get(index);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONObject* getAsObject(const int index) const
  public final JSONObject getAsObject(int index)
  {
	final JSONBaseObject object = get(index);
	return (object == null) ? null : object.asObject();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONArray* getAsArray(const int index) const
  public final JSONArray getAsArray(int index)
  {
	final JSONBaseObject object = get(index);
	return (object == null) ? null : object.asArray();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONBoolean* getAsBoolean(const int index) const
  public final JSONBoolean getAsBoolean(int index)
  {
	final JSONBaseObject object = get(index);
	return (object == null) ? null : object.asBoolean();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONNumber* getAsNumber(const int index) const
  public final JSONNumber getAsNumber(int index)
  {
	final JSONBaseObject object = get(index);
	return (object == null) ? null : object.asNumber();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONString* getAsString(const int index) const
  public final JSONString getAsString(int index)
  {
	final JSONBaseObject object = get(index);
	return (object == null) ? null : object.asString();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<String> asStringVector() const
  public final java.util.ArrayList<String> asStringVector()
  {
	java.util.ArrayList<String> result = new java.util.ArrayList<String>();
	final int size = this.size();
	for (int i = 0; i < size; i++)
	{
	  result.add(getAsString(i).value());
	}
	return result;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean getAsBoolean(const int index, boolean defaultValue) const
  public final boolean getAsBoolean(int index, boolean defaultValue)
  {
	final JSONBoolean jsBool = getAsBoolean(index);
	return (jsBool == null) ? defaultValue : jsBool.value();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double getAsNumber(const int index, double defaultValue) const
  public final double getAsNumber(int index, double defaultValue)
  {
	final JSONNumber jsNumber = getAsNumber(index);
	return (jsNumber == null) ? defaultValue : jsNumber.value();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getAsString(const int index, const String& defaultValue) const
  public final String getAsString(int index, String defaultValue)
  {
	final JSONString jsString = getAsString(index);
	return (jsString == null) ? defaultValue : jsString.value();
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
  public final void add(String value)
  {
	_entries.add(new JSONString(value));
  }
  public final void add(double value)
  {
	_entries.add(new JSONDouble(value));
  }
  public final void add(float value)
  {
	_entries.add(new JSONFloat(value));
  }
  public final void add(int value)
  {
	_entries.add(new JSONInteger(value));
  }
  public final void add(long value)
  {
	_entries.add(new JSONLong(value));
  }
  public final void add(boolean value)
  {
	_entries.add(new JSONBoolean(value));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
  
	final int size = this.size();
  
	isb.addString("[");
  
	if (size > 0)
	{
	  isb.addString((get(0) == null) ? "null" : get(0).description());
  
	  if (size <= 10)
	  {
		for (int i = 1; i < size; i++)
		{
		  isb.addString(", ");
		  isb.addString((get(i) == null) ? "null" : get(i).description());
		}
	  }
	  else
	  {
		for (int i = 1; i < 10; i++)
		{
		  isb.addString(", ");
		  isb.addString((get(i) == null) ? "null" : get(i).description());
		}
		isb.addString(", ...");
		isb.addString(" size=");
		isb.addLong(size);
  
	  }
	}
  
	isb.addString("]");
  
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
  
	final int size = this.size();
  
	isb.addString("[");
  
	if (size > 0)
	{
	  isb.addString((get(0) == null) ? "null" : get(0).toString());
  
	  if (size <= 10)
	  {
		for (int i = 1; i < size; i++)
		{
		  isb.addString(", ");
		  isb.addString((get(i) == null) ? "null" : get(i).toString());
		}
	  }
	  else
	  {
		for (int i = 1; i < 10; i++)
		{
		  isb.addString(", ");
		  isb.addString((get(i) == null) ? "null" : get(i).toString());
		}
		isb.addString(", ...");
		isb.addString(" size=");
		isb.addLong(size);
	  }
	}
  
	isb.addString("]");
  
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONArray* deepCopy() const
  public final JSONArray deepCopy()
  {
	JSONArray result = new JSONArray();
  
	final int size = this.size();
	for (int i = 0; i < size; i++)
	{
	  result.add(JSONBaseObject.deepCopy(get(i)));
	}
  
	return result;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void acceptVisitor(JSONVisitor* visitor) const
  public final void acceptVisitor(JSONVisitor visitor)
  {
	visitor.visitArrayBeforeChildren(this);
  
	final int size = this.size();
	for (int i = 0; i < size; i++)
	{
	  if (i != 0)
	  {
		visitor.visitArrayInBetweenChildren(this);
	  }
	  visitor.visitArrayBeforeChild(this, i);
	  if(get(i)!= null)
	  {
		  get(i).acceptVisitor(visitor);
	  }
	}
  
	visitor.visitArrayAfterChildren(this);
  }

}
