package org.glob3.mobile.generated; 
public class GPUUniformValueMatrix4FloatTransform extends GPUUniformValue
{

  private GPUUniformValueMatrix4FloatTransform(GPUUniformValueMatrix4FloatTransform that)
  {
     super(GLType.glMatrix4Float());
     _m = new MutableMatrix44D(new MutableMatrix44D(that._m));
     _isTransform = that._isTransform;
  }

  public MutableMatrix44D _m = new MutableMatrix44D();
  public boolean _isTransform;

  public GPUUniformValueMatrix4FloatTransform(MutableMatrix44D m, boolean isTransform) //, _transformedMatrix(m)
{
     super(GLType.glMatrix4Float());
     _m = new MutableMatrix44D(new MutableMatrix44D(m));
     _isTransform = isTransform;
}

  public final void setUniform(GL gl, IGLUniformID id)
  {
    gl.uniformMatrix4fv(id, false, _m.asMatrix44D());
  }

  public final boolean isEqualsTo(GPUUniformValue v)
  {
    GPUUniformValueMatrix4FloatTransform v2 = (GPUUniformValueMatrix4FloatTransform)v;
    return _m.isEqualsTo(v2._m);
  }

  public final GPUUniformValue copyOrCreate(GPUUniformValue value)
  {
    if (value == null)
    {
      return new GPUUniformValueMatrix4FloatTransform(_m, _isTransform);
    }
    else
    {
      GPUUniformValueMatrix4FloatTransform valueM = (GPUUniformValueMatrix4FloatTransform)value;
      if (_isTransform){
        valueM._m = valueM._m.multiply(_m);
      } else {
        valueM._m = new MutableMatrix44D(_m);
      }
      return value;
    }
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
    //    return &_transformedMatrix;
    return _m;
  }
}