package org.glob3.mobile.generated; 
//
//  JSONNull.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/19/13.
//
//

//
//  JSONNull.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/19/13.
//
//




public class JSONNull extends JSONBaseObject
{

  public JSONNull()
  {
  }

  public final JSONNull asNull()
  {
    return this;
  }

  public final String description()
  {
    return "null";
  }
  public final String toString()
  {
    return "null";
  }

  public final JSONNull deepCopy()
  {
    return new JSONNull();
  }

  public final void acceptVisitor(JSONVisitor visitor)
  {
    visitor.visitNull();
  }

}