package org.glob3.mobile.generated;import java.util.*;

public class ModelViewGLFeature extends GLCameraGroupFeature
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
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
