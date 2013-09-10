package org.glob3.mobile.generated; 
//
//  FloatBufferBuilderFromCartesian3D.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public class FloatBufferBuilderFromCartesian3D extends FloatBufferBuilder
{
  private final CenterStrategy _centerStrategy;
  private float _cx;
  private float _cy;
  private float _cz;

  private void setCenter(double x, double y, double z)
  {
    _cx = (float) x;
    _cy = (float) y;
    _cz = (float) z;
  }

  private FloatBufferBuilderFromCartesian3D(CenterStrategy centerStrategy, Vector3D center)
  {
     _centerStrategy = centerStrategy;
    setCenter(center._x, center._y, center._z);
  }



  public static FloatBufferBuilderFromCartesian3D builderWithoutCenter()
  {
    return new FloatBufferBuilderFromCartesian3D(CenterStrategy.NO_CENTER, Vector3D.zero);
  }

  public static FloatBufferBuilderFromCartesian3D builderWithFirstVertexAsCenter()
  {
    return new FloatBufferBuilderFromCartesian3D(CenterStrategy.FIRST_VERTEX, Vector3D.zero);
  }

  public static FloatBufferBuilderFromCartesian3D builderWithGivenCenter(Vector3D center)
  {
    return new FloatBufferBuilderFromCartesian3D(CenterStrategy.GIVEN_CENTER, center);
  }

  public final void add(Vector3D vector)
  {
    add(vector._x, vector._y, vector._z);
  }

  public final void add(double x, double y, double z)
  {
    if (_centerStrategy == CenterStrategy.FIRST_VERTEX)
    {
      if (_values.size() == 0)
      {
        setCenter(x, y, z);
      }
    }

    if (_centerStrategy == CenterStrategy.NO_CENTER)
    {
      _values.push_back((float) x);
      _values.push_back((float) y);
      _values.push_back((float) z);
    }
    else
    {
      _values.push_back((float)(x - _cx));
      _values.push_back((float)(y - _cy));
      _values.push_back((float)(z - _cz));
    }
  }

  public final void add(float x, float y, float z)
  {
    if (_centerStrategy == CenterStrategy.FIRST_VERTEX)
    {
      if (_values.size() == 0)
      {
        setCenter(x, y, z);
      }
    }

    if (_centerStrategy == CenterStrategy.NO_CENTER)
    {
      _values.push_back(x);
      _values.push_back(y);
      _values.push_back(z);
    }
    else
    {
      _values.push_back(x - _cx);
      _values.push_back(y - _cy);
      _values.push_back(z - _cz);
    }
  }

  public final Vector3D getCenter()
  {
    return new Vector3D(_cx, _cy, _cz);
  }

}