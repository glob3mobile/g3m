//
//  PointCloudsRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/19/14.
//
//

#include "PointCloudsRenderer.hpp"

void PointCloudsRenderer::PointCloud::initialize(const G3MContext* context) {

}

RenderState PointCloudsRenderer::PointCloud::getRenderState(const G3MRenderContext* rc) {
#warning DGD at work;
}

PointCloudsRenderer::~PointCloudsRenderer() {
  const int cloudsSize = _clouds.size();
  for (int i = 0; i < cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    delete cloud;
  }
}

void PointCloudsRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                                int width, int height) {

}

void PointCloudsRenderer::onChangedContext() {
  const int cloudsSize = _clouds.size();
  for (int i = 0; i < cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    cloud->initialize(_context);
  }
}

RenderState PointCloudsRenderer::getRenderState(const G3MRenderContext* rc) {
  _errors.clear();
  bool busyFlag  = false;
  bool errorFlag = false;

  const int cloudsSize = _clouds.size();
  for (int i = 0; i < cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    const RenderState childRenderState = cloud->getRenderState(rc);

    const RenderState_Type childRenderStateType = childRenderState._type;

    if (childRenderStateType == RENDER_ERROR) {
      errorFlag = true;

      const std::vector<std::string> childErrors = childRenderState.getErrors();
#ifdef C_CODE
      _errors.insert(_errors.end(),
                     childErrors.begin(),
                     childErrors.end());
#endif
#ifdef JAVA_CODE
      _errors.addAll(childErrors);
#endif
    }
    else if (childRenderStateType == RENDER_BUSY) {
      busyFlag = true;
    }
  }

  if (errorFlag) {
    return RenderState::error(_errors);
  }
  else if (busyFlag) {
    return RenderState::busy();
  }
  else {
    return RenderState::ready();
  }
}

void PointCloudsRenderer::addPointCloud(const URL& url) {
#warning DGD at work!
}

void PointCloudsRenderer::removeAllPointClouds() {
#warning DGD at work!
}

void PointCloudsRenderer::render(const G3MRenderContext* rc,
                                 GLState* glState) {
#warning DGD at work!
}
