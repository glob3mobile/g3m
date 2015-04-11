//
//  DefaultInfoDisplay.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 11/08/14.
//
//

#include "DefaultInfoDisplay.hpp"
#include "ICanvasUtils.hpp"
#include "Info.hpp"


void DefaultHUDInfoRenderer_ImageFactory::drawOn(ICanvas* canvas,
                                                 int width,
                                                 int height) {
  std::vector<std::string> strings;

  const size_t size = _info.size();
  for (size_t i = 0; i < size; i++) {
    strings.push_back(_info.at(i)->getText());
  }
  ICanvasUtils::drawStringsOn(strings,
                              canvas,
                              width,
                              height,
                              Left,
                              Bottom,
                              Left,
                              Color::white(),
                              16,
                              10,
                              Color::transparent(),
                              Color::black(),
                              5);
}

bool DefaultHUDInfoRenderer_ImageFactory::isEquals(const std::vector<const Info*>& v1,
                                                   const std::vector<const Info*>& v2) const {
  const size_t size1 = v1.size();
  const size_t size2 = v2.size();
  if (size1 != size2) {
    return false;
  }

  for (size_t i = 0; i < size1; i++) {
    const Info* str1 = v1[i];
    const Info* str2 = v2[i];
    if (str1 != str2) {
      return false;
    }
  }
  return true;
}

bool DefaultHUDInfoRenderer_ImageFactory::setInfo(const std::vector<const Info*>& info) {
  _info.clear();
#ifdef C_CODE
  _info.insert(_info.end(),
               info.begin(),
               info.end());
#endif
#ifdef JAVA_CODE
  _info.addAll(info);
#endif

  return true;
}

Default_HUDRenderer::Default_HUDRenderer() {
  _hudImageRenderer = new HUDImageRenderer(new DefaultHUDInfoRenderer_ImageFactory());
}

Default_HUDRenderer::~Default_HUDRenderer() {
  delete _hudImageRenderer;
}

void Default_HUDRenderer::updateInfo(const std::vector<const Info*>& info) {
  DefaultHUDInfoRenderer_ImageFactory* factory = (DefaultHUDInfoRenderer_ImageFactory*) (_hudImageRenderer->getImageFactory());
  if (factory->setInfo(info)) {
    _hudImageRenderer->recreateImage();
  }
}

void Default_HUDRenderer::initialize(const G3MContext* context) {
  _hudImageRenderer->initialize(context);
}

void Default_HUDRenderer::render(const G3MRenderContext* rc,
                                 GLState* glState) {
  _hudImageRenderer->render(rc, glState);
}

void Default_HUDRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                                int width, int height) {
  _hudImageRenderer->onResizeViewportEvent(ec,
                                           width, height);
}

void Default_HUDRenderer::start(const G3MRenderContext* rc) {
  _hudImageRenderer->start(rc);
}

void Default_HUDRenderer::stop(const G3MRenderContext* rc) {
  _hudImageRenderer->stop(rc);
}

void Default_HUDRenderer::onResume(const G3MContext* context) {
  _hudImageRenderer->onResume(context);
}

void Default_HUDRenderer::onPause(const G3MContext* context) {
  _hudImageRenderer->onPause(context);
}

void Default_HUDRenderer::onDestroy(const G3MContext* context) {
  _hudImageRenderer->onDestroy(context);
}
