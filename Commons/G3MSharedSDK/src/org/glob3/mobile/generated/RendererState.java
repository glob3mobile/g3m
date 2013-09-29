package org.glob3.mobile.generated; 
//
//  RendererState.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//

//
//  RendererState.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//



public class RendererState
{
  private enum Type
  {
    READY,
    BUSY,
    ERROR;

     public int getValue()
     {
        return this.ordinal();
     }

     public static Type forValue(int value)
     {
        return values()[value];
     }
  }

  private final java.lang.Class _type = new java.lang.Class();
  private final java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private RendererState(java.lang.Class type)
  {
     _type = type;
  }

  private RendererState(java.util.ArrayList<String> errors)
  {
     _type = Type.ERROR;
     _errors = errors;
  }

  public static RendererState ready()
  {
    return new RendererState(Type.READY);
  }
  public static RendererState busy()
  {
    return new RendererState(Type.BUSY);
  }
  public static RendererState error(java.util.ArrayList<String> errors)
  {
    return new RendererState(errors);
  }

  public final boolean isReady()
  {
    return (_type == Type.READY);
  }

  public final boolean isBusy()
  {
    return (_type == Type.BUSY);
  }

  public final boolean isError()
  {
    return (_type == Type.ERROR);
  }

}