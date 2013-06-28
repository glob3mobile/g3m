package org.glob3.mobile.generated; 
public class SimpleTextureMapping extends TextureMapping
{
  private final IGLTextureId _glTextureId;

  private IFloatBuffer _texCoords;
  private final boolean _ownedTexCoords;

  private MutableVector2D _translation = new MutableVector2D();
  private MutableVector2D _scale = new MutableVector2D();

  private final boolean _isTransparent;


  public SimpleTextureMapping(IGLTextureId glTextureId, IFloatBuffer texCoords, boolean ownedTexCoords, boolean isTransparent)
  {
     _glTextureId = glTextureId;
     _texCoords = texCoords;
     _translation = new MutableVector2D(0, 0);
     _scale = new MutableVector2D(1, 1);
     _ownedTexCoords = ownedTexCoords;
     _isTransparent = isTransparent;

  }

  public final void setTranslationAndScale(Vector2D translation, Vector2D scale)
  {
    _translation = translation.asMutableVector2D();
    _scale = scale.asMutableVector2D();
  }

  public void dispose()
  {
    if (_ownedTexCoords)
    {
      if (_texCoords != null)
         _texCoords.dispose();
    }
  }

  public final IGLTextureId getGLTextureId()
  {
    return _glTextureId;
  }

  public final IFloatBuffer getTexCoords()
  {
    return _texCoords;
  }

//  GLGlobalState* bind(const G3MRenderContext* rc, const GLGlobalState& parentState, GPUProgramState& progState) const;

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return _isTransparent;
  }

//  void modifyGLGlobalState(GLGlobalState& GLGlobalState) const;
//  
//  void modifyGPUProgramState(GPUProgramState& progState) const;

  public final void modifyGLState(GLState state)
  {
  
    GLGlobalState glGlobalState = state.getGLGlobalState();
    GPUProgramState progState = state.getGPUProgramState();
  
    if (_texCoords != null)
    {
      glGlobalState.bindTexture(_glTextureId);
  
      progState.setAttributeValue(GPUAttributeKey.TEXTURE_COORDS, _texCoords, 2, 2, 0, false, 0);
  
      if (!_scale.isEqualsTo(1.0, 1.0) || !_translation.isEqualsTo(0.0, 0.0))
      {
        progState.setUniformValue(GPUUniformKey.SCALE_TEXTURE_COORDS, _scale.asVector2D());
        progState.setUniformValue(GPUUniformKey.TRANSLATION_TEXTURE_COORDS, _translation.asVector2D());
      }
      else
      {
        //ILogger::instance()->logInfo("No transformed TC");
        progState.removeGPUUniformValue(GPUUniformKey.SCALE_TEXTURE_COORDS);
        progState.removeGPUUniformValue(GPUUniformKey.TRANSLATION_TEXTURE_COORDS);
      }
  
  //    progState->setUniformValue(SCALE_TEXTURE_COORDS, _scale.asVector2D());
  //    progState->setUniformValue(TRANSLATION_TEXTURE_COORDS, _translation.asVector2D());
  
    }
    else
    {
      ILogger.instance().logError("SimpleTextureMapping::bind() with _texCoords == NULL");
    }
  }

}