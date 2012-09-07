package org.glob3.mobile.generated; 
//
//  IntBufferBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

//
//  IntBufferBuilder.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IIntBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFactory;

public class IntBufferBuilder
{
  private java.util.ArrayList<Integer> _values = new java.util.ArrayList<Integer>();


  public final void add(int value)
  {
	_values.add(value);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IIntBuffer* create() const
  public final IIntBuffer create()
  {
	final int size = _values.size();
  
	IIntBuffer result = IFactory.instance().createIntBuffer(size);
  
	for (int i = 0; i < size; i++)
	{
	  result.put(i, _values.get(i));
	}
  
	return result;
  }

}