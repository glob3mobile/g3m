package org.glob3.mobile.generated;import java.util.*;

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
//



public class JSONString extends JSONBaseObject
{
  private final String _value;

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif

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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONString* asString() const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String toString() const
  public final String toString()
  {
	return _value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONString* deepCopy() const
  public final JSONString deepCopy()
  {
	return new JSONString(_value);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void acceptVisitor(JSONVisitor* visitor) const
  public final void acceptVisitor(JSONVisitor visitor)
  {
	visitor.visitString(this);
  }

}
