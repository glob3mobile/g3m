package org.glob3.mobile.generated; 
//
//  StenciledMultiTextureMapping.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/12/13.
//
//

//
//  StenciledMultiTextureMapping.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/12/13.
//
//





public class StenciledMultiTextureMapping extends TextureMapping
{
  private TextureIDReference _glTextureId;

  private TextureIDReference _glTextureId2;

  private TextureIDReference _stencilID;

  private IFloatBuffer _texCoords;
  private final boolean _ownedTexCoords;

  private IFloatBuffer _texCoords2;
  private final boolean _ownedTexCoords2;

  private IFloatBuffer _texCoordsStencil;
  private final boolean _ownedTexCoordsStencil;

  //TRANSFORMS FOR TEX 0
  private float _translationU;
  private float _translationV;
  private float _scaleU;
  private float _scaleV;
  private float _rotationInRadians;
  private float _rotationCenterU;
  private float _rotationCenterV;

  private final boolean _transparent;
  private final boolean _transparent2;

  private void releaseGLTextureId()
  {
  
    if (_glTextureId != null)
    {
      _glTextureId.dispose();
      _glTextureId = null;
    }
    else
    {
      ILogger.instance().logError("Releasing invalid Multi texture mapping");
    }
  
  
    if (_glTextureId2 != null)
    {
      _glTextureId2.dispose();
      _glTextureId2 = null;
    }
    else
    {
      ILogger.instance().logError("Releasing invalid Multi texture mapping");
    }
  
    if (_stencilID != null)
    {
      _stencilID.dispose();
      _stencilID = null;
    }
    else
    {
      ILogger.instance().logError("Releasing invalid Multi texture mapping");
    }
  }


  public StenciledMultiTextureMapping(TextureIDReference glTextureId, IFloatBuffer texCoords, boolean ownedTexCoords, boolean transparent, TextureIDReference glTextureId2, IFloatBuffer texCoords2, boolean ownedTexCoords2, boolean transparent2, TextureIDReference stencilID, IFloatBuffer texCoordsStencil, boolean ownedTexCoordsStencil)
  {
     _glTextureId = glTextureId;
     _texCoords = texCoords;
     _translationU = 0F;
     _translationV = 0F;
     _scaleU = 1F;
     _scaleV = 1F;
     _ownedTexCoords = ownedTexCoords;
     _transparent = transparent;
     _rotationInRadians = 0F;
     _rotationCenterU = 0F;
     _rotationCenterV = 0F;
     _glTextureId2 = glTextureId2;
     _texCoords2 = texCoords2;
     _ownedTexCoords2 = ownedTexCoords2;
     _transparent2 = transparent2;
     _stencilID = stencilID;
     _texCoordsStencil = texCoordsStencil;
     _ownedTexCoordsStencil = ownedTexCoordsStencil;
  }

  public final void setTranslation(float u, float v)
  {
    _translationU = u;
    _translationV = v;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning updateState();
  }

  public final void setScale(float u, float v)
  {
    _scaleU = u;
    _scaleV = v;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning updateState();
  }

  public final void setRotation(float angleInRadians, float centerU, float centerV)
  {
    _rotationInRadians = angleInRadians;
    _rotationCenterU = centerU;
    _rotationCenterV = centerV;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning updateState();
  }

  public void dispose()
  {
    if (_ownedTexCoords)
    {
      if (_texCoords != null)
         _texCoords.dispose();
    }
  
    if (_ownedTexCoords2)
    {
      if (_texCoords2 != null)
         _texCoords2.dispose();
    }
  
    if (_ownedTexCoordsStencil)
    {
      if (_texCoordsStencil != null)
         _texCoordsStencil.dispose();
    }
  
    releaseGLTextureId();
  
    super.dispose();
  }

  public final IGLTextureId getGLTextureId()
  {
    return _glTextureId.getID();
  }

  public final IFloatBuffer getTexCoords()
  {
    return _texCoords;
  }

  public final void modifyGLState(GLState state)
  {
    //TARGET 0
    if (_texCoords == null)
    {
      ILogger.instance().logError("StenciledMultiTextureMapping::bind() with _texCoords == NULL");
    }
    else
    {
      state.clearGLFeatureGroup(GLFeatureGroupName.COLOR_GROUP);
  
      if ((_scaleU != 1) || (_scaleV != 1) || (_translationU != 0) || (_translationV != 0) || (_rotationInRadians != 0))
      {
        state.addGLFeature(new TextureGLFeature(_glTextureId.getID(), _texCoords, 2, 0, false, 0, _transparent, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), _translationU, _translationV, _scaleU, _scaleV, _rotationInRadians, _rotationCenterU, _rotationCenterV), false);
      }
      else
      {
        state.addGLFeature(new TextureGLFeature(_glTextureId.getID(), _texCoords, 2, 0, false, 0, _transparent, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
      }
    }
  
    //TAGET 1
    if (_texCoords2 == null)
    {
      ILogger.instance().logError("StenciledMultiTextureMapping::bind() with _texCoords2 == NULL");
    }
    else
    {
  
      state.addGLFeature(new TextureGLFeature(_glTextureId2.getID(), _texCoords2, 2, 0, false, 0, _transparent2, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), 1), false); //TARGET
    }
  
    //STENCIL
    if (_texCoordsStencil == null)
    {
      ILogger.instance().logError("StenciledMultiTextureMapping::bind() with _texCoordsStencil == NULL");
    }
    else
    {
  
      state.addGLFeature(new TextureGLFeature(_stencilID.getID(), _texCoordsStencil, 2, 0, false, 0, true, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), 2), false); //TARGET
    }
  }

}