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

#include "IFactory.hpp"
#include "ITimer.hpp"

void DefaultSceneLighting::modifyGLState(GLState* glState){
  glState->clearGLFeatureGroup(LIGHTING_GROUP);

  Vector3D lightDir(1, 0,0);

  ITimer *timer = IFactory::instance()->createTimer();

  double sec = timer->now().milliseconds();

  delete timer;

  double angle = ((int)sec % 36000) / 100.0;

  MutableMatrix44D m = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(angle),
                                                                     Vector3D::upZ(),
                                                                     Vector3D::zero);

  glState->addGLFeature(new DirectionLightGLFeature(lightDir.transformedBy(m, 1.0),
                                                    Color::yellow(),
                                                    (float)0.2),
                        false);
}