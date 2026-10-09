package org.glob3.mobile.generated;
public class BillboardGLFeature extends GLFeature
{
  public void dispose()
  {
    super.dispose();
  }

  private GPUUniformValueVec2FloatMutable _size;
  private GPUUniformValueVec2FloatMutable _anchor;
  private GPUUniformValueVec4FloatMutable _colorFactor;
  private final boolean _premultipliedAlpha;

  /** premultipliedAlpha: the texture's, so fading multiplies the color too, not only the alpha */
  public BillboardGLFeature(float billboardWidth, float billboardHeight, float anchorU, float anchorV, boolean premultipliedAlpha)
  {
     super(GLFeatureGroupName.NO_GROUP, GLFeatureID.GLF_BILLBOARD);
     _premultipliedAlpha = premultipliedAlpha;
  
    _anchor = new GPUUniformValueVec2FloatMutable(anchorU, anchorV);
    _values.addUniformValue(GPUUniformKey.BILLBOARD_ANCHOR, _anchor, false);
  
  
    _size = new GPUUniformValueVec2FloatMutable(billboardWidth, billboardHeight);
    _values.addUniformValue(GPUUniformKey.TEXTURE_EXTENT, _size, false);
  
    _values.addUniformValue(GPUUniformKey.BILLBOARD_POSITION, new GPUUniformValueVec4Float(0, 0, 0, 1), false);
  
    _colorFactor = new GPUUniformValueVec4FloatMutable(1, 1, 1, 1);
    _values.addUniformValue(GPUUniformKey.BILLBOARD_COLOR_FACTOR, _colorFactor, false);
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

  public final void changeAlpha(float alpha)
  {
    if (_premultipliedAlpha)
    {
      _colorFactor.changeValue(alpha, alpha, alpha, alpha);
    }
    else
    {
      _colorFactor.changeValue(1, 1, 1, alpha);
    }
  }
}