package org.glob3.mobile.generated; 
public class GLTextureType
{
  private static int _texture2D = 0;
  public static int texture2D()
  {
     return _texture2D;
  }

  public static void init(INativeGL ngl)
  {
    _texture2D = ngl.TextureType_Texture2D();
  }
}