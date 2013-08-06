package org.glob3.mobile.generated; 
public class ViewportExtentGLFeature extends GLFeature
{

  //BillboardGLFeature::BillboardGLFeature(int textureWidth, int textureHeight, int viewportWidth, int viewportHeight):
  //GLFeature(NO_GROUP){
  //
  //  _texExtent = new GPUUniformValueVec2Float(textureWidth, textureHeight);
  //  _values.addUniformValue(TEXTURE_EXTENT, _texExtent, false);
  //
  //  _viewportExtent = new GPUUniformValueVec2Float(viewportWidth, viewportHeight);
  //  _values.addUniformValue(VIEWPORT_EXTENT, _viewportExtent, false);
  //}
  
  public ViewportExtentGLFeature(int viewportWidth, int viewportHeight)
  {
     super(GLFeatureGroupName.NO_GROUP);
    _values.addUniformValue(GPUUniformKey.VIEWPORT_EXTENT, new GPUUniformValueVec2Float(viewportWidth, viewportHeight), false);
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
}