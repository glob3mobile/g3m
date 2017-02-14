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

  public ViewportExtentGLFeature(Camera camera, ViewMode viewMode)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_VIEWPORT_EXTENT);
  
    int logicWidth = camera.getViewPortWidth();
    if (viewMode == ViewMode.STEREO)
    {
      logicWidth /= 2;
    }
  
    _extent = new GPUUniformValueVec2FloatMutable(logicWidth, camera.getViewPortHeight());
  
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
