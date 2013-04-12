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
#include "Vector2I.hpp"
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
  const Vector2I _resolution;

public:
  ElevationData(const Sector& sector,
                const Vector2I& resolution);

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
                                int *type,
                                double valueForNoData = IMathUtils::instance()->NanD()) const = 0;

  virtual double getElevationAt(const Angle& latitude,
                                const Angle& longitude,
                                int *type,
                                double valueForNoData = IMathUtils::instance()->NanD()) const = 0;

  double getElevationAt(const Geodetic2D& position,
                                int *type) const {
    return getElevationAt(position.latitude(),
                          position.longitude(),
                          type);
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
  
  const Vector2I getResolution() const {
    return _resolution;
  }

};

#endif
