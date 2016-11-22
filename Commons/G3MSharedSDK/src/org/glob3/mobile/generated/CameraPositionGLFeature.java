package org.glob3.mobile.generated;
/////////////////////////////////////////////////////////

public class CameraPositionGLFeature extends GLFeature
{
  public void dispose()
  {
    super.dispose();
  }

  private GPUUniformValueVec3FloatMutable _camPos;

  public CameraPositionGLFeature(Camera cam)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_CAMERA_POSITION);
    final Vector3D p = cam.getCartesianPosition();
    _camPos = new GPUUniformValueVec3FloatMutable((float) p._x, (float) p._y, (float) p._z);
    _values.addUniformValue(GPUUniformKey.CAMERA_POSITION, _camPos, false);
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    //Used for atmospheric blending
    state.enableBlend();
    state.setBlendFactors(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
  }

  public final void update(Camera cam)
  {
    final Vector3D p = cam.getCartesianPosition();
    _camPos.changeValue((float) p._x, (float) p._y, (float) p._z);
  }
}
