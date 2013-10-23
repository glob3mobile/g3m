package org.glob3.mobile.generated; 
//
//  IBuffer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

//
//  IBuffer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//




public abstract class IBuffer
{

  private static long _nextbufferID = 0;

  private final long _id;

  void dispose(){}

  public IBuffer()
  {
     _id = _nextbufferID++;
  }

  public final long getID()
  {
    return _id;
  }

  /**
   Answer the size (the count of elements) of the buffer
   **/
  public abstract int size();

  /**
   Answer the timestamp of the buffer.
   
   This number will be different each time the buffer changes its contents.
   It provides a fast method to check if the Buffer has changed.
   **/
  public abstract int timestamp();

  public abstract String description();

}