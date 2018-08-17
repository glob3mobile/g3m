package org.glob3.mobile.generated;import java.util.*;

//////////////////////////

public class PointShapeGLFeature extends GLFeature
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  private GPUUniformValueVec4Float _borderColor;

  public PointShapeGLFeature(Color borderColor)
  {
	  super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_POINT_SHAPE);
	  _borderColor = new GPUUniformValueVec4Float(borderColor);
	_values.addUniformValue(GPUUniformKey.ROUNDED_POINT_BORDER_COLOR, _borderColor, false);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void applyOnGlobalGLState(GLGlobalState* state) const
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
}
