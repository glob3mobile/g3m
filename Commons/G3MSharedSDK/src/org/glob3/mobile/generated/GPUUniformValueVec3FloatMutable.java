package org.glob3.mobile.generated; 
public class GPUUniformValueVec3FloatMutable extends GPUUniformValueVec3Float
{
  public void dispose()
  {
    super.dispose();
  }

  private boolean _hasChangedSinceLastSetUniform;


  public GPUUniformValueVec3FloatMutable(float x, float y, float z)
  {
     super(x, y, z);
     _hasChangedSinceLastSetUniform = true;
  }

  public final void changeValue(float x, float y, float z)
  {
    if (x != _x || y != _y || z != _z)
    {
      _x = x;
      _y = y;
      _z = z;
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