package org.glob3.mobile.generated; 
public class GPUAttribute
{
  protected final String _name;
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
     _name = name;
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

  public final void unset()
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
      //delete v;
    }
  }

//  void setEnable(bool b){
//    if (b != _enabled){
//      _enabled = b;
//      _dirtyEnabled = true;
//    }
//  }

  public void applyChanges(GL gl)
  {
//    if (_dirtyEnabled){
//      _dirtyEnabled = false;
//      if (_enabled){
//        gl->enableVertexAttribArray(_id);
//      } else{
//        gl->disableVertexAttribArray(_id);
//      }
//    }

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
  }
}