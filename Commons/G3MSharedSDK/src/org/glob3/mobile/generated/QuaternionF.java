package org.glob3.mobile.generated;
//
//  QuaternionF.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/27/17.
//
//

//
//  QuaternionF.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/27/17.
//
//


public class QuaternionF
{
  public final float _x;
  public final float _y;
  public final float _z;
  public final float _w;

  public QuaternionF(float x, float y, float z, float w)
  {
     _x = 0.0f;
     _y = 0.0f;
     _z = 0.0f;
     _w = 0.0f;
  
  }

  public void dispose()
  {
  
  }

  public final float dot(QuaternionF that)
  {
    return (_x * that._x) + (_y * that._y) + (_z * that._z) + (_w * that._w);
  }

  public final QuaternionF multiplyBy(QuaternionF that)
  {
    final float w = (_w * that._w) - (_x * that._x) - (_y * that._y) - (_z * that._z);
    final float x = ((_w * that._x) + (_x * that._w) + (_y * that._z)) - (_z * that._y);
    final float y = ((_w * that._y) + (_y * that._w) + (_z * that._x)) - (_x * that._z);
    final float z = ((_w * that._z) + (_z * that._w) + (_x * that._y)) - (_y * that._x);
  
    return new QuaternionF(x, y, z, w);
  }

  public final QuaternionF slerp(QuaternionF that, float t)
  {
    float cosHalftheta = dot(that);
  
    float tempX;
    float tempY;
    float tempZ;
    float tempW;
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
  
    final float w = (float)((_w * ratioA) + (tempW * ratioB));
    final float x = (float)((_x * ratioA) + (tempX * ratioB));
    final float y = (float)((_y * ratioA) + (tempY * ratioB));
    final float z = (float)((_z * ratioA) + (tempZ * ratioB));
  
    return new QuaternionF(x, y, z, w);
  }

}
