//
//  Renderer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Renderer
#define G3MiOSSDK_Renderer

class TouchEvent;
class SurfaceElevationProvider;
class PlanetRenderer;
class RenderState;

#include "ProtoRenderer.hpp"
#include "RenderState.hpp"
#include "ChangedRendererInfoListener.hpp"


class Renderer : public ProtoRenderer {
public:
  
#ifdef C_CODE
  virtual ~Renderer() { }
#endif
  
  
  virtual bool isEnable() const = 0;
  
  virtual void setEnable(bool enable) = 0;
  
  virtual RenderState getRenderState(const G3MRenderContext* rc) = 0;

  virtual void render(const G3MRenderContext* rc,
                      GLState* glState) = 0;

  virtual void zRender(const G3MRenderContext* rc,
                      GLState* glState) = 0;

  /**
   Gives to Renderer the opportunity to process touch events.
   
   The Renderer answer true if the event was processed.
   */
  virtual bool onTouchEvent(const G3MEventContext* ec,
                            const TouchEvent* touchEvent) = 0;
  
  /**
   * Allows us to know if the renderer is a PlanetRenderer.
   * It is invoked by IG3MBuilder::addRenderer to avoid adding instances of PlanetRenderer.
   * Default value: FALSE
   */
  virtual bool isPlanetRenderer() = 0;
  
  virtual SurfaceElevationProvider* getSurfaceElevationProvider() = 0;
  
  virtual PlanetRenderer* getPlanetRenderer() = 0;
    
  virtual void setChangedRendererInfoListener(ChangedRendererInfoListener* changedInfoListener, const int rendererIdentifier) = 0;
  
//  virtual void setInfo(const std::vector<std::string>& info) = 0;
  
};
#endif
