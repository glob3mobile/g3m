package org.glob3.mobile.generated; 
public class SimpleInitialCameraPositionProvider extends InitialCameraPositionProvider
{


  public final Geodetic3D getCameraPosition(Planet planet, PlanetRenderer planetRenderer)
  {
    final Sector sector = (planetRenderer == null) ? null : planetRenderer.getRenderedSector();
    return ((sector == null) ? planet.getDefaultCameraPosition(Sector.FULL_SPHERE) : planet.getDefaultCameraPosition(sector));
  }
}