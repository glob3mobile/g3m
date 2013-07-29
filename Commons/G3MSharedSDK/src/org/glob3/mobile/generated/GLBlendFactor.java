package org.glob3.mobile.generated; 
public class GLBlendFactor
{
  private static int _srcAlpha = 0;
  private static int _oneMinusSrcAlpha = 0;
  private static int _one = 0;
  private static int _zero = 0;


  public static int srcAlpha()
  {
     return _srcAlpha;
  }
  public static int oneMinusSrcAlpha()
  {
     return _oneMinusSrcAlpha;
  }
  public static int one()
  {
     return _one;
  }
  public static int zero()
  {
     return _zero;
  }

  public static void init(INativeGL ngl)
  {
    _srcAlpha = ngl.BlendFactor_SrcAlpha();
    _oneMinusSrcAlpha = ngl.BlendFactor_OneMinusSrcAlpha();
    _one = ngl.BlendFactor_One();
    _zero = ngl.BlendFactor_Zero();
  }
}