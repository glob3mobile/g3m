//
//  MutableMatrix44D.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 05/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "MutableMatrix44D.hpp"

#include "Frustum.hpp"
#include "MutableVector3D.hpp"

#include "Vector2D.hpp"


MutableMatrix44D MutableMatrix44D::multiply(const MutableMatrix44D &that) const{
  double that00 = that._m00;
  double that10 = that._m10;
  double that20 = that._m20;
  double that30 = that._m30;
  
  double that01 = that._m01;
  double that11 = that._m11;
  double that21 = that._m21;
  double that31 = that._m31;
  
  double that02 = that._m02;
  double that12 = that._m12;
  double that22 = that._m22;
  double that32 = that._m32;
  
  double that03 = that._m03;
  double that13 = that._m13;
  double that23 = that._m23;
  double that33 = that._m33;
  
  //Rows of this X Columns of that
  double m00 = (_m00 * that00) + (_m01 * that10) + (_m02 * that20) + (_m03 * that30);
  double m01 = (_m00 * that01) + (_m01 * that11) + (_m02 * that21) + (_m03 * that31);
  double m02 = (_m00 * that02) + (_m01 * that12) + (_m02 * that22) + (_m03 * that32);
  double m03 = (_m00 * that03) + (_m01 * that13) + (_m02 * that23) + (_m03 * that33);
  
  double m10 = (_m10 * that00) + (_m11 * that10) + (_m12 * that20) + (_m13 * that30);
  double m11 = (_m10 * that01) + (_m11 * that11) + (_m12 * that21) + (_m13 * that31);
  double m12 = (_m10 * that02) + (_m11 * that12) + (_m12 * that22) + (_m13 * that32);
  double m13 = (_m10 * that03) + (_m11 * that13) + (_m12 * that23) + (_m13 * that33);

  double m20 = (_m20 * that00) + (_m21 * that10) + (_m22 * that20) + (_m23 * that30);
  double m21 = (_m20 * that01) + (_m21 * that11) + (_m22 * that21) + (_m23 * that31);
  double m22 = (_m20 * that02) + (_m21 * that12) + (_m22 * that22) + (_m23 * that32);
  double m23 = (_m20 * that03) + (_m21 * that13) + (_m22 * that23) + (_m23 * that33);

  double m30 = (_m30 * that00) + (_m31 * that10) + (_m32 * that20) + (_m33 * that30);
  double m31 = (_m30 * that01) + (_m31 * that11) + (_m32 * that21) + (_m33 * that31);
  double m32 = (_m30 * that02) + (_m31 * that12) + (_m32 * that22) + (_m33 * that32);
  double m33 = (_m30 * that03) + (_m31 * that13) + (_m32 * that23) + (_m33 * that33);
  
  return MutableMatrix44D(m00, m10, m20, m30,
                m01, m11, m21, m31,
                m02, m12, m22, m32,
                m03, m13, m23, m33);
}

MutableMatrix44D MutableMatrix44D::createProjectionMatrix(const FrustumData& data)
{
  return createProjectionMatrix(data._left, data._right,
                                data._bottom, data._top,
                                data._znear, data._zfar);
}

MutableMatrix44D MutableMatrix44D::createProjectionMatrix(double left, double right,
                                     double bottom, double top,
                                     double znear, double zfar){
  // set frustum MutableMatrix44D in double
  const double rl = right - left;
  const double tb = top - bottom;
  const double fn = zfar - znear;
  
  return MutableMatrix44D(2 * znear / rl, 0, 0, 0,
                          0, 2 * znear / tb, 0, 0,
                          (right + left) / rl, (top + bottom) / tb, -(zfar + znear) / fn, -1,
                          0, 0, -2 * zfar / fn * znear, 0);
  
}

MutableMatrix44D MutableMatrix44D::transposed() const {
  return MutableMatrix44D(_m00, _m01, _m02, _m03,
                          _m10, _m11, _m12, _m13,
                          _m20, _m21, _m22, _m23,
                          _m30, _m31, _m32, _m33);
}

