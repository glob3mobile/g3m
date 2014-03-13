//
//  MutableMatrix44D.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 05/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_MutableMatrix44D
#define G3MiOSSDK_MutableMatrix44D

class FrustumData;
class Vector3D;
class Vector2D;
class Vector3F;
class Vector2F;
class MutableVector3D;
//class IFloatBuffer;

#include "Angle.hpp"

#include "ILogger.hpp"
#include "Geodetic2D.hpp"
#include "Geodetic3D.hpp"
#include "IStringBuilder.hpp"

#include <string>

#include "Matrix44D.hpp"

//#include "MutableMatrix44D.hpp"


class MutableMatrix44D {

private:

  //_m23 -> row 2, column 3
  double _m00;
  double _m01;
  double _m02;
  double _m03;
  double _m10;
  double _m11;
  double _m12;
  double _m13;
  double _m20;
  double _m21;
  double _m22;
  double _m23;
  double _m30;
  double _m31;
  double _m32;
  double _m33;

  //  mutable IFloatBuffer* _columnMajorFloatBuffer;
  //  mutable float*        _columnMajorFloatArray;

  mutable Matrix44D* _matrix44D;

  bool _isValid;


  MutableMatrix44D(bool isValid):
  _isValid(isValid),
  _matrix44D(NULL)
  {
  }

public:

  //CONTRUCTORS
  //Contructor parameters in column major order
  MutableMatrix44D(double m00, double m10, double m20, double m30,
                   double m01, double m11, double m21, double m31,
                   double m02, double m12, double m22, double m32,
                   double m03, double m13, double m23, double m33):
  _isValid(true),
  _matrix44D(NULL)
  {
    _m00  = m00;
    _m01  = m01;
    _m02  = m02;
    _m03  = m03;

    _m10  = m10;
    _m11  = m11;
    _m12  = m12;
    _m13  = m13;

    _m20  = m20;
    _m21  = m21;
    _m22  = m22;
    _m23  = m23;

    _m30  = m30;
    _m31  = m31;
    _m32  = m32;
    _m33  = m33;

    _matrix44D = NULL;
  }

  MutableMatrix44D():
  _isValid(true),
  _matrix44D(NULL)
  {
    _m00 = 0.0;
    _m01 = 0.0;
    _m02 = 0.0;
    _m03 = 0.0;

    _m10 = 0.0;
    _m11 = 0.0;
    _m12 = 0.0;
    _m13 = 0.0;

    _m20 = 0.0;
    _m21 = 0.0;
    _m22 = 0.0;
    _m23 = 0.0;

    _m30 = 0.0;
    _m31 = 0.0;
    _m32 = 0.0;
    _m33 = 0.0;
  }

  MutableMatrix44D(const MutableMatrix44D &m):
  _isValid(m._isValid)
  {
    _m00 = m._m00;
    _m01 = m._m01;
    _m02 = m._m02;
    _m03 = m._m03;

    _m10 = m._m10;
    _m11 = m._m11;
    _m12 = m._m12;
    _m13 = m._m13;

    _m20 = m._m20;
    _m21 = m._m21;
    _m22 = m._m22;
    _m23 = m._m23;

    _m30 = m._m30;
    _m31 = m._m31;
    _m32 = m._m32;
    _m33 = m._m33;

    _matrix44D = m._matrix44D;
    if (_matrix44D != NULL) {
      _matrix44D->_retain();
    }

  }

  explicit MutableMatrix44D(const Matrix44D &m):
  _isValid(true)
  {
    _m00 = m._m00;
    _m01 = m._m01;
    _m02 = m._m02;
    _m03 = m._m03;

    _m10 = m._m10;
    _m11 = m._m11;
    _m12 = m._m12;
    _m13 = m._m13;

    _m20 = m._m20;
    _m21 = m._m21;
    _m22 = m._m22;
    _m23 = m._m23;

    _m30 = m._m30;
    _m31 = m._m31;
    _m32 = m._m32;
    _m33 = m._m33;

    _matrix44D = NULL;
    
  }

  Matrix44D* asMatrix44D() const{
    if (_matrix44D == NULL) {
      _matrix44D = new Matrix44D(_m00, _m10, _m20, _m30,
                                 _m01, _m11, _m21, _m31,
                                 _m02, _m12, _m22, _m32,
                                 _m03, _m13, _m23, _m33);
    }
    return _matrix44D;
  }

  void copyValue(const MutableMatrix44D &m);

  bool isEquals(const MutableMatrix44D& m) const{
    return (
            (_m00 == m._m00) && (_m01 == m._m01) && (_m02 == m._m02) && (_m03 == m._m03) &&
            (_m10 == m._m10) && (_m11 == m._m11) && (_m12 == m._m12) && (_m13 == m._m13) &&
            (_m20 == m._m20) && (_m21 == m._m21) && (_m22 == m._m22) && (_m23 == m._m23) &&
            (_m30 == m._m30) && (_m31 == m._m31) && (_m32 == m._m32) && (_m33 == m._m33)
            );
  }

  MutableMatrix44D& operator=(const MutableMatrix44D &m);

