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

MutableMatrix44D MutableMatrix44D::multMatrix(const MutableMatrix44D& m) const {
  double R[16];
  for (int j = 0; j < 4; j++)
    for (int i = 0; i < 4; i++)
      R[j * 4 + i] = m.get(j * 4) * _m[i] + 
      m.get(j * 4 + 1) * _m[4 + i] + 
      m.get(j * 4 + 2) * _m[8 + i] + 
      m.get(j * 4 + 3) * _m[12 + i];
  
  MutableMatrix44D m2(R);
  
  return m2;
}

void MutableMatrix44D::print() const
{
  for (int j = 0; j < 4; j++)
    printf("%.2f  %.2f %.2f %.2f\n", _m[j * 4], _m[j * 4 + 1],_m[j * 4 + 2], _m[j * 4 + 3] );

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

MutableMatrix44D MutableMatrix44D::inverse() const
{
  double out[16];
  invert_matrix(_m, out);
  
  MutableMatrix44D  m(out);
  return m;
}

void MutableMatrix44D::transformPoint(double out[4], const double in[4]) {
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
Vector3D *MutableMatrix44D::unproject(const Vector3D& pixel3D, const int viewport[4]) const {
  
  double winx = pixel3D.x();
  double winy = pixel3D.y();
  double winz = pixel3D.z();
  
  /* matrice de transformation */
  double in[4], out[4];
  
  /* transformation coordonnees normalisees entre -1 et 1 */
  in[0] = (winx - viewport[0]) * 2 / viewport[2] - 1.0;
  in[1] = (winy - viewport[1]) * 2 / viewport[3] - 1.0;
  in[2] = 2 * winz - 1.0;
  in[3] = 1.0;
  
  /* calcul transformation inverse */
  MutableMatrix44D m = this->inverse();
  
  /* d'ou les coordonnees objets */
  m.transformPoint(out, in);
  if (out[3] == 0.0)
    return NULL;
  double objx = out[0] / out[3];
  double objy = out[1] / out[3];
  double objz = out[2] / out[3];
  return new Vector3D(objx, objy, objz);
}

