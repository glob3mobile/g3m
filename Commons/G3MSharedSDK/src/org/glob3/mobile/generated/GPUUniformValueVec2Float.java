package org.glob3.mobile.generated; 
public class GPUUniformValueVec2Float extends GPUUniformValue
{
  public double _x;
  public double _y;

  public GPUUniformValueVec2Float(double x, double y)
  {
     super(GLType.glVec2Float());
     _x = x;
     _y = y;
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

  public final GPUUniformValue copyOrCreate(GPUUniformValue value)
  {
    if (value == null)
    {
      return new GPUUniformValueVec2Float(_x, _y);
    }
    else
    {
      ((GPUUniformValueVec2Float)value)._x = _x;
      ((GPUUniformValueVec2Float)value)._y = _y;
      return value;
    }
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("Uniform Value Vec2Float: x:");
    isb.addDouble(_x);
    isb.addString("y:");
    isb.addDouble(_y);
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
}