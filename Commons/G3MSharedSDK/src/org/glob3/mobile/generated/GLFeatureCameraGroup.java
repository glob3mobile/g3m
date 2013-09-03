package org.glob3.mobile.generated; 
public class GLFeatureCameraGroup extends GLFeatureGroup
{
//  void applyOnGlobalGLState(GLGlobalState* state) {}
//  void addToGPUVariableSet(GPUVariableValueSet* vs);

  public final void apply(GLFeatureSet features, GPUVariableValueSet vs, GLGlobalState state)
  {
  
    final int size = features.size();
  
    Matrix44DMultiplicationHolderBuilder modelViewHolderBuilder = new Matrix44DMultiplicationHolderBuilder();
    Matrix44DMultiplicationHolderBuilder modelTransformHolderBuilder = new Matrix44DMultiplicationHolderBuilder();
  
    boolean normalsAvailable = false;
  
    for (int i = 0; i < size; i++)
    {
      final GLFeature f = features.get(i);
      final GLFeatureGroupName group = f.getGroup();
      final GLFeatureID id = f.getID();
      if (group == GLFeatureGroupName.CAMERA_GROUP)
      {
        GLCameraGroupFeature cf = ((GLCameraGroupFeature) f);
  
        if (id == GLFeatureID.GLF_MODEL_TRANSFORM)
        {
          modelTransformHolderBuilder.add(cf.getMatrixHolder());
        }
        else
        {
          modelViewHolderBuilder.add(cf.getMatrixHolder());
        }
  
      }
      else
      {
        if (group == GLFeatureGroupName.LIGHTING_GROUP)
        {
          if (id == GLFeatureID.GLF_VERTEX_NORMAL)
          {
            normalsAvailable = true;
          }
        }
      }
    }
  
    if (modelTransformHolderBuilder.size() > 0)
    {
      Matrix44DProvider prov = modelTransformHolderBuilder.create();
      modelViewHolderBuilder.add(prov);
  
      if (normalsAvailable)
      {
        vs.addUniformValue(GPUUniformKey.MODEL, new GPUUniformValueMatrix4(prov), false); //FOR LIGHTING
      }
  
      prov._release();
    }
  
    Matrix44DProvider modelViewProvider = modelViewHolderBuilder.create();
  
    vs.addUniformValue(GPUUniformKey.MODELVIEW, new GPUUniformValueMatrix4(modelViewProvider), false);
  
    modelViewProvider._release();
  }
}