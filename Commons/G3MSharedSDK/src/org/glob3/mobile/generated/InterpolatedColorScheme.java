package org.glob3.mobile.generated;public abstract class InterpolatedColorScheme extends VertexColorScheme
{
    protected IFloatBuffer _valuesInColorRange;
    protected IFloatBuffer _nextValuesInColorRange;
    protected float _time;

    protected abstract void setValues(IFloatBuffer values, IFloatBuffer nextValues);

    public InterpolatedColorScheme(IFloatBuffer valuesInColorRange, IFloatBuffer nextValuesInColorRange)
    {
        _valuesInColorRange = valuesInColorRange;
        _nextValuesInColorRange = nextValuesInColorRange;
        _time = java.lang.Float.NaN;
    }

    public final void setColorRangeDynamicValues(IFloatBuffer values, IFloatBuffer nextValues)
    {
        setValues(values, nextValues);
        _valuesInColorRange = values;
        _nextValuesInColorRange = nextValues;
    }

    public final float getValue(int index)
    {

        return IMathUtils.instance().linearInterpolation(_valuesInColorRange.get(index), _nextValuesInColorRange.get(index), _time);
    }
}
