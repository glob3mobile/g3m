package org.glob3.mobile.generated; 
//
//  IntBufferBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

//
//  IntBufferBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//



//class IIntBuffer;

public class IntBufferBuilder
{
  private java.util.ArrayList<Integer> _values = new java.util.ArrayList<Integer>();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  IntBufferBuilder(IntBufferBuilder that);


  public final void add(int value)
  {
    _values.add(value);
  }

  public final IIntBuffer create()
  {
    final int size = _values.size();
  
    IIntBuffer result = IFactory.instance().createIntBuffer(size);
  
    for (int i = 0; i < size; i++)
    {
      result.rawPut(i, _values.get(i));
    }
  
    return result;
  }

}