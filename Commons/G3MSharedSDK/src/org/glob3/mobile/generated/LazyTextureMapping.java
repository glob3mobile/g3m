package org.glob3.mobile.generated;import java.util.*;

public class LazyTextureMapping extends TextureMapping
{
  private LazyTextureMappingInitializer _initializer;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final TextureIDReference _glTextureId;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public TextureIDReference _glTextureId = new internal();
//#endif

  private boolean _initialized;

  private boolean _ownedTexCoords;
  private IFloatBuffer _texCoords;

  private float _translationU;
  private float _translationV;

  private float _scaleU;
  private float _scaleV;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  LazyTextureMapping operator =(LazyTextureMapping that);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  LazyTextureMapping(LazyTextureMapping that);
  private void releaseGLTextureId()
  {
	if (_glTextureId != null)
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  if (_glTextureId != null)
		  _glTextureId.dispose();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  _glTextureId.dispose();
//#endif
	  _glTextureId = null;
	}
  }



  public final boolean _transparent;

  public LazyTextureMapping(LazyTextureMappingInitializer initializer, boolean ownedTexCoords, boolean transparent)
  {
	  _initializer = initializer;
	  _glTextureId = null;
	  _initialized = false;
	  _texCoords = null;
	  _translationU = 0F;
	  _translationV = 0F;
	  _scaleU = 1F;
	  _scaleV = 1F;
	  _ownedTexCoords = ownedTexCoords;
	  _transparent = transparent;
  }

  public void dispose()
  {
	if (_initializer != null)
		_initializer.dispose();
	_initializer = null;

	if (_ownedTexCoords)
	{
	  if (_texCoords != null)
		  _texCoords.dispose();
	}
	_texCoords = null;

	releaseGLTextureId();

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isValid() const
  public final boolean isValid()
  {
	return _glTextureId != null;
  }

  public final void setGLTextureId(TextureIDReference glTextureId)
  {
	releaseGLTextureId();
	_glTextureId = glTextureId;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const TextureIDReference* getGLTextureId() const
  public final TextureIDReference getGLTextureId()
  {
	return _glTextureId;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void modifyGLState(GLState& state) const
  public final void modifyGLState(tangible.RefObject<GLState> state)
  {
	if (!_initialized)
	{
	  _initializer.initialize();
  
	  final Vector2F scale = _initializer.getScale();
	  _scaleU = scale._x;
	  _scaleV = scale._y;
  
	  final Vector2F translation = _initializer.getTranslation();
	  _translationU = translation._x;
	  _translationV = translation._y;
  
	  _texCoords = _initializer.createTextCoords();
  
	  if (_initializer != null)
		  _initializer.dispose();
	  _initializer = null;
  
	  _initialized = true;
	}
  
	if (_texCoords != null)
	{
	  state.argvalue.clearGLFeatureGroup(GLFeatureGroupName.COLOR_GROUP);
  
	  if (_scaleU != 1 || _scaleV != 1 || _translationU != 0 || _translationV != 0)
	  {
		state.argvalue.addGLFeature(new TextureGLFeature(_glTextureId.getID(), _texCoords, 2, 0, false, 0, _transparent, _glTextureId.isPremultiplied() ? GLBlendFactor.one() : GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), _translationU, _translationV, _scaleU, _scaleV, 0, 0, 0), false);
	  }
	  else
	  {
		state.argvalue.addGLFeature(new TextureGLFeature(_glTextureId.getID(), _texCoords, 2, 0, false, 0, _transparent, _glTextureId.isPremultiplied() ? GLBlendFactor.one() : GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
	  }
  
	}
	else
	{
	  ILogger.instance().logError("LazyTextureMapping::bind() with _texCoords == NULL");
	}
  
  }

}
