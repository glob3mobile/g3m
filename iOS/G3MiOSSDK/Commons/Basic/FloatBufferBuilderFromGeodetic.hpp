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
#include "Planet.hpp"

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
    _cx = (float)center._x;
    _cy = (float)center._y;
    _cz = (float)center._z;
  }
  
  const Planet * _planet;
  
public:
  
  FloatBufferBuilderFromGeodetic(int cs, const Planet* planet, const Vector3D& center):
  _planet(planet),
  _centerStrategy(cs)
  {
    setCenter(center);
  }
  
  FloatBufferBuilderFromGeodetic(int cs, const Planet* planet, const Geodetic2D& center):
  _planet(planet),
  _centerStrategy(cs)
  {
    setCenter( _planet->toCartesian(center) );
  }
  
  FloatBufferBuilderFromGeodetic(int cs, const Planet* planet, const Geodetic3D& center):
  _planet(planet),
  _centerStrategy(cs)
  {
    setCenter( _planet->toCartesian(center) );
  }
  
  void add(const Geodetic3D& g) {
    const Vector3D vector = _planet->toCartesian(g);
    
    float x = (float) vector._x;
    float y = (float) vector._y;
    float z = (float) vector._z;
    
    if (_centerStrategy == CenterStrategy::firstVertex() && _values.size() == 0) {
      setCenter(vector);
    }
    
    if (_centerStrategy != CenterStrategy::noCenter()) {
      x -= _cx;
      y -= _cy;
      z -= _cz;
    }
    
    _values.push_back(x);
    _values.push_back(y);
    _values.push_back(z);
  }
  
  void add(const Geodetic2D& g) {
    const Vector3D vector = _planet->toCartesian(g);
    
    float x = (float) vector._x;
    float y = (float) vector._y;
    float z = (float) vector._z;
    
    if (_centerStrategy == CenterStrategy::firstVertex() && _values.size() == 0) {
      setCenter(vector);
    }
    
    if (_centerStrategy != CenterStrategy::noCenter()) {
      x -= _cx;
      y -= _cy;
      z -= _cz;
    }
    
    _values.push_back(x);
    _values.push_back(y);
    _values.push_back(z);
  }
  
  Vector3D getCenter() {
    return Vector3D((double)_cx,(double)_cy,(double)_cz);
  }
};

#endif
