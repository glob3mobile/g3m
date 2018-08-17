package org.glob3.mobile.generated;//
//  Renderer.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 23/04/14.
//
//

//
//  Renderer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TouchEvent;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SurfaceElevationProvider;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class PlanetRenderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RenderState;



public abstract class Renderer extends ProtoRenderer
{

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  public void dispose()
  {
  }
//#endif


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isEnable() const = 0;
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

  public abstract void setChangedRendererInfoListener(ChangedRendererInfoListener changedInfoListener, int rendererIdentifier);

//  virtual void setInfo(const std::vector<std::string>& info) = 0;

}
