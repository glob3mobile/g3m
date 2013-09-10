package org.glob3.mobile.generated; 
 //: public GLFeatureSet
public class GLFeatureNoGroup extends GLFeatureGroup
{
//  void applyOnGlobalGLState(GLGlobalState* state);
//  void addToGPUVariableSet(GPUVariableValueSet* vs);

  public final void apply(GLFeatureSet features, GPUVariableValueSet vs, GLGlobalState state)
  {
  
    final int size = features.size();
    for(int i = 0; i < size; i++)
    {
      final GLFeature f = features.get(i);
      if (f.getGroup() == GLFeatureGroupName.NO_GROUP)
      {
        f.applyOnGlobalGLState(state);
        vs.combineWith(f.getGPUVariableValueSet());
      }
    }
  }
}