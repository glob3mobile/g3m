package org.glob3.mobile.generated; 
//
//  Ellipsoid.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 04/06/13.
//
//

//
//  Ellipsoid.h
//  G3MiOSSDK
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
     _center = new Vector3D(center);
     _radii = new Vector3D(radii);
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
    java.util.ArrayList<Double> intersections = new java.util.ArrayList<Double>();
  
    // By laborious algebraic manipulation....
    final double a = (directionX * directionX * _oneOverRadiiSquared._x + directionY * directionY * _oneOverRadiiSquared._y + directionZ * directionZ * _oneOverRadiiSquared._z);
  
    final double b = 2.0 * (originX * directionX * _oneOverRadiiSquared._x + originY * directionY * _oneOverRadiiSquared._y + originZ * directionZ * _oneOverRadiiSquared._z);
  
    final double c = (originX * originX * _oneOverRadiiSquared._x + originY * originY * _oneOverRadiiSquared._y + originZ * originZ * _oneOverRadiiSquared._z - 1.0);
  
    // Solve the quadratic equation: ax^2 + bx + c = 0.
    // Algorithm is from Wikipedia's "Quadratic equation" topic, and Wikipedia credits
    // Numerical Recipes in C, section 5.6: "Quadratic and Cubic Equations"
    final double discriminant = b * b - 4 * a * c;
    if (discriminant < 0.0)
    {
      // no intersections
      return intersections;
    }
    else if (discriminant == 0.0)
    {
      // one intersection at a tangent point
      //return new double[1] { -0.5 * b / a };
      intersections.add(-0.5 * b / a);
      return intersections;
    }
  
    final double t = -0.5 * (b + (b > 0.0 ? 1.0 : -1.0) * IMathUtils.instance().sqrt(discriminant));
    final double root1 = t / a;
    final double root2 = c / t;
  
    // Two intersections - return the smallest first.
    if (root1 < root2)
    {
      intersections.add(root1);
      intersections.add(root2);
    }
    else
    {
      intersections.add(root2);
      intersections.add(root1);
    }
    return intersections;
  }
}