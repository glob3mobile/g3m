package org.glob3.mobile.generated; 
//
//  RenderState.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//

//
//  RenderState.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//



public class RenderState
{
  public enum Type
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

  public static RenderState ready()
  {
    return new RenderState(Type.READY);
  }
  public static RenderState busy()
  {
    return new RenderState(Type.BUSY);
  }
  public static RenderState error(java.util.ArrayList<String> errors)
  {
    return new RenderState(errors);
  }

  public final java.lang.Class _type = new java.lang.Class();

  public final java.util.ArrayList<String> getErrors()
  {
    return _errors;
  }

  private final java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private RenderState(java.lang.Class type)
  {
     _type = type;
  }

  private RenderState(java.util.ArrayList<String> errors)
  {
     _type = Type.ERROR;
     _errors = errors;
  }

}