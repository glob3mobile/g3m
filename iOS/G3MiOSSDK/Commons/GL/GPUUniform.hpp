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
#include "GPUVariable.hpp"

#include "RCObject.hpp"

class GPUUniform;

class GPUUniformValue: public RCObject{
  const int _type;

  //  mutable GPUUniform* _uniform;

public:
  GPUUniformValue(int type):_type(type)
  //, _uniform(NULL)
  {}

  virtual ~GPUUniformValue() {
//    ILogger::instance()->logInfo("Deleting Uniform Value");
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  
  int getType() const { return _type;}
  virtual void setUniform(GL* gl, const IGLUniformID* id) const = 0;
  virtual bool isEqualsTo(const GPUUniformValue* v) const = 0;

  //  GPUUniform* getLinkedUniform() const { return _uniform;}

  virtual std::string description() const = 0;

  //  void linkToGPUUniform(GPUUniform* u) const{
  //    _uniform = u;
  //  }
  //
  //  void unLinkToGPUUniform() {
  //    _uniform = NULL;
  //  }

  //  void setValueToLinkedUniform() const;

  //  virtual GPUUniformValue* copyOrCreate(GPUUniformValue* value) const {
  //    return value;
  //  }

  //  virtual GPUUniformValue* copyOrCreate(GPUUniformValue* value) const = 0;

  //  bool linkToGPUProgram(const GPUProgram* prog, int key) const{
  //    GPUUniform* u = prog->getGPUUniform(key);
  //    if (u == NULL) {
  //      ILogger::instance()->logError("UNIFORM WITH KEY %d NOT FOUND", key);
  //      return false;
  //    } else{
  //      _uniform = u;
  //      return true;
  //    }
  //  }
};


class GPUUniform: public GPUVariable {
private:
  const IGLUniformID* _id;

  bool _dirty;
#ifdef C_CODE
  const GPUUniformValue* _value;
#endif
#ifdef JAVA_CODE
  private GPUUniformValue _value;
#endif
  const int _type;

  const GPUUniformKey _key;

public:

  virtual ~GPUUniform() {
    delete _id;
    if (_value != NULL) {
      _value->_release();
    }

#ifdef JAVA_CODE
  super.dispose();
#endif

  }

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

  const std::string getName() const { return _name; }
  const IGLUniformID* getID() const { return _id; }
  int getType() const { return _type; }
  bool wasSet() const { return _value != NULL; }
  const GPUUniformValue* getSetValue() const { return _value; }
  GPUUniformKey getKey() const { return _key;}


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
//    if (v != _value) {
      if (_type == v->getType()) { //type checking
        if (_value == NULL || !_value->isEqualsTo(v)) {
          _dirty = true;
          //        _value = v->copyOrCreate(_value);

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
//    }
  }

  void applyChanges(GL* gl);

};


class GPUUniformValueBool:public GPUUniformValue{
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
  bool isEqualsTo(const GPUUniformValue* v) const{
    return _value == ((GPUUniformValueBool*)v)->_value;
  }

  //  GPUUniformValue* copyOrCreate(GPUUniformValue* value) const {
  //    if (value != NULL) {
  //      delete value;
  //    }
  //    return new GPUUniformValueBool(_value);
  //  }

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
  GPUUniformBool(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glBool()) {}
};


class GPUUniformValueVec2Float:public GPUUniformValue{
public:
  const float _x, _y;

  GPUUniformValueVec2Float(float x, float y):GPUUniformValue(GLType::glVec2Float()), _x(x),_y(y) {}

  void setUniform(GL* gl, const IGLUniformID* id) const{
    gl->uniform2f(id, _x, _y);
  }
  bool isEqualsTo(const GPUUniformValue* v) const{
    GPUUniformValueVec2Float *v2 = (GPUUniformValueVec2Float *)v;
    return (_x == v2->_x) && (_y == v2->_y);
  }

  //  GPUUniformValue* copyOrCreate(GPUUniformValue* value) const {
  //    if (value == NULL) {
  //      delete value;
  //    }
  //      return new GPUUniformValueVec2Float(_x,_y);
  //  }

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
  GPUUniformVec2Float(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glVec2Float()) {}
};


class GPUUniformValueVec4Float:public GPUUniformValue{
public:
  const float _x, _y, _z, _w;

