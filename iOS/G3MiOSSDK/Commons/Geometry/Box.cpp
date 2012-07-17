//
//  Box.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 17/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "Box.h"
#include "Camera.hpp"


std::vector<Vector3D> Box::getCorners() const
{  
  Vector3D c[8] = {
    Vector3D(_lower.x(), _lower.y(), _lower.z()),
    Vector3D(_lower.x(), _lower.y(), _upper.z()),
    Vector3D(_lower.x(), _upper.y(), _lower.z()),
    Vector3D(_lower.x(), _upper.y(), _upper.z()),
    Vector3D(_upper.x(), _lower.y(), _lower.z()),
    Vector3D(_upper.x(), _lower.y(), _upper.z()),
    Vector3D(_upper.x(), _upper.y(), _lower.z()),
    Vector3D(_upper.x(), _upper.y(), _upper.z())
  };
  
  std::vector<Vector3D> corners (c, c+8);
  return corners;
}


unsigned int Box::projectedSize(const RenderContext* rc) const
{
  int __agustin_at_work;

  std::vector<Vector3D> corners = getCorners();
  
  Vector2D pixel = rc->getCamera()->point2Pixel(corners[0]);
  //printf ("pixel %f %f\n", pixel.x(), pixel.y());
  
  
  return 200;
}
