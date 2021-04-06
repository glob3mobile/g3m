package org.glob3.mobile.generated;
public class Measure_MarkFilter extends MarkFilter
{
  private final String _token;

  public Measure_MarkFilter(String token)
  {
     _token = token;
  }

  public final boolean test(Mark mark)
  {
    return (_token.equals(mark.getToken()));
  }
}