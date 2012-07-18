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
  
  return std::vector<Vector3D>(c, c+8);
}


int Box::squaredProjectedSize(const RenderContext* rc) const {
  std::vector<Vector3D> corners = getCorners();
  
  double lowerX = 1E7;
  double lowerY = 1E7;
  double upperX = -1E7;
  double upperY = -1E7;
  
  const int cornersSize = corners.size();
  for (int i = 0; i < cornersSize; i++) {
    const Vector2D pixel = rc->getCamera()->point2Pixel(corners[i]);
    
    const double x = pixel.x();
    const double y = pixel.y();
    
    if (x < lowerX) { lowerX = x; }
    if (y < lowerY) { lowerY = y; }
    
    if (x > upperX) { upperX = x; }
    if (y > upperY) { upperY = y; }
  }
  
  const double width = upperX - lowerX;
  const double height = upperY - lowerY;
  
  return (width * height);
}
