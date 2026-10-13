package org.glob3.mobile.generated;
//
//  Ellipsoid.cpp
//  G3M
//
//  Created by Jose Miguel SN on 04/06/13.
//
//

//
//  Ellipsoid.h
//  G3M
//
//  Created by Jose Miguel SN on 04/06/13.
//
//





public class Ellipsoid
{
  public final Vector3D _center ;
  public final Vector3D _radii ;

  public final Vector3D _radiiSquared ;
  public final Vector3D _radiiToTheFourth ;
  public final Vector3D _oneOverRadiiSquared ;


  public Ellipsoid(Vector3D center, Vector3D radii)
  {
     _center = center;
     _radii = radii;
     _radiiSquared = new Vector3D(new Vector3D(radii._x * radii._x, radii._y * radii._y, radii._z * radii._z));
     _radiiToTheFourth = new Vector3D(new Vector3D(_radiiSquared._x * _radiiSquared._x, _radiiSquared._y * _radiiSquared._y, _radiiSquared._z * _radiiSquared._z));
     _oneOverRadiiSquared = new Vector3D(new Vector3D(1.0 / (radii._x * radii._x), 1.0 / (radii._y * radii._y), 1.0 / (radii._z * radii._z)));
  }

  public final Vector3D getCenter()
  {
    return _center;
  }

  public final Vector3D getRadii()
  {
    return _radii;
  }

  public final Vector3D getRadiiSquared()
  {
    return _radiiSquared;
  }

  public final Vector3D getRadiiToTheFourth()
  {
    return _radiiToTheFourth;
  }

  public final Vector3D getOneOverRadiiSquared()
  {
    return _oneOverRadiiSquared;
  }

  public final double getMeanRadius()
  {
    return (_radii._x + _radii._y + _radii._y) / 3;
  }

  public final java.util.ArrayList<Double> intersectionsDistances(Vector3D origin, Vector3D direction)
  {
    return intersectionsDistances(origin._x, origin._y, origin._z, direction._x, direction._y, direction._z);
  }

  public final java.util.ArrayList<Double> intersectionsDistances(double originX, double originY, double originZ, double directionX, double directionY, double directionZ)
  {
    return intersectionsDistances(_center, _oneOverRadiiSquared, originX, originY, originZ, directionX, directionY, directionZ);
  }

  public static java.util.ArrayList<Double> intersectionsDistances(Vector3D center, Vector3D oneOverRadiiSquared, double originX, double originY, double originZ, double directionX, double directionY, double directionZ)
  {
    final double mX = originX - center._x;
    final double mY = originY - center._y;
    final double mZ = originZ - center._z;
  
    java.util.ArrayList<Double> result = new java.util.ArrayList<Double>();
  
    // By laborious algebraic manipulation....
    final double a = (directionX * directionX * oneOverRadiiSquared._x + directionY * directionY * oneOverRadiiSquared._y + directionZ * directionZ * oneOverRadiiSquared._z);
  
    final double b = 2.0 * (mX * directionX * oneOverRadiiSquared._x + mY * directionY * oneOverRadiiSquared._y + mZ * directionZ * oneOverRadiiSquared._z);
  
    final double c = (mX * mX * oneOverRadiiSquared._x + mY * mY * oneOverRadiiSquared._y + mZ * mZ * oneOverRadiiSquared._z - 1.0);
  
    // Solve the quadratic equation: ax^2 + bx + c = 0.
    // Algorithm is from Wikipedia's "Quadratic equation" topic, and Wikipedia credits
    // Numerical Recipes in C, section 5.6: "Quadratic and Cubic Equations"
    final double discriminant = b * b - 4 * a * c;
    if (discriminant < 0.0)
    {
      // no intersections
    }
    else if (discriminant == 0.0)
    {
      // one intersection at a tangent point
      result.add(-0.5 * b / a);
    }
    else
    {
      final double t = -0.5 * (b + (b > 0.0 ? 1.0 : -1.0) * IMathUtils.instance().sqrt(discriminant));
      final double root1 = t / a;
      final double root2 = c / t;
  
      // Two intersections - return the smallest first.
      if (root1 < root2)
      {
        result.add(root1);
        result.add(root2);
      }
      else
      {
        result.add(root2);
        result.add(root1);
      }
    }
  
    return result;
  }
}