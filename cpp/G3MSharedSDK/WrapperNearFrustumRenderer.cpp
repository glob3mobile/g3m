//
//  WrapperNearFrustumRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/17.
//
//

#include "WrapperNearFrustumRenderer.hpp"

//#include "Camera.hpp"
#include "FrustumData.hpp"
#include "G3MRenderContext.hpp"
#include "GL.hpp"
#include "FrustumPolicyHandler.hpp"


WrapperNearFrustumRenderer::WrapperNearFrustumRenderer(const double zNear,
                                                       Renderer* renderer) :
_zNear(zNear),
_renderer(renderer)
{
}

WrapperNearFrustumRenderer::~WrapperNearFrustumRenderer() {
  delete _renderer;
}

void WrapperNearFrustumRenderer::initialize(const G3MContext* context) {
  _renderer->initialize(context);
}

void WrapperNearFrustumRenderer::start(const G3MRenderContext* rc) {
  _renderer->start(rc);
}

void WrapperNearFrustumRenderer::stop(const G3MRenderContext* rc) {
  _renderer->stop(rc);
}

void WrapperNearFrustumRenderer::onResume(const G3MContext* context) {
  _renderer->onResume(context);
}

void WrapperNearFrustumRenderer::onPause(const G3MContext* context) {
  _renderer->onPause(context);
}

void WrapperNearFrustumRenderer::onDestroy(const G3MContext* context) {
  _renderer->onDestroy(context);
}

bool WrapperNearFrustumRenderer::isEnable() const {
  return _renderer->isEnable();
}

void WrapperNearFrustumRenderer::setEnable(bool enable) {
  _renderer->setEnable(enable);
}

RenderState WrapperNearFrustumRenderer::getRenderState(const G3MRenderContext* rc) {
  return _renderer->getRenderState(rc);
}

void WrapperNearFrustumRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                                       int width,
                                                       int height) {
  _renderer->onResizeViewportEvent(ec, width, height);
}

bool WrapperNearFrustumRenderer::onTouchEvent(const G3MEventContext* ec,
                                              const TouchEvent* touchEvent) {
  return _renderer->onTouchEvent(ec, touchEvent);
}

void WrapperNearFrustumRenderer::setChangedRendererInfoListener(ChangedRendererInfoListener* changedInfoListener,
                                                                const size_t rendererID) {
  _renderer->setChangedRendererInfoListener(changedInfoListener,
                                            rendererID);
}

void WrapperNearFrustumRenderer::render(const G3MRenderContext* rc,
                                        GLState* glState) {
  _renderer->render(rc, glState);
}

void WrapperNearFrustumRenderer::render(const FrustumData* currentFrustumData,
                                        FrustumPolicyHandler* handler,
                                        const G3MRenderContext* rc,
                                        GLState* glState) {
  handler->changeToFixedFrustum(_zNear, currentFrustumData->_zNear);
  rc->getGL()->clearDepthBuffer();
  render(rc, glState);
  handler->resetFrustumPolicy();
}
