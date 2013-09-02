package org.glob3.mobile.generated; 
public class FlatColorGLFeature extends GLColorGroupFeature
{
  public FlatColorGLFeature(Color color, boolean blend, int sFactor, int dFactor)
  {
     super(GLFeatureID.GLF_FLATCOLOR, 2, blend, sFactor, dFactor);
    _values.addUniformValue(GPUUniformKey.FLAT_COLOR, new GPUUniformValueVec4Float(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()), false);
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    blendingOnGlobalGLState(state);
  }
}