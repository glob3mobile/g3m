package org.glob3.mobile.generated;
//
//  Renderer.cpp
//  G3M
//
//  Created by Vidal Toboso on 23/04/14.
//
//

//
//  Renderer.hpp
//  G3M
//
//  Created by José Miguel S N on 31/05/12.
//


//class TouchEvent;
//class SurfaceElevationProvider;
//class PlanetRenderer;
//class RenderState;



public interface Renderer extends ProtoRenderer
{



  public abstract boolean isEnable();

  public abstract void setEnable(boolean enable);

  /** true while enabled, and while a disabled renderer still fades out what it showed: it is drawn, but takes no touches */
  public abstract boolean isRendering();

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

  public abstract void setChangedRendererInfoListener(ChangedRendererInfoListener changedInfoListener, int rendererID);

//  virtual void setInfo(const std::vector<std::string>& info) = 0;

}