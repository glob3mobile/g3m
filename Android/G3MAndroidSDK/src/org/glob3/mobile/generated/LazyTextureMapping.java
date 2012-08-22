package org.glob3.mobile.generated; 
public class LazyTextureMapping extends TextureMapping
{
  private LazyTextureMappingInitializer _initializer;

  private GLTextureID _glTextureId = new GLTextureID();

  private boolean _initialized;

  private boolean _ownedTexCoords;
  private final float[] _texCoords;
  private MutableVector2D _translation = new MutableVector2D();
  private MutableVector2D _scale = new MutableVector2D();

  private TexturesHandler _texturesHandler;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void operator =(LazyTextureMapping that);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  LazyTextureMapping(LazyTextureMapping that);
  private void releaseGLTextureId()
  {
	if (_texturesHandler != null)
	{
	  if (_glTextureId.isValid())
	  {
		_texturesHandler.releaseGLTextureId(_glTextureId);
		_glTextureId = GLTextureID.invalid();
	  }
	}
  }

  public LazyTextureMapping(LazyTextureMappingInitializer initializer, TexturesHandler texturesHandler, boolean ownedTexCoords)
  {
	  _initializer = initializer;
	  _glTextureId = new GLTextureID(GLTextureID.invalid());
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
		_texCoords = null;
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
	return _glTextureId.isValid();
  }

  public final void setGLTextureID(GLTextureID glTextureId)
  {
	releaseGLTextureId();
	_glTextureId = glTextureId;
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const GLTextureID getGLTextureID() const
  public final GLTextureID getGLTextureID()
  {
	return _glTextureId;
  }

}