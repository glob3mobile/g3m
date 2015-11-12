package org.glob3.mobile.generated; 
//
//  JSONInteger.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//

//
//  JSONInteger.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//



public class JSONInteger extends JSONNumber
{
  private final int _value;

  public JSONInteger(int value)
  {
     _value = value;
  }

  public final JSONInteger deepCopy()
  {
    return new JSONInteger(_value);
  }

  public final double value()
  {
    return _value;
  }

  public final int intValue()
  {
    return _value;
  }

  public final void acceptVisitor(JSONVisitor visitor)
  {
    visitor.visitInteger(this);
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("int/");
    isb.addInt(_value);
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final String toString()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addInt(_value);
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

}