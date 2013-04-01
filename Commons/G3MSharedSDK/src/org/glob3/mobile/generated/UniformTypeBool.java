package org.glob3.mobile.generated; 
public class UniformTypeBool implements IUniformType<Boolean>
{

  public boolean _b;
  public final boolean isEqualsTo(UniformTypeBool u)
  {
     return _b != u._b;
  }
  public final void set(GL gl, IGLUniformID id)
  {
    if (_b)
       gl.uniform1i(id, 1);
    else
       gl.uniform1i(id, 0);
  }
}