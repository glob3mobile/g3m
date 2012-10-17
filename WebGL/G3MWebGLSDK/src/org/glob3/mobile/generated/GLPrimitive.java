package org.glob3.mobile.generated; 
public class GLPrimitive
{
  private static int _triangleStrip = 0;
  private static int _lines = 0;
  private static int _lineLoop = 0;
  private static int _points = 0;

  public static int triangleStrip()
  {
	  return _triangleStrip;
  }
  public static int lines()
  {
	  return _lines;
  }
  public static int lineLoop()
  {
	  return _lineLoop;
  }
  public static int points()
  {
	  return _points;
  }

  public static void init(INativeGL ngl)
  {
	_triangleStrip = ngl.Primitive_TriangleStrip();
	_lines = ngl.Primitive_Lines();
	_lineLoop = ngl.Primitive_LineLoop();
	_points = ngl.Primitive_Points();
  }
}