  GPUUniformValueVec4Float(float x, float y, float z, float w):
  GPUUniformValue(GLType::glVec4Float()),_x(x),_y(y), _z(z), _w(w) {}

  void setUniform(GL* gl, const IGLUniformID* id) const{
    gl->uniform4f(id, _x, _y, _z, _w);
  }
  bool isEqualsTo(const GPUUniformValue* v) const{
    GPUUniformValueVec4Float *v2 = (GPUUniformValueVec4Float *)v;
    return (_x == v2->_x) && (_y == v2->_y) && (_z == v2->_z) && (_w == v2->_w);
  }

  //  GPUUniformValue* copyOrCreate(GPUUniformValue* value) const {
  //    if (value != NULL) {
  //      delete value;
  //    }
  //      return new GPUUniformValueVec4Float(_x,_y,_z,_w);
  //  }

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
  GPUUniformVec4Float(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glVec4Float()) {}
};


/////////////////////

class ModelviewMatrixHolder {
  const Matrix44D** _matrix;
  const Matrix44DHolder** _matrixHolders;
  int _nMatrix;
  mutable Matrix44D* _modelview;
public:
  ModelviewMatrixHolder(const Matrix44DHolder* matrixHolders[], int nMatrix):
  _matrixHolders(matrixHolders),
  _nMatrix(nMatrix),
  _modelview(NULL)
  {
#ifdef C_CODE
    _matrix = new const Matrix44D*[nMatrix];
#endif
#ifdef JAVA_CODE
    _matrix = new Matrix44D[nMatrix];
#endif
    for (int i = 0; i < _nMatrix; i++) {
      _matrix[i] = matrixHolders[i]->getMatrix();
      if (_matrix[i] == NULL) {
        ILogger::instance()->logError("Modelview multiplication failure");
      }
    }
  }

  ~ModelviewMatrixHolder() {
#ifdef C_CODE
    delete[] _matrix;
    delete[] _matrixHolders;
#endif
    if (_modelview != NULL) {
      _modelview->_release();
    }

#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  Matrix44D* getModelview() const {

    if (_modelview != NULL) {
      for (int i = 0; i < _nMatrix; i++) {
        const Matrix44D* m = _matrixHolders[i]->getMatrix();
        if (m == NULL) {
          ILogger::instance()->logError("Modelview multiplication failure");
        }

        if (_matrix[i] != m) {

          //If one matrix differs we have to raplace all matrixes on Holders and recalculate modelview
          _modelview->_release();//NEW MODELVIEW NEEDED
          _modelview = NULL;

          for (int j = 0; j < _nMatrix; j++) {
            _matrix[j] = _matrixHolders[j]->getMatrix();
          }
          break;
        }
      }
    }


    if (_modelview == NULL) {
      _modelview = new Matrix44D(*_matrix[0]);
      for (int i = 1; i < _nMatrix; i++) {
        const Matrix44D* m2 = _matrix[i];
        Matrix44D* m3 = _modelview->createMultiplication(*m2);
        _modelview->_release();
        _modelview = m3;
      }
    }
    return _modelview;
  }
  
};

/////////////////////

class GPUUniformValueModelview:public GPUUniformValue{
protected:
  mutable Matrix44D* _lastMatrix;
#ifdef C_CODE
  const ModelviewMatrixHolder _holder;
#endif
#ifdef JAVA_CODE
  protected ModelviewMatrixHolder _holder = null;
#endif
public:
#ifdef C_CODE
  GPUUniformValueModelview(const Matrix44DHolder* matrixHolders[], int nMatrix):
  GPUUniformValue(GLType::glMatrix4Float()),
  _holder(matrixHolders, nMatrix),
  _lastMatrix(NULL)
  {
  }
#endif
#ifdef JAVA_CODE
  public GPUUniformValueModelview(Matrix44DHolder[] matrixHolders, int nMatrix)
  {
    super(GLType.glMatrix4Float());
    _holder = new ModelviewMatrixHolder(matrixHolders, nMatrix);
  }
#endif

  ~GPUUniformValueModelview() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  void setUniform(GL* gl, const IGLUniformID* id) const{
    _lastMatrix = _holder.getModelview();
    gl->uniformMatrix4fv(id, false, _lastMatrix);
  }

  bool isEqualsTo(const GPUUniformValue* v) const{
    return (_lastMatrix == ((GPUUniformValueModelview *)v)->_holder.getModelview());
  }

