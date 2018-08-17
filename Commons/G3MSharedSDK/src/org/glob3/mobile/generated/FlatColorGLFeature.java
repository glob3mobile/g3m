package org.glob3.mobile.generated;import java.util.*;

public class FlatColorGLFeature extends GLColorGroupFeature
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public FlatColorGLFeature(Color color, boolean blend, int sFactor)
  {
	  this(color, blend, sFactor, GLBlendFactor.oneMinusSrcAlpha());
  }
  public FlatColorGLFeature(Color color, boolean blend)
  {
	  this(color, blend, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
  }
  public FlatColorGLFeature(Color color)
  {
	  this(color, false, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: FlatColorGLFeature(const Color& color, boolean blend = false, int sFactor = GLBlendFactor::srcAlpha(), int dFactor = GLBlendFactor::oneMinusSrcAlpha()) : GLColorGroupFeature(GLF_FLATCOLOR, 2, blend, sFactor, dFactor)
  public FlatColorGLFeature(Color color, boolean blend, int sFactor, int dFactor)
  {
	  super(GLFeatureID.GLF_FLATCOLOR, 2, blend, sFactor, dFactor);
	_values.addUniformValue(GPUUniformKey.FLAT_COLOR, new GPUUniformValueVec4Float(color._red, color._green, color._blue, color._alpha), false);
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void applyOnGlobalGLState(GLGlobalState* state) const
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
	blendingOnGlobalGLState(state);
  }
}
