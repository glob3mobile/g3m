package org.glob3.mobile.generated;import java.util.*;

/////////////////////////


public class TransparencyDistanceThresholdGLFeature extends GLFeature
{
	public void dispose()
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		super.dispose();
//#endif
	}

	public TransparencyDistanceThresholdGLFeature(float distance)
	{
		super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_TRANSPARENCY_DISTANCE_THRESHOLD);
		_values.addUniformValue(GPUUniformKey.TRANSPARENCY_DISTANCE_THRESLHOLD, new GPUUniformValueFloat(distance), false);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void applyOnGlobalGLState(GLGlobalState* state) const
	public final void applyOnGlobalGLState(GLGlobalState state)
	{
	}
}
