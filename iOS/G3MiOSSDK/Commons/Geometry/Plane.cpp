//
//  Plane.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 14/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include "Plane.hpp"

Plane Plane::transformedByTranspose(const MutableMatrix44D& M) const {
  //int TODO_Multiplication_with_Matrix;

  const double a = _normal._x*M.get0() + _normal._y*M.get1() + _normal._z*M.get2() + _d*M.get3();
  const double b = _normal._x*M.get4() + _normal._y*M.get5() + _normal._z*M.get6() + _d*M.get7();
  const double c = _normal._x*M.get8() + _normal._y*M.get9() + _normal._z*M.get10() + _d*M.get11();
  const double d = _normal._x*M.get12() + _normal._y*M.get13() + _normal._z*M.get14() + _d*M.get15();

  return Plane(a,b,c,d);
}

Vector3D Plane::intersectionWithRay(const Vector3D& origin,
                                    const Vector3D& direction) const {
  //P = P1 + u (P2 - P1)

  const double x1 = origin._x, y1 = origin._y, z1 = origin._z;
  const Vector3D P2 = origin.add(direction);
  const double x2 = P2._x, y2 = P2._y, z2 = P2._z;
  const double A = _normal._x, B = _normal._y, C = _normal._z;

  const double den = A * (x1 -x2) + B * (y1 - y2) + C * (z1 - z2);

  if (den == 0) {
    return Vector3D::nan();
  }

  const double num = A * x1 + B * y1 + C * z1 + _d;
  const double t = num / den;

  const Vector3D intersection = origin.add(direction.times(t));
  return intersection;
}


Vector3D Plane::intersectionXYPlaneWithRay(const Vector3D& origin,
                                           const Vector3D& direction)
{
  if (direction._z == 0) return Vector3D::nan();
  const double t = -origin._z / direction._z;
  if (t<0) return Vector3D::nan();
  Vector3D point = origin.add(direction.times(t));
  return point;
}

