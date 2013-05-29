package org.glob3.mobile.generated; 
public class GPUUniformValueMatrix4Float extends GPUUniformValue
{
  public final MutableMatrix44D _m = new MutableMatrix44D();

  public GPUUniformValueMatrix4Float(MutableMatrix44D m)
  {
     _m = new MutableMatrix44D(m);
     super(GLType.glMatrix4Float());
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    gl.uniformMatrix4fv(id, false, _m);
  }

  public final boolean isEqualsTo(GPUUniformValue v)
  {
    GPUUniformValueMatrix4Float v2 = (GPUUniformValueMatrix4Float)v;
    final MutableMatrix44D m = (v2._m);
    return _m.isEqualsTo(m);
  }
  public final GPUUniformValue deepCopy()
  {
    return new GPUUniformValueMatrix4Float(_m);
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("Uniform Value Matrix44D.");
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final MutableMatrix44D getValue()
  {
     return _m;
  }
}