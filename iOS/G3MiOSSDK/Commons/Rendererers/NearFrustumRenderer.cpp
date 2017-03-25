//
//  NearFrustumRenderer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/03/2017.
//
//

#include "NearFrustumRenderer.hpp"

#include "MeshRenderer.hpp"
#include "G3MRenderContext.hpp"
#include "CoordinateSystem.hpp"
#include "Camera.hpp"
#include "Planet.hpp"
#include "Geodetic3D.hpp"
#include "Color.hpp"
#include "FrustumData.hpp"


NearFrustumRenderer::NearFrustumRenderer() :
_mr(new MeshRenderer()) {
}

NearFrustumRenderer::~NearFrustumRenderer() {
#ifdef JAVA_CODE
  super.dispose();
#endif
  delete _mr;
}

void NearFrustumRenderer::onChangedContext() {
}

RenderState NearFrustumRenderer::getRenderState(const G3MRenderContext* rc) {
  return RenderState::ready();
}

void NearFrustumRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                                int width,
                                                int height) {
}

void NearFrustumRenderer::render(const G3MRenderContext* rc,
                                 GLState* glState){

  const Camera* cam = rc->getCurrentCamera();
  CoordinateSystem camCS = cam->getCameraCoordinateSystem();

  const Vector3D up    = rc->getPlanet()->geodeticSurfaceNormal(cam->getGeodeticPosition());
  const Vector3D right = camCS._y.cross(up);
  const Vector3D front = up.cross(right);

  const Vector3D controllerDisp = front.scaleToLength(0.7).sub(up.scaleToLength(0.5)).add(right.scaleToLength(0.1));

  const Vector3D origin = cam->getCartesianPosition().add(controllerDisp);

  const CoordinateSystem camCS2 = camCS.changeOrigin(origin);

  Mesh* mesh = camCS2.createMesh(1000000, Color::RED, Color::GREEN, Color::BLUE);

  _mr->clearMeshes();
  _mr->addMesh(mesh);
  _mr->render(rc, glState);

  ILogger::instance()->logInfo("Frustum %f - %f. Model at: %f meters.",
                               cam->getFrustumData()->_zNear,
                               cam->getFrustumData()->_zFar,
                               camCS._origin.distanceTo(origin));
  
}
