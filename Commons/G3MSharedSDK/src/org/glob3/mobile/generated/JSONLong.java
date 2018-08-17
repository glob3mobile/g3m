package org.glob3.mobile.generated;import java.util.*;

//
//  JSONLong.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//

//
//  JSONLong.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//



public class JSONLong extends JSONNumber
{
  private final long _value;

  public JSONLong(long value)
  {
	  _value = value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONLong* deepCopy() const
  public final JSONLong deepCopy()
  {
	return new JSONLong(_value);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double value() const
  public final double value()
  {
	return _value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long longValue() const
  public final long longValue()
  {
	return _value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void acceptVisitor(JSONVisitor* visitor) const
  public final void acceptVisitor(JSONVisitor visitor)
  {
	visitor.visitLong(this);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
  
	isb.addString("long/");
	isb.addLong(_value);
  
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
  
	isb.addLong(_value);
  
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

}
