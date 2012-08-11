//
//  Mat4.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "MutableMatrix44D.hpp"

#include <stdio.h>

#include "Vector3D.hpp"
#include "Vector2D.hpp"
#include "Angle.hpp"
#include "MutableVector3D.hpp"
#include "Camera.hpp"


MutableMatrix44D MutableMatrix44D::multiply(const MutableMatrix44D& m) const {
  double R[16];
  for (int j = 0; j < 4; j++) {
    const int j4 = j*4;
    for (int i = 0; i < 4; i++) {
//      R[j * 4 + i] = m.get(j * 4) * _m[i] + m.get(j * 4 + 1) * _m[4 + i] + m.get(j * 4 + 2) * _m[8 + i] + m.get(j * 4 + 3) * _m[12 + i];
      R[j4 + i] = m._m[j4] * _m[i] + m._m[j4 + 1] * _m[4 + i] + m._m[j4 + 2] * _m[8 + i] + m._m[j4 + 3] * _m[12 + i];
    }
  }
  
  return MutableMatrix44D(R);
}

void MutableMatrix44D::print(const std::string& name, const ILogger* log) const
{
  log->logInfo("MATRIX %s:\n", name.c_str());
  for (int j = 0; j < 4; j++) {
    log->logInfo("%.2f  %.2f %.2f %.2f\n", _m[j * 4], _m[j * 4 + 1],_m[j * 4 + 2], _m[j * 4 + 3] );
  }
  log->logInfo("\n"); 
}



/*
 * Compute inverse of 4x4 transformation matrix.
 * Code contributed by Jacques Leroy jle@star.be
 * Return GL_TRUE for success, GL_FALSE for failure (singular matrix)
 */
bool MutableMatrix44D::invert_matrix(const double m[16], double out[16]) {
  //NEW METHOD FOR ENHANCING JAVA TRANSLATION
  double inv[16], det;
  int i;
  
  inv[0] = m[5] * m[10] * m[15] - m[5] * m[11] * m[14] - m[9] * m[6] * m[15]
  + m[9] * m[7] * m[14] + m[13] * m[6] * m[11] - m[13] * m[7] * m[10];
  inv[4] = -m[4] * m[10] * m[15] + m[4] * m[11] * m[14] + m[8] * m[6] * m[15]
  - m[8] * m[7] * m[14] - m[12] * m[6] * m[11] + m[12] * m[7] * m[10];
  inv[8] = m[4] * m[9] * m[15] - m[4] * m[11] * m[13] - m[8] * m[5] * m[15]
  + m[8] * m[7] * m[13] + m[12] * m[5] * m[11] - m[12] * m[7] * m[9];
  inv[12] = -m[4] * m[9] * m[14] + m[4] * m[10] * m[13] + m[8] * m[5] * m[14]
  - m[8] * m[6] * m[13] - m[12] * m[5] * m[10] + m[12] * m[6] * m[9];
  inv[1] = -m[1] * m[10] * m[15] + m[1] * m[11] * m[14] + m[9] * m[2] * m[15]
  - m[9] * m[3] * m[14] - m[13] * m[2] * m[11] + m[13] * m[3] * m[10];
  inv[5] = m[0] * m[10] * m[15] - m[0] * m[11] * m[14] - m[8] * m[2] * m[15]
  + m[8] * m[3] * m[14] + m[12] * m[2] * m[11] - m[12] * m[3] * m[10];
  inv[9] = -m[0] * m[9] * m[15] + m[0] * m[11] * m[13] + m[8] * m[1] * m[15]
  - m[8] * m[3] * m[13] - m[12] * m[1] * m[11] + m[12] * m[3] * m[9];
  inv[13] = m[0] * m[9] * m[14] - m[0] * m[10] * m[13] - m[8] * m[1] * m[14]
  + m[8] * m[2] * m[13] + m[12] * m[1] * m[10] - m[12] * m[2] * m[9];
  inv[2] = m[1] * m[6] * m[15] - m[1] * m[7] * m[14] - m[5] * m[2] * m[15]
  + m[5] * m[3] * m[14] + m[13] * m[2] * m[7] - m[13] * m[3] * m[6];
  inv[6] = -m[0] * m[6] * m[15] + m[0] * m[7] * m[14] + m[4] * m[2] * m[15]
  - m[4] * m[3] * m[14] - m[12] * m[2] * m[7] + m[12] * m[3] * m[6];
  inv[10] = m[0] * m[5] * m[15] - m[0] * m[7] * m[13] - m[4] * m[1] * m[15]
  + m[4] * m[3] * m[13] + m[12] * m[1] * m[7] - m[12] * m[3] * m[5];
  inv[14] = -m[0] * m[5] * m[14] + m[0] * m[6] * m[13] + m[4] * m[1] * m[14]
  - m[4] * m[2] * m[13] - m[12] * m[1] * m[6] + m[12] * m[2] * m[5];
  inv[3] = -m[1] * m[6] * m[11] + m[1] * m[7] * m[10] + m[5] * m[2] * m[11]
  - m[5] * m[3] * m[10] - m[9] * m[2] * m[7] + m[9] * m[3] * m[6];
  inv[7] = m[0] * m[6] * m[11] - m[0] * m[7] * m[10] - m[4] * m[2] * m[11]
  + m[4] * m[3] * m[10] + m[8] * m[2] * m[7] - m[8] * m[3] * m[6];
  inv[11] = -m[0] * m[5] * m[11] + m[0] * m[7] * m[9] + m[4] * m[1] * m[11]
  - m[4] * m[3] * m[9] - m[8] * m[1] * m[7] + m[8] * m[3] * m[5];
  inv[15] = m[0] * m[5] * m[10] - m[0] * m[6] * m[9] - m[4] * m[1] * m[10]
  + m[4] * m[2] * m[9] + m[8] * m[1] * m[6] - m[8] * m[2] * m[5];
  
  det = m[0] * inv[0] + m[1] * inv[4] + m[2] * inv[8] + m[3] * inv[12];
  if (det == 0)
    return false;
  
  det = 1.0 / det;
  
  for (i = 0; i < 16; i++)
    out[i] = inv[i] * det;
  
  return true;
}

