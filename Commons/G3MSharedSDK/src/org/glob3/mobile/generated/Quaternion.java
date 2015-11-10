package org.glob3.mobile.generated; 
//
//  Quaternion.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/3/15.
//
//

//
//  Quaternion.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/3/15.
//
//




public class Quaternion
{
  public final double _x;
  public final double _y;
  public final double _z;
  public final double _w;

  public Quaternion(double x, double y, double z, double w)
  {
     _x = x;
     _y = y;
     _z = z;
     _w = w;
  
  }

  public final Vector3D getRotationAxis()
  {
  
    final double w2 = _w * _w;
  
    double x = _x / Math.sqrt(1-w2);
    double y = _y / Math.sqrt(1-w2);
    double z = _z / Math.sqrt(1-w2);
  
    return new Vector3D(x, y, z);
  }
  public final Angle getRotationAngle()
  {
    return Angle.fromRadians(2 * Math.acos(_w));
  }
  public final MutableMatrix44D getRotationMatrix()
  {
    return MutableMatrix44D.createRotationMatrix(getRotationAngle(), getRotationAxis());
  }
}