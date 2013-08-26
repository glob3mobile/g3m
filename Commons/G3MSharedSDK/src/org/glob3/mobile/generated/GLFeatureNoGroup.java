package org.glob3.mobile.generated; 
 //: public GLFeatureSet
public class GLFeatureNoGroup extends GLFeatureGroup
{
//  void applyOnGlobalGLState(GLGlobalState* state);
//  void addToGPUVariableSet(GPUVariableValueSet* vs);

  public final void apply(GLFeatureSet features, GPUVariableValueSet vs, GLGlobalState state)
  {
  
    for(int i = 0; i < features.size(); i++)
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