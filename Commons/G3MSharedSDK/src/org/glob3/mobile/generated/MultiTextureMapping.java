package org.glob3.mobile.generated;import java.util.*;

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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TextureIDReference;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLTextureId;

public class MultiTextureMapping extends TransformableTextureMapping
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final TextureIDReference _glTextureId;
  private final TextureIDReference _glTextureId2;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public TextureIDReference _glTextureId = new internal();
  public TextureIDReference _glTextureId2 = new internal();
//#endif

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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  if (_glTextureId != null)
		  _glTextureId.dispose();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  _glTextureId.dispose();
//#endif
	  _glTextureId = null;
	}
	else
	{
	  ILogger.instance().logError("Releasing invalid Multi texture mapping");
	}
  
	if (_glTextureId2 != null)
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  if (_glTextureId2 != null)
		  _glTextureId2.dispose();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  _glTextureId2.dispose();
//#endif
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
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IGLTextureId* getGLTextureId() const
  public final IGLTextureId getGLTextureId()
  {
	return _glTextureId.getID();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* getTexCoords() const
  public final IFloatBuffer getTexCoords()
  {
	return _texCoords;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void modifyGLState(GLState& state) const
  public final void modifyGLState(tangible.RefObject<GLState> state)
  {
	GLFeatureSet tglfs = state.argvalue.getGLFeatures(GLFeatureID.GLF_TEXTURE);
  
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
  
	state.argvalue.clearGLFeatureGroup(GLFeatureGroupName.COLOR_GROUP);
  
	// TARGET 0
	if (_texCoords == null)
	{
	  ILogger.instance().logError("MultiTextureMapping::bind() with _texCoords == NULL");
	}
	else
	{
	  state.argvalue.addGLFeature(new TextureGLFeature(_glTextureId.getID(), _texCoords, 2, 0, false, 0, _transparent, _glTextureId.isPremultiplied() ? GLBlendFactor.one() : GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), _translationU, _translationV, _scaleU, _scaleV, _rotationInRadians, _rotationCenterU, _rotationCenterV), false);
  
	}
  
	// TARGET 1
	if (_texCoords2 == null)
	{
	  ILogger.instance().logError("MultiTextureMapping::bind() with _texCoords2 == NULL");
	}
	else
	{
	  state.argvalue.addGLFeature(new TextureGLFeature(_glTextureId2.getID(), _texCoords2, 2, 0, false, 0, _transparent2, _glTextureId.isPremultiplied() ? GLBlendFactor.one() : GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), 1), false); //TARGET
	}
  
  }

}
