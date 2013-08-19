package org.glob3.mobile.generated; 
public class SurfaceElevationProvider.SurfaceElevationProvider_Visitor
{
   public boolean visitElement(Sector sector, Object element)
   {
     SurfaceElevationListener listener = (SurfaceElevationListener) element;
     listener.elevationChanged(sector, _elevationData, _verticalExaggeration);
     return false;
   }
   public boolean visitElement(Geodetic2D geodetic, Object element)
   {
     SurfaceElevationListener listener = (SurfaceElevationListener) element;
     double height = _elevationData.getElevationAt(geodetic);
     listener.elevationChanged(geodetic, height, _verticalExaggeration);
     return false;
   }
}