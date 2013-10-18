package org.glob3.mobile.generated; 
public class LazyTextureMapping extends TextureMapping
{
  private LazyTextureMappingInitializer _initializer;

  private TextureIDReference _glTextureId;

  private boolean _initialized;

  private boolean _ownedTexCoords;
  private IFloatBuffer _texCoords;
  private MutableVector2D _translation = new MutableVector2D();
  private MutableVector2D _scale = new MutableVector2D();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  LazyTextureMapping operator =(LazyTextureMapping that);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  LazyTextureMapping(LazyTextureMapping that);
  private void releaseGLTextureId()
  {
    _glTextureId = null;
    _glTextureId = null;
  }

  private final boolean _transparent;


  public LazyTextureMapping(LazyTextureMappingInitializer initializer, boolean ownedTexCoords, boolean transparent)
  {
     _initializer = initializer;
     _glTextureId = null;
     _initialized = false;
     _texCoords = null;
     _translation = new MutableVector2D(0,0);
     _scale = new MutableVector2D(1,1);
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

  super.dispose();
  }

  public final boolean isValid()
  {
    return _glTextureId != null;
  }

  public final void setGLTextureId(TextureIDReference glTextureId)
  {
    releaseGLTextureId();
    _glTextureId = glTextureId;
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GLGlobalState bind(G3MRenderContext rc, GLGlobalState parentState, GPUProgramState progState);


  public final TextureIDReference getGLTextureId()
  {
    return _glTextureId;
  }

  public final boolean isTransparent()
  {
    return _transparent;
  }


  ///#include "GPUProgramState.hpp"
  
  public final void modifyGLState(GLState state)
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
      state.clearGLFeatureGroup(GLFeatureGroupName.COLOR_GROUP);
  
      if (!_scale.isEquals(1.0, 1.0) || !_translation.isEquals(0.0, 0.0))
      {
  
        state.addGLFeature(new TextureGLFeature(_glTextureId.getID(), _texCoords, 2, 0, false, 0, isTransparent(), GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), true, _translation.asVector2D(), _scale.asVector2D()), false); //TRANSFORM - BLEND
      }
      else
      {
        state.addGLFeature(new TextureGLFeature(_glTextureId.getID(), _texCoords, 2, 0, false, 0, isTransparent(), GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), false, Vector2D.zero(), Vector2D.zero()), false); //TRANSFORM - BLEND
      }
  
    }
    else
    {
      ILogger.instance().logError("LazyTextureMapping::bind() with _texCoords == NULL");
    }
  
  }

}