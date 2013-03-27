//
//  Uniforms.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//

#ifndef G3MiOSSDK_Uniform_hpp
#define G3MiOSSDK_Uniform_hpp

#include "Vector2D.hpp"
#include "IGLUniformID.hpp"

class Uniform{
protected:
  std::string _name;
  IGLUniformID* _id;
public:
  ~Uniform(){ delete _id;}
  
  const std::string getName() const{ return _name;}
  IGLUniformID* getID() const{ return _id;}
};

class UniformVec2Float: public Uniform{
  double _x, _y;
public:
  void set(INativeGL* nativeGL, const Vector2D& v) {
    double x = v.x();
    double y = v.y();
    if (x != _x || y != _y){
      nativeGL->uniform2f(_id, (float)x, (float)y);
      _x = x;
      _y = y;
    }
  }
};

class UniformBool: public Uniform{
  bool _b;
public:
  void set(INativeGL* nativeGL, bool b) {
    if (_b != b){
      if (b) nativeGL->uniform1i(_id, 1);
      else nativeGL->uniform1i(_id, 0);
      _b = b;
    }
  }
};

#endif
