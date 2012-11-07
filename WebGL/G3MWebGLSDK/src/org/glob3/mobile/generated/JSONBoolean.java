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
  private boolean _value;

  public JSONBoolean(boolean value)
  {
	  _value = value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const boolean value() const
  public final boolean value()
  {
	return _value;
  }

  public final JSONBoolean asBoolean()
  {
	return this;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addBool(_value);
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

}