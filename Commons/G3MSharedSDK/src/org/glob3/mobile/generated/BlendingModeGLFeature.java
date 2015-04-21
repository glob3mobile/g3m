package org.glob3.mobile.generated; 
public class BlendingModeGLFeature extends GLColorGroupFeature
{
  public void dispose()
  {
    super.dispose();
  }

  public BlendingModeGLFeature(boolean blend, int sFactor, int dFactor)
  {
     super(GLFeatureID.GLF_BLENDING_MODE, 4, blend, sFactor, dFactor);
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    blendingOnGlobalGLState(state);
  }
}