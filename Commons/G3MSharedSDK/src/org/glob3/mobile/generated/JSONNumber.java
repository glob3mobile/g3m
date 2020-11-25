package org.glob3.mobile.generated;
//
//  JSONNumber.cpp
//  G3M
//
//  Created by Oliver Koehler on 03/10/12.
//


//
//  JSONNumber.hpp
//  G3M
//
//  Created by Oliver Koehler on 03/10/12.
//




public abstract class JSONNumber extends JSONBaseObject
{
  public abstract double value();

  public final JSONNumber asNumber()
  {
    return this;
  }

  public final JSONBoolean asBoolean()
  {
    return new JSONBoolean(value() != 0);
  }

}