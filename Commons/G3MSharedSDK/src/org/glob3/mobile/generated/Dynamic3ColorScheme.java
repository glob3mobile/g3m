package org.glob3.mobile.generated;import java.util.*;

public class Dynamic3ColorScheme extends InterpolatedColorScheme
{
	public Dynamic3ColorScheme(IFloatBuffer valuesInColorRange, IFloatBuffer nextValuesInColorRange, Color colorRangeAt0, Color colorRangeAt0_5, Color colorRangeAt1)
	{
		super(valuesInColorRange, nextValuesInColorRange);
		_feat = new DynamicColorRange3GLFeature(colorRangeAt0, colorRangeAt0_5, colorRangeAt1, valuesInColorRange, nextValuesInColorRange, 0.0f);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void setTime(float time) const
	public void setTime(float time)
	{
		_time = time;
		((DynamicColorRange3GLFeature)_feat).setTime(time);
	}
}
