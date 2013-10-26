package org.glob3.mobile.generated; 
public class TextureCoordsGLFeature extends PriorityGLFeature
{
  public TextureCoordsGLFeature(IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean coordsTransformed, Vector2D translate, Vector2D scale)
  {
     super(GLFeatureGroupName.COLOR_GROUP, GLFeatureID.GLF_TEXTURE_COORDS, 4);
  
    GPUAttributeValueVec2Float value = new GPUAttributeValueVec2Float(texCoords, arrayElementSize, index, stride, normalized);
    _values.addAttributeValue(GPUAttributeKey.TEXTURE_COORDS, value, false);
  
    if (coordsTransformed)
    {
      _values.addUniformValue(GPUUniformKey.TRANSLATION_TEXTURE_COORDS, new GPUUniformValueVec2Float((float)translate._x, (float)translate._y), false);
      _values.addUniformValue(GPUUniformKey.SCALE_TEXTURE_COORDS, new GPUUniformValueVec2Float((float)scale._x, (float)scale._y), false);
    }
  
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  
  }
}