package org.glob3.mobile.generated; 
public interface SurfaceElevationProvider
{


  public class SurfaceElevationProvider_Visitor extends GenericQuadTreeVisitor
  {
    private final Sector _sector ;
    private final ElevationData _elevationData;
    private final double _verticalExaggeration;


    SurfaceElevationProvider_Visitor(Sector sector, ElevationData ed, double verticalExaggeration);

    boolean visitElement(Sector sector, Object element);

    boolean visitElement(Geodetic2D geodetic, Object element);

    void endVisit(boolean aborted)
    {
    }
  }


  //Every SurfaceElevationProvider should store petitions in a SurfaceElevationProvider_Tree
  public class SurfaceElevationProvider_Tree extends GenericQuadTree
  {
    void notifyListeners(Sector sector, ElevationData ed, double verticalExaggeration);
  }

  public void dispose();

  void addListener(Angle latitude, Angle longitude, SurfaceElevationListener listener);

  void addListener(Geodetic2D position, SurfaceElevationListener listener);

  void removeListener(SurfaceElevationListener listener);

}