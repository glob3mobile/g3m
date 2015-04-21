package org.glob3.mobile.generated; 
public class GLAlignment
{
  private static int _pack = 0;
  private static int _unpack = 0;

  public static int pack()
  {
     return _pack;
  }
  public static int unpack()
  {
     return _unpack;
  }

  public static void init(INativeGL ngl)
  {
    _pack = ngl.Alignment_Pack();
    _unpack = ngl.Alignment_Unpack();
  }
}