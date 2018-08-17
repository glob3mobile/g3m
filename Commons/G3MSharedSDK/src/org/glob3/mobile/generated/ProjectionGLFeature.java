package org.glob3.mobile.generated;import java.util.*;

public class ProjectionGLFeature extends GLCameraGroupFeature
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public ProjectionGLFeature(Matrix44D projection)
  {
	  super(projection, GLFeatureID.GLF_PROJECTION);
  }

  public ProjectionGLFeature(Camera camera)
  {
	  super(camera.getProjectionMatrix44D(), GLFeatureID.GLF_PROJECTION);
  }
}
