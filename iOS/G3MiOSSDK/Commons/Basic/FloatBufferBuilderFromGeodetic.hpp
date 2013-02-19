//
//  FloatBufferBuilderFromGeodetic.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_FloatBufferBuilderFromGeodetic_hpp
#define G3MiOSSDK_FloatBufferBuilderFromGeodetic_hpp

#include "FloatBufferBuilder.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "Ellipsoid.hpp"

#include "Geodetic3D.hpp"
#include "Geodetic2D.hpp"
#include "Vector3D.hpp"

class FloatBufferBuilderFromGeodetic: public FloatBufferBuilder {
private:

  const int _centerStrategy;
  float _cx;
  float _cy;
  float _cz;

  void setCenter(const Vector3D& center) {
    _cx = (float) center._x;
    _cy = (float) center._y;
    _cz = (float) center._z;
  }

  const Ellipsoid * _ellipsoid;

public:

  FloatBufferBuilderFromGeodetic(int centerStrategy,
                                 const Ellipsoid* ellipsoid,
                                 const Vector3D& center):
  _ellipsoid(ellipsoid),
  _centerStrategy(centerStrategy)
  {
    setCenter( center );
  }

  FloatBufferBuilderFromGeodetic(int centerStrategy,
                                 const Ellipsoid* ellipsoid,
                                 const Geodetic2D& center):
  _ellipsoid(ellipsoid),
  _centerStrategy(centerStrategy)
  {
    setCenter( _ellipsoid->toCartesian(center) );
  }

  FloatBufferBuilderFromGeodetic(int centerStrategy,
                                 const Ellipsoid* ellipsoid,
                                 const Geodetic3D& center):
  _ellipsoid(ellipsoid),
  _centerStrategy(centerStrategy)
  {
    setCenter( _ellipsoid->toCartesian(center) );
  }

  void add(const Angle& latitude,
           const Angle& longitude,
           const double height);

  void add(const Geodetic3D& position) {
    add(position.latitude(), position.longitude(), position.height());
  }

  void add(const Geodetic2D& position) {
    add(position.latitude(), position.longitude(), 0.0);
  }

  void add(const Geodetic2D& position,
           const double height) {
    add(position.latitude(), position.longitude(), height);
  }

  Vector3D getCenter() {
    return Vector3D(_cx, _cy, _cz);
  }
};

#endif
