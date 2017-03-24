//
//  NearFrustumRenderer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/03/2017.
//
//

#include "NearFrustumRenderer.hpp"

#include "CoordinateSystem.hpp"
#include "Camera.hpp"
#include "G3MRenderContext.hpp"
#include "Color.hpp"
#include "TransformableMesh.hpp"
#include "Planet.hpp"
#include "Geodetic3D.hpp"


void NearFrustumRenderer::render(const G3MRenderContext* rc,
                                 GLState* glState){
  
  const Camera* cam = rc->getCurrentCamera();
  CoordinateSystem camCS = cam->getCameraCoordinateSystem();
  
//  Vector3D front2 = camCS._y;
//  Vector3D up2 = camCS._z;
//  Vector3D right2 = camCS._x;
  
  Vector3D up = rc->getPlanet()->geodeticSurfaceNormal(cam->getGeodeticPosition());
  Vector3D right = camCS._y.cross(up);
  Vector3D front = up.cross(right);
  
  Vector3D controllerDisp = front.scaleToLength(0.7)
                                  .sub(up.scaleToLength(0.5))
                                  .add(right.scaleToLength(0.1));

  Vector3D origin = cam->getCartesianPosition().add(controllerDisp);
  
  CoordinateSystem camCS2 = camCS.changeOrigin(origin);
  
  Mesh* mesh = camCS2.createMesh(1.2, Color::red(), Color::blue(), Color::green());
  
  _mr->clearMeshes();
  _mr->addMesh(mesh);
  _mr->render(rc, glState);
  
  ILogger::instance()->logInfo("Frustum %f - %f. Model at: %f meters.",
                               cam->getFrustumData()._znear,
                               cam->getFrustumData()._zfar,
                               camCS._origin.distanceTo(origin));
  
  
  
  
}
