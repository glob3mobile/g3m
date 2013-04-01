package org.glob3.mobile.generated; 
template<T extends IUniformType<T>
public class IUniform
{
  protected String _name;
  protected IGLUniformID _id;

  protected T _value;

  public void dispose()
  {
     if (_id != null)
        _id.dispose();
     _value = null;
  }
  public IUniform(String name, IGLUniformID id)
  {
     _name = name;
     _id = id;
  }

  public final void set(GL gl, T x)
  {
    if (_value.isEqualsTo(x))
    {
      x.setUniform(gl, _id);
      _value = x;
    }
  }

  public static class UniformException
  {

  }

  public final void launchException()
  {
    throw new UniformException();
  }
}