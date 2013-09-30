//
//  HUDErrorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//

#include "HUDErrorRenderer.hpp"

void HUDErrorRenderer_ImageFactory::create(const G3MRenderContext* rc,
                                           int width,
                                           int height,
                                           IImageListener* listener,
                                           bool deleteListener) {
  int _DGD_AtWork;
}

#ifdef C_CODE
void HUDErrorRenderer::initialize(const G3MContext* context) {
  HUDImageRenderer::initialize(context);
}

void HUDErrorRenderer::render(const G3MRenderContext* rc,
                              GLState* glState) {
  HUDImageRenderer::render(rc, glState);
}

bool HUDErrorRenderer::onTouchEvent(const G3MEventContext* ec,
                                    const TouchEvent* touchEvent) {
  return HUDImageRenderer::onTouchEvent(ec, touchEvent);
}

void HUDErrorRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                             int width, int height) {
  HUDImageRenderer::onResizeViewportEvent(ec,
                                          width, height);
}

void HUDErrorRenderer::start(const G3MRenderContext* rc) {
  HUDImageRenderer::start(rc);
}

void HUDErrorRenderer::stop(const G3MRenderContext* rc) {
  HUDImageRenderer::stop(rc);
}

void HUDErrorRenderer::onResume(const G3MContext* context) {
  HUDImageRenderer::onResume(context);
}

void HUDErrorRenderer::onPause(const G3MContext* context) {
  HUDImageRenderer::onPause(context);
}

void HUDErrorRenderer::onDestroy(const G3MContext* context) {
  HUDImageRenderer::onDestroy(context);
}
#endif
