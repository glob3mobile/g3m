package org.glob3.mobile.generated; 
//C++ TO JAVA CONVERTER TODO TASK: The original C++ template specifier was replaced with a Java generic specifier, which may not produce the same behavior:
public class IUniform<T>
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
}