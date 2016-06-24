package org.glob3.mobile.generated; 
//
//  JSONDouble.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//

//
//  JSONDouble.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//



public class JSONDouble extends JSONNumber
{
  private final double _value;

  public JSONDouble(double value)
  {
     _value = value;
  }

  public final JSONDouble deepCopy()
  {
    return new JSONDouble(_value);
  }

  public final double value()
  {
    return _value;
  }

  public final double doubleValue()
  {
    return _value;
  }

  public final void acceptVisitor(JSONVisitor visitor)
  {
    visitor.visitDouble(this);
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("double/");
    isb.addDouble(_value);
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
  public final String toString()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addDouble(_value);
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

}