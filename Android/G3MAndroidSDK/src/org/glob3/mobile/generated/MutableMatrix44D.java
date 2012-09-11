package org.glob3.mobile.generated; 
//
//  MutableMatrix44D.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 05/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  MutableMatrix44D.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 05/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class FrustumData;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector3D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MutableVector3D;





public class MutableMatrix44D
{


  //_m23 -> row 2, column 3
  private double _m00;
  private double _m01;
  private double _m02;
  private double _m03;
  private double _m10;
  private double _m11;
  private double _m12;
  private double _m13;
  private double _m20;
  private double _m21;
  private double _m22;
  private double _m23;
  private double _m30;
  private double _m31;
  private double _m32;
  private double _m33;

  private float[] _columnMajorFloatArray;
  private boolean _isValid;

  //Contructor parameters in column major order
  private MutableMatrix44D(double m00, double m10, double m20, double m30, double m01, double m11, double m21, double m31, double m02, double m12, double m22, double m32, double m03, double m13, double m23, double m33)
  {
	  _isValid = true;
	_m00 = m00;
	_m01 = m01;
	_m02 = m02;
	_m03 = m03;

	_m10 = m10;
	_m11 = m11;
	_m12 = m12;
	_m13 = m13;

	_m20 = m20;
	_m21 = m21;
	_m22 = m22;
	_m23 = m23;

	_m30 = m30;
	_m31 = m31;
	_m32 = m32;
	_m33 = m33;

	_columnMajorFloatArray = null;
  }

  private MutableMatrix44D(boolean isValid)
  {
	  _isValid = isValid;
	_columnMajorFloatArray = null;
  }


  //CONTRUCTORS
  public MutableMatrix44D()
  {
	  _isValid = true;
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

	_columnMajorFloatArray = null;
  }

  public MutableMatrix44D(MutableMatrix44D m)
  {
	  _isValid = m._isValid;
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

	_columnMajorFloatArray = null;
  }

//C++ TO JAVA CONVERTER NOTE: This 'copyFrom' method was converted from the original C++ copy assignment operator:
//ORIGINAL LINE: MutableMatrix44D& operator =(const MutableMatrix44D &m)
  public final MutableMatrix44D copyFrom(MutableMatrix44D m)
  {
	if (this != m)
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

	  _isValid = m._isValid;

	  if (_columnMajorFloatArray != null)
	  {
		_columnMajorFloatArray = null;
		_columnMajorFloatArray = null;
	  }
	}

