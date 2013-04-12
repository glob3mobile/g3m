package org.glob3.mobile.generated; 
////////////////////////////////////////////////////////////////////////
public class GPUUniformValueVec2Float extends GPUUniformValue
{
  public final double _x;
  public final double _y;

  public GPUUniformValueVec2Float(double x, double y)
  {
     _x = x;
     _y = y;
     super(GLType.glVec2Float());
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    gl.uniform2f(id, (float)_x, (float)_y);
  }
  public final boolean isEqualsTo(GPUUniformValue v)
  {
    GPUUniformValueVec2Float v2 = (GPUUniformValueVec2Float)v;
    return (_x == v2._x) && (_y == v2._y);
  }
  public final GPUUniformValue deepCopy()
  {
    return new GPUUniformValueVec2Float(_x, _y);
  }
}