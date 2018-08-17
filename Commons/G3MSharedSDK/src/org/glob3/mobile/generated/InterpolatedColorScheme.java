package org.glob3.mobile.generated;import java.util.*;

public class InterpolatedColorScheme extends VertexColorScheme
{
	protected IFloatBuffer _valuesInColorRange;
	protected IFloatBuffer _nextValuesInColorRange;
	protected float _time;

	public InterpolatedColorScheme(IFloatBuffer valuesInColorRange, IFloatBuffer nextValuesInColorRange)
	{
		_valuesInColorRange = valuesInColorRange;
		_nextValuesInColorRange = nextValuesInColorRange;
//C++ TO JAVA CONVERTER TODO TASK: The #define macro NANF was defined in alternate ways and cannot be replaced in-line:
		_time = NANF;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void setColorRangeDynamicValues(IFloatBuffer* values, IFloatBuffer* nextValues) const
	public final void setColorRangeDynamicValues(IFloatBuffer values, IFloatBuffer nextValues)
	{
		((DynamicColorRangeGLFeature)_feat).setValues(values, nextValues);
		_valuesInColorRange = values;
		_nextValuesInColorRange = nextValues;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getValue(int index) const
	public final float getValue(int index)
	{

		return IMathUtils.instance().linearInterpolation(_valuesInColorRange.get(index), _nextValuesInColorRange.get(index), _time);
	}
}
