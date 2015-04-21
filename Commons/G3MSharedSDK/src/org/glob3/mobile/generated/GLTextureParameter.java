package org.glob3.mobile.generated; 
public class GLTextureParameter
{
  private static int _minFilter = 0;
  private static int _magFilter = 0;
  private static int _wrapS = 0;
  private static int _wrapT = 0;

  public static int minFilter()
  {
     return _minFilter;
  }
  public static int magFilter()
  {
     return _magFilter;
  }
  public static int wrapS()
  {
     return _wrapS;
  }
  public static int wrapT()
  {
     return _wrapT;
  }

  public static void init(INativeGL ngl)
  {
    _minFilter = ngl.TextureParameter_MinFilter();
    _magFilter = ngl.TextureParameter_MagFilter();
    _wrapS = ngl.TextureParameter_WrapS();
    _wrapT = ngl.TextureParameter_WrapT();
  }
}