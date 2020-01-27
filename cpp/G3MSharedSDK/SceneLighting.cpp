//
//  SceneLighting.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/08/13.
//
//

#include "SceneLighting.hpp"

#include "GLState.hpp"
#include "Vector3D.hpp"
#include "G3MContext.hpp"
#include "Camera.hpp"
#include "DirectMesh.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "MeshRenderer.hpp"
#include "G3MRenderContext.hpp"


void FixedFocusSceneLighting::modifyGLState(GLState* glState, const G3MRenderContext* rc) {
  const Vector3D lightDir(1, 0,0);

  //STATIC LIGHT
  DirectionLightGLFeature* f = (DirectionLightGLFeature*) glState->getGLFeature(GLF_DIRECTION_LIGTH);
  if (f == NULL) {
    glState->clearGLFeatureGroup(LIGHTING_GROUP);
    glState->addGLFeature(new DirectionLightGLFeature(lightDir,
                                                      Color::YELLOW,
                                                      Color::WHITE),
                          false);
  }
  /* //Add this to obtain a rotating "sun"
   else {

   ITimer *timer = IFactory::instance()->createTimer();
   double sec = timer->now().milliseconds();
   delete timer;
   double angle = ((int)sec % 36000) / 100.0;

   MutableMatrix44D m = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(angle),
   Vector3D::upZ(),
   Vector3D::ZERO);

   f->setLightDirection(lightDir.transformedBy(m, 1.0));

   }
   */

}

CameraFocusSceneLighting::CameraFocusSceneLighting(const Color& ambient, const Color& diffuse):
_ambientColor(ambient),
_diffuseColor(diffuse),
_cameraDirX(0),
_cameraDirY(0),
_cameraDirZ(0),
_meshRenderer(NULL)
{

}

void CameraFocusSceneLighting::modifyGLState(GLState* glState, const G3MRenderContext* rc) {
  const Camera* camera = rc->getCurrentCamera();

  camera->getViewDirectionInto(_camDir);
  camera->getUpMutable(_up);

  if ((_cameraDirX == _camDir.x()) &&
      (_cameraDirY == _camDir.y()) &&
      (_cameraDirZ == _camDir.z()) &&
      (_upX == _up.x()) &&
      (_upY == _up.y()) &&
      (_upZ == _up.z())) {
    return;
  }

  const MutableVector3D cameraVector = _camDir.times(-1);

  //Light slightly different of camera position
  const MutableVector3D rotationLightDirAxis = _up.cross(cameraVector);
  const MutableVector3D lightDir = cameraVector.rotateAroundAxis(rotationLightDirAxis,
                                                                 Angle::fromDegrees(45.0));

  DirectionLightGLFeature* f = (DirectionLightGLFeature*) glState->getGLFeature(GLF_DIRECTION_LIGTH);
  if (f == NULL) {
    glState->clearGLFeatureGroup(LIGHTING_GROUP);
    glState->addGLFeature(new DirectionLightGLFeature(lightDir.asVector3D(),
                                                      _diffuseColor,
                                                      _ambientColor),
                          false);
  }
  else {
    f->setLightDirection(lightDir.asVector3D());
  }

  //ADD MESH
  if (_meshRenderer != NULL) {
    Vector3D lastCamDir(_cameraDirX, _cameraDirY, _cameraDirZ);

    if (lastCamDir.angleBetween(lightDir.asVector3D())._degrees > 10) {

      FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
      vertices->add(camera->getCartesianPosition());
      vertices->add(camera->getCartesianPosition().add(lightDir.times(1000).asVector3D()) );

      DirectMesh* mesh = new DirectMesh(GLPrimitive::lines(),
                                        true,
                                        vertices->getCenter(),
                                        vertices->create(),
                                        (float)3.0,
                                        (float)1.0,
                                        new Color(Color::RED));
      _meshRenderer->addMesh(mesh);
    }
  }

  //SAVING STATE
  _cameraDirX = _camDir.x();
  _cameraDirY = _camDir.y();
  _cameraDirZ = _camDir.z();

  _upX = _up.x();
  _upY = _up.y();
  _upZ = _up.z();
}
