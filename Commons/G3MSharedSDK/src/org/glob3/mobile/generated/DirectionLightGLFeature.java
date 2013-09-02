package org.glob3.mobile.generated; 
public class DirectionLightGLFeature extends GLFeature
{
  public DirectionLightGLFeature(Vector3D dir, Color lightColor, float ambientLight)
  {
     super(GLFeatureGroupName.LIGHTING_GROUP, GLFeatureID.GLF_DIRECTION_LIGTH);
    _values.addUniformValue(GPUUniformKey.AMBIENT_LIGHT, new GPUUniformValueFloat(ambientLight), false);
  
    Vector3D dirN = dir.normalized();
  
    _values.addUniformValue(GPUUniformKey.LIGHT_DIRECTION, new GPUUniformValueVec3Float((float)dirN.x(), (float)dirN.y(), (float)dirN.z()), false);
    _values.addUniformValue(GPUUniformKey.LIGHT_COLOR, new GPUUniformValueVec4Float(lightColor), false);
  
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
}