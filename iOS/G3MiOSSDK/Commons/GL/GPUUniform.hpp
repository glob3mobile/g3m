//
//  GPUUniform.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//

#ifndef G3MiOSSDK_GPUUniform_h
#define G3MiOSSDK_GPUUniform_h

#include "GL.hpp"
#include "GLConstants.hpp"
#include "IGLUniformID.hpp"
#include "IStringBuilder.hpp"

class GPUUniform;

class GPUUniformValue{
  const int _type;
  
  mutable GPUUniform* _uniform;
  
public:
  GPUUniformValue(int type):_type(type), _uniform(NULL){}
  int getType() const { return _type;}
  virtual ~GPUUniformValue(){}
  virtual void setUniform(GL* gl, const IGLUniformID* id) const = 0;
  virtual bool isEqualsTo(const GPUUniformValue* v) const = 0;
  virtual GPUUniformValue* deepCopy() const = 0;
  
  virtual std::string description() const = 0;

  void linkToGPUUniform(GPUUniform* u){
    _uniform = u;
  }
  
  void unLinkToGPUUniform(){
    _uniform = NULL;
  }
  
  void setValueToLinkedUniform(GL* gl) const;
};


class GPUUniform{
protected:
  const std::string _name;
  const IGLUniformID* _id;
  
  bool _dirty;
  GPUUniformValue* _value;
  const int _type;
public:
  
  virtual ~GPUUniform(){
    delete _id;
    delete _value;
  }
  
  GPUUniform(const std::string&name, IGLUniformID* id, int type):
  _name(name),
  _id(id),
  _dirty(false),
  _value(NULL),
  _type(type){}
  
  const std::string getName() const{ return _name;}
  const IGLUniformID* getID() const{ return _id;}
  int getType() const{ return _type;}
  bool wasSet() const { return _value != NULL;}
  
  void unset(){
    if (_value != NULL){
      delete _value;
      _value = NULL;
    }
    _dirty = false;
  }
  
  void set(GPUUniformValue* v){
    if (_type != v->getType()){ //type checking 
//      delete v;
      ILogger::instance()->logError("Attempting to set uniform " + _name + "with invalid value type.");
      return;
    }
    if (_value == NULL || !_value->isEqualsTo(v)){
      _dirty = true;
      if (_value != NULL){
        delete _value;
      }
      _value = v->deepCopy();
//      delete v;
    }
  }
  
  virtual void applyChanges(GL* gl){
    if (_dirty){
      _value->setUniform(gl, _id);
      _dirty = false;
    }
  }
};

////////////////////////////////////////////////////////////////////////
class GPUUniformValueBool:public GPUUniformValue{
public:
  const bool _value;
  
  GPUUniformValueBool(bool b):_value(b),GPUUniformValue(GLType::glBool()){} 
  
  void setUniform(GL* gl, const IGLUniformID* id) const{
    if (_value) gl->uniform1i(id, 1);
    else gl->uniform1i(id, 0);
  }
  bool isEqualsTo(const GPUUniformValue* v) const{
    return _value == ((GPUUniformValueBool*)v)->_value;
  }
  GPUUniformValue* deepCopy() const{
    return new GPUUniformValueBool(_value);
  }
  
  std::string description() const{
    IStringBuilder *isb = IStringBuilder::newStringBuilder();
    isb->addString("Uniform Value Boolean: ");
    isb->addBool(_value);
    std::string s = isb->getString();
    delete isb;
    return s;
  }
};
class GPUUniformBool: public GPUUniform{
public:
  GPUUniformBool(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glBool()){}
};
////////////////////////////////////////////////////////////////////////
class GPUUniformValueVec2Float:public GPUUniformValue{
public:
  const double _x, _y;
  
  GPUUniformValueVec2Float(double x, double y):_x(x),_y(y),GPUUniformValue(GLType::glVec2Float()){}
  
  void setUniform(GL* gl, const IGLUniformID* id) const{
    gl->uniform2f(id, (float)_x, (float)_y);
  }
  bool isEqualsTo(const GPUUniformValue* v) const{
    GPUUniformValueVec2Float *v2 = (GPUUniformValueVec2Float *)v;
    return (_x == v2->_x) && (_y == v2->_y);
  }
  GPUUniformValue* deepCopy() const{
    return new GPUUniformValueVec2Float(_x,_y);
  }
  
  std::string description() const{
    IStringBuilder *isb = IStringBuilder::newStringBuilder();
    isb->addString("Uniform Value Vec2Float: x:");
    isb->addDouble(_x);
    isb->addString("y:");
    isb->addDouble(_y);
    std::string s = isb->getString();
    delete isb;
    return s;
  }
};
class GPUUniformVec2Float: public GPUUniform{
public:
  GPUUniformVec2Float(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glVec2Float()){}
};
////////////////////////////////////////////////////////////////////////
class GPUUniformValueVec4Float:public GPUUniformValue{
public:
  const double _x, _y, _z, _w;
  
  GPUUniformValueVec4Float(double x, double y, double z, double w):
  _x(x),_y(y), _z(z), _w(w),GPUUniformValue(GLType::glVec4Float()){}
  
