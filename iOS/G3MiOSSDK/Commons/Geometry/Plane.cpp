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

bool Plane::isVectorParallel(const Vector3D& vector) const{
  const double d = _normal.dot(vector);
  return (ISNAN(d) || IMathUtils::instance()->abs(d) < 0.01);
}

Angle Plane::vectorRotationForAxis(const Vector3D& vector, const Vector3D& axis) const{

  //Check Agustin Trujillo's document that explains how this algorithm works

  if (isVectorParallel(vector)) {
    return Angle::zero();
  }

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
  const double d1_2 = d1*d1;
  const double d2 = mu->sqrt(a*a + d1_2);
  const double d2_2 = d2*d2;

  //Calculating k's

  //Symplified by mathematica
  const double k1 =  (d1_2*x - a*(b*y + c*z))/d2_2;
  const double k2 =  (-(c*d2*y) + b*d2*z)/d2_2;
  const double k3 =  (a*(a*x + b*y + c*z))/d2_2;

  const double k4 =  (-(a*b*d1_2*x) + c*d2_2*(-(c*y) + b*z) + a_2*b*(b*y + c*z))/(d1_2*d2_2);
  const double k5 =  -(-(c*d1_2*x) + 2*a*b*c*y - a*b_2*z + a*c_2*z)/(d1_2*d2);
  const double k6 =  (b*(a*x + b*y + c*z))/d2_2;
  
  const double k7 =  -((-(a*c*d1_2*x) + b*d2_2*(c*y - b*z) + a_2*c*(b*y + c*z))/(d1_2*d2_2));
  const double k8 =  (-(b*d1_2*x) + a*b_2*y - a*c_2*y + 2*a*b*c*z)/(d1_2*d2);
  const double k9 =  -((c*(a*x + b*y + c*z))/d2_2);

/*
 
  const double k1 = (x*d1_2 - a*b*y - a*c*z) / d2_2;
  const double k2 = (b*z*d2 - c*y*d2) / d2_2;
  const double k3 = (a_2*x + a*b*y + a*c*z) / d2_2;

  const double k4 = ((a_2*b_2*y) + (c_2*y*d2_2) + (a_2*b*c*z) - (a*b*x*d1_2) - (b*c*z*d2_2)) / (d1_2 * d2_2);
  const double k5 = ((c*x*d1_2*d2) + (a_2*b*c*z) - (a*b*x*d1_2) - (b*c*z*d2_2)) / (d1_2 * d2_2);
  const double k6 = ((b_2*y*d1_2) + (a*b*x*d1_2) + (b*c*z*d1_2)) / (d1_2 * d2_2);

  const double k7 = ((a_2*c_2*z) + (b_2*z*d2_2) + (a_2*b*c*y) - (a*c*x*d1_2) - (b*c*y*d2_2)) / (d1_2 * d2_2);
  const double k8 = ((-b*x*d1_2*d2) + (a*b_2*y*d2) + (a*c_2*y*d2)) / (d1_2 * d2_2);
  const double k9 = ((c_2*z*d1_2) + (a*c*x*d1_2) + (b*c*y*d1_2)) / (d1_2 * d2_2);
*/


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

  const double firstSolution = mu->acos(c1);
  const double secondSolution = mu->acos(c2);

  //Considering the lower angle to rotate as solution
  double solution = firstSolution;
  if (firstSolution > secondSolution) { //Taking smaller value
    solution = secondSolution;
  }

  if (mu->abs((s1 * c1 + s2*SIN(solution) + s3)) > 0.001) { //if valid solution (can't compare with 0)
    solution = -solution;
  }

  //*********
  /*
   //Check code
  Angle res = Angle::fromRadians(solution);
  Vector3D nv = vector.rotateAroundAxis(axis, res);
  if (!isVectorParallel(nv)) {

    ILogger::instance()->logError("PROBLEM AT vectorRotationForAxis() V = %s, AXIS = %s, RESULT = %s, DEVIANCE = %f",
                                  vector.description().c_str(),
                                  axis.description().c_str(),
                                  res.description().c_str(),
                                  _normal.dot(nv));
  }
*/
   

   //**********

  return Angle::fromRadians(solution);

}

