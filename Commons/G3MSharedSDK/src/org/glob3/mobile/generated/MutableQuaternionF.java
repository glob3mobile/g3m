package org.glob3.mobile.generated;
//
//  MutableQuaternionF.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/19/17.
//
//

//
//  MutableQuaternionF.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/19/17.
//
//


public class MutableQuaternionF
{
  private float _x;
  private float _y;
  private float _z;
  private float _w;

  private MutableQuaternionF _temp;

  public MutableQuaternionF()
  {
     _x = 0.0f;
     _y = 0.0f;
     _z = 0.0f;
     _w = 0.0f;
     _temp = null;
  }

  public MutableQuaternionF(float x, float y, float z, float w)
  {
     _x = x;
     _y = y;
     _z = z;
     _w = w;
     _temp = null;
  }

  public void dispose()
  {
    if (_temp != null)
       _temp.dispose();
  }

  public final void setXYZW(float x, float y, float z, float w)
  {
    _x = x;
    _y = y;
    _z = z;
    _w = w;
  }
  public final void setX(float x)
  {
    _x = x;
  }
  public final void setY(float y)
  {
    _y = y;
  }
  public final void setZ(float z)
  {
    _z = z;
  }
  public final void setW(float w)
  {
    _w = w;
  }

  public final float getX()
  {
    return _x;
  }
  public final float getY()
  {
    return _y;
  }
  public final float getZ()
  {
    return _z;
  }
  public final float getW()
  {
    return _w;
  }

  public final void copyFrom(MutableQuaternionF that)
  {
    _x = that._x;
    _y = that._y;
    _z = that._z;
    _w = that._w;
  }

  public final float dot(MutableQuaternionF that)
  {
    return (_x * that._x) + (_y * that._y) + (_z * that._z) + (_w * that._w);
  }

  public final void multiplyBy(MutableQuaternionF that, MutableQuaternionF output)
  {
    if (that == output)
    {
      if (_temp == null)
      {
        _temp = new MutableQuaternionF();
      }
      _temp.copyFrom(that);
  
      output._w = (_w * _temp._w) - (_x * _temp._x) - (_y * _temp._y) - (_z * _temp._z);
      output._x = ((_w * _temp._x) + (_x * _temp._w) + (_y * _temp._z)) - (_z * _temp._y);
      output._y = ((_w * _temp._y) + (_y * _temp._w) + (_z * _temp._x)) - (_x * _temp._z);
      output._z = ((_w * _temp._z) + (_z * _temp._w) + (_x * _temp._y)) - (_y * _temp._x);
    }
    else
    {
      output._w = (_w * that._w) - (_x * that._x) - (_y * that._y) - (_z * that._z);
      output._x = ((_w * that._x) + (_x * that._w) + (_y * that._z)) - (_z * that._y);
      output._y = ((_w * that._y) + (_y * that._w) + (_z * that._x)) - (_x * that._z);
      output._z = ((_w * that._z) + (_z * that._w) + (_x * that._y)) - (_y * that._x);
    }
  }

  public final void slerp(MutableQuaternionF that, MutableQuaternionF output, float t)
  {
    if (_temp == null)
    {
      _temp = new MutableQuaternionF();
    }
  
    float cosHalftheta = dot(that);
  
    if (cosHalftheta < 0)
    {
      cosHalftheta = -cosHalftheta;
      _temp._x = -that._x;
      _temp._y = -that._y;
      _temp._z = -that._z;
      _temp._w = -that._w;
    }
    else
    {
      _temp.copyFrom(that);
    }
  
    final IMathUtils mu = IMathUtils.instance();
  
    if (mu.abs(cosHalftheta) >= 1.0)
    {
      output._x = _x;
      output._y = _y;
      output._z = _z;
      output._w = _w;
    }
    else
    {
      final double sinHalfTheta = mu.sqrt(1.0 - (cosHalftheta * cosHalftheta));
      final double halfTheta = mu.acos(cosHalftheta);
  
      final double ratioA = mu.sin((1 - t) * halfTheta) / sinHalfTheta;
      final double ratioB = mu.sin(t * halfTheta) / sinHalfTheta;
  
      output._w = (float)((_w * ratioA) + (_temp._w * ratioB));
      output._x = (float)((_x * ratioA) + (_temp._x * ratioB));
      output._y = (float)((_y * ratioA) + (_temp._y * ratioB));
      output._z = (float)((_z * ratioA) + (_temp._z * ratioB));
    }
  
  }

}