MutableMatrix44D MutableMatrix44D::inversed() const
{
  double out[16];
  invert_matrix(_m, out);
  
  return MutableMatrix44D(out);
}


MutableMatrix44D MutableMatrix44D::transposed() const
{
  MutableMatrix44D T(_m[0], _m[4], _m[8], _m[12],
                     _m[1], _m[5], _m[9], _m[13], 
                     _m[2], _m[6], _m[10], _m[14],
                     _m[3], _m[7], _m[11], _m[15]);
  return T;
}


void MutableMatrix44D::transformPoint(double out[4], const double in[4]) const {
#define M(row,col)  _m[col*4+row]
  out[0] =
  M(0, 0) * in[0] + M(0, 1) * in[1] + M(0, 2) * in[2] + M(0, 3) * in[3];
  out[1] =
  M(1, 0) * in[0] + M(1, 1) * in[1] + M(1, 2) * in[2] + M(1, 3) * in[3];
  out[2] =
  M(2, 0) * in[0] + M(2, 1) * in[1] + M(2, 2) * in[2] + M(2, 3) * in[3];
  out[3] =
  M(3, 0) * in[0] + M(3, 1) * in[1] + M(3, 2) * in[2] + M(3, 3) * in[3];
#undef M
}

/*
 This function is intended to be used on a ModelView matrix. ModelView = Projection * Model
 */
Vector3D MutableMatrix44D::unproject(const Vector3D& pixel3D, const int viewport[4]) const {
  
  const double winx = pixel3D.x();
  const double winy = pixel3D.y();
  const double winz = pixel3D.z();
  
  /* matrice de transformation */
  double in[4], out[4];
  
  /* transformation coordonnees normalisees entre -1 et 1 */
  in[0] = (winx - viewport[0]) * 2 / viewport[2] - 1.0;
  in[1] = (winy - viewport[1]) * 2 / viewport[3] - 1.0;
  in[2] = 2 * winz - 1.0;
  in[3] = 1.0;
  
  /* calcul transformation inverse */
  MutableMatrix44D m = this->inversed();
  
  /* d'ou les coordonnees objets */
  m.transformPoint(out, in);
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
  transformPoint(out, in);
  if (out[3] == 0.0f)
    return Vector2D::nan();
  
  out[0] /= out[3];
  out[1] /= out[3];
  out[2] /= out[3];
  
  double winx = viewport[0] + (1.0f + out[0]) * viewport[2] / 2.0f;
  double winy = viewport[1] + (1.0f + out[1]) * viewport[3] / 2.0f;
  //double winz = (1.0f + in[2]) / 2.0f;
  return Vector2D(winx, winy);
}




