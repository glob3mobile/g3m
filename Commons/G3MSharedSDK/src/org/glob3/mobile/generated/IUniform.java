package org.glob3.mobile.generated; 
//C++ TO JAVA CONVERTER TODO TASK: C++ template specifiers with non-type parameters cannot be converted to Java:
public class IUniform<T extends IUniformType<T>>
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
      x.set(gl, _id);
      _value = x;
    }
  }

  public static class UniformException extends Throwable{}

  public final void launchException()
  {
    throw new UniformException();
  }
}