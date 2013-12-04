package org.glob3.mobile.generated; 
public class DepthRangeGLFeature extends GLFeature
{
  public void dispose()
  {
    super.dispose();
  }

  public DepthRangeGLFeature(float far, float near)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_DEPTH_RANGE);
    _values.addUniformValue(GPUUniformKey.DEPTH_FAR, new GPUUniformValueFloat(far), false);
  
    _values.addUniformValue(GPUUniformKey.DEPTH_NEAR, new GPUUniformValueFloat(near), false);
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
}