package org.glob3.mobile.generated; 
public class ColorGLFeature extends GLColorGroupFeature
{
  public void dispose()
  {
    super.dispose();
  }

  public ColorGLFeature(IFloatBuffer colors, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor)
  {
     super(GLFeatureID.GLF_COLOR, 3, blend, sFactor, dFactor);
    GPUAttributeValueVec4Float value = new GPUAttributeValueVec4Float(colors, arrayElementSize, index, stride, normalized);
    _values.addAttributeValue(GPUAttributeKey.COLOR, value, false);
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    blendingOnGlobalGLState(state);
  }
}