//
//  Ellipsoid.cpp
//  G3M
//
//  Created by Jose Miguel SN on 04/06/13.
//
//

#include "Ellipsoid.hpp"

#include "IMathUtils.hpp"


std::vector<double> Ellipsoid::intersectionsDistances(double xoriginX,
                                                      double xoriginY,
                                                      double xoriginZ,
                                                      double directionX,
                                                      double directionY,
                                                      double directionZ) const {
  const double mX = xoriginX - _center._x;
  const double mY = xoriginY - _center._y;
  const double mZ = xoriginZ - _center._z;

  std::vector<double> result;

  // By laborious algebraic manipulation....
  const double a = (directionX * directionX * _oneOverRadiiSquared._x +
                    directionY * directionY * _oneOverRadiiSquared._y +
                    directionZ * directionZ * _oneOverRadiiSquared._z);

  const double b = 2.0 * (mX * directionX * _oneOverRadiiSquared._x +
                          mY * directionY * _oneOverRadiiSquared._y +
                          mZ * directionZ * _oneOverRadiiSquared._z);

  const double c = (mX * mX * _oneOverRadiiSquared._x +
                    mY * mY * _oneOverRadiiSquared._y +
                    mZ * mZ * _oneOverRadiiSquared._z - 1.0);

  // Solve the quadratic equation: ax^2 + bx + c = 0.
  // Algorithm is from Wikipedia's "Quadratic equation" topic, and Wikipedia credits
  // Numerical Recipes in C, section 5.6: "Quadratic and Cubic Equations"
  const double discriminant = b * b - 4 * a * c;
  if (discriminant < 0.0) {
    // no intersections
  }
  else if (discriminant == 0.0) {
    // one intersection at a tangent point
    result.push_back(-0.5 * b / a);
  }
  else {
    const double t = -0.5 * (b + (b > 0.0 ? 1.0 : -1.0) * IMathUtils::instance()->sqrt(discriminant));
    const double root1 = t / a;
    const double root2 = c / t;

    // Two intersections - return the smallest first.
    if (root1 < root2) {
      result.push_back(root1);
      result.push_back(root2);
    }
    else {
      result.push_back(root2);
      result.push_back(root1);
    }
  }

  return result;
}
