package org.glob3.mobile.generated; 
//
//  JSONLong.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//

//
//  JSONLong.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//



public class JSONLong extends JSONNumber
{
  private final long _value;

  public JSONLong(long value)
  {
     _value = value;
  }

  public final JSONLong deepCopy()
  {
    return new JSONLong(_value);
  }

  public final double value()
  {
    return _value;
  }

  public final long longValue()
  {
    return _value;
  }

  public final void acceptVisitor(JSONVisitor visitor)
  {
    visitor.visitLong(this);
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("long/");
    isb.addLong(_value);
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final String toString()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addLong(_value);
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

}