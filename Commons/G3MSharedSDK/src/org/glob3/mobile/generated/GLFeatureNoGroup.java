package org.glob3.mobile.generated; 
public class GLFeatureNoGroup extends GLFeatureGroup
{
  public final void apply(GLFeatureSet features, GPUVariableValueSet vs, GLGlobalState state)
  {
  
    final int size = features.size();
    for(int i = 0; i < size; i++)
    {
      final GLFeature f = features.get(i);
      if (f._group == GLFeatureGroupName.NO_GROUP)
      {
        f.applyOnGlobalGLState(state);
        vs.combineWith(f.getGPUVariableValueSet());
      }
    }
  }
}