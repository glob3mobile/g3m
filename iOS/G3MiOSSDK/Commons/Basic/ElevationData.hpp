//
//  ElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

#ifndef __G3MiOSSDK__ElevationData__
#define __G3MiOSSDK__ElevationData__

#include <string>
#include "Geodetic2D.hpp"
#include "Geodetic3D.hpp"
#include "Sector.hpp"

class Vector2I;
class Mesh;
class Ellipsoid;
class Vector3D;


class ElevationData {
protected:
  const Sector _sector;
  
  const int _width;
  const int _height;

public:
  ElevationData(const Sector& sector,
                const Vector2I& extent);

  virtual ~ElevationData() {

  }

  virtual const Vector2I getExtent() const;

  virtual int getExtentWidth() const {
    return _width;
  }

  virtual int getExtentHeight() const {
    return _height;
  }

  virtual const Geodetic2D getResolution() const = 0;

  virtual double getElevationAt(int x,
                                int y,
                                double valueForNoData) const = 0;

  virtual double getElevationAt(const Angle& latitude,
                                const Angle& longitude,
                                double valueForNoData) const = 0;

  double getElevationAt(const Geodetic2D& position,
                        double valueForNoData) const {
    return getElevationAt(position.latitude(),
                          position.longitude(),
                          valueForNoData);
  }

  virtual const std::string description(bool detailed) const = 0;

  virtual Vector3D getMinMaxAverageHeights() const = 0;

  virtual Mesh* createMesh(const Ellipsoid* ellipsoid,
                           float verticalExaggeration,
                           const Geodetic3D& positionOffset,
                           float pointSize) const;

  virtual const Sector getSector() const {
    return _sector;
  }

  virtual bool hasNoData() const = 0;

};

#endif
