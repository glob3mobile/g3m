//
//  ElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

#ifndef __G3MiOSSDK__ElevationData__
#define __G3MiOSSDK__ElevationData__

#include "Sector.hpp"
#include <string>

class Vector2I;
class Mesh;
class Ellipsoid;
class Vector3D;

class ElevationData {
protected:
  const Sector _sector;
  const int _width;
  const int _height;
  const double _noDataValue;

public:
  ElevationData(const Sector& sector,
                const Vector2I& resolution,
                double noDataValue);

  virtual ~ElevationData() {
  }

  virtual Vector2I getExtent() const;

  virtual int getExtentWidth() const {
    return _width;
  }

  virtual int getExtentHeight() const {
    return _height;
  }

  virtual double getElevationAt(int x, int y,
                                int *type) const = 0;

  virtual double getElevationAt(const Angle& latitude,
                                const Angle& longitude,
                                int *type) const = 0;

  virtual double getElevationAt(const Geodetic2D& position,
                                int *type) const {
    return getElevationAt(position.latitude(),
                          position.longitude(),
                          type);
  }

  virtual const std::string description(bool detailed) const = 0;

  virtual Vector3D getMinMaxAverageHeights() const = 0;

  virtual Mesh* createMesh(const Planet* planet,
                           float verticalExaggeration,
                           const Geodetic3D& positionOffset,
                           float pointSize) const;

  virtual const Sector getSector() const {
    return _sector;
  }

};

#endif
