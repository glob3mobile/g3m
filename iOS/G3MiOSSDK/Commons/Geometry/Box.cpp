//
//  Box.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 17/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include "Box.hpp"
#include "Vector2D.hpp"
#include "Camera.hpp"

const std::vector<Vector3D> Box::getCorners() const
{
#ifdef C_CODE
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
#endif
#ifdef JAVA_CODE
  if (_corners == null) {
    _corners = new java.util.ArrayList<Vector3D>(8);
    
    _corners.add(new Vector3D(_lower.x(), _lower.y(), _lower.z()));
    _corners.add(new Vector3D(_lower.x(), _lower.y(), _upper.z()));
    _corners.add(new Vector3D(_lower.x(), _upper.y(), _lower.z()));
    _corners.add(new Vector3D(_lower.x(), _upper.y(), _upper.z()));
    _corners.add(new Vector3D(_upper.x(), _lower.y(), _lower.z()));
    _corners.add(new Vector3D(_upper.x(), _lower.y(), _upper.z()));
    _corners.add(new Vector3D(_upper.x(), _upper.y(), _lower.z()));
    _corners.add(new Vector3D(_upper.x(), _upper.y(), _upper.z()));
  }
  return _corners;
#endif
}

Vector2D Box::projectedExtent(const RenderContext *rc) const {
  const std::vector<Vector3D> corners = getCorners();

  const Vector2D pixel0 = rc->getCurrentCamera()->point2Pixel(corners[0]);

  double lowerX = pixel0.x();
  double upperX = pixel0.x();
  double lowerY = pixel0.y();
  double upperY = pixel0.y();
  
  const int cornersSize = corners.size();
  for (int i = 1; i < cornersSize; i++) {
    const Vector2D pixel = rc->getCurrentCamera()->point2Pixel(corners[i]);
    
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

bool Box::contains(const Vector3D& p) const{
  const static double margin = 1e-3;
  if (p.x() < _lower.x() - margin) return false;
  if (p.x() > _upper.x() + margin) return false;
  
  if (p.y() < _lower.y() - margin) return false;
  if (p.y() > _upper.y() + margin) return false;
  
  if (p.z() < _lower.z() - margin) return false;
  if (p.z() > _upper.z() + margin) return false;
  
  return true;
}

Vector3D Box::intersectionWithRay(const Vector3D& origin, const Vector3D& direction) const{
  
  //MIN X
  {
    Plane p( Vector3D(1.0, 0.0, 0.0), _lower.x());
    Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  //MAX X
  {
    Plane p( Vector3D(1.0, 0.0, 0.0), _upper.x());
    Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  //MIN Y
  {
    Plane p( Vector3D(0.0, 1.0, 0.0), _lower.y());
    Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  //MAX Y
  {
    Plane p( Vector3D(0.0, 1.0, 0.0), _upper.y());
    Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  //MIN Z
  {
    Plane p( Vector3D(0.0, 0.0, 1.0), _lower.z());
    Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  //MAX Z
  {
    Plane p( Vector3D(0.0, 0.0, 1.0), _upper.z());
    Vector3D inter = p.intersectionWithRay(origin, direction);
    if (!inter.isNan() && contains(inter)) return inter;
  }
  
  return Vector3D::nan();  
}
