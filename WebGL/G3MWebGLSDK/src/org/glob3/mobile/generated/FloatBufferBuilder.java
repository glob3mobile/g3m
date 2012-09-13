package org.glob3.mobile.generated; 
//
//  FloatBufferBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

//
//  FloatBufferBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFactory;

public class FloatBufferBuilder
{
  protected java.util.ArrayList<Float> _values = new java.util.ArrayList<Float>();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* create() const
  public final IFloatBuffer create()
  {
	final int size = _values.size();
  
	IFloatBuffer result = IFactory.instance().createFloatBuffer(size);
  
	for (int i = 0; i < size; i++)
	{
	  result.put(i, _values.get(i));
	}
  
	return result;
  }
}