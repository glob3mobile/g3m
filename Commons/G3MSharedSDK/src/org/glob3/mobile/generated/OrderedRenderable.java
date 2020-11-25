package org.glob3.mobile.generated;
//
//  OrderedRenderable.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/13/12.
//
//

//
//  OrderedRenderable.h
//  G3M
//
//  Created by Diego Gomez Deck on 11/13/12.
//
//


//class G3MRenderContext;
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