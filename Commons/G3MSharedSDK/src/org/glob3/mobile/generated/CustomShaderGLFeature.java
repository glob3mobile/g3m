package org.glob3.mobile.generated; 
public class CustomShaderGLFeature extends GLFeature
{
    private final String _shaderName;

    public void dispose()
    {
        super.dispose();
    }

    public CustomShaderGLFeature(String shaderName)
    {
       super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_CUSTOM_SHADER);
       _shaderName = shaderName;
        _values.setCustomShaderName(shaderName);
    }

    public final void applyOnGlobalGLState(GLGlobalState state)
    {
    }
}