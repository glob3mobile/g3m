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



//class IFloatBuffer;

public class FloatBufferBuilder {
  protected java.util.ArrayList<Float> _values = new java.util.ArrayList<Float>();

  public final IFloatBuffer create() {
    final int size = _values.size();
  
    IFloatBuffer result = IFactory.instance().createFloatBuffer(size);
  
    for (int i = 0; i < size; i++) {
      result.rawPut(i, _values.get(i));
    }
  
    return result;
  }
}