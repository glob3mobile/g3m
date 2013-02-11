package org.glob3.mobile.generated; 
public class GLBlendFactor
{
  private static int _srcAlpha = 0;
  private static int _oneMinusSrcAlpha = 0;


  public static int srcAlpha()
  {
     return _srcAlpha;
  }
  public static int oneMinusSrcAlpha()
  {
     return _oneMinusSrcAlpha;
  }

  public static void init(INativeGL ngl)
  {
    _srcAlpha = ngl.BlendFactor_SrcAlpha();
    _oneMinusSrcAlpha = ngl.BlendFactor_OneMinusSrcAlpha();
  }
}