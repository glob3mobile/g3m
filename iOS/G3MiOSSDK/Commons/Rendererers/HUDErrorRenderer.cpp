//
//  HUDErrorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//

#include "HUDErrorRenderer.hpp"

#include "HUDImageRenderer.hpp"

#include "ICanvas.hpp"
#include "Color.hpp"
#include "ColumnCanvasElement.hpp"
#include "GFont.hpp"
#include "TextCanvasElement.hpp"



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
  const GFont labelFont  = GFont::sansSerif(18);
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

HUDErrorRenderer::HUDErrorRenderer() {
  _hudImageRenderer = new HUDImageRenderer(new HUDErrorRenderer_ImageFactory());
}

void HUDErrorRenderer::setErrors(const std::vector<std::string>& errors) {
  HUDErrorRenderer_ImageFactory* factory = (HUDErrorRenderer_ImageFactory*) (_hudImageRenderer->getImageFactory());
  if (factory->setErrors(errors)) {
    _hudImageRenderer->recreateImage();
  }
}

void HUDErrorRenderer::initialize(const G3MContext* context) {
  _hudImageRenderer->initialize(context);
}

void HUDErrorRenderer::render(const G3MRenderContext* rc,
                              GLState* glState) {
  _hudImageRenderer->render(rc, glState);
}

void HUDErrorRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                             int width, int height) {
  _hudImageRenderer->onResizeViewportEvent(ec,
                                           width, height);
}

void HUDErrorRenderer::start(const G3MRenderContext* rc) {
  _hudImageRenderer->start(rc);
}

void HUDErrorRenderer::stop(const G3MRenderContext* rc) {
  _hudImageRenderer->stop(rc);
}

void HUDErrorRenderer::onResume(const G3MContext* context) {
  _hudImageRenderer->onResume(context);
}

void HUDErrorRenderer::onPause(const G3MContext* context) {
  _hudImageRenderer->onPause(context);
}

void HUDErrorRenderer::onDestroy(const G3MContext* context) {
  _hudImageRenderer->onDestroy(context);
}
