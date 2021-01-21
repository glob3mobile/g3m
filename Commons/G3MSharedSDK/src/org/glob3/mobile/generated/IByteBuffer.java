package org.glob3.mobile.generated;
//
//  IByteBuffer.hpp
//  G3M
//
//  Created by José Miguel S N on 10/09/12.
//




public abstract class IByteBuffer
{

  public void dispose()
  {
  }

  public abstract int size();

  public abstract int timestamp();

  public abstract byte get(int i);

  public abstract void put(int i, byte value);

  public abstract void rawPut(int i, byte value);

  public abstract String description();
  @Override
  public String toString() {
    return description();
  }

  public abstract String getAsString();

  public abstract IByteBuffer copy(int from, int length);

}