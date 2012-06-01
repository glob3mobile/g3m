/*
 *  GLU.h
 *  Terrenos
 *
 *  Created by Agustín Trujillo Pino on 23/11/10.
 *  Copyright 2010 Universidad de Las Palmas. All rights reserved.
 *
 */

#ifndef GLU
#define GLU

#include "Vector3D.hpp"

class Glu {
private:

    static void transform_point(double out[4], const double m[16], const double in[4]);

    static bool invert_matrix(const double m[16], double out[16]);

    static void MultMatrix(double M[16], double N[16]);

public:


    static Vector3D *gluUnProject(double winx, double winy, double winz,
            const double model[16], const double proj[16],
            const int viewport[4]);

    static Vector3D *gluProject(double objx, double objy, double objz,
            const double model[16], const double proj[16],
            const int viewport[4]);


/*
 void GetOGLPos (float winPosX, float winPosY, float &x0, float &y0,
				GLint viewport[4], GLfloat modelview[16], GLfloat projection[16]);

void GetRayFromCameraToPixel (float winPosX, float winPosY,
				GLint viewport[4], GLfloat modelview[16], GLfloat projection[16],
				float &rx, float &ry, float &rz);
*/

    static void ComputeTranslationMatrix(Vector3D t, double M[16]);

    static void ComputeRotationMatrix(double angle_rad, Vector3D p, double M[16]);

    static void ComputeLookAtMatrix(Vector3D pos, Vector3D center, Vector3D up, double M[16]);

    static void ComputeProjectionMatrix(double left, double right, double bottom, double top,
            double near, double far, float projection[]);

};

#endif