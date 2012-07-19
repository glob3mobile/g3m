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

Vector2D Box::projectedExtent(const RenderContext *rc) const {
  const std::vector<Vector3D> corners = getCorners();

  const Vector2D pixel0 = rc->getCamera()->point2Pixel(corners[0]);

  double lowerX = pixel0.x();
  double upperX = pixel0.x();
  double lowerY = pixel0.y();
  double upperY = pixel0.y();
  
  const int cornersSize = corners.size();
  for (int i = 1; i < cornersSize; i++) {
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
  
  return Vector2D(width, height);
}

double Box::squaredProjectedArea(const RenderContext* rc) const {
  const Vector2D extent = projectedExtent(rc);
  return extent.x() * extent.y();
}
