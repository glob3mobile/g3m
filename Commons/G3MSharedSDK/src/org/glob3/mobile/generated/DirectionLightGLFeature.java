package org.glob3.mobile.generated; 
public class DirectionLightGLFeature extends GLFeature
{
  private GPUUniformValueVec3FloatMutable _lightDirectionUniformValue;

  public void dispose()
  {
    super.dispose();
  }

  public DirectionLightGLFeature(Vector3D diffuseLightDirection, Color diffuseLightColor, Color ambientLightColor)
  {
     super(GLFeatureGroupName.LIGHTING_GROUP, GLFeatureID.GLF_DIRECTION_LIGTH);
    _values.addUniformValue(GPUUniformKey.AMBIENT_LIGHT_COLOR, new GPUUniformValueVec3Float(ambientLightColor), false);
  
    Vector3D dirN = diffuseLightDirection.normalized();
  
    _lightDirectionUniformValue = new GPUUniformValueVec3FloatMutable((float) dirN._x, (float) dirN._y, (float) dirN._z);
  
    _values.addUniformValue(GPUUniformKey.DIFFUSE_LIGHT_DIRECTION, _lightDirectionUniformValue, false);
    _values.addUniformValue(GPUUniformKey.DIFFUSE_LIGHT_COLOR, new GPUUniformValueVec3Float(diffuseLightColor), false);
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