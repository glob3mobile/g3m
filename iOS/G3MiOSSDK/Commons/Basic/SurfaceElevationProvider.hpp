//
//  SurfaceElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/2/13.
//
//

#ifndef __G3MiOSSDK__SurfaceElevationProvider__
#define __G3MiOSSDK__SurfaceElevationProvider__

#include "GenericQuadTree.hpp"

#include "ElevationData.hpp"

class Angle;
class Geodetic2D;



class SurfaceElevationListener {
public:

  virtual ~SurfaceElevationListener() {
  }

  virtual void elevationChanged(const Geodetic2D& position,
                                double rawElevation,            //Without considering vertical exaggeration
                                double verticalExaggeration){ }
  
  virtual void elevationChanged(const Sector& position,
                                const ElevationData* rawElevationData, //Without considering vertical exaggeration
                                double verticalExaggeration){ }
};





class SurfaceElevationProvider {

protected:

  class Geodetic2DObserver{
  public:
    SurfaceElevationListener const * _observer;
    Geodetic2D* const _position;

    Geodetic2DObserver(SurfaceElevationListener * o, const Geodetic2D& p):
    _observer(o), _position(new Geodetic2D(p)){}

  };

  class SurfaceElevationProvider_Tree: public GenericQuadTree{

    class SurfaceElevationProvider_Visitor: public GenericQuadTreeVisitor{
      const Sector _sector;
      const ElevationData* _elevationData;
      const double _verticalExaggeration;

    public:


      SurfaceElevationProvider_Visitor(const Sector& sector,
                                       const ElevationData* ed,
                                       double verticalExaggeration = 1.0):
      _sector(sector), _elevationData(ed), _verticalExaggeration(verticalExaggeration){}

//      mutable std::vector<Geodetic2DObserver> observers;

      bool visitElement(const Sector& sector,
                        const void*   element) const{
//        observers.push_back( (SurfaceElevationListener*) element);

        SurfaceElevationListener* observer = (SurfaceElevationListener*) element;
        observer->elevationChanged(sector, _elevationData, _verticalExaggeration);
        return false;
      }

      bool visitElement(const Geodetic2D& geodetic,
                        const void*   element) const{
//        observers.push_back( (SurfaceElevationListener*) element);
//        observers.push_back( Geodetic2DObserver((SurfaceElevationListener*) element, geodetic));
        SurfaceElevationListener* observer = (SurfaceElevationListener*) element;

        double height = _elevationData->getElevationAt(geodetic);

        observer->elevationChanged(geodetic, height, _verticalExaggeration);
        
        return false;
      }

      void endVisit(bool aborted) const{
        
      }
    };

  public:
//    std::vector<Geodetic2DObserver> getObserversForSector(const Sector& sector) const{
//      SurfaceElevationProvider_Visitor visitor;
//      acceptVisitor(sector, visitor);
//      return visitor.observers;
//    }

    void notifyObservers(const Sector& sector, const ElevationData* ed, double verticalExaggeration = 1.0) const{
      SurfaceElevationProvider_Visitor visitor(sector, ed, verticalExaggeration);
      acceptVisitor(sector, visitor);
    }

  };

public:
#ifdef C_CODE
  virtual ~SurfaceElevationProvider() { }
#endif
#ifdef JAVA_CODE
  public void dispose();
#endif

  virtual void addListener(const Angle& latitude,
                           const Angle& longitude,
                           SurfaceElevationListener* observer) = 0;

  virtual void addListener(const Geodetic2D& position,
                           SurfaceElevationListener* observer) = 0;

  virtual void removeListener(SurfaceElevationListener* observer) = 0;
  
};

#endif
