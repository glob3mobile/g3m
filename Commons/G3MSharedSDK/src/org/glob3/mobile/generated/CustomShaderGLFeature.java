package org.glob3.mobile.generated; 
public class CustomShaderGLFeature extends GLFeature
{

    public void dispose()
    {
        super.dispose();
    }

    public CustomShaderGLFeature(String shaderName)
    {
       super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_CUSTOM_SHADER);
        _values.setCustomShaderName(shaderName);
    }

    public final void applyOnGlobalGLState(GLGlobalState state)
    {
    }
}