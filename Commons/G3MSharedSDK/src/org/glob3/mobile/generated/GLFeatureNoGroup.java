package org.glob3.mobile.generated; 
public class GLFeatureNoGroup extends GLFeatureGroup
{
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    for(int i = 0; i < _nFeatures; i++)
    {
      final GLFeature f = _features[i];
      if (f != null)
      {
        f.applyOnGlobalGLState(state);
      }
    }
  }
  public final void addToGPUVariableSet(GPUVariableValueSet vs)
  {
    for(int i = 0; i < _nFeatures; i++)
    {
      final GLFeature f = _features[i];
      if (f != null)
      {
        vs.combineWith(f.getGPUVariableValueSet());
      }
    }
  }
}