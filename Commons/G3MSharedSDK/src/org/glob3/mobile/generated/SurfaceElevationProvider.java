package org.glob3.mobile.generated; 
public interface SurfaceElevationProvider
{


  public class Geodetic2DObserver
  {
    public final SurfaceElevationListener _observer;
    public final Geodetic2D _position;

    Geodetic2DObserver(SurfaceElevationListener o, Geodetic2D p)
    {
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
       _observer = o;
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
       _position = new Geodetic2D(p);
    }

  }

  public class SurfaceElevationProvider_Tree extends GenericQuadTree
  {

    private static class SurfaceElevationProvider_Visitor extends GenericQuadTreeVisitor
    {
      private final Sector _sector ;
      private final ElevationData _elevationData;
      private final double _verticalExaggeration;



      SurfaceElevationProvider_Visitor(Sector sector, ElevationData ed)
      {
         this(sector, ed, 1.0);
      }
      SurfaceElevationProvider_Visitor(Sector sector, ElevationData ed, double verticalExaggeration)
      {
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
         _sector = new Sector(sector);
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
         _elevationData = ed;
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
         _verticalExaggeration = verticalExaggeration;
      }

//      mutable std::vector<Geodetic2DObserver> observers;

      boolean visitElement(Sector sector, Object element)
      {
//        observers.push_back( (SurfaceElevationListener*) element);

        public SurfaceElevationListener observer = (SurfaceElevationListener*) element;
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
        observer.elevationChanged(sector, _elevationData, _verticalExaggeration);
        public return false = new return();
      }

      boolean visitElement(Geodetic2D geodetic, Object element)
      {
//        observers.push_back( (SurfaceElevationListener*) element);
//        observers.push_back( Geodetic2DObserver((SurfaceElevationListener*) element, geodetic));
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

//    std::vector<Geodetic2DObserver> getObserversForSector(const Sector& sector) const{
//      SurfaceElevationProvider_Visitor visitor;
//      acceptVisitor(sector, visitor);
//      return visitor.observers;
//    }

    void notifyObservers(Sector sector, ElevationData ed)
    {
       notifyObservers(sector, ed, 1.0);
    }
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