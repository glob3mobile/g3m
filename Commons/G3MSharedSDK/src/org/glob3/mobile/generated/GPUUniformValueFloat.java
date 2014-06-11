package org.glob3.mobile.generated; 
public class GPUUniformValueFloat extends GPUUniformValue
{
  public void dispose()
  {
    super.dispose();
  }

  public float _value;

  public GPUUniformValueFloat(float d)
  {
     super(GLType.glFloat());
     _value = d;
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    gl.uniform1f(id, _value);
  }
  public final boolean isEquals(GPUUniformValue v)
  {
    GPUUniformValueFloat v2 = (GPUUniformValueFloat)v;
    return _value == v2._value;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("Uniform Value Float: ");
    isb.addDouble(_value);
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
}