package org.glob3.mobile.generated; 
//
//  Plane.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 14/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

//
//  Plane.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 14/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//



public class Plane
{


  public Plane() //Empty constructor
  {
	  _normal = new Vector3D(0.0,0.0,0.0);
	  _d = 0.0;
  }

  public Plane(Vector3D point0, Vector3D point1, Vector3D point2)
  {
	  _normal = new Vector3D(point1.sub(point0).cross(point2.sub(point0)).normalized());
	  _d = -_normal.dot(point0);
  }

  public Plane(Vector3D normal, double d)
  {
	  _normal = new Vector3D(normal.normalized());
	  _d = d;
  }

  public Plane(double a, double b, double c, double d)
  {
	  _normal = new Vector3D(new Vector3D(a,b,c).normalized());
	  _d = d;
  }

  public Plane(Plane that)
  {
	  _normal = new Vector3D(that._normal);
	  _d = that._d;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Plane transformedByTranspose(const MutableMatrix44D& M) const
  public final Plane transformedByTranspose(MutableMatrix44D M)
  {
	int TODO_Multiplication_with_Matrix;
  
	double a = _normal._x *M.get(0) + _normal._y *M.get(1) + _normal._z *M.get(2) + _d *M.get(3);
	double b = _normal._x *M.get(4) + _normal._y *M.get(5) + _normal._z *M.get(6) + _d *M.get(7);
	double c = _normal._x *M.get(8) + _normal._y *M.get(9) + _normal._z *M.get(10) + _d *M.get(11);
	double d = _normal._x *M.get(12) + _normal._y *M.get(13) + _normal._z *M.get(14) + _d *M.get(15);
  
	return new Plane(a, b, c, d);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double signedDistance(const Vector3D& point) const
  public final double signedDistance(Vector3D point)
  {
	return point.dot(_normal) + _d;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D intersectionWithRay(const Vector3D& origin, const Vector3D& direction) const
  public final Vector3D intersectionWithRay(Vector3D origin, Vector3D direction)
  {
	//P = P1 + u (P2 - P1)
  
	double x1 = origin._x;
	double y1 = origin._y;
	double z1 = origin._z;
	Vector3D P2 = origin.add(direction);
	double x2 = P2._x;
	double y2 = P2._y;
	double z2 = P2._z;
	double A = _normal._x;
	double B = _normal._y;
	double C = _normal._z;
  
	double den = A * (x1 -x2) + B * (y1 - y2) + C * (z1 - z2);
  
	if (den == 0)
	{
	  return Vector3D.nan();
	}
	else
	{
	  double num = A * x1 + B * y1 + C * z1 + _d;
	  double t = num / den;
  
	  Vector3D intersection = origin.add(direction.times(t));
  
	  return intersection;
	}
  }

  private final Vector3D _normal ;
  private final double _d;

}