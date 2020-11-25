package org.glob3.mobile.generated;
public class TextureGLFeature extends GLColorGroupFeature
{
  private IGLTextureID _texID = null;

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
        value._release();
        texUnit._release();
        ILogger.instance().logError("Wrong texture target.");
  
        break;
    }
  }

  private GPUUniformValueVec2FloatMutable _translation;
  private GPUUniformValueVec2FloatMutable _scale;
  private GPUUniformValueVec2FloatMutable _rotationCenter;
  private GPUUniformValueFloatMutable _rotationAngle;

  public TextureGLFeature(IGLTextureID texID, IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor, float translateU, float translateV, float scaleU, float scaleV, float rotationAngleInRadians, float rotationCenterU, float rotationCenterV)
  {
     this(texID, texCoords, arrayElementSize, index, normalized, stride, blend, sFactor, dFactor, translateU, translateV, scaleU, scaleV, rotationAngleInRadians, rotationCenterU, rotationCenterV, 0);
  }
  public TextureGLFeature(IGLTextureID texID, IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor, float translateU, float translateV, float scaleU, float scaleV, float rotationAngleInRadians, float rotationCenterU, float rotationCenterV, int target)
  {
     super(GLFeatureID.GLF_TEXTURE, 4, blend, sFactor, dFactor);
     _texID = texID;
     _target = target;
     _translation = null;
     _scale = null;
     _rotationCenter = null;
     _rotationAngle = null;
    createBasicValues(texCoords, arrayElementSize, index, normalized, stride);
  
    setTranslation(translateU, translateV);
    setScale(scaleU, scaleV);
    setRotation(rotationAngleInRadians, rotationCenterU, rotationCenterV);
  }

  public TextureGLFeature(IGLTextureID texID, IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor)
  {
     this(texID, texCoords, arrayElementSize, index, normalized, stride, blend, sFactor, dFactor, 0);
  }
  public TextureGLFeature(IGLTextureID texID, IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor, int target)
  {
     super(GLFeatureID.GLF_TEXTURE, 4, blend, sFactor, dFactor);
     _texID = texID;
     _target = target;
     _translation = null;
     _scale = null;
     _rotationCenter = null;
     _rotationAngle = null;
    createBasicValues(texCoords, arrayElementSize, index, normalized, stride);
  }

  public final boolean hasTranslateAndScale()
  {
     return _translation != null && _scale != null;
  }

  public final void setTranslation(float u, float v)
  {
    if (_translation == null)
    {
      _translation = new GPUUniformValueVec2FloatMutable(u, v);
  
      _values.addUniformValue(GPUUniformKey.TRANSLATION_TEXTURE_COORDS, _translation, false);
    }
    else
    {
      //if ((u == 0) && (v == 0)) {
      //  _values->removeUniformValue(TRANSLATION_TEXTURE_COORDS);
      //  _translation = NULL;
      //}
      //else {
      _translation.changeValue(u, v);
      //}
    }
  }
  public final void setScale(float u, float v)
  {
    if (_scale == null)
    {
      _scale = new GPUUniformValueVec2FloatMutable(u, v);
  
      _values.addUniformValue(GPUUniformKey.SCALE_TEXTURE_COORDS, _scale, false);
    }
    else
    {
      //if ((u == 1) && (v == 1)) {
      //  _values->removeUniformValue(SCALE_TEXTURE_COORDS);
      //  _scale = NULL;
      //}
      //else {
      _scale.changeValue(u, v);
      //}
    }
  }
  public final void setRotation(float angleInRadians, float u, float v)
  {
    if ((_rotationAngle == null) || (_rotationCenter == null))
    {
      if (angleInRadians != 0)
      {
        _rotationCenter = new GPUUniformValueVec2FloatMutable(u, v);
        _values.addUniformValue(GPUUniformKey.ROTATION_CENTER_TEXTURE_COORDS, _rotationCenter, false);
  
        _rotationAngle = new GPUUniformValueFloatMutable(angleInRadians);
        _values.addUniformValue(GPUUniformKey.ROTATION_ANGLE_TEXTURE_COORDS, _rotationAngle, false);
      }
    }
    else
    {
      //if (angleInRadians == 0) {
      //  _values->removeUniformValue(ROTATION_CENTER_TEXTURE_COORDS);
      //  _values->removeUniformValue(ROTATION_ANGLE_TEXTURE_COORDS);
      //  _rotationCenter = NULL;
      //  _rotationAngle  = NULL;
      //}
      //else {
      _rotationCenter.changeValue(u, v);
      _rotationAngle.changeValue(angleInRadians);
      //}
    }
  }

  public final int getTarget()
  {
    return _target;
  }

  public final IGLTextureID getTextureID() {
    return _texID;
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    blendingOnGlobalGLState(state);
    state.bindTexture(_target, _texID);
  }
}