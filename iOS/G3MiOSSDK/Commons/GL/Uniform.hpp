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
#include "MutableMatrix44D.hpp"
#include "GL.hpp"

class Uniform{
protected:
  std::string _name;
  IGLUniformID* _id;
  
  const int _type;
public:
  ~Uniform(){ delete _id;}
  Uniform(const std::string&name, IGLUniformID* id, int type):_name(name), _id(id), _type(type){}
  
  const std::string getName() const{ return _name;}
  IGLUniformID* getID() const{ return _id;}
  int getType() const{ return _type;}
};

class UniformVec2Float: public Uniform{
  double _x, _y;
public:
  UniformVec2Float(const std::string&name, IGLUniformID* id):Uniform(name,id,GLType::glVec2Float()){}
  void set(GL* gl, const Vector2D& v) {
    double x = v.x();
    double y = v.y();
    if (x != _x || y != _y){
      gl->uniform2f(_id, (float)x, (float)y);
      _x = x;
      _y = y;
    }
  }
};

class UniformVec4Float: public Uniform{
  double _x, _y, _z, _w;
public:
  UniformVec4Float(const std::string&name, IGLUniformID* id):Uniform(name,id,GLType::glVec4Float()){}
  void set(GL* gl, double x, double y, double z, double w) {
    if (x != _x || y != _y || z != _z || w != _w){
      gl->uniform4f(_id, (float)x, (float)y, (float)z, (float) w);
      _x = x;
      _y = y;
      _z = z;
      _w = w;
    }
  }
};

class UniformBool: public Uniform{
  bool _b;
public:
  UniformBool(const std::string&name, IGLUniformID* id):Uniform(name,id,GLType::glBool()){}
  void set(GL* gl, bool b) {
    if (_b != b){
      if (b) gl->uniform1i(_id, 1);
      else gl->uniform1i(_id, 0);
      _b = b;
    }
  }
};

class UniformMatrix4Float: public Uniform{
  MutableMatrix44D _m;
public:
  UniformMatrix4Float(const std::string&name, IGLUniformID* id):Uniform(name,id,GLType::glMatrix4Float()){}
  
  void set(GL* gl, MutableMatrix44D m) {
    if (!_m.isEqualsTo(m)){
      gl->uniformMatrix4fv(_id, false, &m);
      _m = m;
    }
  }
};

class UniformFloat: public Uniform{
  double _d;
public:
  UniformFloat(const std::string&name, IGLUniformID* id):Uniform(name,id, GLType::glFloat()){}
  
  void set(GL* gl, double d) {
    if (_d != d){
      gl->uniform1f(_id, (float)d);
      _d = d;
    }
  }
};

#endif
