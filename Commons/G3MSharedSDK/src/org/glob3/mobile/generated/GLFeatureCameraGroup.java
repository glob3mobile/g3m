package org.glob3.mobile.generated; 
public class GLFeatureCameraGroup extends GLFeatureGroup
{
//  GPUVariableValueSet* applyAndCreateGPUVariableSet(GL* gl);
  public final GPUVariableValueSet createGPUVariableSet()
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
  
    GPUVariableValueSet fs = new GPUVariableValueSet();
    fs.addNewUniformValue(GPUUniformKey.MODELVIEW, new GPUUniformValueModelview(matrixHolders, _nFeatures));
  
    return fs;
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
}