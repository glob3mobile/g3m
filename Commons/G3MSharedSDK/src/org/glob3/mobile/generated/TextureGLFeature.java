package org.glob3.mobile.generated; 
public class TextureGLFeature extends GLColorGroupFeature
{
  private IGLTextureId _texID = null;

  public void dispose()
  {
    super.dispose();
  }

  private final int _target;

  private void createBasicValues(IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride)
  {
    GPUAttributeValueVec2Float value = new GPUAttributeValueVec2Float(texCoords, arrayElementSize, index, stride, normalized);
  
    GPUUniformValueInt texUnit = new GPUUniformValueInt(_target);
  
    switch (_target)
    {
      case 0:
        _values.addUniformValue(GPUUniformKey.SAMPLER, texUnit, false);
        _values.addAttributeValue(GPUAttributeKey.TEXTURE_COORDS, value, false);
        break;
  
      case 1:
        _values.addUniformValue(GPUUniformKey.SAMPLER2, texUnit, false);
        _values.addAttributeValue(GPUAttributeKey.TEXTURE_COORDS_2, value, false);
        break;
  
      case 2:
        _values.addUniformValue(GPUUniformKey.SAMPLER3, texUnit, false);
        _values.addAttributeValue(GPUAttributeKey.TEXTURE_COORDS_3, value, false);
        break;
  
      default:
        ILogger.instance().logError("Wrong texture target.");
  
        break;
    }
  }

  public TextureGLFeature(IGLTextureId texID, IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor, float translateU, float translateV, float scaleU, float scaleV, float rotationAngleInRadians, float rotationCenterU, float rotationCenterV)
  {
     this(texID, texCoords, arrayElementSize, index, normalized, stride, blend, sFactor, dFactor, translateU, translateV, scaleU, scaleV, rotationAngleInRadians, rotationCenterU, rotationCenterV, 0);
  }
  public TextureGLFeature(IGLTextureId texID, IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor, float translateU, float translateV, float scaleU, float scaleV, float rotationAngleInRadians, float rotationCenterU, float rotationCenterV, int target)
  {
     super(GLFeatureID.GLF_TEXTURE, 4, blend, sFactor, dFactor);
     _texID = texID;
     _target = target;
  
    createBasicValues(texCoords, arrayElementSize, index, normalized, stride);
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TRANFORMATIONS IF TARGET != 0
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
     this(texID, texCoords, arrayElementSize, index, normalized, stride, blend, sFactor, dFactor, 0);
  }
  public TextureGLFeature(IGLTextureId texID, IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor, int target)
  {
     super(GLFeatureID.GLF_TEXTURE, 4, blend, sFactor, dFactor);
     _texID = texID;
     _target = target;
  
    createBasicValues(texCoords, arrayElementSize, index, normalized, stride);
  
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    blendingOnGlobalGLState(state);
    state.bindTexture(_target, _texID);
  }
}