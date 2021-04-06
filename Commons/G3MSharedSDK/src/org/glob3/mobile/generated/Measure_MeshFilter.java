package org.glob3.mobile.generated;
public class Measure_MeshFilter extends MeshFilter
{
  private final String _token;

  public Measure_MeshFilter(String token)
  {
     _token = token;
  }

  public final boolean test(Mesh mesh)
  {
    return (_token.equals(mesh.getToken()));
  }
}