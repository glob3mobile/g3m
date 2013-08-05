package org.glob3.mobile.generated; 
public class GLTextureParameterValue
{
  private static int _linear = 0;
  private static int _clampToEdge = 0;

  public static int linear()
  {
     return _linear;
  }
  public static int clampToEdge()
  {
     return _clampToEdge;
  }

  public static void init(INativeGL ngl)
  {
    _linear = ngl.TextureParameterValue_Linear();
    _clampToEdge = ngl.TextureParameterValue_ClampToEdge();
  }
}