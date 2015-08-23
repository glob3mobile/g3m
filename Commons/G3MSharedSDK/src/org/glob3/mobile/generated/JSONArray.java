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
  
    super.dispose();
  
  }

  public final JSONBaseObject get(int index)
  {
    return _entries.get(index);
  }

  public final JSONObject getAsObject(int index)
  {
    final JSONBaseObject object = get(index);
    return (object == null) ? null : object.asObject();
  }
  public final JSONArray getAsArray(int index)
  {
    final JSONBaseObject object = get(index);
    return (object == null) ? null : object.asArray();
  }
  public final JSONBoolean getAsBoolean(int index)
  {
    final JSONBaseObject object = get(index);
    return (object == null) ? null : object.asBoolean();
  }
  public final JSONNumber getAsNumber(int index)
  {
    final JSONBaseObject object = get(index);
    return (object == null) ? null : object.asNumber();
  }
  public final JSONString getAsString(int index)
  {
    final JSONBaseObject object = get(index);
    return (object == null) ? null : object.asString();
  }

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

  public final boolean getAsBoolean(int index, boolean defaultValue)
  {
    final JSONBoolean jsBool = getAsBoolean(index);
    return (jsBool == null) ? defaultValue : jsBool.value();
  }

  public final double getAsNumber(int index, double defaultValue)
  {
    final JSONNumber jsNumber = getAsNumber(index);
    return (jsNumber == null) ? defaultValue : jsNumber.value();
  }

  public final String getAsString(int index, String defaultValue)
  {
    final JSONString jsString = getAsString(index);
    return (jsString == null) ? defaultValue : jsString.value();
  }

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

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    final int size = this.size();
  
    isb.addString("[");
  
    if (size > 0)
    {
      isb.addString((this.get(0) == null) ? "null" : this.get(0).description());
  
      if (size <= 10)
      {
        for (int i = 1; i < size; i++)
        {
          isb.addString(", ");
          isb.addString((this.get(i) == null) ? "null" : this.get(i).description());
        }
      }
      else
      {
        for (int i = 1; i < 10; i++)
        {
          isb.addString(", ");
          isb.addString((this.get(i) == null) ? "null" : this.get(i).description());
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
  public final String toString()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    final int size = this.size();
  
    isb.addString("[");
  
    if (size > 0)
    {
      isb.addString((this.get(0) == null) ? "null" : this.get(0).toString());
  
      if (size <= 10)
      {
        for (int i = 1; i < size; i++)
        {
          isb.addString(", ");
          isb.addString((this.get(i) == null) ? "null" : this.get(i).toString());
        }
      }
      else
      {
        for (int i = 1; i < 10; i++)
        {
          isb.addString(", ");
          isb.addString((this.get(i) == null) ? "null" : this.get(i).toString());
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