//
//  Plane.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 14/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include "Plane.hpp"

Plane Plane::transformedBy(const MutableMatrix44D& M) const
{
  int TODO_Multiplication_with_Matrix;
  
  double a = _normal.x()*M.get(0) + _normal.y()*M.get(4) + _normal.z()*M.get(8) + _d*M.get(12);
  double b = _normal.x()*M.get(1) + _normal.y()*M.get(5) + _normal.z()*M.get(9) + _d*M.get(13);
  double c = _normal.x()*M.get(2) + _normal.y()*M.get(6) + _normal.z()*M.get(10) + _d*M.get(14);
  double d = _normal.x()*M.get(3) + _normal.y()*M.get(7) + _normal.z()*M.get(11) + _d*M.get(15);
  
  return Plane(a,b,c,d);
}

Vector3D Plane::intersectionWithRay(const Vector3D& origin, const Vector3D& direction) const
{
  //P = P1 + u (P2 - P1)
  
  double x1 = origin.x(), y1 = origin.y(), z1 = origin.z();
  Vector3D P2 = origin.add(direction);
  double x2 = P2.x(), y2 = P2.y(), z2 = P2.z();
  double A = _normal.x(), B = _normal.y(), C = _normal.z();
  
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
