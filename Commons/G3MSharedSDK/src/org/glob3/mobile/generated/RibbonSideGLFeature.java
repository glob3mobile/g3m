package org.glob3.mobile.generated;
// Per-vertex unit vector the RibbonMesh shader offsets along. Deliberately not the NORMAL
// attribute: normals would drag the lighting path (MODEL uniform) into the GL state.
public class RibbonSideGLFeature extends GLFeature
{
  public void dispose()
  {
    super.dispose();
  }

  public RibbonSideGLFeature(IFloatBuffer sides)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_RIBBON_SIDE);
    _values.addAttributeValue(GPUAttributeKey.RIBBON_SIDE, new GPUAttributeValueVec3Float(sides, 3, 0, 0, false), false);
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
}