package org.glob3.mobile.generated; 
////////////////////////////////////////////////////////////////////////
public class GPUUniformValueBool extends GPUUniformValue
{
  public boolean _value;

  public GPUUniformValueBool(boolean b)
  {
     super(GLType.glBool());
     _value = b;
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    _value? gl.uniform1i(id, 1) : gl.uniform1i(id, 0);
  }
  public final boolean isEqualsTo(GPUUniformValue v)
  {
    return _value == ((GPUUniformValueBool)v)._value;
  }

  public final GPUUniformValue copyOrCreate(GPUUniformValue value)
  {
    if (value == null)
    {
      return new GPUUniformValueBool(_value);
    }
    else
    {
      ((GPUUniformValueBool)value)._value = _value;
      return value;
    }
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("Uniform Value Boolean: ");
    isb.addBool(_value);
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
}