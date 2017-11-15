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




public interface IBuffer
{

  void dispose();

  long getID();

  /**
   Answer the size (the count of elements) of the buffer
   **/
  int size();

  /**
   Answer the timestamp of the buffer.
   
   This number will be different each time the buffer changes its contents.
   It provides a fast method to check if the Buffer has changed.
   **/
  int timestamp();

  String description();

}