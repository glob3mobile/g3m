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
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_CAM_POSITION);
    Vector3D p = cam.getCartesianPosition();
    _camPos = new GPUUniformValueVec3FloatMutable(p._x, p._y, p._z);
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
    Vector3D p = cam.getCartesianPosition();
    _camPos.changeValue(p._x, p._y, p._z);
  }
}