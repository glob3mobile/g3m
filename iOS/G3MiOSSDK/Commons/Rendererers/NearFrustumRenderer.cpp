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


void NearFrustumRenderer::render(const G3MRenderContext* rc,
                                 GLState* glState){
  
  const Camera* cam = rc->getCurrentCamera();
  CoordinateSystem camCS = cam->getCameraCoordinateSystem();
  

  Vector3D origin = cam->getCartesianPosition().add(camCS._y.scaleToLength(1.0))
                                              .sub(camCS._z.scaleToLength(0.2))
                                              .add(camCS._x.scaleToLength(0.1));
  
  
  double h = cam->getGeodeticHeight();
  
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
