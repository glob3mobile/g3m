package org.glob3.mobile.generated;import java.util.*;

public class TextureGLFeature extends GLColorGroupFeature
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final IGLTextureId _const* _texID = new IGLTextureId();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public IGLTextureId _texID = null;
//#endif

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  private final int _target;


  /////////////////////////////////
  
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

  public TextureGLFeature(IGLTextureId texID, IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor, float translateU, float translateV, float scaleU, float scaleV, float rotationAngleInRadians, float rotationCenterU, float rotationCenterV)
  {
	  this(texID, texCoords, arrayElementSize, index, normalized, stride, blend, sFactor, dFactor, translateU, translateV, scaleU, scaleV, rotationAngleInRadians, rotationCenterU, rotationCenterV, 0);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: TextureGLFeature(const IGLTextureId* texID, IFloatBuffer* texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor, float translateU, float translateV, float scaleU, float scaleV, float rotationAngleInRadians, float rotationCenterU, float rotationCenterV, int target = 0) : GLColorGroupFeature(GLF_TEXTURE, 4, blend, sFactor, dFactor), _texID(texID), _target(target), _translation(null), _scale(null), _rotationCenter(null), _rotationAngle(null)
  public TextureGLFeature(IGLTextureId texID, IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor, float translateU, float translateV, float scaleU, float scaleV, float rotationAngleInRadians, float rotationCenterU, float rotationCenterV, int target)
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
	setRotationAngleInRadiansAndRotationCenter(rotationAngleInRadians, rotationCenterU, rotationCenterV);
  }

  public TextureGLFeature(IGLTextureId texID, IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor)
  {
	  this(texID, texCoords, arrayElementSize, index, normalized, stride, blend, sFactor, dFactor, 0);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: TextureGLFeature(const IGLTextureId* texID, IFloatBuffer* texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor, int target = 0) : GLColorGroupFeature(GLF_TEXTURE, 4, blend, sFactor, dFactor), _texID(texID), _target(target), _translation(null), _scale(null), _rotationCenter(null), _rotationAngle(null)
  public TextureGLFeature(IGLTextureId texID, IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor, int target)
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean hasTranslateAndScale() const
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
	  if (u == 0.0 && v == 0.0)
	  {
		_values.removeUniformValue(GPUUniformKey.TRANSLATION_TEXTURE_COORDS);
	  }
	  else
	  {
		_translation.changeValue(u, v);
	  }
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
	  if (u == 1.0 && v == 1.0)
	  {
		_values.removeUniformValue(GPUUniformKey.SCALE_TEXTURE_COORDS);
	  }
	  else
	  {
		_scale.changeValue(u, v);
	  }
	}
  }
  public final void setRotationAngleInRadiansAndRotationCenter(float angle, float u, float v)
  {
	if (_rotationAngle == null || _rotationCenter == null)
	{
	  if (angle != 0.0)
	  {
		_rotationCenter = new GPUUniformValueVec2FloatMutable(u, v);
  
		_values.addUniformValue(GPUUniformKey.ROTATION_CENTER_TEXTURE_COORDS, _rotationCenter, false);
  
		_rotationAngle = new GPUUniformValueFloatMutable(angle);
  
		_values.addUniformValue(GPUUniformKey.ROTATION_ANGLE_TEXTURE_COORDS, _rotationAngle, false);
	  }
	}
	else
	{
	  if (angle == 0.0)
	  {
		_values.removeUniformValue(GPUUniformKey.ROTATION_CENTER_TEXTURE_COORDS);
		_values.removeUniformValue(GPUUniformKey.ROTATION_ANGLE_TEXTURE_COORDS);
	  }
	  else
	  {
		_rotationCenter.changeValue(u, v);
		_rotationAngle.changeValue(angle);
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getTarget() const
  public final int getTarget()
  {
	return _target;
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IGLTextureId const* getTextureID() const
  public final IGLTextureId _const getTextureID()
  {
	return _texID;
  }
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final IGLTextureId getTextureID()
  {
	return _texID;
  }
//#endif

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void applyOnGlobalGLState(GLGlobalState* state) const
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
	blendingOnGlobalGLState(state);
	state.bindTexture(_target, _texID);
  }
}
