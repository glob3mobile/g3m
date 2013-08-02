package org.glob3.mobile.generated; 
public class GLFeatureCameraGroup extends GLFeatureGroup
{
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
  public final void addToGPUVariableSet(GPUVariableValueSet vs)
  {
  
    Matrix44DProvider[] modelTransformHolders = new const Matrix44DProvider[_nFeatures];
  
    int modelTransformCount = 0;
    for (int i = 0; i < _nFeatures; i++)
    {
      GLCameraGroupFeature f = ((GLCameraGroupFeature) _features[i]);
      GLCameraGroupFeatureType t = f.getType();
      switch (t)
      {
        case F_PROJECTION:
          modelTransformHolders[0] = f.getMatrixHolder();
          break;
        case F_CAMERA_MODEL:
          modelTransformHolders[1] = f.getMatrixHolder();
          break;
        case F_MODEL_TRANSFORM:
        {
          final Matrix44D m = f.getMatrixHolder().getMatrix();
  
          if (!m.isScaleMatrix() && !m.isTranslationMatrix())
          {
            modelTransformHolders[2 + modelTransformCount++] = f.getMatrixHolder();
          }
        }
          break;
        default:
          ILogger.instance().logError("Error on GLFeatureCameraGroup::addToGPUVariableSet");
          break;
      }
    }
  
    Matrix44DProvider modelViewProvider = new Matrix44DMultiplicationHolder(modelTransformHolders,modelTransformCount+2);
  
    if (modelTransformCount > 0)
    {
      Matrix44DProvider modelProvider = new Matrix44DMultiplicationHolder(modelTransformHolders[2], modelTransformCount);
      vs.addUniformValue(GPUUniformKey.MODEL, new GPUUniformValueMatrix4(modelProvider), false);
  
  //    const Matrix44D* mv1 = modelViewProvider->getMatrix();
  //
  //    const Matrix44D* proj = modelTransformHolders[0]->getMatrix();
  //    const Matrix44D* camMod = modelTransformHolders[1]->getMatrix();
  //
  //    const Matrix44D* mv2 = proj->createMultiplication(*camMod)->createMultiplication(*modelProvider->getMatrix());
  //
  //    if (!mv1->isEqualsTo(*mv2)){
  //      ILogger::instance()->logError("...");
  //    }
  
    }
    else
    {
      final Matrix44D id = Matrix44D.createIdentity();
      vs.addUniformValue(GPUUniformKey.MODEL, new GPUUniformValueMatrix4(id), false);
      id._release();
    }
  
    vs.addUniformValue(GPUUniformKey.MODELVIEW, new GPUUniformValueMatrix4(modelViewProvider), false);
  /*
    const Matrix44DProvider** modelTransformHolders = new const Matrix44DProvider*[_nFeatures-2];
    const Matrix44DProvider** cameraHolders = new const Matrix44DProvider*[2];
  
    int modelTransformCount = 0;
    for (int i = 0; i < _nFeatures; i++){
      GLCameraGroupFeature* f = ((GLCameraGroupFeature*) _features[i]);
      GLCameraGroupFeatureType t = f->getType();
      switch (t) {
        case F_PROJECTION:
          cameraHolders[0] = f->getMatrixHolder();
          break;
        case F_CAMERA_MODEL:
          cameraHolders[1] = f->getMatrixHolder();
          break;
        case F_MODEL_TRANSFORM:
          modelTransformHolders[modelTransformCount++] = f->getMatrixHolder();
          break;
        default:
          ILogger::instance()->logError("Error on GLFeatureCameraGroup::addToGPUVariableSet");
          break;
      }
    }
  
    if (modelTransformCount > 0){
  
      const Matrix44DProvider** modelviewHolders = new const Matrix44DProvider*[2];
  
      modelviewHolders[0] = new Matrix44DMultiplicationHolder(cameraHolders, 2);
      modelviewHolders[1] = new Matrix44DMultiplicationHolder(modelTransformHolders, modelTransformCount);
  
      vs->addUniformValue(MODEL, new GPUUniformValueMatrix4(modelviewHolders[1]), false);
      vs->addUniformValue(MODELVIEW, new GPUUniformValueMatrix4(new Matrix44DMultiplicationHolder(modelviewHolders,2)), false);
  
    } else{
      const Matrix44D* id = Matrix44D::createIdentity();
      vs->addUniformValue(MODEL, new GPUUniformValueMatrix4(id), false);
      id->_release();
  
      vs->addUniformValue(MODELVIEW, new GPUUniformValueMatrix4(new Matrix44DMultiplicationHolder(cameraHolders,2)), false);
    }
  */
  /*
  #ifdef C_CODE
    const Matrix44DProvider** matrixHolders = new const Matrix44DProvider*[_nFeatures-2];
  #endif
  #ifdef JAVA_CODE
    final Matrix44DProvider[] matrixHolders = new Matrix44DProvider[_nFeatures-2];
  #endif
  
    for (int i = 0; i < _nFeatures; i++){
      GLCameraGroupFeature* f = ((GLCameraGroupFeature*) _features[i]);
      GLCameraGroupFeatureType t = f->getType();
      switch (t) {
        case F_PROJECTION:
          vs->addUniformValue(PROJECTION, new GPUUniformValueMatrix4(f->getMatrixHolder()), false);
          break;
        case F_CAMERA_MODEL:
          vs->addUniformValue(CAMERA_MODEL, new GPUUniformValueMatrix4(f->getMatrixHolder()), false);
          break;
        case F_MODEL_TRANSFORM:
          matrixHolders[i-2] = f->getMatrixHolder();
        default:
          break;
      }
    }
  
    if (_nFeatures> 2){
      vs->addUniformValue(MODEL, new GPUUniformValueMatrix4(matrixHolders, _nFeatures-2), false);
    } else{
      const Matrix44D* id = Matrix44D::createIdentity();
      vs->addUniformValue(MODEL, new GPUUniformValueMatrix4(id), false);
      id->_release();
      delete[] matrixHolders;
    }
   */
  /*
  #ifdef C_CODE
    const Matrix44DProvider** matrixHolders = new const Matrix44DProvider*[_nFeatures];
  #endif
  #ifdef JAVA_CODE
    final Matrix44DProvider[] matrixHolders = new Matrix44DProvider[_nFeatures];
  #endif
    for (int i = 0; i < _nFeatures; i++){
      GLCameraGroupFeature* f = ((GLCameraGroupFeature*) _features[i]);
      matrixHolders[i] = f->getMatrixHolder();
      if (matrixHolders[i] == NULL){
        ILogger::instance()->logError("MatrixHolder NULL");
      }
    }
  
    vs->addUniformValue(MODELVIEW, new GPUUniformValueMatrix4(matrixHolders, _nFeatures), false);
   */
  }
}