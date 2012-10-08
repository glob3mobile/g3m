package org.glob3.mobile.generated; 
//
//  JSONBoolean.h
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class JSONBoolean extends JSONBaseObject
{
  private boolean _value;

  public JSONBoolean(boolean value)
  {
	  _value = value;
  }
  public final boolean getValue()
  {
	return _value;
  }
  public final JSONBoolean getBoolean()
  {
	return this;
  }

}