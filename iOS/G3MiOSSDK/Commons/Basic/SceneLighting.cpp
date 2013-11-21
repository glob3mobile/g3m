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
#include "Context.hpp"
#include "Camera.hpp"

void FixedFocusSceneLighting::modifyGLState(GLState* glState, const G3MRenderContext* rc) {
  const Vector3D lightDir(1, 0,0);

  //STATIC LIGHT
  DirectionLightGLFeature* f = (DirectionLightGLFeature*) glState->getGLFeature(GLF_DIRECTION_LIGTH);
  if (f == NULL) {
    glState->clearGLFeatureGroup(LIGHTING_GROUP);
    glState->addGLFeature(new DirectionLightGLFeature(lightDir,
                                                      Color::yellow(),
                                                      Color::white()),
                          false);
  }
  /* //Add this to obtain a rotating "sun"
   else{

   ITimer *timer = IFactory::instance()->createTimer();
   double sec = timer->now().milliseconds();
   delete timer;
   double angle = ((int)sec % 36000) / 100.0;

   MutableMatrix44D m = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(angle),
   Vector3D::upZ(),
   Vector3D::zero);

   f->setLightDirection(lightDir.transformedBy(m, 1.0));

   }
   */

}

CameraFocusSceneLighting::CameraFocusSceneLighting(const Color& ambient, const Color& diffuse):
_ambientColor(ambient),
_diffuseColor(diffuse),
_cameraDirX(0),
_cameraDirY(0),
_cameraDirZ(0)
{

}

void CameraFocusSceneLighting::modifyGLState(GLState* glState, const G3MRenderContext* rc) {

  Vector3D camDir = rc->getCurrentCamera()->getViewDirection();
  if (_cameraDirX == camDir._x && _cameraDirY == camDir._y && _cameraDirZ == camDir._z){
    return;
  }
  _cameraDirX = camDir._x;
  _cameraDirY = camDir._y;
  _cameraDirZ = camDir._z;

  const Vector3D cameraVector = camDir.times(-1);

  //Light slightly different of camera position
  const Vector3D rotationLightDirAxis = rc->getCurrentCamera()->getUp().cross(cameraVector);
  const Vector3D lightDir = cameraVector.rotateAroundAxis(rotationLightDirAxis, Angle::fromDegrees(10.0));

  DirectionLightGLFeature* f = (DirectionLightGLFeature*) glState->getGLFeature(GLF_DIRECTION_LIGTH);
  if (f == NULL) {
    glState->clearGLFeatureGroup(LIGHTING_GROUP);
    glState->addGLFeature(new DirectionLightGLFeature(lightDir,
                                                      _diffuseColor,
                                                      _ambientColor),
                          false);
  }
  else{
    f->setLightDirection(lightDir);

    printf("LD: %f, %f, %f\n", lightDir._x, lightDir._y, lightDir._z);
  }
  
}
