package org.glob3.mobile.generated; 
//
//  Future.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/31/13.
//
//




//C++ TO JAVA CONVERTER TODO TASK: The original C++ template specifier was replaced with a Java generic specifier, which may not produce the same behavior:
public class Future<T>
{
  private T _value;
  private boolean _isDone;

  private boolean _isDoneWithError;
  private String _error;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Future(Future<T> that);

  protected Future()
  {
     _value = null;
     _isDone = false;
     _isDoneWithError = false;
  }

  protected final void done(T value)
  {
    _value = value;
    _isDone = true;
    _isDoneWithError = false;
  }

  protected final void error(String error)
  {
    _error = error;
    _isDone = true;
    _isDoneWithError = true;
  }

  public void dispose()
  {
    _value = null;
  }

  public final T get()
  {
    return _value;
  }

  public final boolean isDone()
  {
    return _isDone;
  }

  public final boolean isDoneWithError()
  {
    return _isDoneWithError;
  }

  public final String getError()
  {
    return _error;
  }

}