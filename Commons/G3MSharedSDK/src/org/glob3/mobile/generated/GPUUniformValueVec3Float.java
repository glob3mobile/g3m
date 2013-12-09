package org.glob3.mobile.generated; 
public class GPUUniformValueVec3Float extends GPUUniformValue
{
  protected float _x;
  protected float _y;
  protected float _z;

  public void dispose()
  {
    super.dispose();
  }


  public GPUUniformValueVec3Float(Color color)
  {
     super(GLType.glVec3Float());
     _x = color._red;
     _y = color._green;
     _z = color._blue;
  }

  public GPUUniformValueVec3Float(float x, float y, float z)
  {
     super(GLType.glVec3Float());
     _x = x;
     _y = y;
     _z = z;
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    gl.uniform3f(id, _x, _y, _z);
  }
  public final boolean isEquals(GPUUniformValue v)
  {
    GPUUniformValueVec3Float v2 = (GPUUniformValueVec3Float)v;
    return (_x == v2._x) && (_y == v2._y) && (_z == v2._z);
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("Uniform Value Vec4Float: x:");
    isb.addDouble(_x);
    isb.addString("y:");
    isb.addDouble(_y);
    isb.addString("z:");
    isb.addDouble(_z);
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
}