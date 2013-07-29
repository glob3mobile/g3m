package org.glob3.mobile.generated; 
public class BillboardGLFeature extends GLFeature
{

  private GPUUniformValueVec2Float _texExtent;
  private GPUUniformValueVec2Float _viewportExtent;



  public BillboardGLFeature(int textureWidth, int textureHeight, int viewportWidth, int viewportHeight)
  {
     super(GLFeatureGroupName.NO_GROUP);
  
    _texExtent = new GPUUniformValueVec2Float(textureWidth, textureHeight);
    _values.addUniformValue(GPUUniformKey.TEXTURE_EXTENT, _texExtent, false);
  
    _viewportExtent = new GPUUniformValueVec2Float(viewportWidth, viewportHeight);
    _values.addUniformValue(GPUUniformKey.VIEWPORT_EXTENT, _viewportExtent, false);
  }

  public void dispose()
  {
  //  _texExtent->_release();
  //  _viewportExtent->_release();
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }

}