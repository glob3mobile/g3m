//
//  Ellipsoid.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 04/06/13.
//
//

#include "Ellipsoid.hpp"


std::vector<double> Ellipsoid::intersectionsDistances(const Vector3D& origin,
                                                              const Vector3D& direction) const {
  std::vector<double> intersections;
  
  Vector3D oneOverRadiiSquared = getOneOverRadiiSquared();
  
  // By laborious algebraic manipulation....
  const double a = (direction._x * direction._x * oneOverRadiiSquared._x +
                    direction._y * direction._y * oneOverRadiiSquared._y +
                    direction._z * direction._z * oneOverRadiiSquared._z);
  
  const double b = 2.0 * (origin._x * direction._x * oneOverRadiiSquared._x +
                          origin._y * direction._y * oneOverRadiiSquared._y +
                          origin._z * direction._z * oneOverRadiiSquared._z);
  
  const double c = (origin._x * origin._x * oneOverRadiiSquared._x +
                    origin._y * origin._y * oneOverRadiiSquared._y +
                    origin._z * origin._z * oneOverRadiiSquared._z - 1.0);
  
  // Solve the quadratic equation: ax^2 + bx + c = 0.
  // Algorithm is from Wikipedia's "Quadratic equation" topic, and Wikipedia credits
  // Numerical Recipes in C, section 5.6: "Quadratic and Cubic Equations"
  const double discriminant = b * b - 4 * a * c;
  if (discriminant < 0.0) {
    // no intersections
    return intersections;
  }
  else if (discriminant == 0.0) {
    // one intersection at a tangent point
    //return new double[1] { -0.5 * b / a };
    intersections.push_back(-0.5 * b / a);
    return intersections;
  }
  
  const double t = -0.5 * (b + (b > 0.0 ? 1.0 : -1.0) * IMathUtils::instance()->sqrt(discriminant));
  const double root1 = t / a;
  const double root2 = c / t;
  
  // Two intersections - return the smallest first.
  if (root1 < root2) {
    intersections.push_back(root1);
    intersections.push_back(root2);
  }
  else {
    intersections.push_back(root2);
    intersections.push_back(root1);
  }
  return intersections;
}

