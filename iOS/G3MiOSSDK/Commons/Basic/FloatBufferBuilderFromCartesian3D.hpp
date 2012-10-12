//
//  FloatBufferBuilderFromCartesian3D.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_FloatBufferBuilderFromCartesian3D_hpp
#define G3MiOSSDK_FloatBufferBuilderFromCartesian3D_hpp

#include "Vector3D.hpp"
#include "FloatBufferBuilder.hpp"

class CenterStrategy {
  static const int _noCenter = 0;
  static const int _firstVertex = 1;
  static const int _givenCenter = 2;
  
  CenterStrategy(){};
  
public:
  
  static int noCenter(){ return _noCenter;}
  static int firstVertex(){ return _firstVertex;}
  static int givenCenter(){ return _givenCenter;}
};

class FloatBufferBuilderFromCartesian3D: public FloatBufferBuilder {
private:
  
  const int _centerStrategy;
  float _cx;
  float _cy;
  float _cz;
  
  void setCenter(const Vector3D& center){
    _cx = (float)center._x;
    _cy = (float)center._y;
    _cz = (float)center._z;
  }
  
public:
  
  FloatBufferBuilderFromCartesian3D(int cs, const Vector3D& center):
  _centerStrategy(cs){
    setCenter(center);
  }
  
  void add(const Vector3D& vector) {
    add((float) vector._x,
        (float) vector._y,
        (float) vector._z);
  }
  
  void add(double x, double y, double z) {
    add((float) x, (float) y, (float) z);
  }
  
  void add(float x, float y, float z) {
    if (_centerStrategy == CenterStrategy::firstVertex() && _values.size() == 0){
      setCenter(Vector3D(x,y,z));
    }
    
    if (_centerStrategy != CenterStrategy::noCenter()){
      x -= _cx;
      y -= _cy;
      z -= _cz;
    }
    
    _values.push_back(x);
    _values.push_back(y);
    _values.push_back(z);
  }
  
  Vector3D getCenter(){
    return Vector3D((double)_cx,(double)_cy,(double)_cz);
  }
};

#endif
