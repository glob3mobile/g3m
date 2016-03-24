package org.glob3.mobile.generated; 
public class CityGLMBuildingSurface extends Surface
{
  private CityGLMBuildingSurfaceType _type;
  public CityGLMBuildingSurface(java.util.ArrayList<Geodetic3D> geodeticCoordinates, CityGLMBuildingSurfaceType type)
  {
     super(geodeticCoordinates);
     _type = type;
  }

  public final CityGLMBuildingSurfaceType getType()
  {
    return _type;
  }

  public static CityGLMBuildingSurface createFromArrayOfCityGMLWGS84Coordinates(java.util.ArrayList<Double> coor, CityGLMBuildingSurfaceType type)
  {

    java.util.ArrayList<Geodetic3D> geodeticCoordinates = new java.util.ArrayList<Geodetic3D>();

    for (int i = 0; i < coor.size(); i += 3)
    {
      final double lat = coor.get(i + 1);
      final double lon = coor.get(i);
      final double h = coor.get(i + 2);
      geodeticCoordinates.add(new Geodetic3D(Geodetic3D.fromDegrees(lat, lon, h)));
    }
    return new CityGLMBuildingSurface(geodeticCoordinates, type);
  }
}