package org.glob3.mobile.generated; 
////////////////////////////////////////////////////////////

public class GPUUniformValueVec4Float extends GPUUniformValue
{
  public void dispose()
  {
    super.dispose();
  }

  public final float _x;
  public final float _y;
  public final float _z;
  public final float _w;

  public GPUUniformValueVec4Float(Color color)
  {
     super(GLType.glVec4Float());
     _x = color._red;
     _y = color._green;
     _z = color._blue;
     _w = color._alpha;
  }

  public GPUUniformValueVec4Float(float x, float y, float z, float w)
  {
     super(GLType.glVec4Float());
     _x = x;
     _y = y;
     _z = z;
     _w = w;
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    gl.uniform4f(id, _x, _y, _z, _w);
  }
  public final boolean isEquals(GPUUniformValue v)
  {
    GPUUniformValueVec4Float v2 = (GPUUniformValueVec4Float)v;
    return (_x == v2._x) && (_y == v2._y) && (_z == v2._z) && (_w == v2._w);
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
    isb.addString("w:");
    isb.addDouble(_w);
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
}