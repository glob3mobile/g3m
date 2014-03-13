package org.glob3.mobile.generated; 
public class ProjectionGLFeature extends GLCameraGroupFeature
{
  public void dispose()
  {
    super.dispose();
  }

  public ProjectionGLFeature(Matrix44D projection)
  {
     super(projection, GLFeatureID.GLF_PROJECTION);
  }

  public ProjectionGLFeature(Camera cam)
  {
     super(cam.getProjectionMatrix44D(), GLFeatureID.GLF_PROJECTION);
  }
}