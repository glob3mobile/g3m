package org.glob3.mobile.generated;import java.util.*;

//
//  JSONInteger.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//

//
//  JSONInteger.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//



public class JSONInteger extends JSONNumber
{
  private final int _value;

  public JSONInteger(int value)
  {
	  _value = value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONInteger* deepCopy() const
  public final JSONInteger deepCopy()
  {
	return new JSONInteger(_value);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double value() const
  public final double value()
  {
	return _value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int intValue() const
  public final int intValue()
  {
	return _value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void acceptVisitor(JSONVisitor* visitor) const
  public final void acceptVisitor(JSONVisitor visitor)
  {
	visitor.visitInteger(this);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
  
	isb.addString("int/");
	isb.addInt(_value);
  
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String toString() const
  public final String toString()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
  
	isb.addInt(_value);
  
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

}
