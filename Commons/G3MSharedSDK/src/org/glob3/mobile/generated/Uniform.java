package org.glob3.mobile.generated; 
public class Uniform
{
  protected String _name;
  protected IGLUniformID _id;
  public void dispose()
  {
     if (_id != null)
        _id.dispose();
  }
  public Uniform(String name, IGLUniformID id)
  {
     _name = name;
     _id = id;
  }

  public final String getName()
  {
     return _name;
  }
  public final IGLUniformID getID()
  {
     return _id;
  }
}