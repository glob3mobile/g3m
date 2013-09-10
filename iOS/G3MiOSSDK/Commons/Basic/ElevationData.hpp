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
class Interpolator;

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

  //  bool isEquivalentTo(const ElevationData* ed) {
  //    bool equivalent = true;
  //    const int width  = 3;
  //    const int height = 3;
  //    for (int x = 0; x < width; x++) {
  //      const double u = (double) x / (width  - 1);
  //
  //      for (int y = 0; y < height; y++) {
  //        const double v = 1.0 - ( (double) y / (height - 1) );
  //
  //        const Geodetic2D position = _sector.getInnerPoint(u, v);
  //
  //        const double elevation = getElevationAt(position);
  //        const double elevation2 = ed->getElevationAt(position);
  //
  //        if (elevation != elevation2) {
  //          printf("%s -> %f != %f\n", position.description().c_str(), elevation, elevation2);
  //          equivalent = false;
  //        }
  //      }
  //    }
  //    return equivalent;
  //  }
  
};

#endif
