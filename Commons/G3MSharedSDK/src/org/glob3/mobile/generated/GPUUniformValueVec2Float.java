package org.glob3.mobile.generated;import java.util.*;

public class GPUUniformValueVec2Float extends GPUUniformValue
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public float _x;
  public float _y;

  public GPUUniformValueVec2Float(float x, float y)
  {
	  super(GLType.glVec2Float());
	  _x = x;
	  _y = y;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void setUniform(GL* gl, const IGLUniformID* id) const
  public final void setUniform(GL gl, IGLUniformID id)
  {
	gl.uniform2f(id, _x, _y);
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const GPUUniformValue* v) const
  public final boolean isEquals(GPUUniformValue v)
  {
	GPUUniformValueVec2Float v2 = (GPUUniformValueVec2Float)v;
	return (_x == v2._x) && (_y == v2._y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString("Uniform Value Vec2Float: x:");
	isb.addDouble(_x);
	isb.addString("y:");
	isb.addDouble(_y);
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }
}
