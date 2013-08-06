package org.glob3.mobile.generated; 
//class BillboardGLFeature: public GLFeature{
//
//  GPUUniformValueVec2Float* _texExtent;
//  GPUUniformValueVec2Float* _viewportExtent;
//  
//
//public:
//
//  BillboardGLFeature(int textureWidth, int textureHeight, int viewportWidth, int viewportHeight);
//
//  ~BillboardGLFeature();
//
//  void applyOnGlobalGLState(GLGlobalState* state)  const {}
//
//};

public class TextureExtentGLFeature extends GLFeature
{
  public TextureExtentGLFeature(int textureWidth, int textureHeight)
  {
     super(GLFeatureGroupName.NO_GROUP);
    _values.addUniformValue(GPUUniformKey.TEXTURE_EXTENT, new GPUUniformValueVec2Float(textureWidth, textureHeight), false);
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
}