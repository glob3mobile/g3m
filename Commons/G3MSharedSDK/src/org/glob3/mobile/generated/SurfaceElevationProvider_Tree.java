package org.glob3.mobile.generated; 
//Every SurfaceElevationProvider should store petitions in a SurfaceElevationProvider_Tree
public class SurfaceElevationProvider_Tree extends GenericQuadTree
{
  public final void notifyListeners(ElevationData ed, double verticalExaggeration)
  {
    SurfaceElevationProvider_Visitor visitor = new SurfaceElevationProvider_Visitor(ed, verticalExaggeration);
    acceptVisitor(ed.getSector(), visitor);
  }
}