package org.glob3.mobile.generated; 
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

  public abstract int get(int i);

  public abstract void put(int i, int value);

  public abstract void rawPut(int i, int value);

}