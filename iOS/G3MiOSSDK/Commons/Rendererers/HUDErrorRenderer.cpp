//
//  HUDErrorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//

#include "HUDErrorRenderer.hpp"

#include "ICanvas.hpp"
#include "Color.hpp"
#include "ColumnCanvasElement.hpp"
#include "GFont.hpp"
#include "TextCanvasElement.hpp"

void HUDErrorRenderer_ImageFactory::drawOn(ICanvas* canvas,
                                           int width,
                                           int height) {
  canvas->setFillColor(Color::black());
  canvas->fillRectangle(0, 0,
                        width, height);

  ColumnCanvasElement column(Color::fromRGBA(0.9f, 0.4f, 0.4f, 1.0f),
                             0,  /* margin */
                             16, /* padding */
                             8   /* cornerRadius */);
  const GFont labelFont  = GFont::sansSerif(22);
  const Color labelColor = Color::white();

  const int errorsSize = _errors.size();
  for (int i = 0; i < errorsSize; i++) {
    column.add( new TextCanvasElement(_errors[i], labelFont, labelColor) );
  }

  column.drawCentered(canvas);
}

bool HUDErrorRenderer_ImageFactory::isEquals(const std::vector<std::string>& v1,
                                             const std::vector<std::string>& v2) const {
  const int size1 = v1.size();
  const int size2 = v2.size();
  if (size1 != size2) {
    return false;
  }

  for (int i = 0; i < size1; i++) {
    const std::string str1 = v1[i];
    const std::string str2 = v2[i];
    if (str1 != str2) {
      return false;
    }
  }
  return true;
}

bool HUDErrorRenderer_ImageFactory::setErrors(const std::vector<std::string>& errors) {
  if ( isEquals(_errors, errors) ) {
    return false;
  }

  _errors.clear();
#ifdef C_CODE
  _errors.insert(_errors.end(),
                 errors.begin(),
                 errors.end());
#endif
#ifdef JAVA_CODE
  _errors.addAll(errors);
#endif
  return true;
}

void HUDErrorRenderer::setErrors(const std::vector<std::string>& errors) {
  HUDErrorRenderer_ImageFactory* factory = (HUDErrorRenderer_ImageFactory*) getImageFactory();
  if (factory->setErrors(errors)) {
    recreateImage();
  }
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
