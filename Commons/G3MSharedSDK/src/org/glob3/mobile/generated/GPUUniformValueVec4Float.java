package org.glob3.mobile.generated; 
public class GPUUniformValueVec4Float extends GPUUniformValue
{
  public final double _x;
  public final double _y;
  public final double _z;
  public final double _w;

  public GPUUniformValueVec4Float(double x, double y, double z, double w)
  {
     super(GLType.glVec4Float());
     _x = x;
     _y = y;
     _z = z;
     _w = w;
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    gl.uniform4f(id, (float)_x, (float)_y, (float)_z, (float)_w);
  }
  public final boolean isEqualsTo(GPUUniformValue v)
  {
    GPUUniformValueVec4Float v2 = (GPUUniformValueVec4Float)v;
    return (_x == v2._x) && (_y == v2._y) && (_z == v2._z) && (_w == v2._w);
  }

//  GPUUniformValue* copyOrCreate(GPUUniformValue* value) const {
//    if (value != NULL){
//      delete value;
//    }
//      return new GPUUniformValueVec4Float(_x,_y,_z,_w);
//  }

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