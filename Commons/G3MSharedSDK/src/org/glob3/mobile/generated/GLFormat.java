package org.glob3.mobile.generated; 
public class GLFormat
{
  private static int _rgba = 0;

  public static int rgba()
  {
     return _rgba;
  }

  public static void init(INativeGL ngl)
  {
    _rgba = ngl.Format_RGBA();
  }
}