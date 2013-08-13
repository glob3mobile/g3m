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

}