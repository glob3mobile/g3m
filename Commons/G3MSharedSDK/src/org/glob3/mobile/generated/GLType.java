package org.glob3.mobile.generated; 
public class GLType
{
  private static int _float = 0;
  private static int _unsignedByte = 0;
  private static int _unsignedInt = 0;
  private static int _int = 0;

  public static int glFloat()
  {
     return _float;
  }
  public static int glUnsignedByte()
  {
     return _unsignedByte;
  }
  public static int glUnsignedInt()
  {
     return _unsignedInt;
  }
  public static int glInt()
  {
     return _int;
  }

  public static void init(INativeGL ngl)
  {
    _float = ngl.Type_Float();
    _unsignedByte = ngl.Type_UnsignedByte();
    _unsignedInt = ngl.Type_UnsignedInt();
    _int = ngl.Type_Int();
  }
}