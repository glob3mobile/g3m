package org.glob3.mobile.generated; 
//
//  StraightLine.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 5/2/16.
//
//

//
//  StraightLine.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 04/02/16.
//  Copyright (c) 2016 Universidad de Las Palmas. All rights reserved.
//



public class StraightLine
{

  private final Vector3D _point ;
  private final Vector3D _vector ;


  public StraightLine(Vector3D point, Vector3D vector)
  {
     _point = new Vector3D(point);
     _vector = new Vector3D(vector.normalized());
  }

  public final StraightLine transformedBy(MutableMatrix44D m)
  {
    return new StraightLine(_point.transformedBy(m, 1), _vector.transformedBy(m, 0));
  }

  public final double squaredDistanceToPoint(Vector3D point)
  {
    // _vector is normalized
    // original formula: _point.sub(point).cross(_vector).length() / _vector.length()
    double ux = _point._x - point._x;
    double uy = _point._y - point._y;
    double uz = _point._z - point._z;
    double vx = uy * _vector._z - uz * _vector._y;
    double vy = uz * _vector._x - ux * _vector._z;
    double vz = ux * _vector._y - uy * _vector._x;
    return vx * vx + vy * vy + vz * vz;
  }

}