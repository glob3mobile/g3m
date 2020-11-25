package org.glob3.mobile.generated;
public class LazyTextureMapping extends TextureMapping
{
  private LazyTextureMappingInitializer _initializer;

  private TextureIDReference _glTextureID;

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
  private void releaseGLTextureID()
  {
    if (_glTextureID != null)
    {
      _glTextureID.dispose();
      _glTextureID = null;
    }
  }



  public final boolean _transparent;

  public LazyTextureMapping(LazyTextureMappingInitializer initializer, boolean ownedTexCoords, boolean transparent)
  {
     _initializer = initializer;
     _glTextureID = null;
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

    releaseGLTextureID();

  super.dispose();
  }

  public final boolean isValid()
  {
    return _glTextureID != null;
  }

  public final void setGLTextureID(TextureIDReference glTextureID)
  {
    releaseGLTextureID();
    _glTextureID = glTextureID;
  }

  public final TextureIDReference getGLTextureID()
  {
    return _glTextureID;
  }

  public final void modifyGLState(GLState state)
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
      state.clearGLFeatureGroup(GLFeatureGroupName.COLOR_GROUP);
  
      if (_scaleU != 1 || _scaleV != 1 || _translationU != 0 || _translationV != 0)
      {
        state.addGLFeature(new TextureGLFeature(_glTextureID.getID(), _texCoords, 2, 0, false, 0, _transparent, _glTextureID.isPremultiplied() ? GLBlendFactor.one() : GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), _translationU, _translationV, _scaleU, _scaleV, 0, 0, 0), false);
      }
      else
      {
        state.addGLFeature(new TextureGLFeature(_glTextureID.getID(), _texCoords, 2, 0, false, 0, _transparent, _glTextureID.isPremultiplied() ? GLBlendFactor.one() : GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
      }
  
    }
    else
    {
      ILogger.instance().logError("LazyTextureMapping::bind() with _texCoords == NULL");
    }
  
  }

}