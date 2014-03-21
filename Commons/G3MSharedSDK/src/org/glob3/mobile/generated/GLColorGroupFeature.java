package org.glob3.mobile.generated; 
public abstract class GLColorGroupFeature extends PriorityGLFeature
{
  private final boolean _blend;
  private final int _sFactor;
  private final int _dFactor;

  public void dispose()
  {
    super.dispose();
  }

  public GLColorGroupFeature(GLFeatureID id, int priority, boolean blend, int sFactor, int dFactor)
  {
     super(GLFeatureGroupName.COLOR_GROUP, id, priority);
     _blend = blend;
     _sFactor = sFactor;
     _dFactor = dFactor;
  }

  public final void blendingOnGlobalGLState(GLGlobalState state)
  {
    if (_blend)
    {
      state.enableBlend();
      state.setBlendFactors(_sFactor, _dFactor);
    }
    else
    {
      state.disableBlend();
    }
  }
}