package org.glob3.mobile.generated; 
public class ModelViewGLFeature extends GLCameraGroupFeature
{
  public ModelViewGLFeature(Matrix44D modelview)
  {
     super(modelview, GLFeatureID.GLF_MODEL_VIEW);
  }

  public ModelViewGLFeature(Camera cam)
  {
     super(cam.getModelViewMatrix44D(), GLFeatureID.GLF_MODEL_VIEW);
  }
}