package org.glob3.mobile.generated; 
public class GPUUniformValueFloat extends GPUUniformValue
{
  public final float _value;

  public GPUUniformValueFloat(float d)
  {
     super(GLType.glFloat());
     _value = d;
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    gl.uniform1f(id, _value);
  }
  public final boolean isEqualsTo(GPUUniformValue v)
  {
    GPUUniformValueFloat v2 = (GPUUniformValueFloat)v;
    return _value == v2._value;
  }

  //  GPUUniformValue* copyOrCreate(GPUUniformValue* value) const {
  //    if (value != NULL){
  //      delete value;
  //    }
  //    return new GPUUniformValueFloat(_value);
  //  }

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