MutableMatrix44D MutableMatrix44D::inversed() const {
  
  double a0 = (_m00 * _m11) - (_m01 * _m10);
  double a1 = (_m00 * _m12) - (_m02 * _m10);
  double a2 = (_m00 * _m13) - (_m03 * _m10);
  double a3 = (_m01 * _m12) - (_m02 * _m11);
  double a4 = (_m01 * _m13) - (_m03 * _m11);
  double a5 = (_m02 * _m13) - (_m03 * _m12);
  
  double b0 = (_m20 * _m31) - (_m21 * _m30);
  double b1 = (_m20 * _m32) - (_m22 * _m30);
  double b2 = (_m20 * _m33) - (_m23 * _m30);
  double b3 = (_m21 * _m32) - (_m22 * _m31);
  double b4 = (_m21 * _m33) - (_m23 * _m31);
  double b5 = (_m22 * _m33) - (_m23 * _m32);
  
  double determinant = ((((a0 * b5) - (a1 * b4)) + (a2 * b3) + (a3 * b2)) - (a4 * b1)) + (a5 * b0);
  
  if (determinant == 0.0) {
    return MutableMatrix44D::invalid();
  }
  
  double m00 = (((+_m11 * b5) - (_m12 * b4)) + (_m13 * b3)) / determinant;
  double m10 = (((-_m10 * b5) + (_m12 * b2)) - (_m13 * b1)) / determinant;
  double m20 = (((+_m10 * b4) - (_m11 * b2)) + (_m13 * b0)) / determinant;
  double m30 = (((-_m10 * b3) + (_m11 * b1)) - (_m12 * b0)) / determinant;
  double m01 = (((-_m01 * b5) + (_m02 * b4)) - (_m03 * b3)) / determinant;
  double m11 = (((+_m00 * b5) - (_m02 * b2)) + (_m03 * b1)) / determinant;
  double m21 = (((-_m00 * b4) + (_m01 * b2)) - (_m03 * b0)) / determinant;
  double m31 = (((+_m00 * b3) - (_m01 * b1)) + (_m02 * b0)) / determinant;
  double m02 = (((+_m31 * a5) - (_m32 * a4)) + (_m33 * a3)) / determinant;
  double m12 = (((-_m30 * a5) + (_m32 * a2)) - (_m33 * a1)) / determinant;
  double m22 = (((+_m30 * a4) - (_m31 * a2)) + (_m33 * a0)) / determinant;
  double m32 = (((-_m30 * a3) + (_m31 * a1)) - (_m32 * a0)) / determinant;
  double m03 = (((-_m21 * a5) + (_m22 * a4)) - (_m23 * a3)) / determinant;
  double m13 = (((+_m20 * a5) - (_m22 * a2)) + (_m23 * a1)) / determinant;
  double m23 = (((-_m20 * a4) + (_m21 * a2)) - (_m23 * a0)) / determinant;
  double m33 = (((+_m20 * a3) - (_m21 * a1)) + (_m22 * a0)) / determinant;
  
  return MutableMatrix44D(m00, m10, m20, m30,
         m01, m11, m21, m31,
         m02, m12, m22, m32,
         m03, m13, m23, m33);
}

void MutableMatrix44D::print(const std::string& name, const ILogger* log) const
{
  log->logInfo("MutableMatrix44D %s:\n", name.c_str());
  log->logInfo("%.2f  %.2f %.2f %.2f\n", _m00, _m01,_m02, _m03 );
  log->logInfo("%.2f  %.2f %.2f %.2f\n", _m10, _m11,_m12, _m13 );
  log->logInfo("%.2f  %.2f %.2f %.2f\n", _m20, _m21,_m22, _m23 );
  log->logInfo("%.2f  %.2f %.2f %.2f\n", _m30, _m31,_m32, _m33 );
  log->logInfo("\n");
}

/*
 This function is intended to be used on a ModelView MutableMatrix44D. ModelView = Projection * Model
 */
Vector3D MutableMatrix44D::unproject(const Vector3D& pixel3D, const int viewport[4]) const {
  
  int TODO_Remove_UNPROJECT;//!!!!
  
  const double winx = pixel3D.x();
  const double winy = pixel3D.y();
  const double winz = pixel3D.z();
  
  double in[4], out[4];
  
  in[0] = (winx - viewport[0]) * 2 / viewport[2] - 1.0;
  in[1] = (winy - viewport[1]) * 2 / viewport[3] - 1.0;
  in[2] = 2 * winz - 1.0;
  in[3] = 1.0;
  
  //Inverse
  MutableMatrix44D m = inversed();
  
  //To object coordinates
  
  //Transformating point
  out[0] = m._m00 * in[0] + m._m01 * in[1] + m._m02 * in[2] + m._m03 * in[3];
  out[1] = m._m10 * in[0] + m._m11 * in[1] + m._m12 * in[2] + m._m13 * in[3];
  out[2] = m._m20 * in[0] + m._m21 * in[1] + m._m22 * in[2] + m._m23 * in[3];
  out[3] = m._m30 * in[0] + m._m31 * in[1] + m._m32 * in[2] + m._m33 * in[3];

  if (out[3] == 0.0)
    return Vector3D::nan();
 
  const double objx = out[0] / out[3];
  const double objy = out[1] / out[3];
  const double objz = out[2] / out[3];
  
  return Vector3D(objx, objy, objz);
}

