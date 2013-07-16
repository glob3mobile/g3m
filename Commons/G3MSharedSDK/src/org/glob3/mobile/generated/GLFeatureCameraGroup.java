package org.glob3.mobile.generated; 
public class GLFeatureCameraGroup extends GLFeatureGroup
{
//  GPUVariableValueSet* applyAndCreateGPUVariableSet(GL* gl);
//  GPUVariableValueSet* createGPUVariableSet();
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }

  //GPUVariableValueSet* GLFeatureCameraGroup::createGPUVariableSet(){
  ///#ifdef C_CODE
  //  const Matrix44DHolder** matrixHolders = new const Matrix44DHolder*[_nFeatures];
  ///#endif
  ///#ifdef JAVA_CODE
  //  final Matrix44DHolder[] matrixHolders = new Matrix44DHolder[_nFeatures];
  ///#endif
  //  for (int i = 0; i < _nFeatures; i++){
  //    GLCameraGroupFeature* f = ((GLCameraGroupFeature*) _features[i]);
  //    matrixHolders[i] = f->getMatrixHolder();
  //    if (matrixHolders[i] == NULL){
  //      ILogger::instance()->logError("MatrixHolder NULL");
  //    }
  //  }
  //
  //  GPUVariableValueSet* fs = new GPUVariableValueSet();
  //  fs->addUniformValue(MODELVIEW, new GPUUniformValueModelview(matrixHolders, _nFeatures), false);
  //
  //  return fs;
  //}
  
  public final void addToGPUVariableSet(GPUVariableValueSet vs)
  {
    final Matrix44DHolder[] matrixHolders = new Matrix44DHolder[_nFeatures];
    for (int i = 0; i < _nFeatures; i++)
    {
      GLCameraGroupFeature f = ((GLCameraGroupFeature) _features[i]);
      matrixHolders[i] = f.getMatrixHolder();
      if (matrixHolders[i] == null)
      {
        ILogger.instance().logError("MatrixHolder NULL");
      }
    }
  
    vs.addUniformValue(GPUUniformKey.MODELVIEW, new GPUUniformValueModelview(matrixHolders, _nFeatures), false);
  }
}