  std::string description() const{
    IStringBuilder *isb = IStringBuilder::newStringBuilder();
    isb->addString("Uniform Value Matrix44D.");
    std::string s = isb->getString();
    delete isb;
    return s;
  }

//  const Matrix44D* getMatrix() const { return _m;}
};

class GPUUniformValueMatrix4Float:public GPUUniformValue{

  GPUUniformValueMatrix4Float(const GPUUniformValueMatrix4Float* that):
  GPUUniformValue(GLType::glMatrix4Float()),
  _m(that->_m)
  {
    that->_m->_retain();
  }

public:
#ifdef C_CODE
  const Matrix44D* const _m;
#endif
#ifdef JAVA_CODE
  public final Matrix44D _m;
#endif

  GPUUniformValueMatrix4Float(const Matrix44D& m):
  GPUUniformValue(GLType::glMatrix4Float()),_m(&m) {
    m._retain();
  }

  ~GPUUniformValueMatrix4Float() {
    _m->_release();
    
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  void setUniform(GL* gl, const IGLUniformID* id) const{
    gl->uniformMatrix4fv(id, false, _m);
  }

  bool isEqualsTo(const GPUUniformValue* v) const{
    const Matrix44D *m = ((GPUUniformValueMatrix4Float *)v)->_m;
    if (_m == m) {
      return true;
    }

    return _m->isEqualsTo(*m);
  }

  std::string description() const{
    IStringBuilder *isb = IStringBuilder::newStringBuilder();
    isb->addString("Uniform Value Matrix44D.");
    std::string s = isb->getString();
    delete isb;
    return s;
  }

  const Matrix44D* getMatrix() const { return _m;}
};



//class GPUUniformValueMatrix4FloatTransform:public GPUUniformValue{
//
//  GPUUniformValueMatrix4FloatTransform(const GPUUniformValueMatrix4FloatTransform* that):
//  GPUUniformValue(GLType::glMatrix4Float()),
//  _m(MutableMatrix44D(that->_m)),
//  _isTransform(that->_isTransform)
//  {}
//
//public:
//  MutableMatrix44D _m;
//  bool _isTransform;
//
//  GPUUniformValueMatrix4FloatTransform(const MutableMatrix44D& m, bool isTransform):
//  GPUUniformValue(GLType::glMatrix4Float()),_m(MutableMatrix44D(m)), _isTransform(isTransform)/*, _transformedMatrix(m)*/{}
//
//  void setUniform(GL* gl, const IGLUniformID* id) const{
//    gl->uniformMatrix4fv(id, false, _m.asMatrix44D());
//  }
//
//  bool isEqualsTo(const GPUUniformValue* v) const{
//    GPUUniformValueMatrix4FloatTransform *v2 = (GPUUniformValueMatrix4FloatTransform *)v;
//    return _m.isEqualsTo(v2->_m);
//  }
//
//  GPUUniformValue* copyOrCreate(GPUUniformValue* value) const;
//
//  std::string description() const{
//    IStringBuilder *isb = IStringBuilder::newStringBuilder();
//    isb->addString("Uniform Value Matrix44D.");
//    std::string s = isb->getString();
//    delete isb;
//    return s;
//  }
//
//  const MutableMatrix44D* getValue() const{
//    //    return &_transformedMatrix;
//    return &_m;
//  }
//};


class GPUUniformMatrix4Float: public GPUUniform{
public:
  GPUUniformMatrix4Float(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glMatrix4Float()) {}
};


class GPUUniformValueFloat:public GPUUniformValue{
public:
  const float _value;

  GPUUniformValueFloat(float d):GPUUniformValue(GLType::glFloat()),_value(d) {}

  void setUniform(GL* gl, const IGLUniformID* id) const{
    gl->uniform1f(id, _value);
  }
  bool isEqualsTo(const GPUUniformValue* v) const{
    GPUUniformValueFloat *v2 = (GPUUniformValueFloat *)v;
    return _value == v2->_value;
  }

  //  GPUUniformValue* copyOrCreate(GPUUniformValue* value) const {
  //    if (value != NULL) {
  //      delete value;
  //    }
  //    return new GPUUniformValueFloat(_value);
  //  }

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
  GPUUniformFloat(const std::string&name, IGLUniformID* id):GPUUniform(name,id, GLType::glFloat()) {}
};

#endif
