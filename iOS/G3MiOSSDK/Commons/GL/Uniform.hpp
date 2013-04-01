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

template <class T> class IUniformType{
public:
  virtual ~IUniformType() = 0;
  virtual bool isEqualsTo(T x) const = 0;
  virtual void set(GL* gl, IGLUniformID* id) const = 0;
};

class UniformTypeBool: public IUniformType<bool>{
  
public:
  bool _b;
  ~UniformTypeBool(){}
  bool isEqualsTo(UniformTypeBool* u) const{ return _b != u->_b;}
  void set(GL* gl, IGLUniformID* id) const{
    if (_b) gl->uniform1i(id, 1);
    else gl->uniform1i(id, 0);
  }
};

#ifdef C_CODE
class G3MError{};
#endif
#ifdef JAVA_CODE
public static class G3MError extends java.lang.RuntimeException{}
#endif

#ifdef C_CODE
template<class T>
#else
template<T extends IUniformType<T> >
#endif
class IUniform{
protected:
  std::string _name;
  IGLUniformID* _id;
  
  T* _value;
  
public:
  ~IUniform(){ delete _id; delete _value;}
  IUniform(const std::string& name, IGLUniformID* id):_name(name), _id(id){}
  
  void set(GL* gl, T x) {
    if (_value->isEqualsTo(&x)){
      x->set(gl, _id);
      _value = x;
    }
  }
  
  void launchException() throw(G3MError){
    throw new G3MError();
  }
};


class Uniform{
protected:
  std::string _name;
  IGLUniformID* _id;
public:
  ~Uniform(){ delete _id;}
  Uniform(const std::string&name, IGLUniformID* id):_name(name), _id(id){}
  
  const std::string getName() const{ return _name;}
  IGLUniformID* getID() const{ return _id;}
};

class UniformVec2Float: public Uniform{
  double _x, _y;
public:
  UniformVec2Float(const std::string&name, IGLUniformID* id):Uniform(name,id){}
  void set(GL* gl, const Vector2D& v) {
    double x = v.x();
    double y = v.y();
    if (x != _x || y != _y){
      gl->uniform2f(_id, (float)x, (float)y);
      _x = x;
      _y = y;
      
      
      
      
      IUniform<UniformTypeBool > boolUniform("",0);
      boolUniform.launchException();
    }
  }
};

class UniformVec4Float: public Uniform{
  double _x, _y, _z, _w;
public:
  UniformVec4Float(const std::string&name, IGLUniformID* id):Uniform(name,id){}
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
  UniformBool(const std::string&name, IGLUniformID* id):Uniform(name,id){}
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
  UniformMatrix4Float(const std::string&name, IGLUniformID* id):Uniform(name,id){}
  
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
  UniformFloat(const std::string&name, IGLUniformID* id):Uniform(name,id){}
  
  void set(GL* gl, double d) {
    if (_d != d){
      gl->uniform1f(_id, (float)d);
      _d = d;
    }
  }
};

#endif
