package org.glob3.mobile.generated;import java.util.*;

/////////////////////


public class GPUUniformValueMatrix4 extends GPUUniformValue
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Matrix44DProvider _provider;
  private final Matrix44D _lastModelSet;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public Matrix44DProvider _provider = new internal();
  public Matrix44D _lastModelSet = new internal();
//#endif

  public void dispose()
  {
	_provider._release();
	if (_lastModelSet != null)
	{
	  _lastModelSet._release();
	}

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }


  public GPUUniformValueMatrix4(Matrix44DProvider[] providers, int nMatrix)
  {
	  super(GLType.glMatrix4Float());
	  _provider = new Matrix44DMultiplicationHolder(providers, nMatrix);
	  _lastModelSet = null;
  }

  public GPUUniformValueMatrix4(Matrix44DProvider provider)
  {
	  super(GLType.glMatrix4Float());
	  _provider = provider;
	  _lastModelSet = null;
	_provider._retain();
  }

  public GPUUniformValueMatrix4(Matrix44D m)
  {
	  super(GLType.glMatrix4Float());
	  _provider = new Matrix44DHolder(m);
	  _lastModelSet = null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void setUniform(GL* gl, const IGLUniformID* id) const
  public final void setUniform(GL gl, IGLUniformID id)
  {
	if (_lastModelSet != null)
	{
	  _lastModelSet._release();
	}

	_lastModelSet = _provider.getMatrix();

	_lastModelSet._retain();

	gl.uniformMatrix4fv(id, false, _lastModelSet);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const GPUUniformValue* v) const
  public final boolean isEquals(GPUUniformValue v)
  {
	if (_lastModelSet == ((GPUUniformValueMatrix4)v)._provider.getMatrix())
	{
	  return true;
	}

	return false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString("Uniform Value Matrix44D.");
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }
}
