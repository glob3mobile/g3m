package org.glob3.mobile.generated; 
//
//  Future.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/31/13.
//
//

//
//  Future.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/31/13.
//
//



//C++ TO JAVA CONVERTER TODO TASK: The original C++ template specifier was replaced with a Java generic specifier, which may not produce the same behavior:
public abstract class Future<T>
{
  private T _value;

  protected Future()
  {
     _value = null;
  }

  protected final void set(T value)
  {
    _value = value;
  }

  public abstract T get();

  public void dispose()
  {
    _value = null;
  }

}