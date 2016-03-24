package org.glob3.mobile.generated; 
public class CityGMLBuildingSurface extends Surface
{
  private CityGMLBuildingSurfaceType _type;
  public CityGMLBuildingSurface(java.util.ArrayList<Geodetic3D> geodeticCoordinates, CityGMLBuildingSurfaceType type)
  {
     super(geodeticCoordinates);
     _type = type;
  }

  public final CityGMLBuildingSurfaceType getType()
  {
    return _type;
  }

  public static CityGMLBuildingSurface createFromArrayOfCityGMLWGS84Coordinates(java.util.ArrayList<Double> coor, CityGMLBuildingSurfaceType type)
  {

    java.util.ArrayList<Geodetic3D> geodeticCoordinates = new java.util.ArrayList<Geodetic3D>();

    for (int i = 0; i < coor.size(); i += 3)
    {
      final double lat = coor.get(i + 1);
      final double lon = coor.get(i);
      final double h = coor.get(i + 2);
      geodeticCoordinates.add(new Geodetic3D(Geodetic3D.fromDegrees(lat, lon, h)));
    }
    return new CityGMLBuildingSurface(geodeticCoordinates, type);
  }
}