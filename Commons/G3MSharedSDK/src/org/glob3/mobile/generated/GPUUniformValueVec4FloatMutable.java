package org.glob3.mobile.generated;
public class GPUUniformValueVec4FloatMutable extends GPUUniformValueVec4Float
{
  public void dispose()
  {
    super.dispose();
  }


  public GPUUniformValueVec4FloatMutable(float x, float y, float z, float w)
  {
     super(x, y, z, w);
  }

  public final void changeValue(float x, float y, float z, float w)
  {
    _x = x;
    _y = y;
    _z = z;
    _w = w;
  }
}