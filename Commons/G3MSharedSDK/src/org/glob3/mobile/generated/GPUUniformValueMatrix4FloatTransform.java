package org.glob3.mobile.generated; 
////////////////////////////////////////////////////////////////////////
public class GPUUniformValueMatrix4FloatTransform extends GPUUniformValue
{

  private GPUUniformValueMatrix4FloatTransform(GPUUniformValueMatrix4FloatTransform that)
  {
     super(GLType.glMatrix4Float());
     _m = new MutableMatrix44D(that._m);
     _isTransform = that._isTransform;
     _transformedMatrix = new MutableMatrix44D(new MutableMatrix44D(that._transformedMatrix));
  }

  public MutableMatrix44D _m;

  public MutableMatrix44D _transformedMatrix = new MutableMatrix44D();

  public boolean _isTransform;

  public GPUUniformValueMatrix4FloatTransform(MutableMatrix44D m, boolean isTransform)
  {
     super(GLType.glMatrix4Float());
     _m = m;
     _isTransform = isTransform;
     _transformedMatrix = new MutableMatrix44D(m);
  }

  public final void setLastGPUUniformValue(GPUUniformValue old)
  {
    if (_isTransform)
    {
      if (old == null)
      {
        ILogger.instance().logError("Trying to apply transformation to matrix without previous value");
      }
      else
      {
        final MutableMatrix44D lastM = ((GPUUniformValueMatrix4FloatTransform)old).getValue();
        _transformedMatrix.copyValue(lastM.multiply(_m));
      }
    }
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    gl.uniformMatrix4fv(id, false, _transformedMatrix);
  }

  public final boolean isEqualsTo(GPUUniformValue v)
  {
    GPUUniformValueMatrix4FloatTransform v2 = (GPUUniformValueMatrix4FloatTransform)v;
    return _transformedMatrix.isEqualsTo(v2._transformedMatrix);
  }
  public final GPUUniformValue deepCopy()
  {
    return new GPUUniformValueMatrix4FloatTransform(this);
  }

  public final void copyFrom(GPUUniformValue v)
  {
    _m.copyValue(((GPUUniformValueMatrix4FloatTransform)v)._m);
    _transformedMatrix.copyValue(((GPUUniformValueMatrix4FloatTransform)v)._transformedMatrix);
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
     return _transformedMatrix;
  }
}