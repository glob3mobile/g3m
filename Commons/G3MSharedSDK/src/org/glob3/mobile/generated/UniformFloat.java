package org.glob3.mobile.generated; 
public class UniformFloat extends Uniform
{
  private double _d;
  public UniformFloat(String name, IGLUniformID id)
  {
     super(name,id);
  }

  public final void set(GL gl, double d)
  {
    if (_d != d)
    {
      gl.uniform1f(_id, (float)d);
      _d = d;
    }
  }
}