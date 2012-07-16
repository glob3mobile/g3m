//
//  Frustum.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 15/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "Frustum.h"


bool Frustum::contains(const Vector3D& point) {
  if (_leftPlane.evaluate(point)   > 0) return false;
  if (_rightPlane.evaluate(point)  > 0) return false;
  if (_bottomPlane.evaluate(point) > 0) return false;
  if (_topPlane.evaluate(point)    > 0) return false;
  if (_nearPlane.evaluate(point)   > 0) return false;
  if (_farPlane.evaluate(point)    > 0) return false;
  return true;
}
