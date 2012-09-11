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
  
  const CenterStrategy _centerStrategy;
  float _cx;
  float _cy;
  float _cz;
  
  void setCenter(const Vector3D& center) {
    _cx = (float)center.x();
    _cy = (float)center.y();
    _cz = (float)center.z();
  }
  
  const Planet * _planet;
  
public:
  
  FloatBufferBuilderFromGeodetic(CenterStrategy cs, const Planet* planet, const Vector3D& center):
  _planet(planet),
  _centerStrategy(cs)
  {
    setCenter(center);
  }
  
  FloatBufferBuilderFromGeodetic(CenterStrategy cs, const Planet* planet, const Geodetic2D& center):
  _planet(planet),
  _centerStrategy(cs)
  {
    setCenter( _planet->toCartesian(center) );
  }
  
  FloatBufferBuilderFromGeodetic(CenterStrategy cs, const Planet* planet, const Geodetic3D& center):
  _planet(planet),
  _centerStrategy(cs)
  {
    setCenter( _planet->toCartesian(center) );
  }
  
  void add(const Geodetic3D& g) {
    const Vector3D vector = _planet->toCartesian(g);
    
    float x = (float) vector.x();
    float y = (float) vector.y();
    float z = (float) vector.z();
    
    if (_centerStrategy == FirstVertex && _values.size() == 0) {
      setCenter(vector);
    }
    
    if (_centerStrategy != NoCenter) {
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
    
    float x = (float) vector.x();
    float y = (float) vector.y();
    float z = (float) vector.z();
    
    if (_centerStrategy == FirstVertex && _values.size() == 0) {
      setCenter(vector);
    }
    
    if (_centerStrategy != NoCenter) {
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
