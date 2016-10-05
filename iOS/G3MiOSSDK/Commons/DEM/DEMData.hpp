//
//  DEM.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

#ifndef DEM_hpp
#define DEM_hpp

#include "RCObject.hpp"
#include "Sector.hpp"
#include "Geodetic2D.hpp"
#include "Vector2I.hpp"

class Mesh;
class Geodetic3D;


class DEMData : public RCObject {
protected:
  const Sector     _sector;
  const Vector2I   _extent;
  const Geodetic2D _resolution;

  DEMData(const Sector&   sector,
          const Vector2I& extent);

  virtual ~DEMData();

public:

  const Vector2I getExtent() const {
    return _extent;
  }

  const Geodetic2D getResolution() const {
    return _resolution;
  }

  virtual double getElevationAt(int x,
                                int y) const = 0;

  virtual Vector3D getMinMaxAverageElevations() const = 0;

  Mesh* createMesh(const Planet* planet,
                   float verticalExaggeration,
                   const Geodetic3D& positionOffset,
                   float pointSize) const;
  
};

#endif
