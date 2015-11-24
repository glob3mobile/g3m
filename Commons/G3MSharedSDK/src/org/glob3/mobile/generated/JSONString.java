package org.glob3.mobile.generated; 
//
//  JSONString.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/7/12.
//
//

//
//  JSONString.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class JSONString extends JSONBaseObject
{
  private final String _value;

  public void dispose()
  {
  super.dispose();

  }

  public JSONString(String value)
  {
     _value = value;
  }

  public final String value()
  {
    return _value;
  }

  public final JSONString asString()
  {
    return this;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("\"");
    isb.addString(_value);
    isb.addString("\"");
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final String toString()
  {
    return _value;
  }

  public final JSONString deepCopy()
  {
    return new JSONString(_value);
  }

  public final void acceptVisitor(JSONVisitor visitor)
  {
    visitor.visitString(this);
  }

}