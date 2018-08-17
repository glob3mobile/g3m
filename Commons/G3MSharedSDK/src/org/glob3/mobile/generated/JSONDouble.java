package org.glob3.mobile.generated;import java.util.*;

//
//  JSONDouble.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//

//
//  JSONDouble.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//



public class JSONDouble extends JSONNumber
{
  private final double _value;

  public JSONDouble(double value)
  {
	  _value = value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONDouble* deepCopy() const
  public final JSONDouble deepCopy()
  {
	return new JSONDouble(_value);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double value() const
  public final double value()
  {
	return _value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double doubleValue() const
  public final double doubleValue()
  {
	return _value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void acceptVisitor(JSONVisitor* visitor) const
  public final void acceptVisitor(JSONVisitor visitor)
  {
	visitor.visitDouble(this);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
  
	isb.addString("double/");
	isb.addDouble(_value);
  
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
  
	isb.addDouble(_value);
  
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

}
