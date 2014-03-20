package org.glob3.mobile.generated; 
//
//  Renderer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//class TouchEvent;
//class SurfaceElevationProvider;
//class PlanetRenderer;
//class RenderState;



public abstract class Renderer implements ProtoRenderer
{



  public abstract boolean isEnable();

  public abstract void setEnable(boolean enable);

  public abstract RenderState getRenderState(G3MRenderContext rc);

  /**
   Gives to Renderer the opportunity to process touch events.
   
   The Renderer answer true if the event was processed.
   */
  public abstract boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent);

  /**
   * Allows us to know if the renderer is a PlanetRenderer.
   * It is invoked by IG3MBuilder::addRenderer to avoid adding instances of PlanetRenderer.
   * Default value: FALSE
   */
  public abstract boolean isPlanetRenderer();

  public abstract SurfaceElevationProvider getSurfaceElevationProvider();

  public abstract PlanetRenderer getPlanetRenderer();


}