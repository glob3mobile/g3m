package org.glob3.mobile.generated; 
public class GPUUniformValueVec2Float extends GPUUniformValue
{
  public void dispose()
  {
    super.dispose();
  }

  public float _x;
  public float _y;

  public GPUUniformValueVec2Float(float x, float y)
  {
     super(GLType.glVec2Float());
     _x = x;
     _y = y;
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    gl.uniform2f(id, _x, _y);
  }
  public final boolean isEquals(GPUUniformValue v)
  {
    GPUUniformValueVec2Float v2 = (GPUUniformValueVec2Float)v;
    return (_x == v2._x) && (_y == v2._y);
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