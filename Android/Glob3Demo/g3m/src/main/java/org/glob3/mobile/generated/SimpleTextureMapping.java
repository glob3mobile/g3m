package org.glob3.mobile.generated;
//
//  SimpleTextureMapping.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//

//
//  SimpleTextureMapping.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//



//class TextureIDReference;
//class IFloatBuffer;
//class IGLTextureID;

public class SimpleTextureMapping extends TransformableTextureMapping
{
  private TextureIDReference _glTextureID;

  private IFloatBuffer _texCoords;
  private final boolean _ownedTexCoords;

  private final boolean _transparent;

  private void releaseGLTextureID()
  {
  
    if (_glTextureID != null)
    {
      _glTextureID.dispose();
      _glTextureID = null;
    }
    else
    {
      ILogger.instance().logError("Releasing invalid simple texture mapping");
    }
  }


  public SimpleTextureMapping(TextureIDReference glTextureID, IFloatBuffer texCoords, boolean ownedTexCoords, boolean transparent)
  {
     super(0, 0, 1, 1, 0, 0, 0);
     _glTextureID = glTextureID;
     _texCoords = texCoords;
     _ownedTexCoords = ownedTexCoords;
     _transparent = transparent;
  }

  public SimpleTextureMapping(TextureIDReference glTextureID, IFloatBuffer texCoords, boolean ownedTexCoords, boolean transparent, float translationU, float translationV, float scaleU, float scaleV, float rotationAngleInRadians, float rotationCenterU, float rotationCenterV)
  {
     super(translationU, translationV, scaleU, scaleV, rotationAngleInRadians, rotationCenterU, rotationCenterV);
     _glTextureID = glTextureID;
     _texCoords = texCoords;
     _ownedTexCoords = ownedTexCoords;
     _transparent = transparent;
  }

  public void dispose()
  {
    if (_ownedTexCoords)
    {
      if (_texCoords != null)
         _texCoords.dispose();
    }
  
    releaseGLTextureID();
  
    super.dispose();
  }

  public final IGLTextureID getGLTextureID()
  {
    return _glTextureID.getID();
  }

  public final IFloatBuffer getTexCoords()
  {
    return _texCoords;
  }

  public final void modifyGLState(GLState state)
  {
    if (_texCoords == null)
    {
      ILogger.instance().logError("SimpleTextureMapping::bind() with _texCoords == NULL");
    }
    else
    {
      TextureGLFeature tglf = (TextureGLFeature) state.getGLFeature(GLFeatureID.GLF_TEXTURE);
      if ((tglf != null) && (tglf.getTextureID() == _glTextureID.getID()))
      {
        tglf.setScale(_scaleU, _scaleV);
        tglf.setTranslation(_translationU, _translationV);
        tglf.setRotation(_rotationInRadians, _rotationCenterU, _rotationCenterV);
      }
      else
      {
        state.clearGLFeatureGroup(GLFeatureGroupName.COLOR_GROUP);
        //if ((_scaleU != 1) ||
        //    (_scaleV != 1) ||
        //    (_translationU != 0) ||
        //    (_translationV != 0) ||
        //    (_rotationInRadians != 0)) {
        state.addGLFeature(new TextureGLFeature(_glTextureID.getID(), _texCoords, 2, 0, false, 0, _transparent, _glTextureID.isPremultiplied() ? GLBlendFactor.one() : GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), _translationU, _translationV, _scaleU, _scaleV, _rotationInRadians, _rotationCenterU, _rotationCenterV), false);
        //}
        //else {
        //  state.addGLFeature(new TextureGLFeature(_glTextureID->getID(),
        //                                          _texCoords,
        //                                          2,
        //                                          0,
        //                                          false,
        //                                          0,
        //                                          _transparent,
        //                                          _glTextureID->isPremultiplied() ? GLBlendFactor::one() : GLBlendFactor::srcAlpha(),
        //                                          GLBlendFactor::oneMinusSrcAlpha()
        //                                          ),
        //                     false);
        //}
      }
    }
  }

}
