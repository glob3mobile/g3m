package org.glob3.mobile.generated;import java.util.*;

/////////////////////////

public class DynamicColorRange3GLFeature extends GLFeature
{
	public void dispose()
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		super.dispose();
//#endif
	}

	private GPUAttributeValueVec1Float _parameterValue;
	private GPUAttributeValueVec1Float _parameterValueNext;
	private GPUUniformValueVec4Float _colorAt0;
	private GPUUniformValueVec4Float _colorAt0_5;
	private GPUUniformValueVec4Float _colorAt1;

	private GPUUniformValueFloatMutable _time;

	public DynamicColorRange3GLFeature(Color colorAt0, Color colorAt0_5, Color colorAt1, IFloatBuffer values, IFloatBuffer valuesNext, float time)
	{
		super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_DYNAMIC_COLOR_RANGE);
		_colorAt0 = new GPUUniformValueVec4Float(colorAt0);
		_colorAt0_5 = new GPUUniformValueVec4Float(colorAt0_5);
		_colorAt1 = new GPUUniformValueVec4Float(colorAt1);
		_parameterValue = new GPUAttributeValueVec1Float(values, 1, 0, 0, false);
		_parameterValueNext = new GPUAttributeValueVec1Float(valuesNext, 1, 0, 0, false);
		_time = new GPUUniformValueFloatMutable(time);
		_values.addUniformValue(GPUUniformKey.COLORRANGE_COLOR_AT_0, _colorAt0, false);
		_values.addUniformValue(GPUUniformKey.COLORRANGE_COLOR_AT_1, _colorAt1, false);
		_values.addUniformValue(GPUUniformKey.COLORRANGE_COLOR_AT_0_5, _colorAt0_5, false);
    
		_values.addUniformValue(GPUUniformKey.TIME, _time, false);
    
		_values.addAttributeValue(GPUAttributeKey.COLORRANGE_VALUE, _parameterValue, false);
		_values.addAttributeValue(GPUAttributeKey.COLORRANGE_VALUE_NEXT, _parameterValueNext, false);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void applyOnGlobalGLState(GLGlobalState* state) const
	public final void applyOnGlobalGLState(GLGlobalState state)
	{
	}

	public final void setValues(IFloatBuffer values, IFloatBuffer valuesNext)
	{
		_parameterValue.replaceBuffer(values);
		_parameterValueNext.replaceBuffer(valuesNext);
	}
	public final void setTime(float time)
	{
		_time.changeValue(time);
	}
}
