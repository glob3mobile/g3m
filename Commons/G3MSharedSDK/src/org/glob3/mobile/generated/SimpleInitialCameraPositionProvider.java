package org.glob3.mobile.generated;import java.util.*;

public class SimpleInitialCameraPositionProvider extends InitialCameraPositionProvider
{


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic3D getCameraPosition(const Planet* planet, const PlanetRenderer* planetRenderer) const
  public final Geodetic3D getCameraPosition(Planet planet, PlanetRenderer planetRenderer)
  {
	final Sector sector = (planetRenderer == null) ? null : planetRenderer.getRenderedSector();
	return ((sector == null) ? planet.getDefaultCameraPosition(Sector.fullSphere()) : planet.getDefaultCameraPosition(sector));
  }
}
