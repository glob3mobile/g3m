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

  public final GLGlobalState bind(G3MRenderContext rc, GLGlobalState parentState, GPUProgramState progState)
  {
  
    GLGlobalState state = new GLGlobalState(parentState);
    if (_texCoords != null)
    {
  
  
      //Activating Attribute in Shader program
      progState.setAttributeEnabled("TextureCoord", true);
      progState.setUniformValue("EnableTexture", true);
      progState.setAttributeValue("TextureCoord", _texCoords, 2, 2, 0, false, 0);
  
      progState.setUniformValue("ScaleTexCoord", _scale.asVector2D());
      progState.setUniformValue("TranslationTexCoord", _translation.asVector2D());
  
      state.bindTexture(_glTextureId);
    }
    else
    {
      ILogger.instance().logError("SimpleTextureMapping::bind() with _texCoords == NULL");
    }
  
    return state;
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return _isTransparent;
  }

  public final void modifyGLGlobalState(GLGlobalState GLGlobalState)
  {
    if (_texCoords != null)
    {
      GLGlobalState.bindTexture(_glTextureId);
    }
    else
    {
      ILogger.instance().logError("SimpleTextureMapping::bind() with _texCoords == NULL");
    }
  }

  public final void modifyGPUProgramState(GPUProgramState progState)
  {
    if (_texCoords != null)
    {
      //Activating Attribute in Shader program
      progState.setAttributeEnabled("TextureCoord", true);
      progState.setUniformValue("EnableTexture", true);
      progState.setAttributeValue("TextureCoord", _texCoords, 2, 2, 0, false, 0);
  
      progState.setUniformValue("ScaleTexCoord", _scale.asVector2D());
      progState.setUniformValue("TranslationTexCoord", _translation.asVector2D());
    }
    else
    {
      ILogger.instance().logError("SimpleTextureMapping::bind() with _texCoords == NULL");
    }
  }

}