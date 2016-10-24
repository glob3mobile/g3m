package org.glob3.mobile.generated; 
//
//  Future.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/24/16.
//
//

//
//  Future.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/24/16.
//
//


//C++ TO JAVA CONVERTER TODO TASK: The original C++ template specifier was replaced with a Java generic specifier, which may not produce the same behavior:
public class Future<T>
{
  private T _value;
  private boolean _isResolved;
  private boolean _isCanceled;

  public Future()
  {
     _value = null;
     _isResolved = false;
     _isCanceled = false;
  }

  public Future(T value)
  {
     _value = value;
     _isResolved = true;
     _isCanceled = true;
  }

  public final T get()
  {
    if (_isResolved)
    {
      return _value;
    }
    if (_isCanceled)
    {
      throw new RuntimeException("The future was canceled");
    }
    throw new RuntimeException("Can't get the future's value before it gets resolved");
  }

  public final boolean isResolved()
  {
    return _isResolved;
  }
  public final boolean isCanceled()
  {
    return _isCanceled;
  }

}