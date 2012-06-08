package org.glob3.mobile.generated; 
/*
 *  GLU.cpp
 *  Terrenos
 *
 *  Created by http: //www.flexicoder.com/blog/index.php/2009/11/iphone-gluproject-and-gluunproject/
 *  Copyright 2010 Universidad de Las Palmas. All rights reserved.
 * 
 *
 */


/*
 *  GLU.h
 *  Terrenos
 *
 *  Created by Agustín Trujillo Pino on 23/11/10.
 *  Copyright 2010 Universidad de Las Palmas. All rights reserved.
 *
 */





public class GLU
{


	/*
	 * Transform a point (column vector) by a 4x4 matrix.  I.e.  out = m * in
	 * Input:  m - the 4x4 matrix
	 *         in - the 4x1 vector
	 * Output:  out - the resulting 4x1 vector.
	 */
	private static void transform_point(double[] out, double[] m, double[] in)
	{
//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
//#define M(row,col) m[col*4+row]
		out[0] = m[0 *4+0] * in[0] + m[1 *4+0] * in[1] + m[2 *4+0] * in[2] + m[3 *4+0] * in[3];
		out[1] = m[0 *4+1] * in[0] + m[1 *4+1] * in[1] + m[2 *4+1] * in[2] + m[3 *4+1] * in[3];
		out[2] = m[0 *4+2] * in[0] + m[1 *4+2] * in[1] + m[2 *4+2] * in[2] + m[3 *4+2] * in[3];
		out[3] = m[0 *4+3] * in[0] + m[1 *4+3] * in[1] + m[2 *4+3] * in[2] + m[3 *4+3] * in[3];
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#undef M
	}


  public static Vector3D unproject(double winx, double winy, double winz, MutableMatrix44D model, MutableMatrix44D proj, int[] viewport)
  {
	  /* matrice de transformation */
	  double[] in = new double[4];
	  double[] out = new double[4];
  
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
		  return null;
	  double objx = out[0] / out[3];
	  double objy = out[1] / out[3];
	  double objz = out[2] / out[3];
	  return new Vector3D(objx, objy, objz);
  }

  public static MutableMatrix44D translationMatrix(Vector3D t)
  {
  
	  double[] T = { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, t.x(), t.y(), t.z(), 1};
  
	MutableMatrix44D res = new MutableMatrix44D(T);
	return res;
  }

  public static MutableMatrix44D rotationMatrix(Angle angle, Vector3D p)
  {
  
	  Vector3D p0 = p.normalized();
	  double c = angle.cosinus();
	  double s = angle.sinus();
  
	  double[] R = {p0.x() * p0.x() * (1 - c) + c, p0.x() * p0.y() * (1 - c) + p0.z() * s, p0.x() * p0.z() * (1 - c) - p0.y() * s, 0, p0.y() * p0.x() * (1 - c) - p0.z() * s, p0.y() * p0.y() * (1 - c) + c, p0.y() * p0.z() * (1 - c) + p0.x() * s, 0, p0.x() * p0.z() * (1 - c) + p0.y() * s, p0.y() * p0.z() * (1 - c) - p0.x() * s, p0.z() * p0.z() * (1 - c) + c, 0, 0, 0, 0, 1};
  
	MutableMatrix44D rot = new MutableMatrix44D(R);
	return rot;
  }

  public static MutableMatrix44D lookAtMatrix(Vector3D pos, Vector3D center, Vector3D up)
  {
  
	  Vector3D w = center.sub(pos).normalized();
	  double pe = w.dot(up);
	  Vector3D v = up.sub(w.times(pe)).normalized();
	  Vector3D u = w.cross(v);
	  double[] LA = { u.x(), v.x(), -w.x(), 0, u.y(), v.y(), -w.y(), 0, u.z(), v.z(), -w.z(), 0, -pos.dot(u), -pos.dot(v), pos.dot(w), 1};
  
	MutableMatrix44D m = new MutableMatrix44D(LA);
  
	return m;
  }

  public static MutableMatrix44D projectionMatrix(double left, double right, double bottom, double top, double near, double far)
  {
	  // set frustum matrix in double
	  double rl = right - left;
	  double tb = top - bottom;
	  double fn = - near;
	  double[] P = new double[16];
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
  
	MutableMatrix44D m = new MutableMatrix44D(P);
	return m;
  }

}