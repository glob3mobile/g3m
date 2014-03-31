//
//  DefaultRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//

#ifndef G3MiOSSDK_DefaultRenderer
#define G3MiOSSDK_DefaultRenderer

#include "Renderer.hpp"
#include <stddef.h>


class GPUProgramState;

class DefaultRenderer : public Renderer {
  
private:
  
  bool _enable;
  
protected:
  
#ifdef C_CODE
  const G3MContext* _context;
#else
  G3MContext* _context;
#endif
  
  DefaultRenderer() :
  _enable(true)
  {
    
  }
  
  DefaultRenderer(bool enable) :
  _enable(enable)
  {
    
  }
  
  ~DefaultRenderer() {
    _context = NULL;
  }
  
public:
  
  bool isEnable() const {
    return _enable;
  }

  virtual void setEnable(bool enable) {
    _enable = enable;
  }
  
  virtual void initialize(const G3MContext* context) {
    _context = context;
    onChangedContext();
  }

  virtual void onResume(const G3MContext* context) {
    _context = context;
    onChangedContext();
  }

  virtual void onPause(const G3MContext* context) {
    _context = NULL;
    onLostContext();
  }

  virtual void onDestroy(const G3MContext* context) {
    _context = NULL;
    onLostContext();
  }

  virtual void onChangedContext() { }
  
  virtual void onLostContext() { }


  virtual RenderState getRenderState(const G3MRenderContext* rc) {
    return RenderState::ready();
  }
  
  virtual void render(const G3MRenderContext* rc,
                      GLState* glState) = 0;

  virtual bool onTouchEvent(const G3MEventContext* ec,
                            const TouchEvent* touchEvent) {
    return false;
  }

  virtual void onResizeViewportEvent(const G3MEventContext* ec,
                                     int width, int height) = 0;

  virtual void start(const G3MRenderContext* rc) {
  
  }

  virtual void stop(const G3MRenderContext* rc) {
    
  }

  virtual SurfaceElevationProvider* getSurfaceElevationProvider() {
    return NULL;
  }

  virtual PlanetRenderer* getPlanetRenderer() {
    return NULL;
  }

  virtual bool isPlanetRenderer() {
    return false;
  }
  
};

#endif
