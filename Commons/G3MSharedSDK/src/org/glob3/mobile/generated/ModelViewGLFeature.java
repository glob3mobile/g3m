package org.glob3.mobile.generated; 
public class ModelViewGLFeature extends GLCameraGroupFeature
{
  public void dispose()
  {
    super.dispose();
  }

  public ModelViewGLFeature(Matrix44D modelview)
  {
     super(modelview, GLFeatureID.GLF_MODEL_VIEW);
  }

  public ModelViewGLFeature(Camera camera)
  {
     super(camera.getModelViewMatrix44D(), GLFeatureID.GLF_MODEL_VIEW);
  }
}