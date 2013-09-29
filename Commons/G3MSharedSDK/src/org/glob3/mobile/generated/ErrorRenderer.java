package org.glob3.mobile.generated; 
//
//  ErrorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//

//
//  ErrorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//



public interface ErrorRenderer
{

  void initialize(G3MContext context);

  void render(G3MRenderContext rc, GLState glState);

  boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent);

  void onResizeViewportEvent(G3MEventContext ec, int width, int height);

  void start(G3MRenderContext rc);

  void stop(G3MRenderContext rc);

  public void dispose();

  void onResume(G3MContext context);

  void onPause(G3MContext context);

  void onDestroy(G3MContext context);

}