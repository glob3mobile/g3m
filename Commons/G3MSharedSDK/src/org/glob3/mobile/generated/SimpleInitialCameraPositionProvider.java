package org.glob3.mobile.generated; 
public class SimpleInitialCameraPositionProvider extends InitialCameraPositionProvider
{


  public final Geodetic3D getCameraPosition(Planet planet, PlanetRenderer planetRenderer, Vector2I viewportExtent)
  {
    return planet.getDefaultCameraPosition(viewportExtent, planetRenderer.getRenderedSector());
  }
}