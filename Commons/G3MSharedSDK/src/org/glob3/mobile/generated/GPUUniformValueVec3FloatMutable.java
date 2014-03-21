package org.glob3.mobile.generated; 
public class GPUUniformValueVec3FloatMutable extends GPUUniformValueVec3Float
{
  public void dispose()
  {
    super.dispose();
  }


  public GPUUniformValueVec3FloatMutable(float x, float y, float z)
  {
     super(x,y,z);
  }

  public final void changeValue(float x, float y, float z)
  {
    _x = x;
    _y = y;
    _z = z;
  }
}