package org.glob3.mobile.generated;/////////////////////////


public class TransparencyDistanceThresholdGLFeature extends GLFeature
{
    public void dispose()
    {
        super.dispose();
    }

    public TransparencyDistanceThresholdGLFeature(float distance)
    {
       super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_TRANSPARENCY_DISTANCE_THRESHOLD);
        _values.addUniformValue(GPUUniformKey.TRANSPARENCY_DISTANCE_THRESLHOLD, new GPUUniformValueFloat(distance), false);
    }

    public final void applyOnGlobalGLState(GLGlobalState state)
    {
    }
}
