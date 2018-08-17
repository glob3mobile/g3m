package org.glob3.mobile.generated;import java.util.*;

//
//  JSONNull.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/19/13.
//
//

//
//  JSONNull.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/19/13.
//
//




public class JSONNull extends JSONBaseObject
{

  public JSONNull()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONNull* asNull() const
  public final JSONNull asNull()
  {
	return this;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	return "null";
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String toString() const
  public final String toString()
  {
	return "null";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: JSONNull* deepCopy() const
  public final JSONNull deepCopy()
  {
	return new JSONNull();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void acceptVisitor(JSONVisitor* visitor) const
  public final void acceptVisitor(JSONVisitor visitor)
  {
	visitor.visitNull();
  }

}
