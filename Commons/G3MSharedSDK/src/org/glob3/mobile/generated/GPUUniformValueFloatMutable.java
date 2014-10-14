package org.glob3.mobile.generated; 
//class GPUUniformValueFloatMutable : public GPUUniformValueFloat {
//private:
//  ~GPUUniformValueFloatMutable() {
///#ifdef JAVA_CODE
//    super.dispose();
///#endif
//  }
//
//public:
//
//  GPUUniformValueFloatMutable(float x):
//  GPUUniformValueFloat(x) {}
//
//  void changeValue(float x) {
//    _value = x;
//  }
//};

public class GPUUniformValueFloatMutable extends GPUUniformValue
{
  public void dispose()
  {
    super.dispose();
  }

  private boolean _hasChangedSinceLastSetUniform;

  public float _value;

  public GPUUniformValueFloatMutable(float d)
  {
     super(GLType.glFloat());
     _value = d;
     _hasChangedSinceLastSetUniform = true;
  }

  public final void changeValue(float x)
  {
    if (x != _value)
    {
      _value = x;
      _hasChangedSinceLastSetUniform = true;
    }
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    gl.uniform1f(id, _value);
    _hasChangedSinceLastSetUniform = false;
  }
  public final boolean isEquals(GPUUniformValue v)
  {

    if (v == this && _hasChangedSinceLastSetUniform)
    {
      return true;
    }

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