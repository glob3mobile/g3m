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
void Glu::transform_point(double out[4], const double m[16], const double in[4]) {
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


/*
* Compute inverse of 4x4 transformation matrix.
* Code contributed by Jacques Leroy jle@star.be
* Return GL_TRUE for success, GL_FALSE for failure (singular matrix)
*/
bool Glu::invert_matrix(const double m[16], double out[16]) {
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


//   OLD INVERTING METHOD 
//    
//	/* NB. OpenGL Matrices are COLUMN major. */
//    
//    
// #define SWAP_ROWS(a, b) { double *_tmp = a; (a)=(b); (b)=_tmp; }
// #define MAT(m,r,c) (m)[(c)*4+(r)]
//	
//	double wtmp[4][8];
//	double m0, m1, m2, m3, s;
//	double *r0, *r1, *r2, *r3;
//	
//	r0 = wtmp[0]; r1 = wtmp[1]; r2 = wtmp[2]; r3 = wtmp[3];
//	
//	r0[0] = MAT(m, 0, 0); r0[1] = MAT(m, 0, 1);
//	r0[2] = MAT(m, 0, 2); r0[3] = MAT(m, 0, 3);
//	r0[4] = 1.0; r0[5] = r0[6] = r0[7] = 0.0;
//	r1[0] = MAT(m, 1, 0); r1[1] = MAT(m, 1, 1);
//	r1[2] = MAT(m, 1, 2); r1[3] = MAT(m, 1, 3);
//	r1[5] = 1.0; r1[4] = r1[6] = r1[7] = 0.0;
//	r2[0] = MAT(m, 2, 0); r2[1] = MAT(m, 2, 1);
//	r2[2] = MAT(m, 2, 2); r2[3] = MAT(m, 2, 3);
//	r2[6] = 1.0; r2[4] = r2[5] = r2[7] = 0.0;
//	r3[0] = MAT(m, 3, 0); r3[1] = MAT(m, 3, 1);
//	r3[2] = MAT(m, 3, 2); r3[3] = MAT(m, 3, 3);
//	r3[7] = 1.0; r3[4] = r3[5] = r3[6] = 0.0;
//	
//	/* choose pivot - or die */
//	if (fabs(r3[0]) > fabs(r2[0]))
//		SWAP_ROWS(r3, r2);
//	if (fabs(r2[0]) > fabs(r1[0]))
//		SWAP_ROWS(r2, r1);
//	if (fabs(r1[0]) > fabs(r0[0]))
//		SWAP_ROWS(r1, r0);
//	if (0.0 == r0[0])
//		return false;
//	
//	/* eliminate first variable     */
//	m1 = r1[0] / r0[0];
//	m2 = r2[0] / r0[0];
//	m3 = r3[0] / r0[0];
//	s = r0[1];
//	r1[1] -= m1 * s;
//	r2[1] -= m2 * s;
//	r3[1] -= m3 * s;
//	s = r0[2];
//	r1[2] -= m1 * s;
//	r2[2] -= m2 * s;
//	r3[2] -= m3 * s;
//	s = r0[3];
//	r1[3] -= m1 * s;
//	r2[3] -= m2 * s;
//	r3[3] -= m3 * s;
//	s = r0[4];
//	if (s != 0.0) {
//		r1[4] -= m1 * s;
//		r2[4] -= m2 * s;
//		r3[4] -= m3 * s;
//	}
//	s = r0[5];
//	if (s != 0.0) {
//		r1[5] -= m1 * s;
//		r2[5] -= m2 * s;
//		r3[5] -= m3 * s;
//	}
//	s = r0[6];
//	if (s != 0.0) {
//		r1[6] -= m1 * s;
//		r2[6] -= m2 * s;
//		r3[6] -= m3 * s;
//	}
//	s = r0[7];
//	if (s != 0.0) {
//		r1[7] -= m1 * s;
//		r2[7] -= m2 * s;
//		r3[7] -= m3 * s;
//	}
//	
//	/* choose pivot - or die */
//	if (fabs(r3[1]) > fabs(r2[1]))
//		SWAP_ROWS(r3, r2);
//	if (fabs(r2[1]) > fabs(r1[1]))
//		SWAP_ROWS(r2, r1);
//	if (0.0 == r1[1])
//		return false;
//	
//	/* eliminate second variable */
//	m2 = r2[1] / r1[1];
//	m3 = r3[1] / r1[1];
//	r2[2] -= m2 * r1[2];
//	r3[2] -= m3 * r1[2];
//	r2[3] -= m2 * r1[3];
//	r3[3] -= m3 * r1[3];
//	s = r1[4];
//	if (0.0 != s) {
//		r2[4] -= m2 * s;
//		r3[4] -= m3 * s;
//	}
//	s = r1[5];
//	if (0.0 != s) {
//		r2[5] -= m2 * s;
//		r3[5] -= m3 * s;
//	}
//	s = r1[6];
//	if (0.0 != s) {
//		r2[6] -= m2 * s;
//		r3[6] -= m3 * s;
//	}
//	s = r1[7];
//	if (0.0 != s) {
//		r2[7] -= m2 * s;
//		r3[7] -= m3 * s;
//	}
//	
//	/* choose pivot - or die */
//	if (fabs(r3[2]) > fabs(r2[2]))
//		SWAP_ROWS(r3, r2);
//	if (0.0 == r2[2])
//		return false;
//	
//	/* eliminate third variable */
//	m3 = r3[2] / r2[2];
//	r3[3] -= m3 * r2[3]; r3[4] -= m3 * r2[4];
//	r3[5] -= m3 * r2[5]; r3[6] -= m3 * r2[6]; r3[7] -= m3 * r2[7];
//	
//	/* last check */
//	if (0.0 == r3[3])
//		return false;
//	
//	s = 1.0 / r3[3];		/* now back substitute row 3 */
//	r3[4] *= s;
//	r3[5] *= s;
//	r3[6] *= s;
//	r3[7] *= s;
//	
//	m2 = r2[3];			/* now back substitute row 2 */
//	s = 1.0 / r2[2];
//	r2[4] = s * (r2[4] - r3[4] * m2); r2[5] = s * (r2[5] - r3[5] * m2);
//	r2[6] = s * (r2[6] - r3[6] * m2); r2[7] = s * (r2[7] - r3[7] * m2);
//	m1 = r1[3];
//	r1[4] -= r3[4] * m1; r1[5] -= r3[5] * m1;
//	r1[6] -= r3[6] * m1; r1[7] -= r3[7] * m1;
//	m0 = r0[3];
//	r0[4] -= r3[4] * m0; r0[5] -= r3[5] * m0;
//	r0[6] -= r3[6] * m0; r0[7] -= r3[7] * m0;
//	
//	m1 = r1[2];			/* now back substitute row 1 */
//	s = 1.0 / r1[1];
//	r1[4] = s * (r1[4] - r2[4] * m1); r1[5] = s * (r1[5] - r2[5] * m1);
//	r1[6] = s * (r1[6] - r2[6] * m1); r1[7] = s * (r1[7] - r2[7] * m1);
//	m0 = r0[2];
//	r0[4] -= r2[4] * m0; r0[5] -= r2[5] * m0;
//	r0[6] -= r2[6] * m0; r0[7] -= r2[7] * m0;
//	
//	m0 = r0[1];			/* now back substitute row 0 */
//	s = 1.0 / r0[0];
//	r0[4] = s * (r0[4] - r1[4] * m0); r0[5] = s * (r0[5] - r1[5] * m0);
//	r0[6] = s * (r0[6] - r1[6] * m0); r0[7] = s * (r0[7] - r1[7] * m0);
//	
//	MAT(out, 0, 0) = r0[4];
//	MAT(out, 0, 1) = r0[5]; MAT(out, 0, 2) = r0[6];
//	MAT(out, 0, 3) = r0[7]; MAT(out, 1, 0) = r1[4];
//	MAT(out, 1, 1) = r1[5]; MAT(out, 1, 2) = r1[6];
//	MAT(out, 1, 3) = r1[7]; MAT(out, 2, 0) = r2[4];
//	MAT(out, 2, 1) = r2[5]; MAT(out, 2, 2) = r2[6];
//	MAT(out, 2, 3) = r2[7]; MAT(out, 3, 0) = r3[4];
//	MAT(out, 3, 1) = r3[5]; MAT(out, 3, 2) = r3[6];
//	MAT(out, 3, 3) = r3[7];
//	
//	return true;
//    
// #undef MAT
// #undef SWAP_ROWS
}


/*
* Perform a 4x4 matrix multiplication  (product = a x b).
* Input:  a, b - matrices to multiply
* Output:  product - product of a and b
*/
static void
matmul(double product[], const double a[], const double b[]) {
    /* This matmul was contributed by Thomas Malik */
    double temp[16];
    int i;

#define A(row,col)  a[(col<<2)+row]
#define B(row,col)  b[(col<<2)+row]
#define T(row,col)  temp[(col<<2)+row]

    /* i-te Zeile */
    for (i = 0; i < 4; i++) {
        T(i, 0) =
                A(i, 0) * B(0, 0) + A(i, 1) * B(1, 0) + A(i, 2) * B(2, 0) + A(i,
                3) *
                        B(3, 0);
        T(i, 1) =
                A(i, 0) * B(0, 1) + A(i, 1) * B(1, 1) + A(i, 2) * B(2, 1) + A(i,
                3) *
                        B(3, 1);
        T(i, 2) =
                A(i, 0) * B(0, 2) + A(i, 1) * B(1, 2) + A(i, 2) * B(2, 2) + A(i,
                3) *
                        B(3, 2);
        T(i, 3) =
                A(i, 0) * B(0, 3) + A(i, 1) * B(1, 3) + A(i, 2) * B(2, 3) + A(i,
                3) *
                        B(3, 3);
    }

#undef A
#undef B
#undef T
    //memcpy(product, temp, 16 * sizeof(double));

    for (int j = 0; j < 16; j++) {
        product[j] = temp[j];
    }
}



//bool Glu::gluUnProject(double winx, double winy, double winz,
//				   const double model[16], const double proj[16],
//				   const int viewport[4],
//				   double &objx, double  &objy, double &objz)
//{
//	/* matrice de transformation */
//	double m[16], A[16];
//	double in[4], out[4];
//	
//	/* transformation coordonnees normalisees entre -1 et 1 */
//	in[0] = (winx - viewport[0]) * 2 / viewport[2] - 1.0;
//	in[1] = (winy - viewport[1]) * 2 / viewport[3] - 1.0;
//	in[2] = 2 * winz - 1.0;
//	in[3] = 1.0;
//	
//	/* calcul transformation inverse */
//	matmul(A, proj, model);
//	invert_matrix(A, m);
//	
//	/* d'ou les coordonnees objets */
//	transform_point(out, m, in);
//	if (out[3] == 0.0)
//		return false;
//	objx = out[0] / out[3];
//	objy = out[1] / out[3];
//	objz = out[2] / out[3];
//	return true;
//}

Vector3D *Glu::gluUnProject(double winx, double winy, double winz,
        const double model[16], const double proj[16],
        const int viewport[4]) {
    /* matrice de transformation */
    double m[16], A[16];
    double in[4], out[4];

    /* transformation coordonnees normalisees entre -1 et 1 */
    in[0] = (winx - viewport[0]) * 2 / viewport[2] - 1.0;
    in[1] = (winy - viewport[1]) * 2 / viewport[3] - 1.0;
    in[2] = 2 * winz - 1.0;
    in[3] = 1.0;

    /* calcul transformation inverse */
    matmul(A, proj, model);
    invert_matrix(A, m);

    /* d'ou les coordonnees objets */
    transform_point(out, m, in);
    if (out[3] == 0.0)
        return NULL;
    double objx = out[0] / out[3];
    double objy = out[1] / out[3];
    double objz = out[2] / out[3];
    return new Vector3D(objx, objy, objz);
}


Vector3D *Glu::gluProject(double objx, double objy, double objz,
        const double model[16], const double proj[16],
        const int viewport[4]) {
    // matrice de transformation
    double in[4], out[4];

    // initilise la matrice et le vecteur a transformer
    in[0] = objx;
    in[1] = objy;
    in[2] = objz;
    in[3] = 1.0f;
    transform_point(out, model, in);
    transform_point(in, proj, out);

    // d'ou le resultat normalise entre -1 et 1
    if (in[3] == 0.0f)
        return NULL;

    in[0] /= in[3];
    in[1] /= in[3];
    in[2] /= in[3];

    // en coordonnees ecran
    double winx = viewport[0] + (1.0f + in[0]) * viewport[2] / 2.0f;
    double winy = viewport[1] + (1.0f + in[1]) * viewport[3] / 2.0f;
    // entre 0 et 1 suivant z
    double winz = (1.0f + in[2]) / 2.0f;
    return new Vector3D(winx, winy, winz);
}



/*
void GetOGLPos (float winPosX, float winPosY, float &x0, float &y0,
				GLint viewport[4], GLfloat modelview[16], GLfloat projection[16])
{
	
	//opengl 0,0 is at the bottom not at the top
	winPosY = (float)viewport[3] - winPosY;
	// float winZ;
	//we cannot do the following in openGL ES due to tile rendering
	// glReadPixels( (int)winPos.x(), (int)winPos.y(), 1, 1, GL_DEPTH_COMPONENT24_OES, GL_FLOAT, &winZ );
	
	float cX, cY, cZ, fX, fY, fZ;
	//gives us camera position (near plan)
	gluUnProject( winPosX, winPosY, 0, modelview, projection, viewport, &cX, &cY, &cZ);
	//far plane
	gluUnProject( winPosX, winPosY, 1, modelview, projection, viewport, &fX, &fY, &fZ);
	
	//We could use some vector3d class, but this will do fine for now
	//ray
	fX -= cX;
	fY -= cY;
	fZ -= cZ;
	float rayLength = sqrtf(cX*cX + cY*cY + cZ*cZ);
	//normalize
	fX /= rayLength;
	fY /= rayLength;
	fZ /= rayLength;
	
	//T = [planeNormal.(pointOnPlane - rayOrigin)]/planeNormal.rayDirection;
	//pointInPlane = rayOrigin + (rayDirection * T);
	
	float dot1, dot2;
	
	float pointInPlaneX = 0;
	float pointInPlaneY = 0;
	float pointInPlaneZ = 0;
	float planeNormalX = 0;
	float planeNormalY = 0;
	float planeNormalZ = -1;
	
	pointInPlaneX -= cX;
	pointInPlaneY -= cY;
	pointInPlaneZ -= cZ;
	
	dot1 = (planeNormalX * pointInPlaneX) + (planeNormalY * pointInPlaneY) + (planeNormalZ * pointInPlaneZ);
	dot2 = (planeNormalX * fX) + (planeNormalY * fY) + (planeNormalZ * fZ);
	
	float t = dot1/dot2;
	
	fX *= t;
	fY *= t;
	//we don't need the z coordinate in my case
	
	//return CGPointMake(fX + cX, fY + cY);
	x0 = fX + cX;
	y0 = fY + cY;
}
 */

/*
void GetRayFromCameraToPixel (float winPosX, float winPosY,
							  GLint viewport[4], GLfloat modelview[16], GLfloat projection[16],
							  float &rx, float &ry, float &rz)
{	
	//opengl 0,0 is at the bottom not at the top
	winPosY = (float)viewport[3] - winPosY;
	//we cannot do the follo4wing in openGL ES due to tile rendering
	// glReadPixels( (int)winPos.x(), (int)winPos.y(), 1, 1, GL_DEPTH_COMPONENT24_OES, GL_FLOAT, &winZ );
	
	// obtenemos los puntos de corte en los planos znear y zfar
	float cX, cY, cZ, fX, fY, fZ;
	//gives us camera position (near plan)
	gluUnProject( winPosX, winPosY, 0, modelview, projection, viewport, &cX, &cY, &cZ);
	//far plane
	gluUnProject( winPosX, winPosY, 1, modelview, projection, viewport, &fX, &fY, &fZ);
		
	// devolvemos los vectores
	rx = fX-cX;
	ry = fY-cY;
	rz = fZ-cZ;
}
*/

inline void Glu::MultMatrix(double M[16], double N[16]) {
    // set N = M * N
    double I[16];
    //memcpy (I, N, 16*sizeof(double));
    for (int i = 0; i < 16; i++) {
        I[i] = N[i];
    }

    for (int j = 0; j < 4; j++)
        for (int i = 0; i < 4; i++) {
            N[j * 4 + i] = 0;
            for (int k = 0; k < 4; k++) N[j * 4 + i] += M[j * 4 + k] * I[k * 4 + i];
        }
}


void Glu::ComputeTranslationMatrix(Vector3D t, double M[16]) {
    // INCREDIBLE!! glTranslated FUNCTION DOES NOT WORK PROPERLY!!!
    double T[16] = {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            t.x(), t.y(), t.z(), 1};
    MultMatrix(T, M);
}


void Glu::ComputeRotationMatrix(double angle_rad, Vector3D p, double M[16]) {
    // INCREDIBLE!! glRotated FUNCTION DOES NOT WORK PROPERLY!!!
    //double mod0 = sqrt(x*x+y*y+z*z);
    //double x0=x/mod0, y0=y/mod0, z0=z/mod0;
    Vector3D p0 = p.normalized();
    double c = cos(angle_rad), s = sin(angle_rad);
    /*
     double R[16] = {	x0*x0*(1-c)+c,		x0*y0*(1-c)+z0*s,		x0*z0*(1-c)-y0*s,		0,
                         y0*x0*(1-c)-z0*s,	y0*y0*(1-c)+c,			y0*z0*(1-c)+x0*s,		0,
                         x0*z0*(1-c)+y0*s,	y0*z0*(1-c)-x0*s,		z0*z0*(1-c)+c,			0,
                         0,					0,						0,						1};*/
    double R[16] = {p0.x() * p0.x() * (1 - c) + c, p0.x() * p0.y() * (1 - c) + p0.z() * s, p0.x() * p0.z() * (1 - c) - p0.y() * s, 0,
            p0.y() * p0.x() * (1 - c) - p0.z() * s, p0.y() * p0.y() * (1 - c) + c, p0.y() * p0.z() * (1 - c) + p0.x() * s, 0,
            p0.x() * p0.z() * (1 - c) + p0.y() * s, p0.y() * p0.z() * (1 - c) - p0.x() * s, p0.z() * p0.z() * (1 - c) + c, 0,
            0, 0, 0, 1};
    MultMatrix(R, M);
}


void Glu::ComputeLookAtMatrix(Vector3D pos, Vector3D center, Vector3D up, double M[16]) {
    //double wx=center.x()-pos.x(), wy=center.y()-pos.y(), wz=center.z()-pos.z();
    Vector3D w = center.sub(pos).normalized();
    //double mod=sqrt(wx*wx+wy*wy+wz*wz);
    //wx/=mod; wy/=mod; wz/=mod;
    //double pe=wx*upx+wy*upy+wz*upz;
    double pe = w.dot(up);
    //double vx=upx-pe*wx, vy=upy-pe*wy, vz=upz-pe*wz;
    Vector3D v = up.sub(w.times(pe)).normalized();
    //mod=sqrt(vx*vx+vy*vy+vz*vz);
    //vx/=mod; vy/=mod; vz/=mod;
    //double ux=wy*vz-wz*vy, uy=wz*vx-wx*vz, uz=wx*vy-wy*vx;
    Vector3D u = w.cross(v);
    double LA[16] = {
            u.x(), v.x(), -w.x(), 0,
            u.y(), v.y(), -w.y(), 0,
            u.z(), v.z(), -w.z(), 0,
//		-pos.x()*ux-pos.y()*uy-pos.z()*uz, -pos.x()*vx-pos.y()*vy-pos.z()*vz, pos.x()*wx+pos.y()*wy+pos.z()*wz, 1};
            -pos.dot(u), -pos.dot(v), pos.dot(w), 1};
    for (int i = 0; i < 16; i++) {M[i] = LA[i];}
}


extern bool DEBUG_PRINT;
// #include "stdio.h"






void Glu::ComputeProjectionMatrix(double left, double right, double bottom, double top,
        double near, double far, float projection[]) {
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

    // convert matrix from double to float
    for (int n = 0; n < 16; n++) projection[n] = (float) P[n];

    /*
     if (DEBUG_PRINT) {
        int n=0;
        iprintf ("Matrix de proyección (near=%f  far=%f):\n", near, far);
        for (int j=0; j<4; j++) {
            for (int i=0; i<4; i++, n++)
                iprintf ("%f  ", projection[n]);
            iprintf ("\n");
        }
            
        DEBUG_PRINT = false;
    }*/


}



