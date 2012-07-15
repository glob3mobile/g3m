//
//  Frustum.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 15/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "Frustum.h"


bool Frustum::isInside(Vector3D point)
{
  if (leftPlane.evaluate(point)>0) return false;
  if (rightPlane.evaluate(point)>0) return false;
  if (bottomPlane.evaluate(point)>0) return false;
  if (topPlane.evaluate(point)>0) return false;
  if (nearPlane.evaluate(point)>0) return false;
  if (farPlane.evaluate(point)>0) return false;
  return true;
}