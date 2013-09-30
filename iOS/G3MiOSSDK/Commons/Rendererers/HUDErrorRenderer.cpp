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

void HUDErrorRenderer_ImageFactory::setErrors(const std::vector<std::string>& errors) {
  _errors.clear();
#ifdef C_CODE
  _errors.insert(_errors.end(),
                 errors.begin(),
                 errors.end());
#endif
#ifdef JAVA_CODE
  _errors.addAll(errors);
#endif
}

void HUDErrorRenderer::setErrors(const std::vector<std::string>& errors) {
  ((HUDErrorRenderer_ImageFactory*) getImageFactory())->setErrors(errors);

  recreateImage();
}

#ifdef C_CODE
bool HUDErrorRenderer::isEnable() const {
  return true;
}

void HUDErrorRenderer::setEnable(bool enable) {
  // do nothing
}

RenderState HUDErrorRenderer::getRenderState(const G3MRenderContext* rc) {
  return RenderState::ready();
}

bool HUDErrorRenderer::isPlanetRenderer() {
  return false;
}

SurfaceElevationProvider* HUDErrorRenderer::getSurfaceElevationProvider() {
  return NULL;
}

PlanetRenderer* HUDErrorRenderer::getPlanetRenderer() {
  return NULL;
}

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
