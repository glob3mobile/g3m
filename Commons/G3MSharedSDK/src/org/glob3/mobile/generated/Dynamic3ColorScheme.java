package org.glob3.mobile.generated;public class Dynamic3ColorScheme extends InterpolatedColorScheme
{
    private DynamicColorRange3GLFeature _specificFeatureHandler;

    protected void setValues(IFloatBuffer values, IFloatBuffer nextValues)
    {
        _specificFeatureHandler.setValues(values, nextValues);
    }
    public Dynamic3ColorScheme(IFloatBuffer valuesInColorRange, IFloatBuffer nextValuesInColorRange, Color colorRangeAt0, Color colorRangeAt0_5, Color colorRangeAt1)
    {
       super(valuesInColorRange, nextValuesInColorRange);
        _specificFeatureHandler = new DynamicColorRange3GLFeature(colorRangeAt0, colorRangeAt0_5, colorRangeAt1, valuesInColorRange, nextValuesInColorRange, 0.0f);
        _feat = _specificFeatureHandler;
    }

    public void setTime(float time)
    {
        _time = time;
        _specificFeatureHandler.setTime(time);
    }
}
