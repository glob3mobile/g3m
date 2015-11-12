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
#include "ICanvasUtils.hpp"
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
  ICanvasUtils::drawStringsOn(_errors,
                              canvas,
                              width,
                              height,
                              Center,
                              Middle,
                              Center,
                              Color::white(),
                              20,
                              5,
                              Color::fromRGBA(0.9f, 0.4f, 0.4f, 1.0f),
                              Color::transparent(),
                              16);
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

HUDErrorRenderer::HUDErrorRenderer(ErrorMessagesCustomizer* errorMessageCustomizer) {
  _hudImageRenderer = new HUDImageRenderer(new HUDErrorRenderer_ImageFactory());
  _errorMessageCustomizer = errorMessageCustomizer;
}

void HUDErrorRenderer::setErrors(const std::vector<std::string>& errors) {
  HUDErrorRenderer_ImageFactory* factory = (HUDErrorRenderer_ImageFactory*) (_hudImageRenderer->getImageFactory());
  const std::vector<std::string> customizedErrors = (_errorMessageCustomizer != NULL) ? _errorMessageCustomizer->customize(errors) : errors;
  if (factory->setErrors(customizedErrors)) {
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
