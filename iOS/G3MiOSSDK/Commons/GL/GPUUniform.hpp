//
//  GPUUniform.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//

#ifndef G3MiOSSDK_GPUUniform
#define G3MiOSSDK_GPUUniform

#include "GL.hpp"
#include "GLConstants.hpp"
#include "IGLUniformID.hpp"
#include "IStringBuilder.hpp"
#include "GPUVariable.hpp"

#include "RCObject.hpp"

#include "Matrix44DProvider.hpp"

class GPUUniform;

class GPUUniformValue: public RCObject {
private:
  const int _type;

protected:
  virtual ~GPUUniformValue() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUUniformValue(int type):_type(type)
  {}



  int getType() const { return _type;}
  virtual void setUniform(GL* gl, const IGLUniformID* id) const = 0;
  virtual bool isEquals(const GPUUniformValue* v) const = 0;

  virtual std::string description() const = 0;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};


class GPUUniform: public GPUVariable {
private:

  bool _dirty;
#ifdef C_CODE
  const GPUUniformValue* _value;
#endif
#ifdef JAVA_CODE
  private GPUUniformValue _value;
#endif



public:
  const IGLUniformID* _id;
  const int           _type;
  const GPUUniformKey _key;


  GPUUniform(const std::string& name,
             IGLUniformID* id,
             int type) :
  GPUVariable(name, UNIFORM),
  _id(id),
  _dirty(false),
  _value(NULL),
  _type(type),
  _key(getUniformKey(name))
  {
  }

  virtual ~GPUUniform() {
    delete _id;
    if (_value != NULL) {
      _value->_release();
    }

#ifdef JAVA_CODE
    super.dispose();
#endif
  }

//  const std::string getName() const { return _name; }
//  const IGLUniformID* getID() const { return _id; }
//  int getType() const { return _type; }
  bool wasSet() const { return _value != NULL; }
  const GPUUniformValue* getSetValue() const { return _value; }
//  GPUUniformKey getKey() const { return _key;}


  int getIndex() const {
#ifdef C_CODE
    return _key;
#endif
#ifdef JAVA_CODE
    return _key.getValue();
#endif
  }

  void unset();

  void set(const GPUUniformValue* v) {
    if (_type == v->getType()) { //type checking
      if (_value == NULL || !_value->isEquals(v)) {
        _dirty = true;
        v->_retain();
        if (_value != NULL) {
          _value->_release();
        }
        _value = v;
      }
    }
    else {
      ILogger::instance()->logError("Attempting to set uniform " + _name + " with invalid value type.");
    }
  }

  void applyChanges(GL* gl);

};


class GPUUniformValueBool : public GPUUniformValue {
private:
  ~GPUUniformValueBool() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  const bool _value;

  GPUUniformValueBool(bool b):GPUUniformValue(GLType::glBool()),_value(b) {}

  void setUniform(GL* gl, const IGLUniformID* id) const{
    if (_value) {
      gl->uniform1i(id, 1);
    } else{
      gl->uniform1i(id, 0);
    }
  }
  bool isEquals(const GPUUniformValue* v) const{
    return _value == ((GPUUniformValueBool*)v)->_value;
  }

  std::string description() const{
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("Uniform Value Boolean: ");
    isb->addBool(_value);
    std::string s = isb->getString();
    delete isb;
    return s;
  }
};


class GPUUniformBool: public GPUUniform {

public:
  GPUUniformBool(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glBool()) {}

  ~GPUUniformBool() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
};


class GPUUniformValueVec2Float:public GPUUniformValue {
protected:
  ~GPUUniformValueVec2Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  float _x, _y;

  GPUUniformValueVec2Float(float x, float y):GPUUniformValue(GLType::glVec2Float()), _x(x),_y(y) {}

  void setUniform(GL* gl, const IGLUniformID* id) const{
    gl->uniform2f(id, _x, _y);
  }
  bool isEquals(const GPUUniformValue* v) const{
    GPUUniformValueVec2Float *v2 = (GPUUniformValueVec2Float *)v;
    return (_x == v2->_x) && (_y == v2->_y);
  }

