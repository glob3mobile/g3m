package org.glob3.mobile.generated;
public class TextureIDGLFeature extends PriorityGLFeature
{
  private IGLTextureID _texID = null;

  public void dispose()
  {
    super.dispose();
  }

  public TextureIDGLFeature(IGLTextureID texID)
  {
     super(GLFeatureGroupName.COLOR_GROUP, GLFeatureID.GLF_TEXTURE_ID, 4);
     _texID = texID;
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    state.bindTexture(0, _texID);
  }
}