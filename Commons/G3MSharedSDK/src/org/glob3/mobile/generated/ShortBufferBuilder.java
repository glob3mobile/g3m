package org.glob3.mobile.generated; 
//
//  ShortBufferBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/19/13.
//
//

//
//  ShortBufferBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/19/13.
//
//



//class IShortBuffer;

public class ShortBufferBuilder {
  private java.util.ArrayList<Short> _values = new java.util.ArrayList<Short>();


  public final void add(short value) {
    _values.add(value);
  }

  public final IShortBuffer create() {
    final int size = _values.size();
  
    IShortBuffer result = IFactory.instance().createShortBuffer(size);
  
    for (int i = 0; i < size; i++) {
      result.rawPut(i, _values.get(i));
    }
  
    return result;
  }

}