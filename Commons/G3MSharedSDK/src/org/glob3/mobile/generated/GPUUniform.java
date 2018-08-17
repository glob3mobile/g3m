package org.glob3.mobile.generated;import java.util.*;

public class GPUUniform extends GPUVariable
{

  private boolean _dirty;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final GPUUniformValue _value;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public GPUUniformValue _value = new internal();
//#endif



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

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean wasSet() const
  public final boolean wasSet()
  {
	  return _value != null;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const GPUUniformValue* getSetValue() const
  public final GPUUniformValue getSetValue()
  {
	  return _value;
  }


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
	if (_type == v._type) //type checking
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
	  THROW_EXCEPTION("Attempting to set uniform \"" + _name + "\" with invalid value type.");
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
		THROW_EXCEPTION("Uniform \"" + _name + "\" was not set.");
	  }
	}
  }

}
