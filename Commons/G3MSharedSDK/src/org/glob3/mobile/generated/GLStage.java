package org.glob3.mobile.generated; 
public class GLStage
{
  private static int _polygonOffsetFill = 0;
  private static int _depthTest = 0;
  private static int _blend = 0;
  private static int _cullFace = 0;

  public static int polygonOffsetFill()
  {
    return _polygonOffsetFill;
  }

  public static int depthTest()
  {
    return _depthTest;
  }

  public static int blend()
  {
    return _blend;
  }

  public static int cullFace()
  {
    return _cullFace;
  }

  public static void init(INativeGL ngl)
  {
    _polygonOffsetFill = ngl.Feature_PolygonOffsetFill();
    _depthTest = ngl.Feature_DepthTest();
    _blend = ngl.Feature_Blend();
    _cullFace = ngl.Feature_CullFace();
  }
}