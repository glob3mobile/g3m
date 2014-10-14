package org.glob3.mobile.generated; 
public class GPUUniformValueFloatMutable extends GPUUniformValueFloat
{
  public void dispose()
  {
    super.dispose();
  }

  private boolean _hasChangedSinceLastSetUniform;


  public GPUUniformValueFloatMutable(float x)
  {
     super(x);
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
    super.setUniform(gl, id);
    _hasChangedSinceLastSetUniform = false;
  }

  public final boolean isEquals(GPUUniformValue v)
  {
    if (_hasChangedSinceLastSetUniform && v == this)
    {
      return false;
    }
    return super.isEquals(v);
  }
}