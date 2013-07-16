package org.glob3.mobile.generated; 
public class TextureIDGLFeature extends GLColorGroupFeature
{
  private IGLTextureId _texID = null;


  //////////////////////////////////////////
  
  public TextureIDGLFeature(IGLTextureId texID, boolean blend, int sFactor, int dFactor)
  {
     super(4, blend, sFactor, dFactor);
     _texID = texID;
  
  
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    blendingOnGlobalGLState(state);
    state.bindTexture(_texID);
  }
}