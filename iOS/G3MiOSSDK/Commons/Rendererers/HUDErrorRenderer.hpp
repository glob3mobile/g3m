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
#include "ImageFactory.hpp"


class HUDErrorRenderer_ImageFactory : public ImageFactory {
public:
  void create(const G3MRenderContext* rc,
              int width,
              int height,
              IImageListener* listener,
              bool deleteListener);
};

class HUDErrorRenderer : public HUDImageRenderer, public ErrorRenderer {
public:
  HUDErrorRenderer() :
  HUDImageRenderer(new HUDErrorRenderer_ImageFactory())
  {

  }

#ifdef C_CODE
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
