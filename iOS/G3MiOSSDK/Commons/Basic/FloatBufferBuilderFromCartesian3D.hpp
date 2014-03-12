//
//  FloatBufferBuilderFromCartesian3D.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_FloatBufferBuilderFromCartesian3D
#define G3MiOSSDK_FloatBufferBuilderFromCartesian3D

#include "Vector3D.hpp"
#include "FloatBufferBuilder.hpp"


class FloatBufferBuilderFromCartesian3D: public FloatBufferBuilder {
private:
  const CenterStrategy _centerStrategy;
  float _cx;
  float _cy;
  float _cz;

  void setCenter(double x, double y, double z) {
    _cx = (float) x;
    _cy = (float) y;
    _cz = (float) z;
  }

  FloatBufferBuilderFromCartesian3D(CenterStrategy centerStrategy,
                                    const Vector3D& center):
  _centerStrategy(centerStrategy)
  {
    setCenter(center._x, center._y, center._z);
  }

public:


  static FloatBufferBuilderFromCartesian3D* builderWithoutCenter() {
#ifdef C_CODE
    return new FloatBufferBuilderFromCartesian3D(NO_CENTER,Vector3D::zero);
#endif
#ifdef JAVA_CODE
    return new FloatBufferBuilderFromCartesian3D(CenterStrategy.NO_CENTER, Vector3D.zero);
#endif
  }

  static FloatBufferBuilderFromCartesian3D* builderWithFirstVertexAsCenter() {
#ifdef C_CODE
    return new FloatBufferBuilderFromCartesian3D(FIRST_VERTEX,Vector3D::zero);
#endif
#ifdef JAVA_CODE
    return new FloatBufferBuilderFromCartesian3D(CenterStrategy.FIRST_VERTEX, Vector3D.zero);
#endif
  }

  static FloatBufferBuilderFromCartesian3D* builderWithGivenCenter(const Vector3D& center) {
#ifdef C_CODE
    return new FloatBufferBuilderFromCartesian3D(GIVEN_CENTER, center);
#endif
#ifdef JAVA_CODE
    return new FloatBufferBuilderFromCartesian3D(CenterStrategy.GIVEN_CENTER, center);
#endif
  }

  void add(const Vector3D& vector) {
    add(vector._x,
        vector._y,
        vector._z);
  }

  void add(double x, double y, double z) {
    if (_centerStrategy ==
#ifdef C_CODE
        FIRST_VERTEX
#else
        CenterStrategy.FIRST_VERTEX
#endif
        ) {
      if (_values.size() == 0) {
        setCenter(x, y, z);
      }
    }

    if (_centerStrategy ==
#ifdef C_CODE
        NO_CENTER
#else
        CenterStrategy.NO_CENTER
#endif
        ) {
      _values.push_back( (float) x );
      _values.push_back( (float) y );
      _values.push_back( (float) z );
    }
    else {
      _values.push_back( (float) (x - _cx) );
      _values.push_back( (float) (y - _cy) );
      _values.push_back( (float) (z - _cz) );
    }
  }

  void add(float x, float y, float z) {
    if (_centerStrategy ==
#ifdef C_CODE
        FIRST_VERTEX
#else
        CenterStrategy.FIRST_VERTEX
#endif
        ) {
      if (_values.size() == 0) {
        setCenter(x, y, z);
      }
    }

    if (_centerStrategy ==
#ifdef C_CODE
        NO_CENTER
#else
        CenterStrategy.NO_CENTER
#endif
        ) {
      _values.push_back( x );
      _values.push_back( y );
      _values.push_back( z );
    }
    else {
      _values.push_back( x - _cx );
      _values.push_back( y - _cy );
      _values.push_back( z - _cz );
    }
  }

  Vector3D getCenter() {
    return Vector3D(_cx, _cy, _cz);
  }
  
};

#endif
