package org.glob3.mobile.generated;
//
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

  public abstract float get(int i);

  public abstract void put(int i, float value);

  public final void putVector3D(int i, Vector3D v)
  {
    final int i3 = i *3;
    put(i3, (float) v._x);
    put(i3 + 1, (float) v._y);
    put(i3 + 2, (float) v._z);
  }

  public abstract void rawPut(int i, float value);

  public abstract void rawAdd(int i, float value);

  public abstract void rawPut(int i, IFloatBuffer srcBuffer, int srcFromIndex, int count);

  public final void rawPut(int i, IFloatBuffer srcBuffer)
  {
    rawPut(i, srcBuffer, 0, srcBuffer.size());
  }

}
