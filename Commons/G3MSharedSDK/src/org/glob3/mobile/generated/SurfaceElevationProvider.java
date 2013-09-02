package org.glob3.mobile.generated; 
public interface SurfaceElevationProvider
{






  public void dispose();

  void addListener(Angle latitude, Angle longitude, SurfaceElevationListener listener);

  void addListener(Geodetic2D position, SurfaceElevationListener listener);

  void removeListener(SurfaceElevationListener listener);

}