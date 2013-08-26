package org.glob3.mobile.generated; 
public class GLFeatureCameraGroup extends GLFeatureGroup
{
//  void applyOnGlobalGLState(GLGlobalState* state) {}
//  void addToGPUVariableSet(GPUVariableValueSet* vs);

  public final void apply(GLFeatureSet features, GPUVariableValueSet vs, GLGlobalState state)
  {
  
    final int size = features.size();
    Matrix44DProvider[] modelTransformHolders = new Matrix44DProvider[size];
  
    int modelViewCount = 0;
    for (int i = 0; i < size; i++)
    {
      final GLFeature f = features.get(i);
      if (f.getGroup() == GLFeatureGroupName.CAMERA_GROUP)
      {
        GLCameraGroupFeature cf = ((GLCameraGroupFeature) f);
        modelTransformHolders[modelViewCount++] = cf.getMatrixHolder();
      }
    }
  
    Matrix44DProvider modelViewProvider = new Matrix44DMultiplicationHolder(modelTransformHolders,modelViewCount);
    vs.addUniformValue(GPUUniformKey.MODELVIEW, new GPUUniformValueMatrix4(modelViewProvider, true), false);
  
    modelTransformHolders = null;
  }
}