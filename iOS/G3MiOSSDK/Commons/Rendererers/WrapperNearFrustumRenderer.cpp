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


WrapperNearFrustumRenderer::WrapperNearFrustumRenderer(Renderer* renderer) :
_renderer(renderer)
{
}

WrapperNearFrustumRenderer::~WrapperNearFrustumRenderer() {
#ifdef JAVA_CODE
  super.dispose();
#endif
  delete _renderer;
}

RenderState WrapperNearFrustumRenderer::getRenderState(const G3MRenderContext* rc) {
  return _renderer->getRenderState(rc);
}

void WrapperNearFrustumRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                                       int width,
                                                       int height) {
  _renderer->onResizeViewportEvent(ec, width, height);
}

void WrapperNearFrustumRenderer::render(Camera* currentCamera,
                                        const G3MRenderContext* rc,
                                        GLState* glState) {
  currentCamera->setFixedFrustum(0.0001,
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
