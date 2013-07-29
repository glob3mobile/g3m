package org.glob3.mobile.generated; 
public class GLFeatureColorGroup extends GLFeatureGroup
{
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  
    int priority = -1;
    for (int i = 0; i < _nFeatures; i++)
    {
      PriorityGLFeature f = ((PriorityGLFeature) _features[i]);
      if (f.getPriority() > priority)
      {
        priority = f.getPriority();
      }
    }
  
    for (int i = 0; i < _nFeatures; i++)
    {
      PriorityGLFeature f = ((PriorityGLFeature) _features[i]);
      if (f.getPriority() == priority)
      {
        f.applyOnGlobalGLState(state);
      }
    }
  }
  public final void addToGPUVariableSet(GPUVariableValueSet vs)
  {
  
    int priority = -1;
    for (int i = 0; i < _nFeatures; i++)
    {
      PriorityGLFeature f = ((PriorityGLFeature) _features[i]);
      if (f.getPriority() > priority)
      {
        priority = f.getPriority();
      }
    }
  
    for (int i = 0; i < _nFeatures; i++)
    {
      PriorityGLFeature f = ((PriorityGLFeature) _features[i]);
      if (f.getPriority() == priority)
      {
        priority = f.getPriority();
        vs.combineWith(f.getGPUVariableValueSet());
      }
    }
  }
}