package org.glob3.mobile.generated; 
public class ProjectionGLFeature extends GLCameraGroupFeature
{
  public ProjectionGLFeature(Matrix44D projection)
  {
     super(projection);
  }
  public ProjectionGLFeature(Camera cam)
  {
     super(cam.getProjectionMatrix44D());
  }
}