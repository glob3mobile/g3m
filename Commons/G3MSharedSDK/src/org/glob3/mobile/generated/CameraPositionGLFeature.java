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

  public CameraPositionGLFeature(Camera cam, boolean groundHazePass, Color spaceColor)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_CAMERA_POSITION);
     _groundHazePass = groundHazePass;
    final Vector3D p = cam.getCartesianPosition();
    _camPos = new GPUUniformValueVec3FloatMutable((float) p._x, (float) p._y, (float) p._z);
    _values.addUniformValue(GPUUniformKey.CAMERA_POSITION, _camPos, false);
    _values.addUniformValue(GPUUniformKey.GROUND_HAZE_PASS, new GPUUniformValueFloat(groundHazePass ? 1.0f : 0.0f), false);
    _spaceColor = new GPUUniformValueVec3FloatMutable(spaceColor._red, spaceColor._green, spaceColor._blue);
    _values.addUniformValue(GPUUniformKey.SPACE_COLOR, _spaceColor, false);
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
}