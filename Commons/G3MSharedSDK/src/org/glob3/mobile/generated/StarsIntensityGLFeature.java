package org.glob3.mobile.generated;
// Multiplies the brightness of every star of the Stars program
public class StarsIntensityGLFeature extends GLFeature
{
  public void dispose()
  {
    super.dispose();
  }

  private GPUUniformValueFloatMutable _intensity;

  public StarsIntensityGLFeature(float intensity)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_STARS_INTENSITY);
    _intensity = new GPUUniformValueFloatMutable(intensity);
    _values.addUniformValue(GPUUniformKey.STARS_INTENSITY, _intensity, false);
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }

  public final void changeIntensity(float intensity)
  {
    _intensity.changeValue(intensity);
  }
}