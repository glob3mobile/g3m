package org.glob3.mobile.generated;import java.util.*;

public class Static2ColorScheme extends VertexColorScheme
{
	private IFloatBuffer _valuesInColorRange;
	public Static2ColorScheme(IFloatBuffer valuesInColorRange, Color colorRangeAt0, Color colorRangeAt1)
	{
		_feat = new ColorRangeGLFeature(colorRangeAt0, colorRangeAt1, valuesInColorRange);
		_valuesInColorRange = valuesInColorRange;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void setColorRangeStaticValues(IFloatBuffer* values) const
	public final void setColorRangeStaticValues(IFloatBuffer values)
	{
		((ColorRangeGLFeature)_feat).setValues(values);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getValue(int index) const
	public final float getValue(int index)
	{
		return _valuesInColorRange.get(index);
	}
}