  ~MutableMatrix44D();

  static MutableMatrix44D identity() {
    return MutableMatrix44D(1, 0, 0, 0,
                            0, 1, 0, 0,
                            0, 0, 1, 0,
                            0, 0, 0, 1);
  }

  bool isIdentity() const {
    static const MutableMatrix44D identity = MutableMatrix44D::identity();
    return isEquals(identity);
  }

  static MutableMatrix44D invalid() {
    return MutableMatrix44D(false);
  }

  bool isValid() const {
    return _isValid;
  }

  std::string description() const {
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("MUTABLE MATRIX 44D: ");
    float* f = asMatrix44D()->getColumnMajorFloatArray();
    for (int i = 0; i < 16; i++) {
      isb->addDouble(f[i]);
      if (i < 15) isb->addString(", ");
    }
    const std::string s = isb->getString();
    delete isb;
    return s;
  }
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  void copyValueOfMultiplication(const MutableMatrix44D& m1, const MutableMatrix44D& m2);

  //
  //OPERATIONS

  MutableMatrix44D multiply(const MutableMatrix44D& that) const;

  MutableMatrix44D inversed() const;

  MutableMatrix44D transposed() const;

  //METHODS TO EXTRACT VALUES FROM THE MATRIX

  double get0() const { return _m00; }
  double get1() const { return _m10; }
  double get2() const { return _m20; }
  double get3() const { return _m30; }
  double get4() const { return _m01; }
  double get5() const { return _m11; }
  double get6() const { return _m21; }
  double get7() const { return _m31; }
  double get8() const { return _m02; }
  double get9() const { return _m12; }
  double get10() const { return _m22; }
  double get11() const { return _m32; }
  double get12() const { return _m03; }
  double get13() const { return _m13; }
  double get14() const { return _m23; }
  double get15() const { return _m33; }
  
  /*
  //Returns values from 0..15 in column mayor order
  double get(int i) const {
    switch (i) {
      case 0:
        return _m00;
      case 1:
        return _m10;
      case 2:
        return _m20;
      case 3:
        return _m30;
      case 4:
        return _m01;
      case 5:
        return _m11;
      case 6:
        return _m21;
      case 7:
        return _m31;
      case 8:
        return _m02;
      case 9:
        return _m12;
      case 10:
        return _m22;
      case 11:
        return _m32;
      case 12:
        return _m03;
      case 13:
        return _m13;
      case 14:
        return _m23;
      case 15:
        return _m33;
      default:
        ILogger::instance()->logError("Accesing MutableMutableMatrix44D44D out of index");
        return 0;
    }
  }*/

  void print(const std::string& name, const ILogger* log) const;

  Vector3D unproject(const Vector3D& pixel3D,
                     const int vpLeft,
                     const int vpTop,
                     const int vpWidth,
                     const int vpHeight) const;

  Vector2D project(const Vector3D& point,
                   const int vpLeft,
                   const int vpTop,
                   const int vpWidth,
                   const int vpHeight) const;

  Vector2F project(const Vector3F& point,
                   const int vpLeft,
                   const int vpTop,
                   const int vpWidth,
                   const int vpHeight) const;

  static MutableMatrix44D createTranslationMatrix(const Vector3D& t);

  static MutableMatrix44D createTranslationMatrix(double x,
                                                  double y,
                                                  double z);

  static MutableMatrix44D createRotationMatrix(const Angle& angle,
                                               const Vector3D& axis);

  static MutableMatrix44D createGeneralRotationMatrix(const Angle& angle,
                                                      const Vector3D& axis,
                                                      const Vector3D& point);

  static MutableMatrix44D createModelMatrix(const MutableVector3D& pos,
                                            const MutableVector3D& center,
                                            const MutableVector3D& up);

  static MutableMatrix44D createProjectionMatrix(double left, double right,
                                                 double bottom, double top,
                                                 double znear, double zfar);

  static MutableMatrix44D createProjectionMatrix(const FrustumData& data);

  static MutableMatrix44D createOrthographicProjectionMatrix(double left, double right,
                                                             double bottom, double top,
                                                             double znear, double zfar);

  static MutableMatrix44D createScaleMatrix(double scaleX,
                                            double scaleY,
                                            double scaleZ) {
    return MutableMatrix44D(scaleX, 0, 0, 0,
                            0, scaleY, 0, 0,
                            0, 0, scaleZ, 0,
                            0, 0, 0, 1);

  }

  static MutableMatrix44D createGeodeticRotationMatrix(const Geodetic2D& position) {
    return MutableMatrix44D::createGeodeticRotationMatrix(position._latitude, position._longitude);
  }

  static MutableMatrix44D createGeodeticRotationMatrix(const Geodetic3D& position) {
    return MutableMatrix44D::createGeodeticRotationMatrix(position._latitude, position._longitude);
  }

  static MutableMatrix44D createScaleMatrix(const Vector3D& scale);

  static MutableMatrix44D createGeodeticRotationMatrix(const Angle& latitude,
                                                       const Angle& longitude);
  
};

#endif
