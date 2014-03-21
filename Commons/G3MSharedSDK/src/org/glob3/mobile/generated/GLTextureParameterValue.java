package org.glob3.mobile.generated; 
public class GLTextureParameterValue
{
  private static int _nearest = 0;
  private static int _linear = 0;
  private static int _nearestMipmapNearest = 0;
  private static int _nearestMipmapLinear = 0;
  private static int _linearMipmapNearest = 0;
  private static int _linearMipmapLinear = 0;

  private static int _clampToEdge = 0;


  public static int nearest()
  {
     return _nearest;
  }
  public static int linear()
  {
     return _linear;
  }
  public static int nearestMipmapNearest()
  {
     return _nearestMipmapNearest;
  }
  public static int nearestMipmapLinear()
  {
     return _nearestMipmapLinear;
  }
  public static int linearMipmapNearest()
  {
     return _linearMipmapNearest;
  }
  public static int linearMipmapLinear()
  {
     return _linearMipmapLinear;
  }

  public static int clampToEdge()
  {
     return _clampToEdge;
  }

  public static void init(INativeGL ngl)
  {
    _nearest = ngl.TextureParameterValue_Nearest();
    _linear = ngl.TextureParameterValue_Linear();
    _nearestMipmapNearest = ngl.TextureParameterValue_NearestMipmapNearest();
    _nearestMipmapLinear = ngl.TextureParameterValue_NearestMipmapLinear();
    _linearMipmapNearest = ngl.TextureParameterValue_LinearMipmapNearest();
    _linearMipmapLinear = ngl.TextureParameterValue_LinearMipmapLinear();

    _clampToEdge = ngl.TextureParameterValue_ClampToEdge();
  }
}