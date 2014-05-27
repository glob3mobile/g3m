package org.glob3.mobile.generated; 
public class GPUUniformValueInt extends GPUUniformValue
{
  public void dispose()
  {
    super.dispose();
  }

  public final int _value;

  public GPUUniformValueInt(int b)
  {
     super(GLType.glInt());
     _value = b;
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    gl.uniform1i(id, _value);
  }
  public final boolean isEquals(GPUUniformValue v)
  {
    return _value == ((GPUUniformValueInt)v)._value;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("Uniform Value Integer: ");
    isb.addInt(_value);
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
}