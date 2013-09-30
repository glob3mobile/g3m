//
//  HUDErrorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//

#ifndef __G3MiOSSDK__HUDErrorRenderer__
#define __G3MiOSSDK__HUDErrorRenderer__

#include "ErrorRenderer.hpp"
#include "HUDImageRenderer.hpp"


class HUDErrorRenderer_ImageFactory : public HUDImageRenderer::CanvasImageFactory {
private:
  std::vector<std::string> _errors;

protected:

  void drawOn(ICanvas* canvas,
              int width,
              int height);

  bool isEquals(const std::vector<std::string>& v1,
                const std::vector<std::string>& v2) const;

public:
  ~HUDErrorRenderer_ImageFactory() {
  }

  bool setErrors(const std::vector<std::string>& errors);
};


class HUDErrorRenderer : public HUDImageRenderer, public ErrorRenderer {

public:
  HUDErrorRenderer() :
  HUDImageRenderer(new HUDErrorRenderer_ImageFactory())
  {

  }

  ~HUDErrorRenderer() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void setErrors(const std::vector<std::string>& errors);

#ifdef C_CODE
  bool isEnable() const;

  void setEnable(bool enable);

  RenderState getRenderState(const G3MRenderContext* rc);

  bool isPlanetRenderer();

  SurfaceElevationProvider* getSurfaceElevationProvider();

  PlanetRenderer* getPlanetRenderer();

  void initialize(const G3MContext* context);

  void render(const G3MRenderContext* rc,
              GLState* glState);

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height);

  void start(const G3MRenderContext* rc);

  void stop(const G3MRenderContext* rc);


  void onResume(const G3MContext* context);

  void onPause(const G3MContext* context);

  void onDestroy(const G3MContext* context);
#endif
};

#endif
