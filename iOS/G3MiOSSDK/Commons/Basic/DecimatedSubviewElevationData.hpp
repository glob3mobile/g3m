//
//  DecimatedSubviewElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/1/13.
//
//

#ifndef __G3MiOSSDK__DecimatedSubviewElevationData__
#define __G3MiOSSDK__DecimatedSubviewElevationData__

#include "SubviewElevationData.hpp"

class DecimatedSubviewElevationData : public SubviewElevationData {
private:
  double getElevationBoxAt(const ElevationData* elevationData,
                           double x0, double y0,
                           double x1, double y1) const;

  const Vector2D getParentXYAt(const ElevationData* elevationData,
                               const Geodetic2D& position) const;

public:
  DecimatedSubviewElevationData(const ElevationData* elevationData,
                                const Sector& sector,
                                const Vector2I& extent) :
  SubviewElevationData(elevationData,
                       sector,
                       extent)
  {
    const Vector2D parentXYAtLower = getParentXYAt(elevationData, _sector._lower);
    const Vector2D parentXYAtUpper = getParentXYAt(elevationData, _sector._upper);
    const Vector2D parentDeltaXY = parentXYAtUpper.sub(parentXYAtLower);

    for (int x = 0; x < _width; x++) {
      const double u0 = (double) x     / (_width - 1);
      const double u1 = (double) (x+1) / (_width - 1);
      const double x0 = u0 * parentDeltaXY._x + parentXYAtLower._x;
      const double x1 = u1 * parentDeltaXY._x + parentXYAtLower._x;

      for (int y = 0; y < _height; y++) {
        const double v0 = (double) y     / (_height - 1);
        const double v1 = (double) (y+1) / (_height - 1);
        const double y0 = v0 * parentDeltaXY._y + parentXYAtLower._y;
        const double y1 = v1 * parentDeltaXY._y + parentXYAtLower._y;

        const int index = ((_height-1-y) * _width) + x;

        const double height = getElevationBoxAt(elevationData,
                                                x0, y0,
                                                x1, y1);
        _buffer[index] = (float) height;

        if (!_hasNoData) {
          if (ISNAN(height)) {
            _hasNoData = true;
          }
        }
      }
    }
  }

  
};

#endif
