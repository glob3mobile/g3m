package org.glob3.mobile.generated; 
public interface SurfaceElevationProvider
{






  void dispose();

  void addListener(Angle latitude, Angle longitude, SurfaceElevationListener listener);

  void addListener(Geodetic2D position, SurfaceElevationListener listener);

  boolean removeListener(SurfaceElevationListener listener);

  float getVerticalExaggeration();

}