package org.glob3.mobile.generated;public class Static2ColorScheme extends VertexColorScheme
{
    private IFloatBuffer _valuesInColorRange;
    public Static2ColorScheme(IFloatBuffer valuesInColorRange, Color colorRangeAt0, Color colorRangeAt1)
    {
        _feat = new ColorRangeGLFeature(colorRangeAt0, colorRangeAt1, valuesInColorRange);
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
