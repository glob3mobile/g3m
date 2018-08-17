package org.glob3.mobile.generated;import java.util.*;

public class GPUUniformValueFloat extends GPUUniformValue
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public float _value;

  public GPUUniformValueFloat(float d)
  {
	  super(GLType.glFloat());
	  _value = d;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void setUniform(GL* gl, const IGLUniformID* id) const
  public final void setUniform(GL gl, IGLUniformID id)
  {
	gl.uniform1f(id, _value);
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const GPUUniformValue* v) const
  public final boolean isEquals(GPUUniformValue v)
  {
	GPUUniformValueFloat v2 = (GPUUniformValueFloat)v;
	return _value == v2._value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString("Uniform Value Float: ");
	isb.addDouble(_value);
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }
}
