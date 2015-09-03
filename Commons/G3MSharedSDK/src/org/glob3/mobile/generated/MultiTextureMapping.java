package org.glob3.mobile.generated; 
//
//  MultiTextureMapping.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/12/13.
//
//

//
//  MultiTextureMapping.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/12/13.
//
//



//class TextureIDReference;
//class IFloatBuffer;
//class IGLTextureId;

public class MultiTextureMapping extends TransformableTextureMapping
{
  private TextureIDReference _glTextureId;
  private TextureIDReference _glTextureId2;

  private IFloatBuffer _texCoords;
  private final boolean _ownedTexCoords;

  private IFloatBuffer _texCoords2;
  private final boolean _ownedTexCoords2;

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
  }


  public MultiTextureMapping(TextureIDReference glTextureId, IFloatBuffer texCoords, boolean ownedTexCoords, boolean transparent, TextureIDReference glTextureId2, IFloatBuffer texCoords2, boolean ownedTexCoords2, boolean transparent2)
  {
     super(0, 0, 1, 1, 0, 0, 0);
     _glTextureId = glTextureId;
     _texCoords = texCoords;
     _ownedTexCoords = ownedTexCoords;
     _transparent = transparent;
     _glTextureId2 = glTextureId2;
     _texCoords2 = texCoords2;
     _ownedTexCoords2 = ownedTexCoords2;
     _transparent2 = transparent2;
  }

  public MultiTextureMapping(TextureIDReference glTextureId, IFloatBuffer texCoords, boolean ownedTexCoords, boolean transparent, TextureIDReference glTextureId2, IFloatBuffer texCoords2, boolean ownedTexCoords2, boolean transparent2, float translationU, float translationV, float scaleU, float scaleV, float rotationAngleInRadians, float rotationCenterU, float rotationCenterV)
  {
     super(translationU, translationV, scaleU, scaleV, rotationAngleInRadians, rotationCenterU, rotationCenterV);
     _glTextureId = glTextureId;
     _texCoords = texCoords;
     _ownedTexCoords = ownedTexCoords;
     _transparent = transparent;
     _glTextureId2 = glTextureId2;
     _texCoords2 = texCoords2;
     _ownedTexCoords2 = ownedTexCoords2;
     _transparent2 = transparent2;
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
    GLFeatureSet tglfs = state.getGLFeatures(GLFeatureID.GLF_TEXTURE);
  
    for (int i = 0; i < tglfs.size(); i++)
    {
      TextureGLFeature tglf = (TextureGLFeature) tglfs.get(0);
      if (tglf.getTarget() == 0 && tglf.getTextureID() == _glTextureId.getID())
      {
        tglf.setScale(_scaleU, _scaleV);
        tglf.setTranslation(_translationU, _translationV);
        tglf.setRotationAngleInRadiansAndRotationCenter(_rotationInRadians, _rotationCenterU, _rotationCenterV);
        if (tglfs != null)
           tglfs.dispose();
        return; //The TextureGLFeature for target 0 already exists and we do not have to recreate the state
      }
    }
    if (tglfs != null)
       tglfs.dispose();
  
    //CREATING TWO TEXTURES GLFEATURE
  
    state.clearGLFeatureGroup(GLFeatureGroupName.COLOR_GROUP);
  
    // TARGET 0
    if (_texCoords == null)
    {
      ILogger.instance().logError("MultiTextureMapping::bind() with _texCoords == NULL");
    }
    else
    {
      state.addGLFeature(new TextureGLFeature(_glTextureId.getID(), _texCoords, 2, 0, false, 0, _transparent, _glTextureId.isPremultiplied() ? GLBlendFactor.one() : GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), _translationU, _translationV, _scaleU, _scaleV, _rotationInRadians, _rotationCenterU, _rotationCenterV), false);
  
    }
  
    // TARGET 1
    if (_texCoords2 == null)
    {
      ILogger.instance().logError("MultiTextureMapping::bind() with _texCoords2 == NULL");
    }
    else
    {
      state.addGLFeature(new TextureGLFeature(_glTextureId2.getID(), _texCoords2, 2, 0, false, 0, _transparent2, _glTextureId.isPremultiplied() ? GLBlendFactor.one() : GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), 1), false); //TARGET
    }
  
  }

}