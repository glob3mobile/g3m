package org.glob3.mobile.generated; 
//
//  IIntBuffer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

//
//  IIntBuffer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//



public abstract class IIntBuffer implements IBuffer
{

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int get(int i) const = 0;
  public abstract int get(int i);

//  virtual void put(int i, int value) = 0;

  public abstract void rawPut(int i, int value);

}