package org.glob3.mobile.generated; 
public class SurfaceElevationProvider.SurfaceElevationProvider_Tree
{
   public void notifyListeners(Sector sector, ElevationData ed, double verticalExaggeration)
   {
     SurfaceElevationProvider_Visitor visitor = new SurfaceElevationProvider_Visitor(sector, ed, verticalExaggeration);
     acceptVisitor(sector, visitor);
   }
}