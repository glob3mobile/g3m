package org.glob3.mobile.generated; 
//
//  ErrorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//




public interface ErrorRenderer implements ProtoRenderer
{
  void dispose();

  public abstract void setErrors(java.util.ArrayList<String> errors);

}