  std::string description() const{
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("Uniform Value Vec2Float: x:");
    isb->addDouble(_x);
    isb->addString("y:");
    isb->addDouble(_y);
    std::string s = isb->getString();
    delete isb;
    return s;
  }
};

class GPUUniformValueVec2FloatMutable : public GPUUniformValueVec2Float {
private:
  ~GPUUniformValueVec2FloatMutable() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:

  GPUUniformValueVec2FloatMutable(float x, float y):
  GPUUniformValueVec2Float(x,y) {}

  void changeValue(float x, float y) {
    _x = x;
    _y = y;
  }
};


class GPUUniformVec2Float: public GPUUniform {

public:
  GPUUniformVec2Float(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glVec2Float()) {}

  ~GPUUniformVec2Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

};


class GPUUniformValueVec3Float : public GPUUniformValue{
protected:
  float _x, _y, _z;

  virtual ~GPUUniformValueVec3Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:

  GPUUniformValueVec3Float(const Color& color) :
  GPUUniformValue(GLType::glVec3Float()),
  _x(color._red),
  _y(color._green),
  _z(color._blue)
  {
  }

  GPUUniformValueVec3Float(float x, float y, float z):
  GPUUniformValue(GLType::glVec3Float()),_x(x),_y(y), _z(z) {}

  void setUniform(GL* gl, const IGLUniformID* id) const{
    gl->uniform3f(id, _x, _y, _z);
  }
  bool isEquals(const GPUUniformValue* v) const{
    GPUUniformValueVec3Float *v2 = (GPUUniformValueVec3Float *)v;
    return (_x == v2->_x) && (_y == v2->_y) && (_z == v2->_z);
  }

  std::string description() const{
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("Uniform Value Vec4Float: x:");
    isb->addDouble(_x);
    isb->addString("y:");
    isb->addDouble(_y);
    isb->addString("z:");
    isb->addDouble(_z);
    std::string s = isb->getString();
    delete isb;
    return s;
  }
};

class GPUUniformValueVec3FloatMutable : public GPUUniformValueVec3Float {
private:
  ~GPUUniformValueVec3FloatMutable() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:

  GPUUniformValueVec3FloatMutable(float x, float y, float z):
  GPUUniformValueVec3Float(x,y,z) {}

  void changeValue(float x, float y, float z) {
    _x = x;
    _y = y;
    _z = z;
  }
};

class GPUUniformVec3Float: public GPUUniform {
public:
  GPUUniformVec3Float(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glVec3Float()) {}

  virtual ~GPUUniformVec3Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
};
////////////////////////////////////////////////////////////

class GPUUniformValueVec4Float : public GPUUniformValue {
private:
  virtual ~GPUUniformValueVec4Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  const float _x, _y, _z, _w;

  GPUUniformValueVec4Float(const Color& color) :
  GPUUniformValue(GLType::glVec4Float()),
  _x(color._red),
  _y(color._green),
  _z(color._blue),
  _w(color._alpha)
  {
  }

  GPUUniformValueVec4Float(float x, float y, float z, float w):
  GPUUniformValue(GLType::glVec4Float()),_x(x),_y(y), _z(z), _w(w) {}

  void setUniform(GL* gl, const IGLUniformID* id) const{
    gl->uniform4f(id, _x, _y, _z, _w);
  }
  bool isEquals(const GPUUniformValue* v) const{
    GPUUniformValueVec4Float *v2 = (GPUUniformValueVec4Float *)v;
    return (_x == v2->_x) && (_y == v2->_y) && (_z == v2->_z) && (_w == v2->_w);
  }

