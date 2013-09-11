package org.glob3.mobile.generated; 
//
//  IRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//class TouchEvent;
//class G3MContext;
//class G3MRenderContext;
//class G3MEventContext;
//class GLGlobalState;
//class GLState;
//class SurfaceElevationProvider;
//class PlanetRenderer;



public abstract class Renderer implements EffectTarget
{
  public abstract boolean isEnable();

  public abstract void setEnable(boolean enable);


  public abstract void initialize(G3MContext context);

  public abstract boolean isReadyToRender(G3MRenderContext rc);

  public abstract void render(G3MRenderContext rc, GLState glState);

  /*
   Gives to Renderer the opportunity to process touch, events.
   
   The Renderer answer true if the event was processed.
   */
  public abstract boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent);

  public abstract void onResizeViewportEvent(G3MEventContext ec, int width, int height);

  public abstract void start(G3MRenderContext rc);

  public abstract void stop(G3MRenderContext rc);

  public void dispose()
  {
  }

  // Android activity lifecyle
  public abstract void onResume(G3MContext context);

  public abstract void onPause(G3MContext context);

  public abstract void onDestroy(G3MContext context);

  /**
   * Allows us to know if the renderer is a PlanetRenderer.
   * It is invoked by IG3MBuilder::addRenderer to avoid adding instances of PlanetRenderer.
   * Default value: FALSE
   */
  public boolean isPlanetRenderer()
  {
    return false;
  }

  public abstract SurfaceElevationProvider getSurfaceElevationProvider();

  public abstract PlanetRenderer getPlanetRenderer();

}