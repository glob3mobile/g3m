package org.glob3.mobile.generated; 
////////////////////////////////////////////////////////////
public class GPUUniformValueVec3Float extends GPUUniformValue
{
  public final float _x;
  public final float _y;
  public final float _z;

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
  public final boolean isEqualsTo(GPUUniformValue v)
  {
    GPUUniformValueVec3Float v2 = (GPUUniformValueVec3Float)v;
    return (_x == v2._x) && (_y == v2._y) && (_z == v2._z);
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
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
}