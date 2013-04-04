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
  const std::string _name;
  const IGLUniformID* _id;
  
  const int _type;
  bool _wasSet;
  bool _dirty;
public:
  virtual ~Uniform(){ delete _id;}
  Uniform(const std::string&name, IGLUniformID* id, int type):
  _name(name),
  _id(id),
  _type(type),
  _wasSet(false),
  _dirty(false){}
  
  const std::string getName() const{ return _name;}
  const IGLUniformID* getID() const{ return _id;}
  int getType() const{ return _type;}
  
  virtual void applyChanges(GL* gl) = 0;
};
//////////////////////////////////////////////////////////////////
class UniformVec2Float: public Uniform{
  double _x, _y;
public:
  UniformVec2Float(const std::string&name, IGLUniformID* id):Uniform(name,id,GLType::glVec2Float()){}
  void set(const Vector2D& v) {
    double x = v.x();
    double y = v.y();
    if (!_wasSet || x != _x || y != _y){
      _x = x;
      _y = y;
      _wasSet = true;
      _dirty = true;
    }
  }
  void set(GL* gl, float x, float y) {
    if (!_wasSet || x != _x || y != _y){
      _dirty = true;
      _x = x;
      _y = y;
      _wasSet = true;
    }
  }
  
  void applyChanges(GL* gl){
    if (_dirty){
      gl->uniform2f(_id, (float)_x, (float)_y);
      _dirty = false;
    }
  }
};
//////////////////////////////////////////////////////////////////
class UniformVec4Float: public Uniform{
  double _x, _y, _z, _w;
public:
  UniformVec4Float(const std::string&name, IGLUniformID* id):Uniform(name,id,GLType::glVec4Float()){}
  void set(double x, double y, double z, double w) {
    if (!_wasSet || x != _x || y != _y || z != _z || w != _w){
      _dirty = true;
      _x = x;
      _y = y;
      _z = z;
      _w = w;
      _wasSet = true;
    }
  }
  
  void applyChanges(GL* gl){
    if (_dirty){
      gl->uniform4f(_id, (float)_x, (float)_y, (float)_z, (float) _w);
      _dirty = false;
    }
  }
};
//////////////////////////////////////////////////////////////////
class UniformBool: public Uniform{
  bool _b;
public:
  UniformBool(const std::string&name, IGLUniformID* id):Uniform(name,id,GLType::glBool()){}
  void set(bool b) {
    if (!_wasSet || _b != b){
      _dirty = true;
      _b = b;
      _wasSet = true;
    }
  }
  
  void applyChanges(GL* gl){
    if (_dirty){
      if (_b) gl->uniform1i(_id, 1);
      else gl->uniform1i(_id, 0);
      _dirty = false;
    }
  }
};
//////////////////////////////////////////////////////////////////
class UniformMatrix4Float: public Uniform{
  MutableMatrix44D _m;
public:
  UniformMatrix4Float(const std::string&name, IGLUniformID* id):Uniform(name,id,GLType::glMatrix4Float()){}
  
  void set(MutableMatrix44D m) {
    if (!_wasSet || !_m.isEqualsTo(m)){
      _dirty = true;
      _m = m;
      _wasSet = true;
    }
  }
  
  void applyChanges(GL* gl){
    if (_dirty){
      gl->uniformMatrix4fv(_id, false, &_m);
      _dirty = false;
    }
  }
};
//////////////////////////////////////////////////////////////////
class UniformFloat: public Uniform{
  double _d;
public:
  UniformFloat(const std::string&name, IGLUniformID* id):Uniform(name,id, GLType::glFloat()){}
  
  void set(double d) {
    if (!_wasSet || _d != d){
      _dirty = true;
      _d = d;
      _wasSet = true;
    }
  }
  
  void applyChanges(GL* gl){
    if (_dirty){
      gl->uniform1f(_id, (float)_d);
      _dirty = false;
    }
  }
};
//////////////////////////////////////////////////////////////////
#endif
