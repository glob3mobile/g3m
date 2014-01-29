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

Angle Plane::angleRotatedToVector(const Vector3D& vector, const Vector3D& axis) const{

  IMathUtils* mu = IMathUtils::instance();

  //Vector values

  const double a = axis._x;
  const double b = axis._y;
  const double c = axis._z;
  const double a_2 = a*a;
  const double b_2 = b*b;
  const double c_2 = c*c;

  const double x = vector._x;
  const double y = vector._y;
  const double z = vector._z;

  const double nx = _normal._x;
  const double ny = _normal._y;
  const double nz = _normal._z;

  //Diagonal values
  const double d1 = mu->sqrt(b*b + c*c);
  const double d2 = mu->sqrt(a*a + d1*d1);
  const double d1_2 = d1*d1;
  const double d2_2 = d2*d2;

  //Calculating k's

  const double k1 = (x*d1_2 - a*b*y - a*c*z) / d2_2; //TODO!!!
  const double k2 = (b*z - c*y) / d2;
  const double k3 = (a*x + b*y + c*z) * a / d2_2;

  const double k4 = ((a_2*b_2*y) + (c_2*y*d2_2) + (a_2*b*c*z) - (a*b*x*d1_2) - (b*c*z*d2_2)) / (d1_2 * d2_2);
  const double k5 = ((c*x*d1_2*d2) + (a_2*b*c*z) - (a*b*x*d1_2) - (b*c*z*d2_2)) / (d1_2 * d2_2);
  const double k6 = ((b_2*y*d1_2) + (a*b*x*d1_2) + (b*c*z*d1_2)) / (d1_2 * d2_2);

  const double k7 = ((a_2*c_2*z) + (b_2*z*d2_2) + (a_2*b*c*y) - (a*c*x*d1_2) - (b*c*y*d2_2)) / (d1_2 * d2_2);
  const double k8 = ((-b*x*d1_2*d2) + (a*b_2*y*d2) + (a*c_2*y*d2)) / (d1_2 * d2_2);
  const double k9 = ((c_2*z*d1_2) + (a*c*x*d1_2) + (b*c*y*d1_2)) / (d1_2 * d2_2);

  //Calculating S's
  const double s1 = nx * k1 + ny * k4 + nz * k7;
  const double s2 = nx * k2 + ny * k5 + nz * k8;
  const double s3 = nx * k3 + ny * k6 + nz * k9;

  const double s1_2 = s1*s1;
  const double s2_2 = s2*s2;
  const double s3_2 = s3*s3;

  //Calculating angle sinus
  const double rootValue = 4 * ((s1_2 * s3_2) - ((s1_2 + s2_2) * (s3_2 - s2_2)));
  const double root = mu->sqrt(rootValue);
  const double firstTerm = -2*s1*s3;
  const double divisor = 2 * (s1_2 + s2_2);

  const double c1 = (firstTerm + root) / divisor;
  const double c2 = (firstTerm - root) / divisor;

  double firstSolution = mu->acos(c1);
  //Check if the solution is valid
  if ((s1 * c1 + s2*SIN(firstSolution) + s3) != 0){
    firstSolution = -firstSolution;
  }

  double secondSolution = mu->acos(c2);
  //Check if the solution is valid
  if ((s1 * c2 + s2*SIN(secondSolution) + s3) != 0){
    secondSolution = -secondSolution;
  }

  //We return the shortest angle
  if (mu->abs(firstSolution) < mu->abs(secondSolution)) {
    return Angle::fromDegrees(firstSolution);
  } else{
    return Angle::fromDegrees(secondSolution);
  }

}

