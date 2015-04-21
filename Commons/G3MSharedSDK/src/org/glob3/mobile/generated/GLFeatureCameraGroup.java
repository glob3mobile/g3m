package org.glob3.mobile.generated; 
public class GLFeatureCameraGroup extends GLFeatureGroup
{
  public final void apply(GLFeatureSet features, GPUVariableValueSet vs, GLGlobalState state)
  {
    Matrix44DMultiplicationHolderBuilder modelViewHolderBuilder = new Matrix44DMultiplicationHolderBuilder();
    Matrix44DMultiplicationHolderBuilder modelTransformHolderBuilder = new Matrix44DMultiplicationHolderBuilder();
  
    boolean normalsAvailable = false;
  
    final int featuresSize = features.size();
    for (int i = 0; i < featuresSize; i++)
    {
      final GLFeature f = features.get(i);
      final GLFeatureGroupName group = f._group;
      final GLFeatureID id = f._id;
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
  
    if (modelViewHolderBuilder.size() > 0)
    {
      Matrix44DProvider modelViewProvider = modelViewHolderBuilder.create();
  
      vs.addUniformValue(GPUUniformKey.MODELVIEW, new GPUUniformValueMatrix4(modelViewProvider), false);
  
      modelViewProvider._release();
    }
  
    modelViewHolderBuilder.dispose();
    modelTransformHolderBuilder.dispose();
  }
}