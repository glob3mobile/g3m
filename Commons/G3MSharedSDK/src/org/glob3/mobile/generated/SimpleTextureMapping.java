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

  public final GLState bind(G3MRenderContext rc, GLState parentState)
  {
    GLState state = new GLState(parentState);
    state.enableTextures();
    state.enableTexture2D();
  
    if (_texCoords != null)
    {
      state.scaleTextureCoordinates(_scale);
      state.translateTextureCoordinates(_translation);
      state.bindTexture(_glTextureId);
  
      state.setTextureCoordinates(_texCoords, 2, 0);
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

}