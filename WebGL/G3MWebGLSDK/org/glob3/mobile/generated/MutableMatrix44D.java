package org.glob3.mobile.generated; 
//
//  Mat4.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  Mat4.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector3D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2D;
//class Angle;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MutableVector3D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class FrustumData;



// class to keep a 4x4 matrix
public class MutableMatrix44D
{
  private double[] _m = new double[16];


  /*
   * Compute inverse of 4x4 transformation matrix.
   * Code contributed by Jacques Leroy jle@star.be
   * Return GL_TRUE for success, GL_FALSE for failure (singular matrix)
   */
  private static boolean invert_matrix(double[] m, double[] out)
  {
	//NEW METHOD FOR ENHANCING JAVA TRANSLATION
	double[] inv = new double[16];
	double det;
	int i;
  
	inv[0] = m[5] * m[10] * m[15] - m[5] * m[11] * m[14] - m[9] * m[6] * m[15] + m[9] * m[7] * m[14] + m[13] * m[6] * m[11] - m[13] * m[7] * m[10];
	inv[4] = -m[4] * m[10] * m[15] + m[4] * m[11] * m[14] + m[8] * m[6] * m[15] - m[8] * m[7] * m[14] - m[12] * m[6] * m[11] + m[12] * m[7] * m[10];
	inv[8] = m[4] * m[9] * m[15] - m[4] * m[11] * m[13] - m[8] * m[5] * m[15] + m[8] * m[7] * m[13] + m[12] * m[5] * m[11] - m[12] * m[7] * m[9];
	inv[12] = -m[4] * m[9] * m[14] + m[4] * m[10] * m[13] + m[8] * m[5] * m[14] - m[8] * m[6] * m[13] - m[12] * m[5] * m[10] + m[12] * m[6] * m[9];
	inv[1] = -m[1] * m[10] * m[15] + m[1] * m[11] * m[14] + m[9] * m[2] * m[15] - m[9] * m[3] * m[14] - m[13] * m[2] * m[11] + m[13] * m[3] * m[10];
	inv[5] = m[0] * m[10] * m[15] - m[0] * m[11] * m[14] - m[8] * m[2] * m[15] + m[8] * m[3] * m[14] + m[12] * m[2] * m[11] - m[12] * m[3] * m[10];
	inv[9] = -m[0] * m[9] * m[15] + m[0] * m[11] * m[13] + m[8] * m[1] * m[15] - m[8] * m[3] * m[13] - m[12] * m[1] * m[11] + m[12] * m[3] * m[9];
	inv[13] = m[0] * m[9] * m[14] - m[0] * m[10] * m[13] - m[8] * m[1] * m[14] + m[8] * m[2] * m[13] + m[12] * m[1] * m[10] - m[12] * m[2] * m[9];
	inv[2] = m[1] * m[6] * m[15] - m[1] * m[7] * m[14] - m[5] * m[2] * m[15] + m[5] * m[3] * m[14] + m[13] * m[2] * m[7] - m[13] * m[3] * m[6];
	inv[6] = -m[0] * m[6] * m[15] + m[0] * m[7] * m[14] + m[4] * m[2] * m[15] - m[4] * m[3] * m[14] - m[12] * m[2] * m[7] + m[12] * m[3] * m[6];
	inv[10] = m[0] * m[5] * m[15] - m[0] * m[7] * m[13] - m[4] * m[1] * m[15] + m[4] * m[3] * m[13] + m[12] * m[1] * m[7] - m[12] * m[3] * m[5];
	inv[14] = -m[0] * m[5] * m[14] + m[0] * m[6] * m[13] + m[4] * m[1] * m[14] - m[4] * m[2] * m[13] - m[12] * m[1] * m[6] + m[12] * m[2] * m[5];
	inv[3] = -m[1] * m[6] * m[11] + m[1] * m[7] * m[10] + m[5] * m[2] * m[11] - m[5] * m[3] * m[10] - m[9] * m[2] * m[7] + m[9] * m[3] * m[6];
	inv[7] = m[0] * m[6] * m[11] - m[0] * m[7] * m[10] - m[4] * m[2] * m[11] + m[4] * m[3] * m[10] + m[8] * m[2] * m[7] - m[8] * m[3] * m[6];
	inv[11] = -m[0] * m[5] * m[11] + m[0] * m[7] * m[9] + m[4] * m[1] * m[11] - m[4] * m[3] * m[9] - m[8] * m[1] * m[7] + m[8] * m[3] * m[5];
	inv[15] = m[0] * m[5] * m[10] - m[0] * m[6] * m[9] - m[4] * m[1] * m[10] + m[4] * m[2] * m[9] + m[8] * m[1] * m[6] - m[8] * m[2] * m[5];
  
	det = m[0] * inv[0] + m[1] * inv[4] + m[2] * inv[8] + m[3] * inv[12];
	if (det == 0)
	  return false;
  
	det = 1.0 / det;
  
	for (i = 0; i < 16; i++)
	  out[i] = inv[i] * det;
  
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void transformPoint(double out[4], const double in[4]) const
  private void transformPoint(double[] out, double[] in)
  {
//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
//#define M(row,col) _m[col*4+row]
	out[0] = _m[0 *4+0] * in[0] + _m[1 *4+0] * in[1] + _m[2 *4+0] * in[2] + _m[3 *4+0] * in[3];
	out[1] = _m[0 *4+1] * in[0] + _m[1 *4+1] * in[1] + _m[2 *4+1] * in[2] + _m[3 *4+1] * in[3];
	out[2] = _m[0 *4+2] * in[0] + _m[1 *4+2] * in[1] + _m[2 *4+2] * in[2] + _m[3 *4+2] * in[3];
	out[3] = _m[0 *4+3] * in[0] + _m[1 *4+3] * in[1] + _m[2 *4+3] * in[2] + _m[3 *4+3] * in[3];
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#undef M
  }



  public static MutableMatrix44D identity()
  {
	float[] I = { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 };
	return new MutableMatrix44D(I);
  }

  public static MutableMatrix44D fromRotationX(Angle angle)
  {
	double c = angle.cosinus();
	double s = angle.sinus();
	return new MutableMatrix44D(1.0, 0.0, 0.0, 0.0, 0.0, c, -s, 0.0, 0.0, s, c, 0.0, 0.0, 0.0, 0.0, 1.0);

  }

  public static MutableMatrix44D fromRotationY(Angle angle)
  {
	double c = angle.cosinus();
	double s = angle.sinus();
	return new MutableMatrix44D(c, 0.0, s, 0.0, 0.0, 1.0, 0.0, 0.0, -s, 0.0, c, 0.0, 0.0, 0.0, 0.0, 1.0);

  }

  public static MutableMatrix44D fromRotationZ(Angle angle)
  {
	double c = angle.cosinus();
	double s = angle.sinus();
	return new MutableMatrix44D(c, -s, 0.0, 0.0, s, c, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0);
  }

  public static MutableMatrix44D fromTranslation(MutableVector3D translation)
  {
	return new MutableMatrix44D(1.0, 0.0, 0.0, translation.x(), 0.0, 1.0, 0.0, translation.y(), 0.0, 0.0, 1.0, translation.z(), 0.0, 0.0, 0.0, 1.0);
  }

  public static MutableMatrix44D fromScale(MutableVector3D scale)
  {
	return new MutableMatrix44D(scale.x(), 0.0, 0.0, 0, 0.0, scale.y(), 0.0, 0, 0.0, 0.0, scale.z(), 0, 0.0, 0.0, 0.0, 1.0);
  
  }

  public MutableMatrix44D()
  {
	for (int i = 0; i < 16; i++)
	{
	  _m[i] = 0.0;
	}
  }

  public MutableMatrix44D(MutableMatrix44D m)
  {
	for (int i = 0; i < 16; i++)
	{
	  _m[i] = m._m[i];
	}
  }

  public MutableMatrix44D(double[] M)
  {
	for (int i = 0; i < 16; i++)
	{
	  _m[i] = M[i];
	}
  }

  public MutableMatrix44D(float[] M)
  {
	for (int i = 0; i < 16; i++)
	{
	  _m[i] = M[i];
	}
  }

  public MutableMatrix44D(double m11, double m12, double m13, double m14, double m21, double m22, double m23, double m24, double m31, double m32, double m33, double m34, double m41, double m42, double m43, double m44)
  {
	_m[0] = m11;
	_m[1] = m12;
	_m[2] = m13;
	_m[3] = m14;

	_m[4] = m21;
	_m[5] = m22;
	_m[6] = m23;
	_m[7] = m24;

	_m[8] = m31;
	_m[9] = m32;
	_m[10] = m33;
	_m[11] = m34;

	_m[12] = m41;
	_m[13] = m42;
	_m[14] = m43;
	_m[15] = m44;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D multiply(const MutableMatrix44D& m) const
  public final MutableMatrix44D multiply(MutableMatrix44D m)
  {
	double[] R = new double[16];
	for (int j = 0; j < 4; j++)
	{
	  final int j4 = j *4;
	  for (int i = 0; i < 4; i++)
	  {
  //      R[j * 4 + i] = m.get(j * 4) * _m[i] + m.get(j * 4 + 1) * _m[4 + i] + m.get(j * 4 + 2) * _m[8 + i] + m.get(j * 4 + 3) * _m[12 + i];
		R[j4 + i] = m._m[j4] * _m[i] + m._m[j4 + 1] * _m[4 + i] + m._m[j4 + 2] * _m[8 + i] + m._m[j4 + 3] * _m[12 + i];
	  }
	}
  
	return new MutableMatrix44D(R);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D inversed() const
  public final MutableMatrix44D inversed()
  {
	double[] out = new double[16];
	invert_matrix(_m, out);
  
	return new MutableMatrix44D(out);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D transposed() const
  public final MutableMatrix44D transposed()
  {
	MutableMatrix44D T = new MutableMatrix44D(_m[0], _m[4], _m[8], _m[12], _m[1], _m[5], _m[9], _m[13], _m[2], _m[6], _m[10], _m[14], _m[3], _m[7], _m[11], _m[15]);
	return T;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double get(int i) const
  public final double get(int i)
  {
	return _m[i];
  }

  //const double * getMatrix() const { return _m;}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void copyToFloatMatrix(float M[16]) const
  public final void copyToFloatMatrix(float[] M)
  {
//    for (int i = 0; i < 16; i++) {
//      M[i] = (float) _m[i];
//    }
	M[0] = (float) _m[0];
	M[1] = (float) _m[1];
	M[2] = (float) _m[2];
	M[3] = (float) _m[3];
	M[4] = (float) _m[4];
	M[5] = (float) _m[5];
	M[6] = (float) _m[6];
	M[7] = (float) _m[7];
	M[8] = (float) _m[8];
	M[9] = (float) _m[9];
	M[10] = (float) _m[10];
	M[11] = (float) _m[11];
	M[12] = (float) _m[12];
	M[13] = (float) _m[13];
	M[14] = (float) _m[14];
	M[15] = (float) _m[15];
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void print(const String& name, const ILogger* log) const
  public final void print(String name, ILogger log)
  {
	log.logInfo("MATRIX %s:\n", name);
	for (int j = 0; j < 4; j++)
	{
	  log.logInfo("%.2f  %.2f %.2f %.2f\n", _m[j * 4], _m[j * 4 + 1],_m[j * 4 + 2], _m[j * 4 + 3]);
	}
	log.logInfo("\n");
  }


  /*
   This function is intended to be used on a ModelView matrix. ModelView = Projection * Model
   */
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D unproject(const Vector3D& pixel3D, const int viewport[4]) const
  public final Vector3D unproject(Vector3D pixel3D, int[] viewport)
  {
  
	final double winx = pixel3D.x();
	final double winy = pixel3D.y();
	final double winz = pixel3D.z();
  
	/* matrice de transformation */
	double[] in = new double[4];
	double[] out = new double[4];
  
	/* transformation coordonnees normalisees entre -1 et 1 */
	in[0] = (winx - viewport[0]) * 2 / viewport[2] - 1.0;
	in[1] = (winy - viewport[1]) * 2 / viewport[3] - 1.0;
	in[2] = 2 * winz - 1.0;
	in[3] = 1.0;
  
	/* calcul transformation inverse */
	MutableMatrix44D m = this.inversed();
  
	/* d'ou les coordonnees objets */
	m.transformPoint(out, in);
	if (out[3] == 0.0)
	  return Vector3D.nan();
  
	final double objx = out[0] / out[3];
	final double objy = out[1] / out[3];
	final double objz = out[2] / out[3];
  
	return new Vector3D(objx, objy, objz);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D project(const Vector3D& point, const int viewport[4]) const
  public final Vector2D project(Vector3D point, int[] viewport)
  {
	double[] in = new double[4];
	double[] out = new double[4];
  
	in[0] = point.x();
	in[1] = point.y();
	in[2] = point.z();
	in[3] = 1.0f;
	transformPoint(out, in);
	if (out[3] == 0.0f)
	  return Vector2D.nan();
  
	out[0] /= out[3];
	out[1] /= out[3];
	out[2] /= out[3];
  
	double winx = viewport[0] + (1.0f + out[0]) * viewport[2] / 2.0f;
	double winy = viewport[1] + (1.0f + out[1]) * viewport[3] / 2.0f;
	//double winz = (1.0f + in[2]) / 2.0f;
	return new Vector2D(winx, winy);
  }

  public static MutableMatrix44D createTranslationMatrix(Vector3D t)
  {
	double[] T = { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, t.x(), t.y(), t.z(), 1 };
  
	return new MutableMatrix44D(T);
  }

  public static MutableMatrix44D createRotationMatrix(Angle angle, Vector3D p)
  {
	final Vector3D p0 = p.normalized();
  
	final double c = angle.cosinus();
	final double s = angle.sinus();
  
	double[] R = { p0.x() * p0.x() * (1 - c) + c, p0.x() * p0.y() * (1 - c) + p0.z() * s, p0.x() * p0.z() * (1 - c) - p0.y() * s, 0, p0.y() * p0.x() * (1 - c) - p0.z() * s, p0.y() * p0.y() * (1 - c) + c, p0.y() * p0.z() * (1 - c) + p0.x() * s, 0, p0.x() * p0.z() * (1 - c) + p0.y() * s, p0.y() * p0.z() * (1 - c) - p0.x() * s, p0.z() * p0.z() * (1 - c) + c, 0, 0, 0, 0, 1 };
  
	return new MutableMatrix44D(R);
  }

  public static MutableMatrix44D createGeneralRotationMatrix(Angle angle, Vector3D axis, Vector3D point)
  {
	MutableMatrix44D T1 = MutableMatrix44D.createTranslationMatrix(point.times(-1.0));
	MutableMatrix44D R = MutableMatrix44D.createRotationMatrix(angle, axis);
	MutableMatrix44D T2 = MutableMatrix44D.createTranslationMatrix(point);
	return T2.multiply(R).multiply(T1);
  }

  public static MutableMatrix44D createModelMatrix(MutableVector3D pos, MutableVector3D center, MutableVector3D up)
  {
	final MutableVector3D w = center.sub(pos).normalized();
	final double pe = w.dot(up);
	final MutableVector3D v = up.sub(w.times(pe)).normalized();
	final MutableVector3D u = w.cross(v);
	double[] LA = { u.x(), v.x(), -w.x(), 0, u.y(), v.y(), -w.y(), 0, u.z(), v.z(), -w.z(), 0, -pos.dot(u), -pos.dot(v), pos.dot(w), 1 };
	return new MutableMatrix44D(LA);
  }

  public static MutableMatrix44D createProjectionMatrix(double left, double right, double bottom, double top, double znear, double zfar)
  {
	// set frustum matrix in double
	final double rl = right - left;
	final double tb = top - bottom;
	final double fn = zfar - znear;
  
	double[] P = new double[16];
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
  
	return new MutableMatrix44D(P);
  }

  public static MutableMatrix44D createProjectionMatrix(FrustumData data)
  {
	return createProjectionMatrix(data._left, data._right, data._bottom, data._top, data._znear, data._zfar);
  }

  public static MutableMatrix44D createOrthographicProjectionMatrix(double left, double right, double bottom, double top, double znear, double zfar)
  {
	// set frustum matrix in double
	final double rl = right - left;
	final double tb = top - bottom;
	final double fn = zfar - znear;

	double[] P = new double[16];
	P[0] = 2 / rl;
	P[1] = P[2] = P[3] = P[4] = 0;
	P[5] = 2 / tb;
	P[6] = P[7] = P[8] = P[9] = 0;
	P[10] = -2 / fn;
	P[11] = 0;
	P[12] = -(right+left) / rl;
	P[13] = -(top+bottom) / tb;
	P[14] = -(zfar+znear) / fn;
	P[15] = 1;

	return new MutableMatrix44D(P);
  }


}