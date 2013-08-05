package org.glob3.mobile.generated; 
public class GLError
{
  private static int _noError = 0;

  public static int noError()
  {
     return _noError;
  }

  public static void init(INativeGL ngl)
  {
    _noError = ngl.Error_NoError();
  }
}