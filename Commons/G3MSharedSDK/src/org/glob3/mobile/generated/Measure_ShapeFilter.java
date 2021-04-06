package org.glob3.mobile.generated;
public class Measure_ShapeFilter extends ShapeFilter
{
  private final String _token;

  public Measure_ShapeFilter(String token)
  {
     _token = token;
  }

  public final boolean test(Shape shape)
  {
    return (_token.equals(shape.getToken()));
  }
}