	return this;
  }

  public void dispose()
  {
	if (_columnMajorFloatArray != null)
	{
	  _columnMajorFloatArray = null;
	}
  }

  //SPECIAL MATRICES

  public static MutableMatrix44D identity()
  {
	return new MutableMatrix44D(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }

  public static MutableMatrix44D invalid()
  {
	return new MutableMatrix44D(false);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isValid() const
  public final boolean isValid()
  {
	return _isValid;
  }

  //
  //OPERATIONS

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D multiply(const MutableMatrix44D &that) const
  public final MutableMatrix44D multiply(MutableMatrix44D that)
  {
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
  
	return new MutableMatrix44D(m00, m10, m20, m30, m01, m11, m21, m31, m02, m12, m22, m32, m03, m13, m23, m33);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D inversed() const
  public final MutableMatrix44D inversed()
  {
  
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
  
	if (determinant == 0.0)
	{
	  return MutableMatrix44D.invalid();
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
  
	return new MutableMatrix44D(m00, m10, m20, m30, m01, m11, m21, m31, m02, m12, m22, m32, m03, m13, m23, m33);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D transposed() const
  public final MutableMatrix44D transposed()
  {
	return new MutableMatrix44D(_m00, _m01, _m02, _m03, _m10, _m11, _m12, _m13, _m20, _m21, _m22, _m23, _m30, _m31, _m32, _m33);
  }

  //METHODS TO EXTRACT VALUES FROM THE MATRIX

  //Returns values from 0..15 in column mayor order
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double get(int i) const
  public final double get(int i)
  {
	switch (i)
	{
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
		ILogger.instance().logError("Accesing MutableMutableMatrix44D44D out of index");
		return 0;
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float[] getColumnMajorFloatArray() const
  public final float[] getColumnMajorFloatArray()
  {
	if (_columnMajorFloatArray == null)
	{
	  _columnMajorFloatArray = new float[16];

	  _columnMajorFloatArray[0] = (float) _m00;
	  _columnMajorFloatArray[1] = (float) _m10;
	  _columnMajorFloatArray[2] = (float) _m20;
	  _columnMajorFloatArray[3] = (float) _m30;

	  _columnMajorFloatArray[4] = (float) _m01;
	  _columnMajorFloatArray[5] = (float) _m11;
	  _columnMajorFloatArray[6] = (float) _m21;
	  _columnMajorFloatArray[7] = (float) _m31;

	  _columnMajorFloatArray[8] = (float) _m02;
	  _columnMajorFloatArray[9] = (float) _m12;
	  _columnMajorFloatArray[10] = (float) _m22;
	  _columnMajorFloatArray[11] = (float) _m32;

	  _columnMajorFloatArray[12] = (float) _m03;
	  _columnMajorFloatArray[13] = (float) _m13;
	  _columnMajorFloatArray[14] = (float) _m23;
	  _columnMajorFloatArray[15] = (float) _m33;
	}
	return _columnMajorFloatArray;
  }

  //OTHER OPERATIONS

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void print(const String& name, const ILogger* log) const
  public final void print(String name, ILogger log)
  {
	log.logInfo("MutableMatrix44D %s:\n", name);
	log.logInfo("%.2f  %.2f %.2f %.2f\n", _m00, _m01,_m02, _m03);
	log.logInfo("%.2f  %.2f %.2f %.2f\n", _m10, _m11,_m12, _m13);
	log.logInfo("%.2f  %.2f %.2f %.2f\n", _m20, _m21,_m22, _m23);
	log.logInfo("%.2f  %.2f %.2f %.2f\n", _m30, _m31,_m32, _m33);
	log.logInfo("\n");
  }


  /*
   This function is intended to be used on a ModelView MutableMatrix44D. ModelView = Projection * Model
   */
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D unproject(const Vector3D& pixel3D, const int vpLeft, const int vpTop, const int vpWidth, const int vpHeight) const
  public final Vector3D unproject(Vector3D pixel3D, int vpLeft, int vpTop, int vpWidth, int vpHeight)
  {
  
	int TODO_Remove_UNPROJECT; //!!!!
  
	final double winx = pixel3D.x();
	final double winy = pixel3D.y();
	final double winz = pixel3D.z();
  
	final double in0 = (winx - vpLeft) * 2 / vpWidth - 1.0;
	final double in1 = (winy - vpTop) * 2 / vpHeight - 1.0;
	final double in2 = 2 * winz - 1.0;
	final double in3 = 1.0;
  
	//Inverse
	MutableMatrix44D m = inversed();
  
	//To object coordinates
  
	//Transformating point
	final double out0 = m._m00 * in0 + m._m01 * in1 + m._m02 * in2 + m._m03 * in3;
	final double out1 = m._m10 * in0 + m._m11 * in1 + m._m12 * in2 + m._m13 * in3;
	final double out2 = m._m20 * in0 + m._m21 * in1 + m._m22 * in2 + m._m23 * in3;
	final double out3 = m._m30 * in0 + m._m31 * in1 + m._m32 * in2 + m._m33 * in3;
  
	if (out3 == 0.0)
	{
	  return Vector3D.nan();
	}
  
	final double objx = out0 / out3;
	final double objy = out1 / out3;
	final double objz = out2 / out3;
  
	return new Vector3D(objx, objy, objz);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D project(const Vector3D& point, const int vpLeft, const int vpTop, const int vpWidth, const int vpHeight) const
  public final Vector2D project(Vector3D point, int vpLeft, int vpTop, int vpWidth, int vpHeight)
  {
	final double in0 = point.x();
	final double in1 = point.y();
	final double in2 = point.z();
	final double in3 = 1.0;
  
	//Transformating point
	double out0 = _m00 * in0 + _m01 * in1 + _m02 * in2 + _m03 * in3;
	double out1 = _m10 * in0 + _m11 * in1 + _m12 * in2 + _m13 * in3;
	//double out2 = _m20 * in0 + _m21 * in1 + _m22 * in2 + _m23 * in3;
	final double out3 = _m30 * in0 + _m31 * in1 + _m32 * in2 + _m33 * in3;
  
	if (out3 == 0.0)
	{
	  return Vector2D.nan();
	}
  
	out0 /= out3;
	out1 /= out3;
	//out2 /= out3;
  
	final double winx = vpLeft + (1.0 + out0) * vpWidth / 2.0;
	final double winy = vpTop + (1.0 + out1) * vpHeight / 2.0;
	//double winz = (1.0 + in2) / 2.0;
	return new Vector2D(winx, winy);
  }

  public static MutableMatrix44D createTranslationMatrix(Vector3D t)
  {
	return new MutableMatrix44D(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, t.x(), t.y(), t.z(), 1);
  
  }

  public static MutableMatrix44D createRotationMatrix(Angle angle, Vector3D p)
  {
	final Vector3D p0 = p.normalized();
  
	final double c = angle.cosinus();
	final double s = angle.sinus();
  
	return new MutableMatrix44D(p0.x() * p0.x() * (1 - c) + c, p0.x() * p0.y() * (1 - c) + p0.z() * s, p0.x() * p0.z() * (1 - c) - p0.y() * s, 0, p0.y() * p0.x() * (1 - c) - p0.z() * s, p0.y() * p0.y() * (1 - c) + c, p0.y() * p0.z() * (1 - c) + p0.x() * s, 0, p0.x() * p0.z() * (1 - c) + p0.y() * s, p0.y() * p0.z() * (1 - c) - p0.x() * s, p0.z() * p0.z() * (1 - c) + c, 0, 0, 0, 0, 1);
  
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
	return new MutableMatrix44D(u.x(), v.x(), -w.x(), 0, u.y(), v.y(), -w.y(), 0, u.z(), v.z(), -w.z(), 0, -pos.dot(u), -pos.dot(v), pos.dot(w), 1);
  
  }

  public static MutableMatrix44D createProjectionMatrix(double left, double right, double bottom, double top, double znear, double zfar)
  {
	// set frustum MutableMatrix44D in double
	final double rl = right - left;
	final double tb = top - bottom;
	final double fn = zfar - znear;
  
	return new MutableMatrix44D(2 * znear / rl, 0, 0, 0, 0, 2 * znear / tb, 0, 0, (right + left) / rl, (top + bottom) / tb, -(zfar + znear) / fn, -1, 0, 0, -2 * zfar / fn * znear, 0);
  
  }

  public static MutableMatrix44D createProjectionMatrix(FrustumData data)
  {
	return createProjectionMatrix(data._left, data._right, data._bottom, data._top, data._znear, data._zfar);
  }

  public static MutableMatrix44D createOrthographicProjectionMatrix(double left, double right, double bottom, double top, double znear, double zfar)
  {
	// set frustum MutableMatrix44D in double
	final double rl = right - left;
	final double tb = top - bottom;
	final double fn = zfar - znear;
  
	return new MutableMatrix44D(2 / rl, 0, 0, 0, 0, 2 / tb, 0, 0, 0, 0, -2 / fn, 0, -(right+left) / rl, -(top+bottom) / tb, -(zfar+znear) / fn, 1);
  }
}