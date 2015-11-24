package org.glob3.mobile.generated; 
public class TextureCoordsGLFeature extends PriorityGLFeature
{
  public void dispose()
  {
    super.dispose();
  }

  public TextureCoordsGLFeature(IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean coordsTransformed, Vector2F translate, Vector2F scale)
  {
     super(GLFeatureGroupName.COLOR_GROUP, GLFeatureID.GLF_TEXTURE_COORDS, 4);
    GPUAttributeValueVec2Float value = new GPUAttributeValueVec2Float(texCoords, arrayElementSize, index, stride, normalized);
    _values.addAttributeValue(GPUAttributeKey.TEXTURE_COORDS, value, false);
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning ONLY TARGET 0 FOR SGNODES
    GPUUniformValueInt texUnit = new GPUUniformValueInt(0);
    _values.addUniformValue(GPUUniformKey.SAMPLER, texUnit, false);
  
    if (coordsTransformed)
    {
      _values.addUniformValue(GPUUniformKey.TRANSLATION_TEXTURE_COORDS, new GPUUniformValueVec2Float(translate._x, translate._y), false);
      _values.addUniformValue(GPUUniformKey.SCALE_TEXTURE_COORDS, new GPUUniformValueVec2Float(scale._x, scale._y), false);
    }
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
}