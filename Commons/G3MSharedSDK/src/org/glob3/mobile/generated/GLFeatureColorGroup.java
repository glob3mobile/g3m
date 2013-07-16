package org.glob3.mobile.generated; 
public class GLFeatureColorGroup extends GLFeatureGroup
{
//  GPUVariableValueSet* applyAndCreateGPUVariableSet(GL* gl);
//  GPUVariableValueSet* createGPUVariableSet();

  //GPUVariableValueSet* GLFeatureCameraGroup::applyAndCreateGPUVariableSet(GL* gl){
  ///*
  //  const Matrix44D* m = ((GLCameraGroupFeature*) _features[0])->getMatrix();
  //  m->_retain();
  //  _features[0]->applyGLGlobalState(gl);
  //
  //  for (int i = 1; i < _nFeatures; i++){
  //    GLCameraGroupFeature* f = ((GLCameraGroupFeature*) _features[i]);
  //    //f->applyGLGlobalState(gl);
  //    const Matrix44D* m2 = f->getMatrix();
  //
  //    Matrix44D* m3 = m->createMultiplication(*m2);
  //
  //    m->_release();
  //    m = m3;
  //  }
  //
  //  GPUVariableValueSet* fs = new GPUVariableValueSet();
  //  fs->addUniformValue(MODELVIEW, new GPUUniformValueMatrix4Float(*m));
  //  m->_release();
  //
  //  return fs;
  // */
  //
  //  const Matrix44DHolder** matrixHolders = new const Matrix44DHolder*[_nFeatures];
  //  for (int i = 0; i < _nFeatures; i++){
  //    GLCameraGroupFeature* f = ((GLCameraGroupFeature*) _features[i]);
  //    matrixHolders[i] = f->getMatrixHolder();
  //    if (matrixHolders[i] == NULL){
  //      ILogger::instance()->logError("MatrixHolder NULL");
  //    }
  //  }
  //  
  //  GPUVariableValueSet* fs = new GPUVariableValueSet();
  //  fs->addUniformValue(MODELVIEW, new GPUUniformValueModelview(matrixHolders, _nFeatures));
  //
  //  return fs;
  //}
  
  //GPUVariableValueSet* GLFeatureColorGroup::applyAndCreateGPUVariableSet(GL* gl){
  //
  //  int priority = -1;
  //  GLColorGroupFeature* topPriorityFeature = NULL;
  //  for (int i = 0; i < _nFeatures; i++){
  //    GLColorGroupFeature* f = ((GLColorGroupFeature*) _features[i]);
  //    if (f->getPriority() > priority){
  //      topPriorityFeature = f;
  //      priority = f->getPriority();
  //    }
  //  }
  //
  //  if (topPriorityFeature != NULL){
  //    GPUVariableValueSet* fs = new GPUVariableValueSet();
  //    fs->combineWith(topPriorityFeature->getGPUVariableValueSet());
  ////    topPriorityFeature->applyGLGlobalState(gl);
  //    return fs;
  //  } else{
  //    return NULL;
  //  }
  //}
  
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  
    int priority = -1;
    GLColorGroupFeature topPriorityFeature = null;
    for (int i = 0; i < _nFeatures; i++)
    {
      GLColorGroupFeature f = ((GLColorGroupFeature) _features[i]);
      if (f.getPriority() > priority)
      {
        topPriorityFeature = f;
        priority = f.getPriority();
      }
    }
  
    if (topPriorityFeature != null)
    {
      topPriorityFeature.applyOnGlobalGLState(state);
    }
  }

  //GPUVariableValueSet* GLFeatureColorGroup::createGPUVariableSet(){
  //
  //  int priority = -1;
  //  GLColorGroupFeature* topPriorityFeature = NULL;
  //  for (int i = 0; i < _nFeatures; i++){
  //    GLColorGroupFeature* f = ((GLColorGroupFeature*) _features[i]);
  //    if (f->getPriority() > priority){
  //      topPriorityFeature = f;
  //      priority = f->getPriority();
  //    }
  //  }
  //
  //  if (topPriorityFeature != NULL){
  //    GPUVariableValueSet* fs = new GPUVariableValueSet();
  //    fs->combineWith(topPriorityFeature->getGPUVariableValueSet());
  //    return fs;
  //  } else{
  //    return NULL;
  //  }
  //}
  
  public final void addToGPUVariableSet(GPUVariableValueSet vs)
  {
  
    int priority = -1;
    GLColorGroupFeature topPriorityFeature = null;
    for (int i = 0; i < _nFeatures; i++)
    {
      GLColorGroupFeature f = ((GLColorGroupFeature) _features[i]);
      if (f.getPriority() > priority)
      {
        topPriorityFeature = f;
        priority = f.getPriority();
      }
    }
  
    if (topPriorityFeature != null)
    {
      vs.combineWith(topPriorityFeature.getGPUVariableValueSet());
    }
  }
}