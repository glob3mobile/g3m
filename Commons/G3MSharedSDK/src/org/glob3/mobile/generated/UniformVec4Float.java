package org.glob3.mobile.generated; 
public class UniformVec4Float extends Uniform
{
  private double _x;
  private double _y;
  private double _z;
  private double _w;
  public UniformVec4Float(String name, IGLUniformID id)
  {
     super(name,id);
  }
  public final void set(GL gl, double x, double y, double z, double w)
  {
    if (x != _x || y != _y || z != _z || w != _w)
    {
      gl.uniform4f(_id, (float)x, (float)y, (float)z, (float) w);
      _x = x;
      _y = y;
      _z = z;
      _w = w;
    }
  }
}