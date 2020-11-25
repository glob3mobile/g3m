package org.glob3.mobile.generated;
//
//  GPUAttribute.cpp
//  G3M
//
//  Created by Jose Miguel SN on 16/05/13.
//
//

//
//  GPUAttribute.hpp
//  G3M
//
//  Created by Jose Miguel SN on 27/03/13.
//
//



//class GPUAttributeValue;
//class GL;


public class GPUAttribute extends GPUVariable
{
  private boolean _dirty;
  private GPUAttributeValue _value;
  private boolean _enabled;

  public final int _id;
  public final int _type;
  public final int _size;
  public final GPUAttributeKey _key;

  public GPUAttribute(String name, int id, int type, int size)
  {
     super(name, GPUVariableType.ATTRIBUTE);
     _id = id;
     _dirty = false;
     _value = null;
     _type = type;
     _size = size;
     _enabled = false;
     _key = getAttributeKey(name);
  
  }

  public void dispose()
  {
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

  public final boolean isEnabled()
  {
    return _enabled;
  }

  public final int getIndex()
  {
    return _key.getValue();
  }

  public final void unset(GL gl)
  {
    if (_value != null)
    {
      _value._release();
      _value = null;
    }
    _enabled = false;
    _dirty = false;
  
    gl.disableVertexAttribArray(_id);
  }

  public final void put(GPUAttributeValue v)
  {
    if (v != _value)
    {
      if (v._enabled && _type != v._type) //type checking
      {
        ILogger.instance().logError("Attempting to set attribute " + _name + "with invalid value type.");
        return;
      }
      if (_value == null || !_value.isEquals(v))
      {
        _dirty = true;
  
        if (_value != null)
        {
          _value._release();
        }
        _value = v;
        _value._retain();
      }
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
        if (_value._enabled)
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
    }
  }

}