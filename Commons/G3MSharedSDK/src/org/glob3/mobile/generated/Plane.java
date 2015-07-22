package org.glob3.mobile.generated; 
//
//  Plane.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 14/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

//
//  Plane.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 14/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//



public class Plane
{
  private final Vector3D _normal ;
  private final double _d;

  private final Vector3F _normalF;
  private final float _dF;


  public static Plane fromPoints(Vector3D point0, Vector3D point1, Vector3D point2)
  {
    final Vector3D normal = point1.sub(point0).cross(point2.sub(point0)).normalized();
    final double d = -normal.dot(point0);
    return new Plane(normal, d);
  }

  public Plane(Vector3D normal, double d)
  {
     _normal = new Vector3D(normal.normalized());
     _d = d;
     _normalF = new Vector3F((float) normal._x, (float) normal._y, (float) normal._z).normalized();
     _dF = (float) d;
  }

  public Plane(Vector3D normal, Vector3D point)
  {
     _normal = new Vector3D(normal.normalized());
     _d = - normal._x * point._x - normal._y * point._y - normal._z * point._z;
     _normalF = new Vector3F((float) normal._x, (float) normal._y, (float) normal._z).normalized();
     _dF = (float) _d;
  }

  public Plane(double a, double b, double c, double d)
  {
     _normal = new Vector3D(new Vector3D(a,b,c).normalized());
     _d = d;
     _normalF = new Vector3F((float) a, (float) b, (float) c).normalized();
     _dF = (float) d;
  }

  public Plane(Plane that)
  {
     _normal = new Vector3D(that._normal);
     _d = that._d;
     _normalF = that._normalF;
     _dF = that._dF;
  }

  public final Plane transformedByTranspose(MutableMatrix44D M)
  {
    //int TODO_Multiplication_with_Matrix;
  
    final double a = _normal._x *M.get0() + _normal._y *M.get1() + _normal._z *M.get2() + _d *M.get3();
    final double b = _normal._x *M.get4() + _normal._y *M.get5() + _normal._z *M.get6() + _d *M.get7();
    final double c = _normal._x *M.get8() + _normal._y *M.get9() + _normal._z *M.get10() + _d *M.get11();
    final double d = _normal._x *M.get12() + _normal._y *M.get13() + _normal._z *M.get14() + _d *M.get15();
  
    return new Plane(a, b, c, d);
  }

  public final double signedDistance(Vector3D point)
  {
    // return point.dot(_normal) + _d;
    return ((_normal._x * point._x) + (_normal._y * point._y) + (_normal._z * point._z) + _d);
  }

  public final float signedDistance(Vector3F point)
  {
    // return point.dot(_normalF) + _dF;
    return ((_normalF._x * point._x) + (_normalF._y * point._y) + (_normalF._z * point._z) + _dF);
  }

  public final Vector3D intersectionWithRay(Vector3D origin, Vector3D direction)
  {
    //P = P1 + u (P2 - P1)
  
    final double x1 = origin._x;
    final double y1 = origin._y;
    final double z1 = origin._z;
    final Vector3D P2 = origin.add(direction);
    final double x2 = P2._x;
    final double y2 = P2._y;
    final double z2 = P2._z;
    final double A = _normal._x;
    final double B = _normal._y;
    final double C = _normal._z;
  
    final double den = A * (x1 -x2) + B * (y1 - y2) + C * (z1 - z2);
  
    if (den == 0)
    {
      return Vector3D.nan();
    }
  
    final double num = A * x1 + B * y1 + C * z1 + _d;
    final double t = num / den;
  
    final Vector3D intersection = origin.add(direction.times(t));
    return intersection;
  }

  public static Vector3D intersectionXYPlaneWithRay(Vector3D origin, Vector3D direction)
  {
    if (direction._z == 0)
       return Vector3D.nan();
    final double t = -origin._z / direction._z;
    if (t<0)
       return Vector3D.nan();
    Vector3D point = origin.add(direction.times(t));
    return point;
  }

  public final boolean isVectorParallel(Vector3D vector)
  {
    final double d = _normal.dot(vector);
    return ((d != d) || IMathUtils.instance().abs(d) < 0.01);
  }

  public final Angle vectorRotationForAxis(Vector3D vector, Vector3D axis)
  {
  
    //Check Agustin Trujillo's document that explains how this algorithm works
  
    if (isVectorParallel(vector))
    {
      return Angle.zero();
    }
  
    final IMathUtils mu = IMathUtils.instance();
  
    //Vector values
  
    final double a = axis._x;
    final double b = axis._y;
    final double c = axis._z;
    final double a_2 = a *a;
    final double b_2 = b *b;
    final double c_2 = c *c;
  
    final double x = vector._x;
    final double y = vector._y;
    final double z = vector._z;
  
    final double nx = _normal._x;
    final double ny = _normal._y;
    final double nz = _normal._z;
  
    //Diagonal values
    final double d1 = mu.sqrt(b *b + c *c);
    final double d1_2 = d1 *d1;
    final double d2 = mu.sqrt(a *a + d1_2);
    final double d2_2 = d2 *d2;
  
    //Calculating k's
  
    //Symplified by mathematica
    final double k1 = (d1_2 *x - a*(b *y + c *z))/d2_2;
    final double k2 = (-(c *d2 *y) + b *d2 *z)/d2_2;
    final double k3 = (a*(a *x + b *y + c *z))/d2_2;
  
    final double k4 = (-(a *b *d1_2 *x) + c *d2_2*(-(c *y) + b *z) + a_2 *b*(b *y + c *z))/(d1_2 *d2_2);
    final double k5 = -(-(c *d1_2 *x) + 2 *a *b *c *y - a *b_2 *z + a *c_2 *z)/(d1_2 *d2);
    final double k6 = (b*(a *x + b *y + c *z))/d2_2;
  
    final double k7 = -((-(a *c *d1_2 *x) + b *d2_2*(c *y - b *z) + a_2 *c*(b *y + c *z))/(d1_2 *d2_2));
    final double k8 = (-(b *d1_2 *x) + a *b_2 *y - a *c_2 *y + 2 *a *b *c *z)/(d1_2 *d2);
    final double k9 = -((c*(a *x + b *y + c *z))/d2_2);
  
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
    final double s1 = nx * k1 + ny * k4 + nz * k7;
    final double s2 = nx * k2 + ny * k5 + nz * k8;
    final double s3 = nx * k3 + ny * k6 + nz * k9;
  
    final double s1_2 = s1 *s1;
    final double s2_2 = s2 *s2;
    final double s3_2 = s3 *s3;
  
    //Calculating angle sinus
    final double rootValue = 4 * ((s1_2 * s3_2) - ((s1_2 + s2_2) * (s3_2 - s2_2)));
    final double root = mu.sqrt(rootValue);
    final double firstTerm = -2 *s1 *s3;
    final double divisor = 2 * (s1_2 + s2_2);
  
    final double c1 = (firstTerm + root) / divisor;
    final double c2 = (firstTerm - root) / divisor;
  
    final double firstSolution = mu.acos(c1);
    final double secondSolution = mu.acos(c2);
  
    //Considering the lower angle to rotate as solution
    double solution = firstSolution;
    if (firstSolution > secondSolution) //Taking smaller value
    {
      solution = secondSolution;
    }
  
    if (mu.abs((s1 * c1 + s2 *java.lang.Math.sin(solution) + s3)) > 0.001) //if valid solution (can't compare with 0)
    {
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
  
    return Angle.fromRadians(solution);
  
  }

  public final Vector3D getNormal()
  {
    return _normal;
  }


}