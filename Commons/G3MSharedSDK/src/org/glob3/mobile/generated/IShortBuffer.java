package org.glob3.mobile.generated;//
//  IShortBuffer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/18/13.
//
//



public abstract class IShortBuffer implements IBuffer
{

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual short get(int i) const = 0;
  public abstract short get(int i);

  public abstract void put(int i, short value);

  public abstract void rawPut(int i, short value);

}
