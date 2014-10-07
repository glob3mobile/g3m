package org.glob3.mobile.generated; 
//
//  JSONNumber.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public abstract class JSONNumber extends JSONBaseObject
{
  public abstract double value();

  public final JSONNumber asNumber()
  {
    return this;
  }

}