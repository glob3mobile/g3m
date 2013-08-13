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

class Angle;
class Geodetic2D;



class GeodeticSurfaceElevationListener {
  const Geodetic2D _position;
public:

  GeodeticSurfaceElevationListener(const Geodetic2D& pos):_position(pos){}

  virtual ~GeodeticSurfaceElevationListener() {
  }

  Geodetic2D getPosition() const{ return _position;}

  virtual void elevationChanged(double newElevation) = 0;
};





class SurfaceElevationProvider {

protected:

  class SurfaceElevationProvider_Tree: public GenericQuadTree{

    class SurfaceElevationProvider_Visitor: public GenericQuadTreeVisitor{

    public:

      mutable std::vector<GeodeticSurfaceElevationListener*> observers;

      bool visitElement(const Sector& sector,
                        const void*   element) const{
        observers.push_back( (GeodeticSurfaceElevationListener*) element);
        return false;
      }

      bool visitElement(const Geodetic2D& geodetic,
                        const void*   element) const{
        observers.push_back( (GeodeticSurfaceElevationListener*) element);
        return false;
      }

      void endVisit(bool aborted) const{
        
      }
    };

  public:
    std::vector<GeodeticSurfaceElevationListener*> getObserversForSector(const Sector& sector) const{
      SurfaceElevationProvider_Visitor visitor;
      acceptVisitor(sector, visitor);
      return visitor.observers;
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
                           GeodeticSurfaceElevationListener* observer) = 0;

  virtual void addListener(const Geodetic2D& position,
                           GeodeticSurfaceElevationListener* observer) = 0;

  virtual void removeListener(GeodeticSurfaceElevationListener* observer) = 0;
  
};

#endif
