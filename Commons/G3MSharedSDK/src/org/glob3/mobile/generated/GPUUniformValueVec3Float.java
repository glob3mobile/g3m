package org.glob3.mobile.generated;import java.util.*;

public class GPUUniformValueVec3Float extends GPUUniformValue
{
  protected float _x;
  protected float _y;
  protected float _z;

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }


  public GPUUniformValueVec3Float(Color color)
  {
	  super(GLType.glVec3Float());
	  _x = color._red;
	  _y = color._green;
	  _z = color._blue;
  }

  public GPUUniformValueVec3Float(float x, float y, float z)
  {
	  super(GLType.glVec3Float());
	  _x = x;
	  _y = y;
	  _z = z;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void setUniform(GL* gl, const IGLUniformID* id) const
  public final void setUniform(GL gl, IGLUniformID id)
  {
	gl.uniform3f(id, _x, _y, _z);
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const GPUUniformValue* v) const
  public final boolean isEquals(GPUUniformValue v)
  {
	GPUUniformValueVec3Float v2 = (GPUUniformValueVec3Float)v;
	return (_x == v2._x) && (_y == v2._y) && (_z == v2._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString("Uniform Value Vec4Float: x:");
	isb.addDouble(_x);
	isb.addString("y:");
	isb.addDouble(_y);
	isb.addString("z:");
	isb.addDouble(_z);
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }
}
