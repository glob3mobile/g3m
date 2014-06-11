//
//  FloatBufferBuilderFromGeodetic.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_FloatBufferBuilderFromGeodetic
#define G3MiOSSDK_FloatBufferBuilderFromGeodetic

#include "FloatBufferBuilder.hpp"

#include "EllipsoidalPlanet.hpp"
#include "Geodetic3D.hpp"
#include "Geodetic2D.hpp"
#include "Vector3D.hpp"

class FloatBufferBuilderFromGeodetic: public FloatBufferBuilder {
private:

  FloatBufferBuilderFromGeodetic(const FloatBufferBuilderFromGeodetic& that);

  const CenterStrategy _centerStrategy;
  float _cx;
  float _cy;
  float _cz;

  void setCenter(const Vector3D& center) {
    _cx = (float) center._x;
    _cy = (float) center._y;
    _cz = (float) center._z;
  }

  const Planet* _planet;

  FloatBufferBuilderFromGeodetic(CenterStrategy centerStrategy,
                                 const Planet* planet,
                                 const Vector3D& center):
  _planet(planet),
  _centerStrategy(centerStrategy)
  {
    setCenter( center );
  }

  FloatBufferBuilderFromGeodetic(CenterStrategy centerStrategy,
                                 const Planet* planet,
                                 const Geodetic2D& center):
  _planet(planet),
  _centerStrategy(centerStrategy)
  {
    setCenter( _planet->toCartesian(center) );
  }

  FloatBufferBuilderFromGeodetic(CenterStrategy centerStrategy,
                                 const Planet* planet,
                                 const Geodetic3D& center):
  _planet(planet),
  _centerStrategy(centerStrategy)
  {
    setCenter( _planet->toCartesian(center) );
  }

public:

  static FloatBufferBuilderFromGeodetic* builderWithoutCenter(const Planet* planet) {
    return new FloatBufferBuilderFromGeodetic(
#ifdef C_CODE
                                              NO_CENTER,
#else
                                              CenterStrategy.NO_CENTER,
#endif
                                              planet, Vector3D::zero);
  }

  static FloatBufferBuilderFromGeodetic* builderWithFirstVertexAsCenter(const Planet* planet) {
    return new FloatBufferBuilderFromGeodetic(
#ifdef C_CODE
                                              FIRST_VERTEX,
#else
                                              CenterStrategy.FIRST_VERTEX,
#endif
                                              planet, Vector3D::zero);
  }

  static FloatBufferBuilderFromGeodetic* builderWithGivenCenter(const Planet* planet,
                                                                const Vector3D& center) {
    return new FloatBufferBuilderFromGeodetic(
#ifdef C_CODE
                                              GIVEN_CENTER,
#else
                                              CenterStrategy.GIVEN_CENTER,
#endif
                                              planet, center);
  }

  static FloatBufferBuilderFromGeodetic* builderWithGivenCenter(const Planet* planet,
                                                                const Geodetic2D& center) {
    return new FloatBufferBuilderFromGeodetic(
#ifdef C_CODE
                                              GIVEN_CENTER,
#else
                                              CenterStrategy.GIVEN_CENTER,
#endif
                                              planet, center);
  }

  static FloatBufferBuilderFromGeodetic* builderWithGivenCenter(const Planet* planet,
                                                                const Geodetic3D& center) {
    return new FloatBufferBuilderFromGeodetic(
#ifdef C_CODE
                                              GIVEN_CENTER,
#else
                                              CenterStrategy.GIVEN_CENTER,
#endif
                                              planet, center);
  }

  void add(const Angle& latitude,
           const Angle& longitude,
           const double height);

  void add(const Geodetic3D& position) {
    add(position._latitude,
        position._longitude,
        position._height);
  }

  void add(const Geodetic2D& position) {
    add(position._latitude,
        position._longitude,
        0.0);
  }

  void add(const Geodetic2D& position,
           const double height) {
    add(position._latitude,
        position._longitude,
        height);
  }
  
  Vector3D getCenter() {
    return Vector3D(_cx, _cy, _cz);
  }
};

#endif
