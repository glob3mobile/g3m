package org.glob3.mobile.generated; 
public class ModelGLFeature extends GLCameraGroupFeature
{
  public void dispose()
  {
    super.dispose();
  }

  public ModelGLFeature(Matrix44D model)
  {
     super(model, GLFeatureID.GLF_MODEL);
  }

  public ModelGLFeature(Camera camera)
  {
     super(camera.getModelMatrix44D(), GLFeatureID.GLF_MODEL);
  }
}