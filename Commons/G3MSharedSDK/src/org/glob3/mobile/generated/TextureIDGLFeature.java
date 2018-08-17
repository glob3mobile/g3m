package org.glob3.mobile.generated;import java.util.*;

public class TextureIDGLFeature extends PriorityGLFeature
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final IGLTextureId _const* _texID = new IGLTextureId();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public IGLTextureId _texID = null;
//#endif

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }


  //////////////////////////////////////////
  
  public TextureIDGLFeature(IGLTextureId texID)
  {
	  super(GLFeatureGroupName.COLOR_GROUP, GLFeatureID.GLF_TEXTURE_ID, 4);
	  _texID = texID;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void applyOnGlobalGLState(GLGlobalState* state) const
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
	state.bindTexture(0, _texID);
  }
}
