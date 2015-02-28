package org.glob3.mobile.generated; 
public class ViewportExtentGLFeature extends GLFeature
{
  public void dispose()
  {
    super.dispose();
  }

  private GPUUniformValueVec2FloatMutable _extent;

  public ViewportExtentGLFeature(int viewportWidth, int viewportHeight)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_VIEWPORT_EXTENT);
    _extent = new GPUUniformValueVec2FloatMutable(viewportWidth, viewportHeight);
  
    _values.addUniformValue(GPUUniformKey.VIEWPORT_EXTENT, _extent, false);
  }

  public ViewportExtentGLFeature(Camera camera)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_VIEWPORT_EXTENT);
    _extent = new GPUUniformValueVec2FloatMutable(camera.getViewPortWidth(), camera.getViewPortHeight());
  
    _values.addUniformValue(GPUUniformKey.VIEWPORT_EXTENT, _extent, false);
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }

  public final void changeExtent(int viewportWidth, int viewportHeight)
  {
    _extent.changeValue(viewportWidth, viewportHeight);
  }
}