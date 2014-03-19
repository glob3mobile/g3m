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

public:
  DefaultRenderer() :
  _enable(true)
  {

  }

  DefaultRenderer(bool enable) :
  _enable(enable)
  {

  }

  ~DefaultRenderer() {
#ifdef JAVA_CODE
    super.dispose();
#endif

  }

  bool isEnable() const {
    return _enable;
  }

  virtual void setEnable(bool enable) {
    _enable = enable;
  }

  virtual void onResume(const G3MContext* context) = 0;

  virtual void onPause(const G3MContext* context) = 0;

  virtual void onDestroy(const G3MContext* context) = 0;

  virtual void initialize(const G3MContext* context) = 0;

  virtual RenderState getRenderState(const G3MRenderContext* rc) = 0;

  virtual void render(const G3MRenderContext* rc, GLState* glState) = 0;

  virtual bool onTouchEvent(const G3MEventContext* ec,
                            const TouchEvent* touchEvent) = 0;

  virtual void onResizeViewportEvent(const G3MEventContext* ec,
                                     int width, int height) = 0;

  virtual void start(const G3MRenderContext* rc) = 0;

  virtual void stop(const G3MRenderContext* rc) = 0;

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
