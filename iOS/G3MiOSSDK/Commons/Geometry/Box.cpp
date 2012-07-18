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
  const Vector3D c[8] = {
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


int Box::projectedSize(const RenderContext* rc) const
{
  int __agustin_at_work;

  std::vector<Vector3D> corners = getCorners();

  double lowerX = 1E7;
  double lowerY = 1E7;
  double upperX = -1E7;
  double upperY = -1E7;
  
  const int cornersSize = corners.size();
  for (int i = 0; i < cornersSize; i++) {
    Vector2D pixel = rc->getCamera()->point2Pixel(corners[i]);
    
    const double x = pixel.x();
    const double y = pixel.y();
    
    if (x < lowerX) { lowerX = x; }
    if (y < lowerY) { lowerY = y; }
    
    if (x > upperX) { upperX = x; }
    if (y > upperY) { upperY = y; }
  }
  
  double width = upperX - lowerX;
  double height = upperY - lowerY;

  return sqrt(width * height);
  
//  Vector2D pixel = rc->getCamera()->point2Pixel(corners[0]);
//  //printf ("pixel %f %f\n", pixel.x(), pixel.y());
//  
//  
//  return 200;
}
