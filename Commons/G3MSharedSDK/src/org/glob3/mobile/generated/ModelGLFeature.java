package org.glob3.mobile.generated;import java.util.*;

public class ModelGLFeature extends GLCameraGroupFeature
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
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
