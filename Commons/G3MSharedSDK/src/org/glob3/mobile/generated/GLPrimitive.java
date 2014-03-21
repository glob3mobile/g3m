package org.glob3.mobile.generated; 
public class GLPrimitive
{
  private static int _triangles = 0;
  private static int _triangleStrip = 0;
  private static int _triangleFan = 0;

  private static int _lines = 0;
  private static int _lineStrip = 0;
  private static int _lineLoop = 0;

  private static int _points = 0;

  public static int triangles()
  {
    return _triangles;
  }

  public static int triangleStrip()
  {
    return _triangleStrip;
  }

  public static int triangleFan()
  {
    return _triangleFan;
  }

  public static int lines()
  {
    return _lines;
  }

  public static int lineStrip()
  {
    return _lineStrip;
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
    _triangles = ngl.Primitive_Triangles();
    _triangleStrip = ngl.Primitive_TriangleStrip();
    _triangleFan = ngl.Primitive_TriangleFan();

    _lines = ngl.Primitive_Lines();
    _lineStrip = ngl.Primitive_LineStrip();
    _lineLoop = ngl.Primitive_LineLoop();

    _points = ngl.Primitive_Points();
  }
}