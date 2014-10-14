package org.glob3.mobile.generated; 
public class GPUUniformValueVec2FloatMutable extends GPUUniformValueVec2Float
{
  public void dispose()
  {
    super.dispose();
  }

  private boolean _hasChangedSinceLastSetUniform;


  public GPUUniformValueVec2FloatMutable(float x, float y)
  {
     super(x, y);
     _hasChangedSinceLastSetUniform = true;
  }

  public final void changeValue(float x, float y)
  {
    if (x != _x || y != _y)
    {
      _x = x;
      _y = y;
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