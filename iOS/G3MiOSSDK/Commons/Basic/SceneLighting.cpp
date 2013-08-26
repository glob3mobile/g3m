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

void DefaultSceneLighting::modifyGLState(GLState* glState){
  glState->addGLFeature(new DirectionLightGLFeature(Vector3D(1, 0,0),  Color::yellow(), (float)0.0), false);
}