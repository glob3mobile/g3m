package org.glob3.mobile.generated; 
public class GPUUniform
{
  protected final String _name;
  protected final IGLUniformID _id;

  protected boolean _dirty;
  protected GPUUniformValue _value;
  protected final int _type;

  public void dispose()
  {
    if (_id != null)
       _id.dispose();
    if (_value != null)
       _value.dispose();
  }

  public GPUUniform(String name, IGLUniformID id, int type)
  {
     _name = name;
     _id = id;
     _dirty = false;
     _value = null;
     _type = type;
  }

  public final String getName()
  {
     return _name;
  }
  public final IGLUniformID getID()
  {
     return _id;
  }
  public final int getType()
  {
     return _type;
  }
  public final boolean wasSet()
  {
     return _value != null;
  }

  public final void unset()
  {
    if (_value != null)
    {
      if (_value != null)
         _value.dispose();
      _value = null;
    }
    _dirty = false;
  }

//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
  void set(GPUUniformValue* v);

  public void applyChanges(GL gl)
  {
    if (_dirty)
    {
      _value.setUniform(gl, _id);
      _dirty = false;
    }
  }
}