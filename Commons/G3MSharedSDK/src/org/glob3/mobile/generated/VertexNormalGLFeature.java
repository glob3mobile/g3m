package org.glob3.mobile.generated;import java.util.*;

public class VertexNormalGLFeature extends GLFeature
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public VertexNormalGLFeature(IFloatBuffer buffer, int arrayElementSize, int index, boolean normalized, int stride)
  {
	  super(GLFeatureGroupName.LIGHTING_GROUP, GLFeatureID.GLF_VERTEX_NORMAL);
	_values.addAttributeValue(GPUAttributeKey.NORMAL, new GPUAttributeValueVec3Float(buffer, arrayElementSize, index, stride, normalized), false);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void applyOnGlobalGLState(GLGlobalState* state) const
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
}
