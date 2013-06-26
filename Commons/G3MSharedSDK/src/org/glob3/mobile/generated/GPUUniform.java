package org.glob3.mobile.generated; 
public class GPUUniform extends GPUVariable
{

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
     super(name, GPUVariableType.UNIFORM);
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
  public final GPUUniformValue getSetValue()
  {
     return _value;
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
      ILogger.instance().logError("Attempting to set uniform " + _name + "with invalid value type.");
      return;
    }
    if (_value == null || !_value.isEqualsTo(v))
    {
      _dirty = true;
      _value = v.copyOrCreate(_value);
    }
  }

  public void applyChanges(GL gl)
  {
    if (_dirty)
    {
      _value.setUniform(gl, _id);
      _dirty = false;
    }
    else
    {
      if (_value == null)
      {
        ILogger.instance().logError("Uniform " + _name + " was not set.");
      }
    }
  }
}