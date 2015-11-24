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



//class IStringBuilder;

public class JSONObject extends JSONBaseObject
{
  private java.util.HashMap<String, JSONBaseObject> _entries = new java.util.HashMap<String, JSONBaseObject>();


  private void putKeyAndValueDescription(String key, IStringBuilder isb)
  {
    isb.addString("\"");
    isb.addString(key);
    isb.addString("\":");
    isb.addString((get(key) == null) ? "null" : get(key).description());
  }
  private void putKeyAndValueToString(String key, IStringBuilder isb)
  {
    isb.addString("\"");
    isb.addString(key);
    isb.addString("\":");
    isb.addString((get(key) == null) ? "null" : get(key).toString());
  }

  public void dispose()
  {
    _entries.clear();
  
    super.dispose();
  
  }

  public final JSONObject asObject()
  {
    return this;
  }

  public final JSONBaseObject get(String key)
  {
  
    return _entries.get(key);
  }

  public final JSONObject getAsObject(String key)
  {
    final JSONBaseObject object = get(key);
    return (object == null) ? null : object.asObject();
  }
  public final JSONArray getAsArray(String key)
  {
    final JSONBaseObject object = get(key);
    return (object == null) ? null : object.asArray();
  }
  public final JSONBoolean getAsBoolean(String key)
  {
    final JSONBaseObject object = get(key);
    return (object == null) ? null : object.asBoolean();
  }
  public final JSONNumber getAsNumber(String key)
  {
    final JSONBaseObject object = get(key);
    return (object == null) ? null : object.asNumber();
  }
  public final JSONString getAsString(String key)
  {
    final JSONBaseObject object = get(key);
    return (object == null) ? null : object.asString();
  }

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

  public final int size()
  {
    return _entries.size();
  }

  public final java.util.ArrayList<String> keys()
  {
    return new java.util.ArrayList<String>(_entries.keySet());
  }

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