package org.glob3.mobile.generated;
public class CameraPositionGLFeature extends GLFeature
{
  public void dispose()
  {
    super.dispose();
  }

  private final boolean _groundHazePass;
  private GPUUniformValueVec3FloatMutable _camPos;
  private GPUUniformValueVec3FloatMutable _spaceColor;
  private GPUUniformValueVec3FloatMutable _horizonColor;
  private GPUUniformValueVec3FloatMutable _skyRayleighScattering;

  public CameraPositionGLFeature(Camera cam, boolean groundHazePass, Color spaceColor, Color horizonColor, Vector3F skyRayleighScattering)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_CAMERA_POSITION);
     _groundHazePass = groundHazePass;
    final Vector3D p = cam.getCartesianPosition();
    _camPos = new GPUUniformValueVec3FloatMutable((float) p._x, (float) p._y, (float) p._z);
    _values.addUniformValue(GPUUniformKey.CAMERA_POSITION, _camPos, false);
    _values.addUniformValue(GPUUniformKey.GROUND_HAZE_PASS, new GPUUniformValueFloat(groundHazePass ? 1.0f : 0.0f), false);
    _spaceColor = new GPUUniformValueVec3FloatMutable(spaceColor._red, spaceColor._green, spaceColor._blue);
    _values.addUniformValue(GPUUniformKey.SPACE_COLOR, _spaceColor, false);
    _horizonColor = new GPUUniformValueVec3FloatMutable(horizonColor._red, horizonColor._green, horizonColor._blue);
    _values.addUniformValue(GPUUniformKey.HORIZON_COLOR, _horizonColor, false);
    _skyRayleighScattering = new GPUUniformValueVec3FloatMutable(skyRayleighScattering._x, skyRayleighScattering._y, skyRayleighScattering._z);
    _values.addUniformValue(GPUUniformKey.SKY_RAYLEIGH_SCATTERING, _skyRayleighScattering, false);
  }

  // The sky adds its light and dims what is behind it (the background, the stars) by its transmittance;
  // the haze is a fog colour faded in by its opacity
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    state.enableBlend();
    state.setBlendFactors(_groundHazePass ? GLBlendFactor.srcAlpha() : GLBlendFactor.one(), GLBlendFactor.oneMinusSrcAlpha());
  }

  public final void update(Camera cam, Color spaceColor)
  {
    final Vector3D p = cam.getCartesianPosition();
    _camPos.changeValue((float) p._x, (float) p._y, (float) p._z);
    _spaceColor.changeValue(spaceColor._red, spaceColor._green, spaceColor._blue);
  }

  public final void changeAtmosphereColors(Color horizonColor, Vector3F skyRayleighScattering)
  {
    _horizonColor.changeValue(horizonColor._red, horizonColor._green, horizonColor._blue);
    _skyRayleighScattering.changeValue(skyRayleighScattering._x, skyRayleighScattering._y, skyRayleighScattering._z);
  }
}