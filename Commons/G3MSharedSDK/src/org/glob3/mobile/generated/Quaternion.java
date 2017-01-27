package org.glob3.mobile.generated;
//
//  Quaternion.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/27/17.
//
//

//
//  Quaternion.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/27/17.
//
//


//class MutableMatrix44D;


public class Quaternion
{
  public static final Quaternion NANQD = new Quaternion(Float.NaN, Float.NaN, Float.NaN, Float.NaN);

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

  public void dispose()
  {
  
  }

  public final double dot(Quaternion that)
  {
    return (_x * that._x) + (_y * that._y) + (_z * that._z) + (_w * that._w);
  }

  public final Quaternion times(Quaternion that)
  {
    final double w = (_w * that._w) - (_x * that._x) - (_y * that._y) - (_z * that._z);
    final double x = ((_w * that._x) + (_x * that._w) + (_y * that._z)) - (_z * that._y);
    final double y = ((_w * that._y) + (_y * that._w) + (_z * that._x)) - (_x * that._z);
    final double z = ((_w * that._z) + (_z * that._w) + (_x * that._y)) - (_y * that._x);
  
    return new Quaternion(x, y, z, w);
  }

  public final Quaternion slerp(Quaternion that, double t)
  {
    double cosHalftheta = dot(that);
  
    double tempX;
    double tempY;
    double tempZ;
    double tempW;
    if (cosHalftheta < 0)
    {
      cosHalftheta = -cosHalftheta;
      tempX = -that._x;
      tempY = -that._y;
      tempZ = -that._z;
      tempW = -that._w;
    }
    else
    {
      tempX = that._x;
      tempY = that._y;
      tempZ = that._z;
      tempW = that._w;
    }
  
    final IMathUtils mu = IMathUtils.instance();
  
    if (mu.abs(cosHalftheta) >= 1.0)
    {
      return this;
    }
  
    final double sinHalfTheta = mu.sqrt(1.0 - (cosHalftheta * cosHalftheta));
    final double halfTheta = mu.acos(cosHalftheta);
  
    final double ratioA = mu.sin((1 - t) * halfTheta) / sinHalfTheta;
    final double ratioB = mu.sin(t * halfTheta) / sinHalfTheta;
  
    final double w = (_w * ratioA) + (tempW * ratioB);
    final double x = (_x * ratioA) + (tempX * ratioB);
    final double y = (_y * ratioA) + (tempY * ratioB);
    final double z = (_z * ratioA) + (tempZ * ratioB);
    return new Quaternion(x, y, z, w);
  }

  public final double norm()
  {
    return (_w * _w) + (_x * _x) + (_y * _y) + (_z * _z);
  }

  public final Quaternion inversed()
  {
    final double n = norm();
    if (n > 0.0)
    {
      final double invNorm = 1.0 / n;
      return new Quaternion(-_x * invNorm, -_y * invNorm, -_z * invNorm, +_w * invNorm);
    }
    return Quaternion.NANQD;
  }

  public final void toRotationMatrix(MutableMatrix44D result)
  {
    final double m00 = 1.0 - (2.0 * _y * _y) - (2.0 * _z * _z);
    final double m01 = (2.0 * _x * _y) + (2.0 * _z * _w);
    final double m02 = (2.0 * _x * _z) - (2.0 * _y * _w);
    final double m03 = 0.0;
  
    final double m10 = (2.0 * _x * _y) - (2.0 * _z * _w);
    final double m11 = 1.0 - (2.0 * _x * _x) - (2.0 * _z * _z);
    final double m12 = (2.0 * _y * _z) + (2.0 * _x * _w);
    final double m13 = 0.0;
  
    final double m20 = (2.0 * _x * _z) + (2.0 * _y * _w);
    final double m21 = (2.0 * _y * _z) - (2.0 * _x * _w);
    final double m22 = 1.0 - (2.0 * _x * _x) - (2.0 * _y * _y);
    final double m23 = 0.0;
  
    final double m30 = 0.0;
    final double m31 = 0.0;
    final double m32 = 0.0;
    final double m33 = 1.0;
  
    result.setValue(m00, m10, m20, m30, m01, m11, m21, m31, m02, m12, m22, m32, m03, m13, m23, m33);
    //  result.setValue(m00, m01, m02, m03,
    //                  m10, m11, m12, m13,
    //                  m20, m21, m22, m23,
    //                  m30, m31, m32, m33);
  }

}
