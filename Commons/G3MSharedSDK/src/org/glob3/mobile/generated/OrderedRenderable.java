package org.glob3.mobile.generated; 
//
//  OrderedRenderable.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/13/12.
//
//

//
//  OrderedRenderable.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/13/12.
//
//


//class GLGlobalState;
//class GPUProgramState;
//class GLState;

public abstract class OrderedRenderable
{
  public abstract double squaredDistanceFromEye();

  public abstract void render(G3MRenderContext rc);

  public void dispose()
  {
  }
}