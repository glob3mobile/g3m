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


// class to keep a 4x4 matrix
public class MutableMatrix44D
{

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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D multMatrix(const MutableMatrix44D& m) const
  public final MutableMatrix44D multMatrix(MutableMatrix44D m)
  {
	double[] R = new double[16];
	for (int j = 0; j < 4; j++)
	  for (int i = 0; i < 4; i++)
		R[j * 4 + i] = m.get(j * 4) * _m[i] + m.get(j * 4 + 1) * _m[4 + i] + m.get(j * 4 + 2) * _m[8 + i] + m.get(j * 4 + 3) * _m[12 + i];
  
	MutableMatrix44D m2 = new MutableMatrix44D(R);
  
	return m2;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D inverse() const
  public final MutableMatrix44D inverse()
  {
	double[] out = new double[16];
	invert_matrix(_m, out);
  
	MutableMatrix44D m = new MutableMatrix44D(out);
	return m;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float get(int i) const
  public final float get(int i)
  {
	  return _m[i];
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const double* getMatrix() const
//C++ TO JAVA CONVERTER WARNING: Java has no equivalent to methods returning pointers to value types:
  public final double getMatrix()
  {
	  return _m;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void copyToFloatMatrix(float M[16]) const
  public final void copyToFloatMatrix(float[] M)
  {
	for (int i = 0; i < 16; i++)
	{
		M[i] = _m[i];
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void print() const
  public final void print()
  {
	for (int j = 0; j < 4; j++)
	  System.out.printf("%.2f  %.2f %.2f %.2f\n", _m[j * 4], _m[j * 4 + 1],_m[j * 4 + 2], _m[j * 4 + 3]);
  
  }

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
}