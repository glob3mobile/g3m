package org.glob3.mobile.generated; 
public class GPUAttribute extends GPUVariable
{
  protected final int _id;

  protected boolean _dirty;
  protected GPUAttributeValue _value;

  protected final int _type;
  protected final int _size;

  protected boolean _dirtyEnabled;
  protected boolean _enabled;


  public void dispose()
  {
    if (_value != null)
       _value.dispose();
  }

  public GPUAttribute(String name, int id, int type, int size)
  {
     super(name, GPUVariableType.ATTRIBUTE);
     _id = id;
     _dirty = false;
     _value = null;
     _type = type;
     _size = size;
     _enabled = false;
     _dirtyEnabled = false;
  }

  public final String getName()
  {
     return _name;
  }
  public final int getID()
  {
     return _id;
  }
  public final int getType()
  {
     return _type;
  }
  public final int getSize()
  {
     return _size;
  }
  public final boolean wasSet()
  {
     return _value != null;
  }
  public final boolean isEnabled()
  {
     return _enabled;
  }

  public final void unset(GL gl)
  {
    if (_value != null)
    {
      if (_value != null)
         _value.dispose();
      _value = null;
    }
    _enabled = false;
    _dirty = false;
    _dirtyEnabled = false;

    gl.disableVertexAttribArray(_id);
  }

  public final void set(GPUAttributeValue v)
  {
    if (v.getEnabled() && _type != v.getType()) //type checking
    {
      //delete v;
      ILogger.instance().logError("Attempting to set attribute " + _name + "with invalid value type.");
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
      _value = v.shallowCopy();
    }
  }


  public void applyChanges(GL gl)
  {

    if (_value == null)
    {
      if (_enabled)
      {
        ILogger.instance().logError("Attribute " + _name + " was not set but it is enabled.");
      }
    }
    else
    {
      if (_dirty)
      {

        if (_value.getEnabled())
        {
          if (!_enabled)
          {
            gl.enableVertexAttribArray(_id);
            _enabled = true;
          }
          _value.setAttribute(gl, _id);
        }
        else
        {
          if (_enabled)
          {
            gl.disableVertexAttribArray(_id);
            _enabled = false;
          }
        }

        _dirty = false;
      }
      else
      {

      }
    }
  }
}