MutableMatrix44D MutableMatrix44D::createTranslationMatrix(const Vector3D& t) {
  const double T[16] = {
    1, 0, 0, 0,
    0, 1, 0, 0,
    0, 0, 1, 0,
    t.x(), t.y(), t.z(), 1
  };
  
  return MutableMatrix44D(T);
}

MutableMatrix44D MutableMatrix44D::createRotationMatrix(const Angle& angle, const Vector3D& p)
{
  const Vector3D p0 = p.normalized();
  
  const double c = angle.cosinus();
  const double s = angle.sinus();
  
  const double R[16] = {
    p0.x() * p0.x() * (1 - c) + c, p0.x() * p0.y() * (1 - c) + p0.z() * s, p0.x() * p0.z() * (1 - c) - p0.y() * s, 0,
    p0.y() * p0.x() * (1 - c) - p0.z() * s, p0.y() * p0.y() * (1 - c) + c, p0.y() * p0.z() * (1 - c) + p0.x() * s, 0,
    p0.x() * p0.z() * (1 - c) + p0.y() * s, p0.y() * p0.z() * (1 - c) - p0.x() * s, p0.z() * p0.z() * (1 - c) + c, 0,
    0, 0, 0, 1
  };
  
  return MutableMatrix44D(R);
}

MutableMatrix44D MutableMatrix44D::createGeneralRotationMatrix(const Angle& angle, 
                                                    const Vector3D& axis, const Vector3D& point)
{
  MutableMatrix44D T1 = MutableMatrix44D::createTranslationMatrix(point.times(-1.0));
  MutableMatrix44D R  = MutableMatrix44D::createRotationMatrix(angle, axis);
  MutableMatrix44D T2 = MutableMatrix44D::createTranslationMatrix(point);
  return T2.multiply(R).multiply(T1);
}



MutableMatrix44D MutableMatrix44D::createModelMatrix(const MutableVector3D& pos,
                                                     const MutableVector3D& center,
                                                     const MutableVector3D& up) {
  const MutableVector3D w = center.sub(pos).normalized();
  const double pe = w.dot(up);
  const MutableVector3D v = up.sub(w.times(pe)).normalized();
  const MutableVector3D u = w.cross(v);
  const double LA[16] = {
    u.x(), v.x(), -w.x(), 0,
    u.y(), v.y(), -w.y(), 0,
    u.z(), v.z(), -w.z(), 0,
    -pos.dot(u), -pos.dot(v), pos.dot(w), 1
  };
  return MutableMatrix44D(LA);
}

MutableMatrix44D MutableMatrix44D::createProjectionMatrix(double left, double right,
                                                          double bottom, double top,
                                                          double znear, double zfar) {
  // set frustum matrix in double
  const double rl = right - left;
  const double tb = top - bottom;
  const double fn = zfar - znear;
  
  double P[16];
  P[0] = 2 * znear / rl;
  P[1] = P[2] = P[3] = P[4] = 0;
  P[5] = 2 * znear / tb;
  P[6] = P[7] = 0;
  P[8] = (right + left) / rl;
  P[9] = (top + bottom) / tb;
  P[10] = -(zfar + znear) / fn;
  P[11] = -1;
  P[12] = P[13] = 0;
  P[14] = -2 * zfar / fn * znear;
  P[15] = 0;
  
  return MutableMatrix44D(P);
}

MutableMatrix44D MutableMatrix44D::createProjectionMatrix(const FrustumData& data)
{
  return createProjectionMatrix(data._left, data._right,
                                data._bottom, data._top,
                                data._znear, data._zfar);
}



MutableMatrix44D MutableMatrix44D::fromTranslation(const MutableVector3D& translation) {
  return MutableMatrix44D(1.0, 0.0, 0.0, translation.x(),
                          0.0, 1.0, 0.0, translation.y(),
                          0.0, 0.0, 1.0, translation.z(),
                          0.0, 0.0, 0.0, 1.0);
}

MutableMatrix44D MutableMatrix44D::fromScale(const MutableVector3D& scale) {
  return MutableMatrix44D(scale.x(), 0.0, 0.0, 0,
                          0.0, scale.y(), 0.0, 0,
                          0.0, 0.0, scale.z(), 0,
                          0.0, 0.0, 0.0, 1.0);
  
}

