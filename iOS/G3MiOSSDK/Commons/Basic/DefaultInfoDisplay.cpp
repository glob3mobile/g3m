//
//  DefaultInfoDisplay.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 11/08/14.
//
//

#include "DefaultInfoDisplay.hpp"
#include "ICanvasUtils.hpp"


void DefaultHUDInfoRenderer_ImageFactory::drawOn(ICanvas* canvas,
                                          int width,
                                          int height) {
  ICanvasUtils::drawStringsOn(_infos,
                              canvas,
                              width,
                              height,
                              Left,
                              Bottom,
                              Left,
                              Color::white(),
                              11,
                              2,
                              Color::transparent(),
                              Color::black(),
                              5);
}

bool DefaultHUDInfoRenderer_ImageFactory::isEquals(const std::vector<std::string>& v1,
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

bool DefaultHUDInfoRenderer_ImageFactory::setInfos(const std::vector<std::string>& infos) {
  if ( isEquals(_infos, infos) ) {
    return false;
  }
  
  _infos.clear();
#ifdef C_CODE
  _infos.insert(_infos.end(),
                infos.begin(),
                infos.end());
#endif
#ifdef JAVA_CODE
  _infos.addAll(infos);
#endif
  
  return true;
}

Default_HUDRenderer::Default_HUDRenderer() {
  _hudImageRenderer = new HUDImageRenderer(new DefaultHUDInfoRenderer_ImageFactory());
}

Default_HUDRenderer::~Default_HUDRenderer() {
  delete _hudImageRenderer;
}

void Default_HUDRenderer::updateInfo(const std::vector<std::string> &info) {
  DefaultHUDInfoRenderer_ImageFactory* factory = (DefaultHUDInfoRenderer_ImageFactory*) (_hudImageRenderer->getImageFactory());
  if (factory->setInfos(info)) {
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
