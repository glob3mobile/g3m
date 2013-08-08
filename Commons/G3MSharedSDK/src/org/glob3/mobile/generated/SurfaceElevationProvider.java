package org.glob3.mobile.generated; 
public interface SurfaceElevationProvider
{
  public void dispose();

  void addListener(Angle latitude, Angle longitude, SurfaceElevationListener observer);

  void addListener(Geodetic2D position, SurfaceElevationListener observer);

  void removeListener(SurfaceElevationListener observer);

}