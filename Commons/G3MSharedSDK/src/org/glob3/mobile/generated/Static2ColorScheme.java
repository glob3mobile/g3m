package org.glob3.mobile.generated;public class Static2ColorScheme extends VertexColorScheme
{
    private IFloatBuffer _valuesInColorRange;
    private ColorRangeGLFeature _specificFeatureHandler;
    public Static2ColorScheme(IFloatBuffer valuesInColorRange, Color colorRangeAt0, Color colorRangeAt1)
    {
        _specificFeatureHandler = new ColorRangeGLFeature(colorRangeAt0, colorRangeAt1, valuesInColorRange);
        _feat = _specificFeatureHandler;
        _valuesInColorRange = valuesInColorRange;
    }

    public final void setColorRangeStaticValues(IFloatBuffer values)
    {
        ((ColorRangeGLFeature)_feat).setValues(values);
    }

    public final float getValue(int index)
    {
        return _valuesInColorRange.get(index);
    }
}
