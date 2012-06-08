/*
 *  GLU.cpp
 *  Terrenos
 *
 *  Created by http://www.flexicoder.com/blog/index.php/2009/11/iphone-gluproject-and-gluunproject/
 *  Copyright 2010 Universidad de Las Palmas. All rights reserved.
 * 
 *
 */

#include <math.h>
#include <strings.h>

#include "GLU.hpp"


#define DEG_TO_RAD 0.017453292519943    // PI/180


/*
 * Transform a point (column vector) by a 4x4 matrix.  I.e.  out = m * in
 * Input:  m - the 4x4 matrix
 *         in - the 4x1 vector
 * Output:  out - the resulting 4x1 vector.
 */
void GLU::transform_point(double out[4], const double m[16], const double in[4]) {
#define M(row,col)  m[col*4+row]
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

Vector3D *GLU::unproject(double winx, double winy, double winz, 
                         MutableMatrix44D model, MutableMatrix44D proj,
        const int viewport[4]) {
    /* matrice de transformation */
    double in[4], out[4];

    /* transformation coordonnees normalisees entre -1 et 1 */
    in[0] = (winx - viewport[0]) * 2 / viewport[2] - 1.0;
    in[1] = (winy - viewport[1]) * 2 / viewport[3] - 1.0;
    in[2] = 2 * winz - 1.0;
    in[3] = 1.0;

    /* calcul transformation inverse */
  MutableMatrix44D a = proj.multMatrix(model);
  MutableMatrix44D m = a.inverse();
  
    /* d'ou les coordonnees objets */
    transform_point(out, m.getMatrix(), in);
    if (out[3] == 0.0)
        return NULL;
    double objx = out[0] / out[3];
    double objy = out[1] / out[3];
    double objz = out[2] / out[3];
    return new Vector3D(objx, objy, objz);
}

MutableMatrix44D GLU::translationMatrix(Vector3D t) {
  
    double T[16] = {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            t.x(), t.y(), t.z(), 1};
  
  MutableMatrix44D res(T);
  return res;
}

MutableMatrix44D GLU::rotationMatrix(Angle angle, Vector3D p) {
  
    Vector3D p0 = p.normalized();
    double c = angle.cosinus(), s = angle.sinus();
  
    double R[16] = {p0.x() * p0.x() * (1 - c) + c, p0.x() * p0.y() * (1 - c) + p0.z() * s, p0.x() * p0.z() * (1 - c) - p0.y() * s, 0,
            p0.y() * p0.x() * (1 - c) - p0.z() * s, p0.y() * p0.y() * (1 - c) + c, p0.y() * p0.z() * (1 - c) + p0.x() * s, 0,
            p0.x() * p0.z() * (1 - c) + p0.y() * s, p0.y() * p0.z() * (1 - c) - p0.x() * s, p0.z() * p0.z() * (1 - c) + c, 0,
            0, 0, 0, 1};
  
  MutableMatrix44D rot(R);
  return rot;
}

MutableMatrix44D GLU::lookAtMatrix(Vector3D pos, Vector3D center, Vector3D up) {
  
    Vector3D w = center.sub(pos).normalized();
    double pe = w.dot(up);
    Vector3D v = up.sub(w.times(pe)).normalized();
    Vector3D u = w.cross(v);
    double LA[16] = {
            u.x(), v.x(), -w.x(), 0,
            u.y(), v.y(), -w.y(), 0,
            u.z(), v.z(), -w.z(), 0,
            -pos.dot(u), -pos.dot(v), pos.dot(w), 1};
  
  MutableMatrix44D m(LA);
  
  return m;
}

extern bool DEBUG_PRINT;

MutableMatrix44D GLU::projectionMatrix(double left, double right, double bottom, double top,
        double near, double far) {
    // set frustum matrix in double
    double rl = right - left, tb = top - bottom, fn = far - near;
    double P[16];
    P[0] = 2 * near / rl;
    P[1] = P[2] = P[3] = P[4] = 0;
    P[5] = 2 * near / tb;
    P[6] = P[7] = 0;
    P[8] = (right + left) / rl;
    P[9] = (top + bottom) / tb;
    P[10] = -(far + near) / fn;
    P[11] = -1;
    P[12] = P[13] = 0;
    P[14] = -2 * far / fn * near;
    P[15] = 0;
  
  MutableMatrix44D m(P);
  return m;
}



