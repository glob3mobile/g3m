package org.glob3.mobile.generated;import java.util.*;

public abstract class GLFeature extends RCObject
{
  public void dispose()
  {
	if (_values != null)
		_values.dispose();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  protected GPUVariableValueSet _values;

  public final GLFeatureGroupName _group;
  public final GLFeatureID _id;

  public GLFeature(GLFeatureGroupName group, GLFeatureID id)
  {
	  _group = group;
	  _id = id;
	_values = new GPUVariableValueSet();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const GPUVariableValueSet* getGPUVariableValueSet() const
  public final GPUVariableValueSet getGPUVariableValueSet()
  {
	return _values;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void applyOnGlobalGLState(GLGlobalState* state) const = 0;
  public abstract void applyOnGlobalGLState(GLGlobalState state);

}
