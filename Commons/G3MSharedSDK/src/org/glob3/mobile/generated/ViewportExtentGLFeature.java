package org.glob3.mobile.generated; 
public class ViewportExtentGLFeature extends GLFeature
{
  public ViewportExtentGLFeature(int viewportWidth, int viewportHeight)
  {
     super(GLFeatureGroupName.NO_GROUP);
    _values.addUniformValue(GPUUniformKey.VIEWPORT_EXTENT, new GPUUniformValueVec2Float(viewportWidth, viewportHeight), false);
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
}