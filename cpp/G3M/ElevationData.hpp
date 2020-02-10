//
//  ElevationData.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

#ifndef __G3M__ElevationData__
#define __G3M__ElevationData__

#include <string>
#include "Geodetic2D.hpp"
#include "Sector.hpp"

class Interpolator;
class Mesh;
class Geodetic3D;
class Vector2I;


class ElevationData {
private:
  mutable Interpolator* _interpolator;
  Interpolator* getInterpolator() const;

protected:
  const Sector _sector;
  const int _width;
  const int _height;

  const Geodetic2D _resolution;

public:
  ElevationData(const Sector& sector,
                const Vector2I& extent);

  virtual ~ElevationData();

  virtual const Vector2I getExtent() const;

  virtual int getExtentWidth() const {
    return _width;
  }

  virtual int getExtentHeight() const {
    return _height;
  }

  const Geodetic2D getResolution() const {
    return _resolution;
  }

  virtual double getElevationAt(int x,
                                int y) const = 0;

  double getElevationAt(const Vector2I& position) const;

  virtual const std::string description(bool detailed) const = 0;

  virtual Vector3D getMinMaxAverageElevations() const = 0;

  virtual Mesh* createMesh(const Planet* planet,
                           float verticalExaggeration,
                           const Geodetic3D& positionOffset,
                           float pointSize) const;

  virtual Mesh* createMesh(const Planet* planet,
                           float verticalExaggeration,
                           const Geodetic3D& positionOffset,
                           float pointSize,
                           const Sector& sector,
                           const Vector2I& resolution) const;

  virtual const Sector getSector() const {
    return _sector;
  }

  virtual bool hasNoData() const = 0;

  double getElevationAt(const Angle& latitude,
                        const Angle& longitude) const;

  double getElevationAt(const Geodetic2D& position) const {
    return getElevationAt(position._latitude,
                          position._longitude);
  }
  
};

#endif
