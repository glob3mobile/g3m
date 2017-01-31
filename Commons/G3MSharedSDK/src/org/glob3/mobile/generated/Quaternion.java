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
//class TaitBryanAngles;

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
    //  const double x = (_w * that._x) + (_x * that._w) + (_y * that._z) - (_z * that._y);
    //  const double y = (_w * that._y) + (_y * that._w) + (_z * that._x) - (_x * that._z);
    //  const double z = (_w * that._z) + (_z * that._w) + (_x * that._y) - (_y * that._x);
    //  const double w = (_w * that._w) - (_x * that._x) - (_y * that._y) - (_z * that._z);
  
    final double x = (that._w * _x) + (that._x * _w) + (that._z * _y) - (that._y * _z);
    final double y = (that._w * _y) + (that._y * _w) + (that._x * _z) - (that._z * _x);
    final double z = (that._w * _z) + (that._z * _w) + (that._y * _x) - (that._x * _y);
    final double w = (that._w * _w) - (that._x * _x) - (that._y * _y) - (that._z * _z);
  
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

  //  TaitBryanAngles toTaitBryanAngles() const;

}
//TaitBryanAngles Quaternion::toTaitBryanAngles() const {
//  const IMathUtils* mu = IMathUtils::instance();
//
//  //  const double ysqr = _y * _y;
//  //
//  //  const double t0 = 2.0 * ((_w * _x) + (_y * _z));
//  //  const double t1 = 1.0 - 2.0 * ((_x * _x) + ysqr);
//  //  const double roll = mu->atan2(t0, t1);
//  //
//  //  double t2 = 2.0 * ((_w * _y) - (_z * _x));
//  //  t2 = t2 > 1.0 ? 1.0 : t2;
//  //  t2 = t2 < -1.0 ? -1.0 : t2;
//  //  const double pitch = mu->asin(t2);
//  //
//  //  const double t3 = 2.0 * ((_w * _z) + (_x *_y));
//  //  const double t4 = 1.0 - 2.0 * (ysqr + (_z * _z));
//  //  const double yaw = mu->atan2(t3, t4);
//  //
//  //  return TaitBryanAngles::fromRadians(yaw, pitch, roll);
//
//  double heading;
//  double pitch;
//  double roll;
//
//  const double test = _z * _y + _x * _w;
//  if (mu->abs(test) < 0.4999) {
//    heading = mu->asin(2.0 * test);
//    pitch   = mu->atan2(2.0 * _y * _w - 2.0 * _z * _x,
//                        1.0 - 2.0 * _y * _y - 2.0 * _x * _x);
//    roll    = mu->atan2(2.0 * _z * _w - 2.0 * _y * _x,
//                        1.0 - 2.0 * _z * _z - 2.0 * _x * _x);
//  }
//  else {
//    heading = mu->copySign(1.5707963267948966, test);
//    pitch   = mu->copySign(2.0, test) * mu->atan2(_z, _w);
//    roll    = 0.0;
//  }
//
//  return TaitBryanAngles::fromRadians(heading, pitch, roll);
//
//  //  double heading;
//  //  double pitch;
//  //  double roll;
//  //
//  //  const double sqw = _w * _w;
//  //  const double sqx = _x * _x;
//  //  const double sqy = _y * _y;
//  //  const double sqz = _z * _z;
//  //  const double unit = sqx + sqy + sqz + sqw; // if normalised is one, otherwise is correction factor
//  //  const double test = (_x * _y) + (_z * _w);
//  //  if (test > 0.499*unit) { // singularity at north pole
//  //    heading = 2 * mu->atan2(_x,_w);
//  //    pitch   = HALF_PI;
//  //    roll    = 0;
//  //  }
//  //  else if (test < -0.499*unit) { // singularity at south pole
//  //    heading = -2 * mu->atan2(_x,_w);
//  //    pitch   = -HALF_PI;
//  //    roll    = 0;
//  //  }
//  //  else {
//  //    heading = mu->atan2(2*_y*_w-2*_x*_z,
//  //                        sqx - sqy - sqz + sqw);
//  //    pitch   = mu->asin(2*test/unit);
//  //    roll    = mu->atan2(2*_x*_w-2*_y*_z,
//  //                        -sqx + sqy - sqz + sqw);
//  //  }
//  //
//  //  return TaitBryanAngles::fromRadians(heading, pitch, roll);
//
//}
