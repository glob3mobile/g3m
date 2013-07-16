package org.glob3.mobile.generated; 
public class GLFeatureNoGroup extends GLFeatureGroup
{
//  GPUVariableValueSet* applyAndCreateGPUVariableSet(GL* gl);
//  GPUVariableValueSet* createGPUVariableSet();

  //GPUVariableValueSet* GLFeatureNoGroup::applyAndCreateGPUVariableSet(GL* gl){
  //  GPUVariableValueSet* vs = new GPUVariableValueSet();
  //  for(int i = 0; i < _nFeatures; i++){
  //    const GLFeature* f = _features[i];
  //    if (f != NULL){
  ////      f->applyGLGlobalState(gl);
  //      vs->combineWith(f->getGPUVariableValueSet());
  //    }
  //  }
  //  return vs;
  //}
  
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

  //GPUVariableValueSet* GLFeatureNoGroup::createGPUVariableSet(){
  //  GPUVariableValueSet* vs = new GPUVariableValueSet();
  //  for(int i = 0; i < _nFeatures; i++){
  //    const GLFeature* f = _features[i];
  //    if (f != NULL){
  //      vs->combineWith(f->getGPUVariableValueSet());
  //    }
  //  }
  //  return vs;
  //}
  
  
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