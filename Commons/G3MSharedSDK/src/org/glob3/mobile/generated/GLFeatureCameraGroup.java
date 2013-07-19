package org.glob3.mobile.generated; 
public class GLFeatureCameraGroup extends GLFeatureGroup
{
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
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