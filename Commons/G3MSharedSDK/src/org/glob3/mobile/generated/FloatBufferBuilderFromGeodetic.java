package org.glob3.mobile.generated; 
//
//  FloatBufferBuilderFromGeodetic.cpp
//  G3MiOSSDK


//
//  FloatBufferBuilderFromGeodetic.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public class FloatBufferBuilderFromGeodetic extends FloatBufferBuilder
{

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  FloatBufferBuilderFromGeodetic(FloatBufferBuilderFromGeodetic that);

  private final CenterStrategy _centerStrategy;
  private float _cx;
  private float _cy;
  private float _cz;

  private void setCenter(Vector3D center)
  {
    _cx = (float) center._x;
    _cy = (float) center._y;
    _cz = (float) center._z;
  }

  private final Planet _ellipsoid;

  private FloatBufferBuilderFromGeodetic(CenterStrategy centerStrategy, Planet ellipsoid, Vector3D center)
  {
     _ellipsoid = ellipsoid;
     _centerStrategy = centerStrategy;
    setCenter(center);
  }

  private FloatBufferBuilderFromGeodetic(CenterStrategy centerStrategy, Planet ellipsoid, Geodetic2D center)
  {
     _ellipsoid = ellipsoid;
     _centerStrategy = centerStrategy;
    setCenter(_ellipsoid.toCartesian(center));
  }

  private FloatBufferBuilderFromGeodetic(CenterStrategy centerStrategy, Planet ellipsoid, Geodetic3D center)
  {
     _ellipsoid = ellipsoid;
     _centerStrategy = centerStrategy;
    setCenter(_ellipsoid.toCartesian(center));
  }


  public static FloatBufferBuilderFromGeodetic builderWithoutCenter(Planet planet)
  {
    return new FloatBufferBuilderFromGeodetic(CenterStrategy.NO_CENTER, planet, Vector3D.zero);
  }

  public static FloatBufferBuilderFromGeodetic builderWithFirstVertexAsCenter(Planet planet)
  {
    return new FloatBufferBuilderFromGeodetic(CenterStrategy.FIRST_VERTEX, planet, Vector3D.zero);
  }

  public static FloatBufferBuilderFromGeodetic builderWithGivenCenter(Planet planet, Vector3D center)
  {
    return new FloatBufferBuilderFromGeodetic(CenterStrategy.GIVEN_CENTER, planet, center);
  }

  public static FloatBufferBuilderFromGeodetic builderWithGivenCenter(Planet planet, Geodetic2D center)
  {
    return new FloatBufferBuilderFromGeodetic(CenterStrategy.GIVEN_CENTER, planet, center);
  }

  public static FloatBufferBuilderFromGeodetic builderWithGivenCenter(Planet planet, Geodetic3D center)
  {
    return new FloatBufferBuilderFromGeodetic(CenterStrategy.GIVEN_CENTER, planet, center);
  }

  public final void add(Angle latitude, Angle longitude, double height)
  {
    final Vector3D vector = _ellipsoid.toCartesian(latitude, longitude, height);
  
    if (_centerStrategy == CenterStrategy.FIRST_VERTEX)
    {
      if (_values.size() == 0)
      {
        setCenter(vector);
      }
    }
  
    if (_centerStrategy == CenterStrategy.NO_CENTER)
    {
      _values.push_back((float) vector._x);
      _values.push_back((float) vector._y);
      _values.push_back((float) vector._z);
    }
    else
    {
      _values.push_back((float)(vector._x - _cx));
      _values.push_back((float)(vector._y - _cy));
      _values.push_back((float)(vector._z - _cz));
    }
  }

  public final void add(Geodetic3D position)
  {
    add(position._latitude, position._longitude, position._height);
  }

  public final void add(Geodetic2D position)
  {
    add(position._latitude, position._longitude, 0.0);
  }

  public final void add(Geodetic2D position, double height)
  {
    add(position._latitude, position._longitude, height);
  }

  public final Vector3D getCenter()
  {
    return new Vector3D(_cx, _cy, _cz);
  }
}