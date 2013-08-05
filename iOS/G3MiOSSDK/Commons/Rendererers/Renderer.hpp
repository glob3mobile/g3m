//
//  IRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IRenderer_h
#define G3MiOSSDK_IRenderer_h

class TouchEvent;
class G3MContext;
class G3MRenderContext;
class G3MEventContext;
class GLGlobalState;
class GLState;

#include "Effects.hpp"

class Renderer: public EffectTarget {
public:
  virtual bool isEnable() const = 0;
  
  virtual void setEnable(bool enable) = 0;
  
  
  virtual void initialize(const G3MContext* context) = 0;
  
  virtual bool isReadyToRender(const G3MRenderContext* rc) = 0;
  
  virtual void render(const G3MRenderContext* rc, GLState* glState) = 0;

  /*
   Gives to Renderer the opportunity to process touch, events.
   
   The Renderer answer true if the event was processed.
   */
  virtual bool onTouchEvent(const G3MEventContext* ec,
                            const TouchEvent* touchEvent) = 0;
  
  virtual void onResizeViewportEvent(const G3MEventContext* ec,
                                     int width, int height) = 0;
  
  virtual void start(const G3MRenderContext* rc) = 0;
  
  virtual void stop(const G3MRenderContext* rc) = 0;
  
  virtual ~Renderer() { };

  // Android activity lifecyle
  virtual void onResume(const G3MContext* context) = 0;

  virtual void onPause(const G3MContext* context) = 0;

  virtual void onDestroy(const G3MContext* context) = 0;
  
  /**
   * Allows us to know if the renderer is a TileRenderer.
   * It is invoked by IG3MBuilder::addRenderer to avoid adding instances of TileRenderer.
   * Default value: FALSE
   */
  virtual bool isTileRenderer() {
    return false;
  }

};


#endif
