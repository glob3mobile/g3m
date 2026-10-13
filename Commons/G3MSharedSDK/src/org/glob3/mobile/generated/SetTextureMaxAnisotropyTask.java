package org.glob3.mobile.generated;
public class SetTextureMaxAnisotropyTask extends GTask
{
  private TexturesHandler _texturesHandler;
  private final float _maxAnisotropy;

  public SetTextureMaxAnisotropyTask(TexturesHandler texturesHandler, float maxAnisotropy)
  {
     _texturesHandler = texturesHandler;
     _maxAnisotropy = maxAnisotropy;
  }

  public final void run(G3MContext context)
  {
    _texturesHandler.setTextureMaxAnisotropy(_maxAnisotropy);
  }
}