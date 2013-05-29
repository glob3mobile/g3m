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

  private final boolean _transparent;


  public LazyTextureMapping(LazyTextureMappingInitializer initializer, TexturesHandler texturesHandler, boolean ownedTexCoords, boolean transparent)
  {
     _initializer = initializer;
     _glTextureId = null;
     _initialized = false;
     _texCoords = null;
     _translation = new MutableVector2D(0,0);
     _scale = new MutableVector2D(1,1);
     _texturesHandler = texturesHandler;
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
  }

  //void bind(const G3MRenderContext* rc, const GLGlobalState& parentState) const;

  public final boolean isValid()
  {
    return _glTextureId != null;
  }

  public final void setGLTextureId(IGLTextureId glTextureId)
  {
    releaseGLTextureId();
    _glTextureId = glTextureId;
  }

  public final GLGlobalState bind(G3MRenderContext rc, GLGlobalState parentState, GPUProgramState progState)
  {
    if (!_initialized)
    {
      _initializer.initialize();
  
      _scale = _initializer.getScale();
      _translation = _initializer.getTranslation();
      _texCoords = _initializer.createTextCoords();
  
      if (_initializer != null)
         _initializer.dispose();
      _initializer = null;
  
      _initialized = true;
    }
  
    progState.setAttributeEnabled("TextureCoord", true);
    progState.setUniformValue("EnableTexture", true);
  
    GLGlobalState state = null; //new GLGlobalState(parentState);
    //state->enableTextures();
  
  //  state->enableTexture2D();
  
    if (_texCoords != null)
    {
      progState.setUniformValue("ScaleTexCoord", _scale.asVector2D());
      progState.setUniformValue("TranslationTexCoord", _translation.asVector2D());
  //    state->scaleTextureCoordinates(_scale);
  //    state->translateTextureCoordinates(_translation);
      state.bindTexture(_glTextureId);
  //    state->setTextureCoordinates(_texCoords, 2, 0);
  
      progState.setAttributeValue("TextureCoord", _texCoords, 2, 2, 0, false, 0);
  
    }
    else
    {
      ILogger.instance().logError("LazyTextureMapping::bind() with _texCoords == NULL");
    }
  
  
    return state;
  }


  public final IGLTextureId getGLTextureId()
  {
    return _glTextureId;
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return _transparent;
  }

  public final void modifyGLGlobalState(GLGlobalState GLGlobalState)
  {
    if (!_initialized)
    {
      _initializer.initialize();
  
      _scale = _initializer.getScale();
      _translation = _initializer.getTranslation();
      _texCoords = _initializer.createTextCoords();
  
      if (_initializer != null)
         _initializer.dispose();
      _initializer = null;
  
      _initialized = true;
    }
  
    if (_texCoords != null)
    {
      GLGlobalState.bindTexture(_glTextureId);
    }
    else
    {
      ILogger.instance().logError("LazyTextureMapping::bind() with _texCoords == NULL");
    }
  
  }

  public final void modifyGPUProgramState(GPUProgramState progState)
  {
    if (!_initialized)
    {
      _initializer.initialize();
  
      _scale = _initializer.getScale();
      _translation = _initializer.getTranslation();
      _texCoords = _initializer.createTextCoords();
  
      if (_initializer != null)
         _initializer.dispose();
      _initializer = null;
  
      _initialized = true;
    }
  
    progState.setAttributeEnabled("TextureCoord", true);
    progState.setUniformValue("EnableTexture", true);
  
    if (_texCoords != null)
    {
      progState.setUniformValue("ScaleTexCoord", _scale.asVector2D());
      progState.setUniformValue("TranslationTexCoord", _translation.asVector2D());
  
      progState.setAttributeValue("TextureCoord", _texCoords, 2, 2, 0, false, 0);
    }
    else
    {
      ILogger.instance().logError("LazyTextureMapping::bind() with _texCoords == NULL");
    }
  
  }

}