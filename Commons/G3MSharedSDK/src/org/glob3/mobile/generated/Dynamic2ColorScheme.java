package org.glob3.mobile.generated;import java.util.*;

public class Dynamic2ColorScheme extends InterpolatedColorScheme
{

	public Dynamic2ColorScheme(IFloatBuffer valuesInColorRange, IFloatBuffer nextValuesInColorRange, Color colorRangeAt0, Color colorRangeAt1)
	{
		super(valuesInColorRange, nextValuesInColorRange);
		_feat = new DynamicColorRangeGLFeature(colorRangeAt0, colorRangeAt1, valuesInColorRange, nextValuesInColorRange, 0.0f);
	}
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void setTime(float time) const
	public void setTime(float time)
	{
		_time = time;
		((DynamicColorRangeGLFeature)_feat).setTime(time);
	}
}
