package org.glob3.mobile.generated; 
//Every SurfaceElevationProvider should store petitions in a SurfaceElevationProvider_Tree
public class SurfaceElevationProvider_Tree extends GenericQuadTree
{
  public final void notifyListeners(Sector sector, ElevationData ed, double verticalExaggeration)
  {
    SurfaceElevationProvider_Visitor visitor = new SurfaceElevationProvider_Visitor(sector, ed, verticalExaggeration);
    acceptVisitor(sector, visitor);
  }
}