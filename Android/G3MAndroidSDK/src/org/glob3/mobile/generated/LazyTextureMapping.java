package org.glob3.mobile.generated; 
public class LazyTextureMapping extends TextureMapping
{
  private LazyTextureMappingInitializer _initializer;

  private IGLTextureId _glTextureId;

  private boolean _initialized;

  private boolean _ownedTexCoords;
  private IFloatBuffer _texCoords;
  private MutableVector2D _translation = new MutableVector2D();
  private MutableVector2D _scale = new MutableVector2D();

  private TexturesHandler _texturesHandler;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  LazyTextureMapping operator =(LazyTextureMapping that);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  LazyTextureMapping(LazyTextureMapping that);
  private void releaseGLTextureId()
  {
	if (_texturesHandler != null)
	{
	  if (_glTextureId != null)
	  {
		_texturesHandler.releaseGLTextureId(_glTextureId);
		_glTextureId = null;
	  }
	}
  }

  public LazyTextureMapping(LazyTextureMappingInitializer initializer, TexturesHandler texturesHandler, boolean ownedTexCoords)
  {
	  _initializer = initializer;
	  _glTextureId = null;
	  _initialized = false;
	  _texCoords = null;
	  _translation = new MutableVector2D(0,0);
	  _scale = new MutableVector2D(1,1);
	  _texturesHandler = texturesHandler;
	  _ownedTexCoords = ownedTexCoords;

  }

  public void dispose()
  {
	if (_initializer != null)
	{
	  if (_initializer != null)
		  _initializer.dispose();
	  _initializer = null;
	}

	if (_texCoords != null)
	{
	  if (_ownedTexCoords)
	  {
		if (_texCoords != null)
			_texCoords.dispose();
	  }
	  _texCoords = null;
	}

	releaseGLTextureId();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void bind(const RenderContext* rc) const
  public final void bind(RenderContext rc)
  {
	if (!_initialized)
	{
	  _initializer.initialize();
  
	  _scale = _initializer.getScale();
	  _translation = _initializer.getTranslation();
	  _texCoords = _initializer.getTexCoords();
  
	  if (_initializer != null)
		  _initializer.dispose();
	  _initializer = null;
  
	  _initialized = true;
	}
  
	GL gl = rc.getGL();
  
	gl.transformTexCoords(_scale, _translation);
	gl.bindTexture(_glTextureId);
	gl.setTextureCoordinates(2, 0, _texCoords);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isValid() const
  public final boolean isValid()
  {
	return _glTextureId != null;
  }

  public final void setGLTextureId(IGLTextureId glTextureId)
  {
	releaseGLTextureId();
	_glTextureId = glTextureId;
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const const IGLTextureId* getGLTextureId() const
  public final IGLTextureId getGLTextureId()
  {
	return _glTextureId;
  }

}