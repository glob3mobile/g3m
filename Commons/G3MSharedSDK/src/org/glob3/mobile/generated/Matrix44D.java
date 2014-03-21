package org.glob3.mobile.generated; 
//
//  Matrix44D.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 01/07/13.
//
//

//
//  Matrix44D.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 01/07/13.
//
//




public class Matrix44D extends RCObject
{
  public void dispose()
  {
    _columnMajorFloatArray = null;
    if (_columnMajorFloatBuffer != null)
       _columnMajorFloatBuffer.dispose();
  
    super.dispose();
  }


  //_m23 -> row 2, column 3
  public final double _m00;
  public final double _m01;
  public final double _m02;
  public final double _m03;
  public final double _m10;
  public final double _m11;
  public final double _m12;
  public final double _m13;
  public final double _m20;
  public final double _m21;
  public final double _m22;
  public final double _m23;
  public final double _m30;
  public final double _m31;
  public final double _m32;
  public final double _m33;

  public float[] _columnMajorFloatArray;
  public IFloatBuffer _columnMajorFloatBuffer;

  public Matrix44D(Matrix44D m)
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
    _columnMajorFloatArray = null;
    _columnMajorFloatBuffer = null;
  }

  public Matrix44D(double m00, double m10, double m20, double m30, double m01, double m11, double m21, double m31, double m02, double m12, double m22, double m32, double m03, double m13, double m23, double m33)
  {
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
    _columnMajorFloatBuffer = null;
  }


  public final Matrix44D createMultiplication(Matrix44D that)
  {
  
    final double that00 = that._m00;
    final double that10 = that._m10;
    final double that20 = that._m20;
    final double that30 = that._m30;
  
    final double that01 = that._m01;
    final double that11 = that._m11;
    final double that21 = that._m21;
    final double that31 = that._m31;
  
    final double that02 = that._m02;
    final double that12 = that._m12;
    final double that22 = that._m22;
    final double that32 = that._m32;
  
    final double that03 = that._m03;
    final double that13 = that._m13;
    final double that23 = that._m23;
    final double that33 = that._m33;
  
    //Rows of this X Columns of that
    final double m00 = (_m00 * that00) + (_m01 * that10) + (_m02 * that20) + (_m03 * that30);
    final double m01 = (_m00 * that01) + (_m01 * that11) + (_m02 * that21) + (_m03 * that31);
    final double m02 = (_m00 * that02) + (_m01 * that12) + (_m02 * that22) + (_m03 * that32);
    final double m03 = (_m00 * that03) + (_m01 * that13) + (_m02 * that23) + (_m03 * that33);
  
    final double m10 = (_m10 * that00) + (_m11 * that10) + (_m12 * that20) + (_m13 * that30);
    final double m11 = (_m10 * that01) + (_m11 * that11) + (_m12 * that21) + (_m13 * that31);
    final double m12 = (_m10 * that02) + (_m11 * that12) + (_m12 * that22) + (_m13 * that32);
    final double m13 = (_m10 * that03) + (_m11 * that13) + (_m12 * that23) + (_m13 * that33);
  
    final double m20 = (_m20 * that00) + (_m21 * that10) + (_m22 * that20) + (_m23 * that30);
    final double m21 = (_m20 * that01) + (_m21 * that11) + (_m22 * that21) + (_m23 * that31);
    final double m22 = (_m20 * that02) + (_m21 * that12) + (_m22 * that22) + (_m23 * that32);
    final double m23 = (_m20 * that03) + (_m21 * that13) + (_m22 * that23) + (_m23 * that33);
  
    final double m30 = (_m30 * that00) + (_m31 * that10) + (_m32 * that20) + (_m33 * that30);
    final double m31 = (_m30 * that01) + (_m31 * that11) + (_m32 * that21) + (_m33 * that31);
    final double m32 = (_m30 * that02) + (_m31 * that12) + (_m32 * that22) + (_m33 * that32);
    final double m33 = (_m30 * that03) + (_m31 * that13) + (_m32 * that23) + (_m33 * that33);
  
    return new Matrix44D(m00, m10, m20, m30, m01, m11, m21, m31, m02, m12, m22, m32, m03, m13, m23, m33);
  }

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

  public final IFloatBuffer getColumnMajorFloatBuffer()
  {
    if (_columnMajorFloatBuffer == null)
    {
      _columnMajorFloatBuffer = IFactory.instance().createFloatBuffer((float) _m00, (float) _m10, (float) _m20, (float) _m30, (float) _m01, (float) _m11, (float) _m21, (float) _m31, (float) _m02, (float) _m12, (float) _m22, (float) _m32, (float) _m03, (float) _m13, (float) _m23, (float) _m33);
    }
    return _columnMajorFloatBuffer;
  }

  public final boolean isEquals(Matrix44D m)
  {
    return ((_m00 == m._m00) && (_m01 == m._m01) && (_m02 == m._m02) && (_m03 == m._m03) && (_m10 == m._m10) && (_m11 == m._m11) && (_m12 == m._m12) && (_m13 == m._m13) && (_m20 == m._m20) && (_m21 == m._m21) && (_m22 == m._m22) && (_m23 == m._m23) && (_m30 == m._m30) && (_m31 == m._m31) && (_m32 == m._m32) && (_m33 == m._m33));
  }

  public static Matrix44D createIdentity()
  {
    return new Matrix44D(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }


  public final boolean isScaleMatrix()
  {
  
    return (_m01 == 0 && _m02 == 0 && _m03 == 0 && _m10 == 0 && _m12 == 0 && _m13 == 0 && _m20 == 0 && _m21 == 0 && _m23 == 0 && _m30 == 0 && _m31 == 0 && _m32 == 0 && _m33 == 1.0);
  
  
    //
    //  return MutableMatrix44D(scale._x, 0, 0, 0,
    //                          0, scale._y, 0, 0,
    //                          0, 0, scale._z, 0,
    //                          0, 0, 0, 1);
  
  }

  public final boolean isTranslationMatrix()
  {
  
    return (_m00 == 1 && _m01 == 0 && _m02 == 0 && _m03 == 0 && _m10 == 0 && _m11 == 1 && _m12 == 0 && _m13 == 0 && _m20 == 0 && _m21 == 0 && _m22 == 1 && _m23 == 0 && _m33 == 1);
  
  
  
  //  return MutableMatrix44D(1, 0, 0, 0,
  //                          0, 1, 0, 0,
  //                          0, 0, 1, 0,
  //                          x, y, z, 1);
  
  }

}