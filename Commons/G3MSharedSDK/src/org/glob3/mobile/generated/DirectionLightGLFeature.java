package org.glob3.mobile.generated; 
public class DirectionLightGLFeature extends GLFeature
{

  private GPUUniformValueVec3FloatMutable _lightDirectionUniformValue;

  public DirectionLightGLFeature(Vector3D dir, Color lightColor, float ambientLight)
  {
     super(GLFeatureGroupName.LIGHTING_GROUP, GLFeatureID.GLF_DIRECTION_LIGTH);
    _values.addUniformValue(GPUUniformKey.AMBIENT_LIGHT, new GPUUniformValueFloat(ambientLight), false);
  
    Vector3D dirN = dir.normalized();
  
    _lightDirectionUniformValue = new GPUUniformValueVec3FloatMutable((float) dirN._x, (float) dirN._y, (float) dirN._z);
  
    _values.addUniformValue(GPUUniformKey.LIGHT_DIRECTION, _lightDirectionUniformValue, false);
    _values.addUniformValue(GPUUniformKey.LIGHT_COLOR, new GPUUniformValueVec4Float(lightColor), false);
  
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }

  public final void setLightDirection(Vector3D lightDir)
  {
    Vector3D dirN = lightDir.normalized();
    _lightDirectionUniformValue.changeValue((float)dirN._x, (float)dirN._y, (float)dirN._z);
  }
}