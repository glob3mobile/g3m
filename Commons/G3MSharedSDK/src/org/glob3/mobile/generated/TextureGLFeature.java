package org.glob3.mobile.generated; 
public class TextureGLFeature extends GLColorGroupFeature
{
  private final IGLTextureId _const* _texID = new IGLTextureId();

  public TextureGLFeature(IGLTextureId texID, IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor, boolean coordsTransformed, Vector2D translate, Vector2D scale)
  {
     super(4, blend, sFactor, dFactor);
     _texID = texID;
  //  _globalState->bindTexture(texID);
  
    GPUAttributeValueVec4Float value = new GPUAttributeValueVec4Float(texCoords, arrayElementSize, index, stride, normalized);
    _values.addNewAttributeValue(GPUAttributeKey.TEXTURE_COORDS, value);
  
    if (coordsTransformed)
    {
      _values.addNewUniformValue(GPUUniformKey.TRANSLATION_TEXTURE_COORDS, new GPUUniformValueVec2Float((float)translate._x, (float)translate._y));
      _values.addNewUniformValue(GPUUniformKey.SCALE_TEXTURE_COORDS, new GPUUniformValueVec2Float((float)scale._x, (float)scale._y));
    }
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    blendingOnGlobalGLState(state);
    state.bindTexture(_texID);
  }
}