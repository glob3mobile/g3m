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
  }

  public JSONString(String value)
  {
	  _value = value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String value() const
  public final String value()
  {
	return _value;
  }

  public final JSONString asString()
  {
	return this;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
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

}