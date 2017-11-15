package org.glob3.mobile.generated; 
public class BillboardGLFeature extends GLFeature
{
  public void dispose()
  {
    super.dispose();
  }

  private GPUUniformValueVec2FloatMutable _size;
  private GPUUniformValueVec2FloatMutable _anchor;

  public BillboardGLFeature(Vector3D position, float billboardWidth, float billboardHeight, float anchorU, float anchorV)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_BILLBOARD);
  
    _anchor = new GPUUniformValueVec2FloatMutable(anchorU, anchorV);
    _values.addUniformValue(GPUUniformKey.BILLBOARD_ANCHOR, _anchor, false);
  
  
    _size = new GPUUniformValueVec2FloatMutable(billboardWidth, billboardHeight);
    _values.addUniformValue(GPUUniformKey.TEXTURE_EXTENT, _size, false);
  
    _values.addUniformValue(GPUUniformKey.BILLBOARD_POSITION, new GPUUniformValueVec4Float((float) position._x, (float) position._y, (float) position._z, 1), false);
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    state.disableDepthTest();
    state.disableCullFace();
    state.disablePolygonOffsetFill();
  }

  public final void changeSize(int textureWidth, int textureHeight)
  {
    _size.changeValue(textureWidth, textureHeight);
  }

  public final void changeAnchor(float anchorU, float anchorV)
  {
    _anchor.changeValue(anchorU, anchorV);
  }
}