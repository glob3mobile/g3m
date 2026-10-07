package org.glob3.mobile.generated;
// Multiplies the brightness of every star of the Stars program
public class StarsGLFeature extends GLFeature
{
  public void dispose()
  {
    super.dispose();
  }

  private GPUUniformValueFloatMutable _fullStarMagnitude;

  public StarsGLFeature(float fullStarMagnitude, float starSizeExponent)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_STARS);
    _fullStarMagnitude = new GPUUniformValueFloatMutable(fullStarMagnitude);
    _values.addUniformValue(GPUUniformKey.FULL_STAR_MAGNITUDE, _fullStarMagnitude, false);
    _values.addUniformValue(GPUUniformKey.STAR_SIZE_EXPONENT, new GPUUniformValueFloat(starSizeExponent), false);
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }

  public final void changeFullStarMagnitude(float fullStarMagnitude)
  {
    _fullStarMagnitude.changeValue(fullStarMagnitude);
  }
}