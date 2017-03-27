//
//  WrapperNearFrustumRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/17.
//
//

#include "WrapperNearFrustumRenderer.hpp"

#include "Camera.hpp"
#include "FrustumData.hpp"
#include "G3MRenderContext.hpp"
#include "GL.hpp"


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

}

void WrapperNearFrustumRenderer::render(Camera* currentCamera,
                                        const G3MRenderContext* rc,
                                        GLState* glState) {
  currentCamera->setFixedFrustum(_zNear,
                                 currentCamera->getFrustumData()->_zNear);
  rc->getGL()->clearDepthBuffer();
  render(rc, glState);
  currentCamera->resetFrustumPolicy();
}

void WrapperNearFrustumRenderer::render(const G3MRenderContext* rc,
                                        GLState* glState) {
//  const Camera* cam = rc->getCurrentCamera();
//  const CoordinateSystem camCS = cam->getCameraCoordinateSystem();
//
//  const Vector3D up    = rc->getPlanet()->geodeticSurfaceNormal(cam->getGeodeticPosition());
//  const Vector3D right = camCS._y.cross(up);
//  const Vector3D front = up.cross(right);
//
//  const Vector3D controllerDisp = front.scaleToLength(0.7).sub(up.scaleToLength(0.5)).add(right.scaleToLength(0.1));
//
//  const Vector3D origin = cam->getCartesianPosition().add(controllerDisp);
//
//  const CoordinateSystem camCS2 = camCS.changeOrigin(origin);
//
//  Mesh* mesh = camCS2.createMesh(1000000, Color::RED, Color::GREEN, Color::BLUE);
//
//  _meshRenderer->clearMeshes();
//  _meshRenderer->addMesh(mesh);

  _renderer->render(rc, glState);
}
