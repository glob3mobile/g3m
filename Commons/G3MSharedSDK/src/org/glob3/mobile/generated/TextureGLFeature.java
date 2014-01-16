package org.glob3.mobile.generated; 
public class TextureGLFeature extends GLColorGroupFeature
{
  private IGLTextureId _texID = null;

  public void dispose()
  {
    super.dispose();
  }

  public TextureGLFeature(IGLTextureId texID, IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor, float translateU, float translateV, float scaleU, float scaleV, float rotationAngleInRadians, float rotationCenterU, float rotationCenterV)
  {
     super(GLFeatureID.GLF_TEXTURE, 4, blend, sFactor, dFactor);
     _texID = texID;
    GPUAttributeValueVec2Float value = new GPUAttributeValueVec2Float(texCoords, arrayElementSize, index, stride, normalized);
    _values.addAttributeValue(GPUAttributeKey.TEXTURE_COORDS, value, false);
  
    _values.addUniformValue(GPUUniformKey.TRANSLATION_TEXTURE_COORDS, new GPUUniformValueVec2Float(translateU, translateV), false);
  
    _values.addUniformValue(GPUUniformKey.SCALE_TEXTURE_COORDS, new GPUUniformValueVec2Float(scaleU, scaleV), false);
  
    if (rotationAngleInRadians != 0.0)
    {
      _values.addUniformValue(GPUUniformKey.ROTATION_CENTER_TEXTURE_COORDS, new GPUUniformValueVec2Float(rotationCenterU, rotationCenterV), false);
  
      _values.addUniformValue(GPUUniformKey.ROTATION_ANGLE_TEXTURE_COORDS, new GPUUniformValueFloat(rotationAngleInRadians), false);
    }
  }

  public TextureGLFeature(IGLTextureId texID, IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor)
  {
     super(GLFeatureID.GLF_TEXTURE, 4, blend, sFactor, dFactor);
     _texID = texID;
  
    GPUAttributeValueVec2Float value = new GPUAttributeValueVec2Float(texCoords, arrayElementSize, index, stride, normalized);
    _values.addAttributeValue(GPUAttributeKey.TEXTURE_COORDS, value, false);
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    blendingOnGlobalGLState(state);
    state.bindTexture(_texID);
  }
}