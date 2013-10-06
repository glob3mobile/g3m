package org.glob3.mobile.generated; 
public class SimpleInitialCameraPositionProvider extends InitialCameraPositionProvider
{


  public final Geodetic3D getCameraPosition(Planet planet, PlanetRenderer planetRenderer)
  {
    return planet.getDefaultCameraPosition(planetRenderer.getRenderedSector());
  }
}