  std::string description() const{
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
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


class GPUUniformVec4Float: public GPUUniform {
public:
  GPUUniformVec4Float(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glVec4Float()) {}

  virtual ~GPUUniformVec4Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
};

/////////////////////


class GPUUniformValueMatrix4:public GPUUniformValue{
private:
//  const bool _ownsProvider;
#ifdef C_CODE
  const Matrix44DProvider* _provider;
  mutable const Matrix44D* _lastModelSet;
#endif
#ifdef JAVA_CODE
  protected Matrix44DProvider _provider = null;
  protected  Matrix44D _lastModelSet;
#endif

  ~GPUUniformValueMatrix4() {
    //    if (_ownsProvider) {
    _provider->_release();
    //    }
    if (_lastModelSet != NULL) {
      _lastModelSet->_release();
    }

#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  
  GPUUniformValueMatrix4(const Matrix44DProvider* providers[], int nMatrix):
  GPUUniformValue(GLType::glMatrix4Float()),
  _provider(new Matrix44DMultiplicationHolder( providers, nMatrix ) ),
  _lastModelSet(NULL)
//  _ownsProvider(true)
  {
  }

  GPUUniformValueMatrix4(const Matrix44DProvider* provider):
  GPUUniformValue(GLType::glMatrix4Float()),
  _provider(provider),
  _lastModelSet(NULL)
//  _ownsProvider(ownsProvider)
  {
    _provider->_retain();
  }

  GPUUniformValueMatrix4(const Matrix44D* m):
  GPUUniformValue(GLType::glMatrix4Float()),
  _provider(new Matrix44DHolder(m)),
  _lastModelSet(NULL)
//  _ownsProvider(true)
  {
  }

  void setUniform(GL* gl, const IGLUniformID* id) const{

    if (_lastModelSet != NULL) {
      _lastModelSet->_release();
    }

    _lastModelSet = _provider->getMatrix();

    _lastModelSet->_retain();

    gl->uniformMatrix4fv(id, false, _lastModelSet);
  }

  bool isEquals(const GPUUniformValue* v) const{
    if (_lastModelSet == ((GPUUniformValueMatrix4 *)v)->_provider->getMatrix()) {
      return true;
    }

    return false;
  }

  std::string description() const{
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("Uniform Value Matrix44D.");
    std::string s = isb->getString();
    delete isb;
    return s;
  }

  //  const Matrix44D* getMatrix() const { return _m;}
};


class GPUUniformMatrix4Float: public GPUUniform {
public:
  GPUUniformMatrix4Float(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glMatrix4Float()) {}

  ~GPUUniformMatrix4Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
};


class GPUUniformValueFloat : public GPUUniformValue {
protected:
  ~GPUUniformValueFloat() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  float _value;

  GPUUniformValueFloat(float d):GPUUniformValue(GLType::glFloat()),_value(d) {}

  void setUniform(GL* gl, const IGLUniformID* id) const{
    gl->uniform1f(id, _value);
  }
  bool isEquals(const GPUUniformValue* v) const{
    GPUUniformValueFloat *v2 = (GPUUniformValueFloat *)v;
    return _value == v2->_value;
  }

  std::string description() const{
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("Uniform Value Float: ");
    isb->addDouble(_value);
    std::string s = isb->getString();
    delete isb;
    return s;
  }
};

class GPUUniformValueFloatMutable : public GPUUniformValueFloat {
private:
  ~GPUUniformValueFloatMutable() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:

  GPUUniformValueFloatMutable(float x):
  GPUUniformValueFloat(x) {}

  void changeValue(float x) {
    _value = x;
  }
};


class GPUUniformFloat: public GPUUniform {
public:
  GPUUniformFloat(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glFloat()) {}

  ~GPUUniformFloat() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

};

class GPUUniformSampler2D: public GPUUniform {
public:
  GPUUniformSampler2D(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glInt()) {}

  ~GPUUniformSampler2D() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

};

class GPUUniformValueInt : public GPUUniformValue {
private:
  ~GPUUniformValueInt() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  const int _value;

  GPUUniformValueInt(int b):GPUUniformValue(GLType::glInt()),_value(b) {}

  void setUniform(GL* gl, const IGLUniformID* id) const{
    gl->uniform1i(id, _value);
  }
  bool isEquals(const GPUUniformValue* v) const{
    return _value == ((GPUUniformValueInt*)v)->_value;
  }

  std::string description() const{
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("Uniform Value Integer: ");
    isb->addInt(_value);
    std::string s = isb->getString();
    delete isb;
    return s;
  }
};

#endif
