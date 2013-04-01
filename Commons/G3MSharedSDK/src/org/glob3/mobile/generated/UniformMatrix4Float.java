package org.glob3.mobile.generated; 
public class UniformMatrix4Float extends Uniform
{
  private MutableMatrix44D _m = new MutableMatrix44D();
  public UniformMatrix4Float(String name, IGLUniformID id)
  {
     super(name,id);
  }

  public final void set(GL gl, MutableMatrix44D m)
  {
    if (!_m.isEqualsTo(m))
    {
      gl.uniformMatrix4fv(_id, false, m);
      _m = m;
    }
  }
}