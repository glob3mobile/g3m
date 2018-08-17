package org.glob3.mobile.generated;import java.util.*;

public class ColorGLFeature extends GLColorGroupFeature
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public ColorGLFeature(IFloatBuffer colors, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor)
  {
	  super(GLFeatureID.GLF_COLOR, 3, blend, sFactor, dFactor);
	GPUAttributeValueVec4Float value = new GPUAttributeValueVec4Float(colors, arrayElementSize, index, stride, normalized);
	_values.addAttributeValue(GPUAttributeKey.COLOR, value, false);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void applyOnGlobalGLState(GLGlobalState* state) const
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
	blendingOnGlobalGLState(state);
  }
}
