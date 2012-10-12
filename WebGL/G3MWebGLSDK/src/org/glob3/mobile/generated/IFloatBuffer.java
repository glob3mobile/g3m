package org.glob3.mobile.generated; 
//
//  IFloatBuffer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

//
//  IFloatBuffer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//



public abstract class IFloatBuffer implements IBuffer
{

  public void dispose()
  {

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float get(int i) const = 0;
  public abstract float get(int i);

//  virtual void put(int i, float value) = 0;

  public abstract void rawPut(int i, float value);

}