package org.glob3.mobile.generated;import java.util.*;

public class GPUUniformValueBool extends GPUUniformValue
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public final boolean _value;

  public GPUUniformValueBool(boolean b)
  {
	  super(GLType.glBool());
	  _value = b;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void setUniform(GL* gl, const IGLUniformID* id) const
  public final void setUniform(GL gl, IGLUniformID id)
  {
	if (_value)
	{
	  gl.uniform1i(id, 1);
	}
	else
	{
	  gl.uniform1i(id, 0);
	}
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const GPUUniformValue* v) const
  public final boolean isEquals(GPUUniformValue v)
  {
	return _value == ((GPUUniformValueBool)v)._value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString("Uniform Value Boolean: ");
	isb.addBool(_value);
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }
}
