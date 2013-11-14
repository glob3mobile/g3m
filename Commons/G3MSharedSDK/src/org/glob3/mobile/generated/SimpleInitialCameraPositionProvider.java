package org.glob3.mobile.generated; 
public class SimpleInitialCameraPositionProvider extends InitialCameraPositionProvider
{


  public final Geodetic3D getCameraPosition(Planet planet, PlanetRenderer planetRenderer)
  {
    final Sector sector = planetRenderer.getRenderedSector();
    if (sector == null)
    {
      return planet.getDefaultCameraPosition(Sector.fullSphere());
    }
  
    return planet.getDefaultCameraPosition(sector);
  }
}