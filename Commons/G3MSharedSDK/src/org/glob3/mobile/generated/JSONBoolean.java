package org.glob3.mobile.generated; 
//
//  JSONBoolean.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/7/12.
//
//

//
//  JSONBoolean.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class JSONBoolean extends JSONBaseObject
{
  private final boolean _value;

  public JSONBoolean(boolean value)
  {
     _value = value;
  }

  public final boolean value()
  {
    return _value;
  }

  public final JSONBoolean asBoolean()
  {
    return this;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addBool(_value);
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final String toString()
  {
    return _value ? "true" : "false";
  }

  public final JSONBoolean deepCopy()
  {
    return new JSONBoolean(_value);
  }

  public final void acceptVisitor(JSONVisitor visitor)
  {
    visitor.visitBoolean(this);
  }

}