  void setUniform(GL* gl, const IGLUniformID* id) const{
    gl->uniform4f(id, (float)_x, (float)_y, (float)_z, (float)_w);
  }
  bool isEqualsTo(const GPUUniformValue* v) const{
    GPUUniformValueVec4Float *v2 = (GPUUniformValueVec4Float *)v;
    return (_x == v2->_x) && (_y == v2->_y) && (_z == v2->_z) && (_w == v2->_w);
  }
  GPUUniformValue* deepCopy() const{
    return new GPUUniformValueVec4Float(_x,_y,_z,_w);
  }
  
  std::string description() const{
    IStringBuilder *isb = IStringBuilder::newStringBuilder();
    isb->addString("Uniform Value Vec4Float: x:");
    isb->addDouble(_x);
    isb->addString("y:");
    isb->addDouble(_y);
    isb->addString("z:");
    isb->addDouble(_z);
    isb->addString("w:");
    isb->addDouble(_w);
    std::string s = isb->getString();
    delete isb;
    return s;
  }
};
class GPUUniformVec4Float: public GPUUniform{
public:
  GPUUniformVec4Float(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glVec4Float()){}
};
////////////////////////////////////////////////////////////////////////
class GPUUniformValueMatrix4FloatStack:public GPUUniformValue{
public:
  std::vector<const MutableMatrix44D*> _stack;
  
  GPUUniformValueMatrix4FloatStack(const MutableMatrix44D* m):GPUUniformValue(GLType::glMatrix4Float()){
    _stack.push_back(m);
  }
  
  void setUniform(GL* gl, const IGLUniformID* id) const{
    MutableMatrix44D m = MutableMatrix44D(*_stack[0]);
    const int size = _stack.size();
    for (int i = 1; i < size; i++) {
      m = m.multiply(*_stack[i]);
      //m = _stack[i]->multiply(m);
    }
    
    gl->uniformMatrix4fv(id, false, &m);
  }
  
  bool isEqualsTo(const GPUUniformValue* v) const{
    //TODO: FIX
    return false;
//    GPUUniformValueMatrix4FloatStack *v2 = (GPUUniformValueMatrix4FloatStack *)v;
//    if (_stack.size() != v2->_stack.size()){
//      return false;
//    }
//    
//    for (int i = _stack.size(); i > -1; i--) {
//      const MutableMatrix44D* m = v2->_stack[i];
//      const MutableMatrix44D* m2 = _stack[i];
//      if (!m->isEqualsTo(*m2)){
//        return false;
//      }
//    }
//    return true;
  }
  
  void multiplyMatrix(const MutableMatrix44D* m){
    _stack.push_back(m);
  }
  
  void loadMatrix(const MutableMatrix44D* m){
    _stack.clear();
    _stack.push_back(m);
  }
  
  GPUUniformValue* deepCopy() const{
    GPUUniformValueMatrix4FloatStack* v = new GPUUniformValueMatrix4FloatStack(_stack[0]);
    for (int i = 1; i < _stack.size(); i++) {
      v->multiplyMatrix(_stack[i]);
    }
    return v;
  }
  
  std::string description() const{
    IStringBuilder *isb = IStringBuilder::newStringBuilder();
    isb->addString("Uniform Value Matrix44D Stack.");
    std::string s = isb->getString();
    delete isb;
    return s;
  }
};
class GPUUniformValueMatrix4Float:public GPUUniformValue{
public:
  const MutableMatrix44D _m;
  
  GPUUniformValueMatrix4Float(const MutableMatrix44D m):_m(m),GPUUniformValue(GLType::glMatrix4Float()){}
  
  void setUniform(GL* gl, const IGLUniformID* id) const{
    gl->uniformMatrix4fv(id, false, &_m);
  }

  bool isEqualsTo(const GPUUniformValue* v) const{
    GPUUniformValueMatrix4Float *v2 = (GPUUniformValueMatrix4Float *)v;
    const MutableMatrix44D* m = &(v2->_m);
    return _m.isEqualsTo(*m);
  }
  GPUUniformValue* deepCopy() const{
    return new GPUUniformValueMatrix4Float(_m);
  }
  
  std::string description() const{
    IStringBuilder *isb = IStringBuilder::newStringBuilder();
    isb->addString("Uniform Value Matrix44D.");
    std::string s = isb->getString();
    delete isb;
    return s;
  }
  
  MutableMatrix44D getValue() const{ return _m;}
};
class GPUUniformMatrix4Float: public GPUUniform{
public:
  GPUUniformMatrix4Float(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glMatrix4Float()){}
};
////////////////////////////////////////////////////////////////////////
class GPUUniformValueFloat:public GPUUniformValue{
public:
  const double _value;
  
  GPUUniformValueFloat(double d):_value(d),GPUUniformValue(GLType::glFloat()){}
  
  void setUniform(GL* gl, const IGLUniformID* id) const{
    gl->uniform1f(id, (float)_value);
  }
  bool isEqualsTo(const GPUUniformValue* v) const{
    GPUUniformValueFloat *v2 = (GPUUniformValueFloat *)v;
    return _value != v2->_value;
  }
  GPUUniformValue* deepCopy() const{
    return new GPUUniformValueFloat(_value);
  }
  
  std::string description() const{
    IStringBuilder *isb = IStringBuilder::newStringBuilder();
    isb->addString("Uniform Value Float: ");
    isb->addDouble(_value);
    std::string s = isb->getString();
    delete isb;
    return s;
  }
};
class GPUUniformFloat: public GPUUniform{
public:
  GPUUniformFloat(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glFloat()){}
};
////////////////////////////////////////////////////////////////////////
#endif
