package org.glob3.mobile.generated; 
public class GLVariable
{
  private static int _viewport = 0;

  public static int viewport()
  {
     return _viewport;
  }

  public static void init(INativeGL ngl)
  {
    _viewport = ngl.Variable_Viewport();
  }
}