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

  public final void set(GPUUniformValue v)
  {
    if (_type != v.getType()) //type checking
    {
      //      delete v;
      ILogger.instance().logError("Attempting to set uniform " + _name + "with invalid value type.");
      return;
    }
    if (_value == null || !_value.isEqualsTo(v))
    {
      _dirty = true;
      if (_value != null)
      {
        if (_value != null)
           _value.dispose();
      }
      _value = v.deepCopy();
      //      delete v;
    }
  }

  public void applyChanges(GL gl)
  {
    if (_dirty)
    {
      _value.setUniform(gl, _id);
      _dirty = false;
    }
  }
}