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
//class IGLTextureID;

public class MultiTextureMapping extends TransformableTextureMapping
{
  private TextureIDReference _glTextureID1;
  private TextureIDReference _glTextureID2;

  private IFloatBuffer _texCoords1;
  private final boolean _ownedTexCoords1;

  private IFloatBuffer _texCoords2;
  private final boolean _ownedTexCoords2;

  private final boolean _transparent1;
  private final boolean _transparent2;

  private void releaseGLTextureID()
  {
  
    if (_glTextureID1 != null)
    {
      _glTextureID1.dispose();
      _glTextureID1 = null;
    }
    else
    {
      ILogger.instance().logError("Releasing invalid Multi texture mapping");
    }
  
    if (_glTextureID2 != null)
    {
      _glTextureID2.dispose();
      _glTextureID2 = null;
    }
    else
    {
      ILogger.instance().logError("Releasing invalid Multi texture mapping");
    }
  }


  public MultiTextureMapping(TextureIDReference glTextureID1, IFloatBuffer texCoords1, boolean ownedTexCoords1, boolean transparent1, TextureIDReference glTextureID2, IFloatBuffer texCoords2, boolean ownedTexCoords2, boolean transparent2)
  {
     super(0, 0, 1, 1, 0, 0, 0);
     _glTextureID1 = glTextureID1;
     _texCoords1 = texCoords1;
     _ownedTexCoords1 = ownedTexCoords1;
     _transparent1 = transparent1;
     _glTextureID2 = glTextureID2;
     _texCoords2 = texCoords2;
     _ownedTexCoords2 = ownedTexCoords2;
     _transparent2 = transparent2;
  }

  public MultiTextureMapping(TextureIDReference glTextureID1, IFloatBuffer texCoords1, boolean ownedTexCoords1, boolean transparent1, TextureIDReference glTextureID2, IFloatBuffer texCoords2, boolean ownedTexCoords2, boolean transparent2, float translationU, float translationV, float scaleU, float scaleV, float rotationAngleInRadians, float rotationCenterU, float rotationCenterV)
  {
     super(translationU, translationV, scaleU, scaleV, rotationAngleInRadians, rotationCenterU, rotationCenterV);
     _glTextureID1 = glTextureID1;
     _texCoords1 = texCoords1;
     _ownedTexCoords1 = ownedTexCoords1;
     _transparent1 = transparent1;
     _glTextureID2 = glTextureID2;
     _texCoords2 = texCoords2;
     _ownedTexCoords2 = ownedTexCoords2;
     _transparent2 = transparent2;
  }

  public void dispose()
  {
    if (_ownedTexCoords1)
    {
      if (_texCoords1 != null)
         _texCoords1.dispose();
    }
  
    if (_ownedTexCoords2)
    {
      if (_texCoords2 != null)
         _texCoords2.dispose();
    }
  
    releaseGLTextureID();
  
    super.dispose();
  }

  public final IGLTextureID getGLTextureID()
  {
    return _glTextureID1.getID();
  }

  public final IFloatBuffer getTexCoords()
  {
    return _texCoords1;
  }

  public final void modifyGLState(GLState state)
  {
    GLFeatureSet tglfs = state.getGLFeatures(GLFeatureID.GLF_TEXTURE);
  
    for (int i = 0; i < tglfs.size(); i++)
    {
      TextureGLFeature tglf = (TextureGLFeature) tglfs.get(0);
      if ((tglf.getTarget() == 0) && (tglf.getTextureID() == _glTextureID1.getID()))
      {
        tglf.setScale(_scaleU, _scaleV);
        tglf.setTranslation(_translationU, _translationV);
        tglf.setRotation(_rotationInRadians, _rotationCenterU, _rotationCenterV);
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
    if (_texCoords1 == null)
    {
      ILogger.instance().logError("MultiTextureMapping::bind() with _texCoords == NULL");
    }
    else
    {
      state.addGLFeature(new TextureGLFeature(_glTextureID1.getID(), _texCoords1, 2, 0, false, 0, _transparent1, _glTextureID1.isPremultiplied() ? GLBlendFactor.one() : GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), _translationU, _translationV, _scaleU, _scaleV, _rotationInRadians, _rotationCenterU, _rotationCenterV), false);
  
    }
  
    // TARGET 1
    if (_texCoords2 == null)
    {
      ILogger.instance().logError("MultiTextureMapping::bind() with _texCoords2 == NULL");
    }
    else
    {
      state.addGLFeature(new TextureGLFeature(_glTextureID2.getID(), _texCoords2, 2, 0, false, 0, _transparent2, _glTextureID1.isPremultiplied() ? GLBlendFactor.one() : GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), 1), false); //TARGET
    }
  
  }

}
