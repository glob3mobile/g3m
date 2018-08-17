package org.glob3.mobile.generated;//////////////////////////

public class PointShapeGLFeature extends GLFeature
{
  public void dispose()
  {
    super.dispose();
  }

  private GPUUniformValueVec4Float _borderColor;

  public PointShapeGLFeature(Color borderColor)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_POINT_SHAPE);
     _borderColor = new GPUUniformValueVec4Float(borderColor);
    _values.addUniformValue(GPUUniformKey.ROUNDED_POINT_BORDER_COLOR, _borderColor, false);
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
}
