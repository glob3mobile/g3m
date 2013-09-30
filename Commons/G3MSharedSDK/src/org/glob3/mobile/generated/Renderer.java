package org.glob3.mobile.generated; 
//
//  IRenderer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//class G3MContext;
//class G3MRenderContext;
//class GLState;
//class G3MEventContext;
//class TouchEvent;
//class SurfaceElevationProvider;
//class PlanetRenderer;


public interface Renderer
{
  boolean isEnable();

  void setEnable(boolean enable);

  void initialize(G3MContext context);

  RenderState getRenderState(G3MRenderContext rc);

  void render(G3MRenderContext rc, GLState glState);

  /**
   Gives to Renderer the opportunity to process touch events.

   The Renderer answer true if the event was processed.
   */
  boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent);

  void onResizeViewportEvent(G3MEventContext ec, int width, int height);

  void start(G3MRenderContext rc);

  void stop(G3MRenderContext rc);

  public void dispose();

  // Android activity lifecyle
  void onResume(G3MContext context);

  void onPause(G3MContext context);

  void onDestroy(G3MContext context);

  /**
   * Allows us to know if the renderer is a PlanetRenderer.
   * It is invoked by IG3MBuilder::addRenderer to avoid adding instances of PlanetRenderer.
   * Default value: FALSE
   */
//  virtual bool isPlanetRenderer() {
//    return false;
//  }
  boolean isPlanetRenderer();

  SurfaceElevationProvider getSurfaceElevationProvider();

  PlanetRenderer getPlanetRenderer();

}