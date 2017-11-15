package org.glob3.mobile.generated; 
//
//  JSONFloat.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//

//
//  JSONFloat.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//



public class JSONFloat extends JSONNumber
{
  private final float _value;

  public JSONFloat(float value)
  {
     _value = value;
  }

  public final JSONFloat deepCopy()
  {
    return new JSONFloat(_value);
  }

  public final double value()
  {
    return _value;
  }

  public final float floatValue()
  {
    return _value;
  }

  public final void acceptVisitor(JSONVisitor visitor)
  {
    visitor.visitFloat(this);
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("float/");
    isb.addFloat(_value);
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final String toString()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addFloat(_value);
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

}