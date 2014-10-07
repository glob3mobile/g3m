package org.glob3.mobile.generated; 
public class GLVariable
{
  private static int _viewport = 0;

  private static int _activeUniforms = 0;
  private static int _activeAttributes = 0;

  public static int viewport()
  {
     return _viewport;
  }

  public static int activeUniforms()
  {
     return _activeUniforms;
  }
  public static int activeAttributes()
  {
     return _activeAttributes;
  }


  public static void init(INativeGL ngl)
  {
    _viewport = ngl.Variable_Viewport();
    _activeAttributes = ngl.Variable_ActiveAttributes();
    _activeUniforms = ngl.Variable_ActiveUniforms();
  }
}