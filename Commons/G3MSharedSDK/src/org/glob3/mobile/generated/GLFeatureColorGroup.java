package org.glob3.mobile.generated; 
public class GLFeatureColorGroup extends GLFeatureGroup
{
//  void applyOnGlobalGLState(GLGlobalState* state);
//  void addToGPUVariableSet(GPUVariableValueSet* vs);
  public final void apply(GLFeatureSet features, GPUVariableValueSet vs, GLGlobalState state)
  {
  
    final int size = features.size();
    int priority = -1;
    for (int i = 0; i < size; i++)
    {
      final GLFeature f = features.get(i);
      if (f.getGroup() == GLFeatureGroupName.COLOR_GROUP)
      {
        PriorityGLFeature pf = ((PriorityGLFeature) f);
        if (pf.getPriority() > priority)
        {
  
          if (pf.getID() != GLFeatureID.GLF_BLENDING_MODE) //We do not take into account Blending if TexID not set
          {
            priority = pf.getPriority();
          }
        }
      }
    }
  
    for (int i = 0; i < size; i++)
    {
      final GLFeature f = features.get(i);
      if (f.getGroup() == GLFeatureGroupName.COLOR_GROUP)
      {
        PriorityGLFeature pf = ((PriorityGLFeature) f);
        if (pf.getPriority() == priority)
        {
          pf.applyOnGlobalGLState(state);
          vs.combineWith(f.getGPUVariableValueSet());
        }
      }
    }
  }
}