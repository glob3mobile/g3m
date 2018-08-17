package org.glob3.mobile.generated;import java.util.*;

public class GPUAttribute extends GPUVariable
{

  private boolean _dirty;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final GPUAttributeValue _value;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public GPUAttributeValue _value = new internal();
//#endif

  private boolean _enabled;


  public final int _id;
  public final int _type;
  public final int _size;
  public final GPUAttributeKey _key;

  public void dispose()
  {
	if (_value != null)
	{
	  _value._release();
	}

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
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
	  _key = getAttributeKey(name);
  }

  //  const std::string getName() const { return _name;}
  //  const int getID() const { return _id;}
  //  int getType() const { return _type;}
  //  int getSize() const { return _size;}
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean wasSet() const
  public final boolean wasSet()
  {
	  return _value != null;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEnabled() const
  public final boolean isEnabled()
  {
	  return _enabled;
  }
  //  GPUAttributeKey getKey() const { return _key;}


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getIndex() const
  public final int getIndex()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	return _key;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	return _key.getValue();
//#endif
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

  public final void set(GPUAttributeValue v)
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
