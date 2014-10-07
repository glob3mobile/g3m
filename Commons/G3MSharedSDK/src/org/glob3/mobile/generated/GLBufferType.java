package org.glob3.mobile.generated; 
public class GLBufferType
{
  private static int _colorBuffer = 0;
  private static int _depthBuffer = 0;

  public static int colorBuffer()
  {
     return _colorBuffer;
  }
  public static int depthBuffer()
  {
     return _depthBuffer;
  }

  public static void init(INativeGL ngl)
  {
    _colorBuffer = ngl.BufferType_ColorBuffer();
    _depthBuffer = ngl.BufferType_DepthBuffer();
  }
}