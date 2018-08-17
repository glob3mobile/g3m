package org.glob3.mobile.generated;import java.util.*;

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
//



public class JSONBoolean extends JSONBaseObject
{
  private final boolean _value;

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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONBoolean* asBoolean() const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String toString() const
  public final String toString()
  {
	return _value ? "true" : "false";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONBoolean* deepCopy() const
  public final JSONBoolean deepCopy()
  {
	return new JSONBoolean(_value);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void acceptVisitor(JSONVisitor* visitor) const
  public final void acceptVisitor(JSONVisitor visitor)
  {
	visitor.visitBoolean(this);
  }

}
