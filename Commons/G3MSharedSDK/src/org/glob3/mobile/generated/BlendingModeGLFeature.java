package org.glob3.mobile.generated;import java.util.*;

public class BlendingModeGLFeature extends GLColorGroupFeature
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public BlendingModeGLFeature(boolean blend, int sFactor, int dFactor)
  {
	  super(GLFeatureID.GLF_BLENDING_MODE, 4, blend, sFactor, dFactor);
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void applyOnGlobalGLState(GLGlobalState* state) const
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
	blendingOnGlobalGLState(state);
  }
}
