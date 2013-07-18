package org.glob3.mobile.generated; 
public class ModelGLFeature extends GLCameraGroupFeature
{
  public ModelGLFeature(Matrix44D model)
  {
     super(model);
  }
  public ModelGLFeature(Camera cam)
  {
     super(cam.getModelMatrix44D());
  }
}