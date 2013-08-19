package org.glob3.mobile.generated; 
public class SurfaceElevationProvider_Visitor extends GenericQuadTreeVisitor
{
  private final Sector _sector ;
  private final ElevationData _elevationData;
  private final double _verticalExaggeration;

  public SurfaceElevationProvider_Visitor(Sector sector, ElevationData ed, double verticalExaggeration)
  {
     _sector = new Sector(sector);
     _elevationData = ed;
     _verticalExaggeration = verticalExaggeration;
  }

  public final boolean visitElement(Sector sector, Object element)
  {
    SurfaceElevationListener listener = (SurfaceElevationListener) element;
    listener.elevationChanged(sector, _elevationData, _verticalExaggeration);
    return false;
  }

  public final boolean visitElement(Geodetic2D geodetic, Object element)
  {
    SurfaceElevationListener listener = (SurfaceElevationListener) element;
    double height = _elevationData.getElevationAt(geodetic);
    listener.elevationChanged(geodetic, height, _verticalExaggeration);
    return false;
  }

  public final void endVisit(boolean aborted)
  {
  }
}