Vector2D MutableMatrix44D::project(const Vector3D& point, const int viewport[4]) const
{
  double in[4], out[4];
  
  in[0] = point.x();
  in[1] = point.y();
  in[2] = point.z();
  in[3] = 1.0f;
  
  //Transformating point
  out[0] = _m00 * in[0] + _m01 * in[1] + _m02 * in[2] + _m03 * in[3];
  out[1] = _m10 * in[0] + _m11 * in[1] + _m12 * in[2] + _m13 * in[3];
  out[2] = _m20 * in[0] + _m21 * in[1] + _m22 * in[2] + _m23 * in[3];
  out[3] = _m30 * in[0] + _m31 * in[1] + _m32 * in[2] + _m33 * in[3];

  if (out[3] == 0.0f)
    return Vector2D::nan();
  
  out[0] /= out[3];
  out[1] /= out[3];
  out[2] /= out[3];
  
  const double winx = viewport[0] + (1.0f + out[0]) * viewport[2] / 2.0f;
  const double winy = viewport[1] + (1.0f + out[1]) * viewport[3] / 2.0f;
  //double winz = (1.0f + in[2]) / 2.0f;
  return Vector2D(winx, winy);
}

MutableMatrix44D MutableMatrix44D::createTranslationMatrix(const Vector3D& t){
  return MutableMatrix44D(1, 0, 0, 0,
                          0, 1, 0, 0,
                          0, 0, 1, 0,
                          t.x(), t.y(), t.z(), 1);
  
}

MutableMatrix44D MutableMatrix44D::createRotationMatrix(const Angle& angle, const Vector3D& p)
{
  const Vector3D p0 = p.normalized();
  
  const double c = angle.cosinus();
  const double s = angle.sinus();
  
  return MutableMatrix44D(p0.x() * p0.x() * (1 - c) + c, p0.x() * p0.y() * (1 - c) + p0.z() * s, p0.x() * p0.z() * (1 - c) - p0.y() * s, 0,
                          p0.y() * p0.x() * (1 - c) - p0.z() * s, p0.y() * p0.y() * (1 - c) + c, p0.y() * p0.z() * (1 - c) + p0.x() * s, 0,
                          p0.x() * p0.z() * (1 - c) + p0.y() * s, p0.y() * p0.z() * (1 - c) - p0.x() * s, p0.z() * p0.z() * (1 - c) + c, 0,
                          0, 0, 0, 1);
  
}

MutableMatrix44D MutableMatrix44D::createGeneralRotationMatrix(const Angle& angle, 
                                          const Vector3D& axis, const Vector3D& point){
  MutableMatrix44D T1 = MutableMatrix44D::createTranslationMatrix(point.times(-1.0));
  MutableMatrix44D R  = MutableMatrix44D::createRotationMatrix(angle, axis);
  MutableMatrix44D T2 = MutableMatrix44D::createTranslationMatrix(point);
  return T2.multiply(R).multiply(T1);
}

MutableMatrix44D MutableMatrix44D::createModelMatrix(const MutableVector3D& pos, 
                                const MutableVector3D& center,
                                 const MutableVector3D& up){
  
  const MutableVector3D w = center.sub(pos).normalized();
  const double pe = w.dot(up);
  const MutableVector3D v = up.sub(w.times(pe)).normalized();
  const MutableVector3D u = w.cross(v);
  return MutableMatrix44D(u.x(), v.x(), -w.x(), 0,
                          u.y(), v.y(), -w.y(), 0,
                          u.z(), v.z(), -w.z(), 0,
                          -pos.dot(u), -pos.dot(v), pos.dot(w), 1);
  
}

MutableMatrix44D MutableMatrix44D::createOrthographicProjectionMatrix(double left, double right,
                                                           double bottom, double top,
                                                           double znear, double zfar) {
  // set frustum MutableMatrix44D in double
  const double rl = right - left;
  const double tb = top - bottom;
  const double fn = zfar - znear;
  
  return MutableMatrix44D(2 / rl, 0, 0, 0,
                          0, 2 / tb, 0, 0,
                          0, 0, -2 / fn, 0,
                          -(right+left) / rl, -(top+bottom) / tb, -(zfar+znear) / fn, 1 );
}