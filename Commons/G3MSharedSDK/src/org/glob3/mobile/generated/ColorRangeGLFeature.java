package org.glob3.mobile.generated;import java.util.*;

//////////////////////////

public class ColorRangeGLFeature extends GLFeature
{
	public void dispose()
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		super.dispose();
//#endif
	}

	private GPUAttributeValueVec1Float _parameterValue;
	private GPUUniformValueVec4Float _colorAt0;
	private GPUUniformValueVec4Float _colorAt1;

	public ColorRangeGLFeature(Color colorAt0, Color colorAt1, IFloatBuffer values)
	{
		super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_COLOR_RANGE);
		_colorAt0 = new GPUUniformValueVec4Float(colorAt0);
		_colorAt1 = new GPUUniformValueVec4Float(colorAt1);
		_parameterValue = new GPUAttributeValueVec1Float(values, 1, 0, 0, false);
		_values.addUniformValue(GPUUniformKey.COLORRANGE_COLOR_AT_0, _colorAt0, false);
		_values.addUniformValue(GPUUniformKey.COLORRANGE_COLOR_AT_1, _colorAt1, false);
		_values.addAttributeValue(GPUAttributeKey.COLORRANGE_VALUE, _parameterValue, false);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void applyOnGlobalGLState(GLGlobalState* state) const
	public final void applyOnGlobalGLState(GLGlobalState state)
	{
	}

	public final void setValues(IFloatBuffer values)
	{
		_parameterValue.replaceBuffer(values);
	}
}
