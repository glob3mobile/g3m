package org.glob3.mobile.generated; 
public abstract class CustomShaderGLFeature extends GLFeature
{
    private boolean _initializedShader;

    public void dispose()
    {
        super.dispose();
    }

    protected abstract boolean onInitializeShader(GL gl, GLState state, GPUProgram linkedProgram);
    protected abstract void onAfterApplyShaderOnGPU(GL gl, GLState state, GPUProgram linkedProgram);

    public CustomShaderGLFeature(String shaderName)
    {
       super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_CUSTOM_SHADER);
       _initializedShader = false;
        _values.setCustomShaderName(shaderName);
    }

    public final void afterApplyOnGPU(GL gl, GLState state, GPUProgram linkedProgram)
    {
        if (!_initializedShader)
        {
            _initializedShader = onInitializeShader(gl, state, linkedProgram);
        }
        if (_initializedShader)
        {
            onAfterApplyShaderOnGPU(gl, state, linkedProgram);
        }
    }
}