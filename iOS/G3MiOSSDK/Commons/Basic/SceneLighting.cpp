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
                                                      (float)0.4),
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

void CameraFocusSceneLighting::modifyGLState(GLState* glState, const G3MRenderContext* rc) {

  const Vector3D cameraVector = rc->getCurrentCamera()->getViewDirection().times(-1);
  //const Vector3D lightDir = cameraVector; //Light from camera

  //Light slightly different of camera position
  const Vector3D rotationLightDirAxis = rc->getCurrentCamera()->getUp().cross(cameraVector);
  const Vector3D lightDir = cameraVector.rotateAroundAxis(rotationLightDirAxis, Angle::fromDegrees(10.0));

  DirectionLightGLFeature* f = (DirectionLightGLFeature*) glState->getGLFeature(GLF_DIRECTION_LIGTH);
  if (f == NULL) {
    glState->clearGLFeatureGroup(LIGHTING_GROUP);
    glState->addGLFeature(new DirectionLightGLFeature(lightDir,
                                                      Color::white(),
                                                      (float)0.4),
                          false);
  }
  else{
    f->setLightDirection(lightDir);
  }
  
}
