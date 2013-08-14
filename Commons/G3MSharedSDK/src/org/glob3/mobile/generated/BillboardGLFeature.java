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

public class BillboardGLFeature extends GLFeature
{
  public BillboardGLFeature(Vector3D position, int textureWidth, int textureHeight)
  {
     super(GLFeatureGroupName.NO_GROUP);
    _values.addUniformValue(GPUUniformKey.TEXTURE_EXTENT, new GPUUniformValueVec2Float(textureWidth, textureHeight), false);
  
    _values.addUniformValue(GPUUniformKey.BILLBOARD_POSITION, new GPUUniformValueVec4Float((float)position._x, (float)position._y, (float)position._z, 1.0), false);
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    state.disableDepthTest();
    state.disableCullFace();
    state.disPolygonOffsetFill();
  }
}