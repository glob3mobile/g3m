package org.glob3.mobile.generated; 
public interface SurfaceElevationProvider
{


  public class SurfaceElevationProvider_Visitor extends GenericQuadTreeVisitor
  {
    private final Sector _sector ;
    private final ElevationData _elevationData;
    private final double _verticalExaggeration;


    SurfaceElevationProvider_Visitor(Sector sector, ElevationData ed, double verticalExaggeration)
    {
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
       _sector = new Sector(sector);
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
       _elevationData = ed;
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
       _verticalExaggeration = verticalExaggeration;
    }

    boolean visitElement(Sector sector, Object element)
    {
      public SurfaceElevationListener observer = (SurfaceElevationListener*) element;
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
      observer.elevationChanged(sector, _elevationData, _verticalExaggeration);
      public return false = new return();
    }

    boolean visitElement(Geodetic2D geodetic, Object element)
    {
      public SurfaceElevationListener observer = (SurfaceElevationListener*) element;
      public double height = _elevationData.getElevationAt(geodetic);
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
      observer.elevationChanged(geodetic, height, _verticalExaggeration);
      public return false = new return();
    }

    void endVisit(boolean aborted)
    {
    }
  }


  //Every SurfaceElevationProvider should store petitions in a SurfaceElevationProvider_Tree
  public class SurfaceElevationProvider_Tree extends GenericQuadTree
  {
    void notifyObservers(Sector sector, ElevationData ed, double verticalExaggeration)
    {
      SurfaceElevationProvider_Visitor visitor(sector NamelessParameter1, ed NamelessParameter2, verticalExaggeration NamelessParameter3);
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
      acceptVisitor(sector, visitor);
    }

  }

  public void dispose();

  void addListener(Angle latitude, Angle longitude, SurfaceElevationListener observer);

  void addListener(Geodetic2D position, SurfaceElevationListener observer);

  void removeListener(SurfaceElevationListener observer);

}