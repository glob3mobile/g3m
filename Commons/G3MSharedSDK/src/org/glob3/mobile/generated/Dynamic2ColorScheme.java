package org.glob3.mobile.generated;public class Dynamic2ColorScheme extends InterpolatedColorScheme
{
    private DynamicColorRangeGLFeature _specificFeatureHandler;

    protected void setValues(IFloatBuffer values, IFloatBuffer nextValues)
    {
        _specificFeatureHandler.setValues(values, nextValues);
    }


    public Dynamic2ColorScheme(IFloatBuffer valuesInColorRange, IFloatBuffer nextValuesInColorRange, Color colorRangeAt0, Color colorRangeAt1)
    {
       super(valuesInColorRange, nextValuesInColorRange);
        _specificFeatureHandler = new DynamicColorRangeGLFeature(colorRangeAt0, colorRangeAt1, valuesInColorRange, nextValuesInColorRange, 0.0f);
        _feat = _specificFeatureHandler;
    }
    public void setTime(float time)
    {
        _time = time;
        _specificFeatureHandler.setTime(time);
    }
}
