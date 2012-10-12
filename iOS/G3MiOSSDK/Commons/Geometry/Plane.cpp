//
//  Plane.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 14/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include "Plane.hpp"

Plane Plane::transformedByTranspose(const MutableMatrix44D& M) const
{
  int TODO_Multiplication_with_Matrix;
  
  double a = _normal._x*M.get( 0) + _normal._y*M.get( 1) + _normal._z*M.get( 2) + _d*M.get( 3);
  double b = _normal._x*M.get( 4) + _normal._y*M.get( 5) + _normal._z*M.get( 6) + _d*M.get( 7);
  double c = _normal._x*M.get( 8) + _normal._y*M.get( 9) + _normal._z*M.get(10) + _d*M.get(11);
  double d = _normal._x*M.get(12) + _normal._y*M.get(13) + _normal._z*M.get(14) + _d*M.get(15);
  
  return Plane(a,b,c,d);
}

Vector3D Plane::intersectionWithRay(const Vector3D& origin, const Vector3D& direction) const
{
  //P = P1 + u (P2 - P1)
  
  double x1 = origin._x, y1 = origin._y, z1 = origin._z;
  Vector3D P2 = origin.add(direction);
  double x2 = P2._x, y2 = P2._y, z2 = P2._z;
  double A = _normal._x, B = _normal._y, C = _normal._z;
  
  double den = A * (x1 -x2) + B * (y1 - y2) + C * (z1 - z2);
  
  if (den == 0){
    return Vector3D::nan();
  } else {
    double num = A * x1 + B * y1 + C * z1 + _d;
    double t = num / den;
    
    Vector3D intersection = origin.add(direction.times(t));
    
    return intersection;
  }
}
