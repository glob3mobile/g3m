package org.glob3.mobile.generated;
// Ribbon half-width is resolved in the vertex shader as max(widthInMeters, minWidthInPixels)
public class RibbonWidthGLFeature extends GLFeature
{
  public void dispose()
  {
    super.dispose();
  }

  private GPUUniformValueVec2FloatMutable _width;

  public RibbonWidthGLFeature(float widthInMeters, float minWidthInPixels)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_RIBBON_WIDTH);
    _width = new GPUUniformValueVec2FloatMutable(widthInMeters, minWidthInPixels);
  
    _values.addUniformValue(GPUUniformKey.RIBBON_WIDTH, _width, false);
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }

  public final void changeWidth(float widthInMeters, float minWidthInPixels)
  {
    _width.changeValue(widthInMeters, minWidthInPixels);
  }
}