package org.glob3.mobile.generated; 
public class GPUUniform extends GPUVariable
{

  private boolean _dirty;
  private GPUUniformValue _value;



  public final IGLUniformID _id;
  public final int _type;
  public final GPUUniformKey _key;


  public GPUUniform(String name, IGLUniformID id, int type)
  {
     super(name, GPUVariableType.UNIFORM);
     _id = id;
     _dirty = false;
     _value = null;
     _type = type;
     _key = getUniformKey(name);
  }

  public void dispose()
  {
    if (_id != null)
       _id.dispose();
    if (_value != null)
    {
      _value._release();
    }

    super.dispose();
  }

  public final boolean wasSet()
  {
     return _value != null;
  }
  public final GPUUniformValue getSetValue()
  {
     return _value;
  }


  public final int getIndex()
  {
    return _key.getValue();
  }

  public final void unset()
  {
    if (_value != null)
    {
      _value._release();
      _value = null;
    }
    _dirty = false;
  }

  public final void set(GPUUniformValue v)
  {
    if (_type == v.getType()) //type checking
    {
      if (_value == null || !_value.isEquals(v))
      {
        _dirty = true;
        v._retain();
        if (_value != null)
        {
          _value._release();
        }
        _value = v;
      }
    }
    else
    {
      ILogger.instance().logError("Attempting to set uniform " + _name + " with invalid value type.");
    }
  }

  public final void applyChanges(GL gl)
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