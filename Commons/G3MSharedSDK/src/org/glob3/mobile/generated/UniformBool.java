package org.glob3.mobile.generated; 
public class UniformBool extends Uniform
{
  private boolean _b;
  public UniformBool(String name, IGLUniformID id)
  {
     super(name,id);
  }
  public final void set(GL gl, boolean b)
  {
    if (_b != b)
    {
      if (b)
         gl.uniform1i(_id, 1);
      else
         gl.uniform1i(_id, 0);
      _b = b;
    }
  }
}