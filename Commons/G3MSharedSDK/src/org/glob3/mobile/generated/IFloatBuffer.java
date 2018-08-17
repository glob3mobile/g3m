package org.glob3.mobile.generated;//
//  IFloatBuffer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//



public abstract class IFloatBuffer implements IBuffer
{

  public IFloatBuffer()
  {
	  super();
  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float get(int i) const = 0;
  public abstract float get(int i);

  public abstract void put(int i, float value);

  public abstract void rawPut(int i, float value);

  public abstract void rawAdd(int i, float value);

  public abstract void rawPut(int i, IFloatBuffer srcBuffer, int srcFromIndex, int count);

  public final void rawPut(int i, IFloatBuffer srcBuffer)
  {
	rawPut(i, srcBuffer, 0, srcBuffer.size());
  }

  public abstract void put(int i, IFloatBuffer srcBuffer, int srcFromIndex, int count);

  public final void put(int i, IFloatBuffer srcBuffer)
  {
	put(i, srcBuffer, 0, srcBuffer.size());
  }

}
