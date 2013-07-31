package org.glob3.mobile.generated; 
public class ModelGLFeature extends GLCameraGroupFeature
{
  public ModelGLFeature(Matrix44D model)
  {
     super(model, GLCameraGroupFeatureType.F_CAMERA_MODEL);
  }
  public ModelGLFeature(Camera cam)
  {
     super(cam.getModelMatrix44D(), GLCameraGroupFeatureType.F_CAMERA_MODEL);
  }
}