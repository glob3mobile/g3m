//
//  DecimatedSubviewElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/1/13.
//
//

#include "DecimatedSubviewElevationData.hpp"

const Vector2D DecimatedSubviewElevationData::getParentXYAt(const ElevationData* elevationData,
                                                            const Geodetic2D& position) const {
  const Sector parentSector = elevationData->getSector();
  const Geodetic2D parentLower = parentSector._lower;

  const double parentX = (
                          ( position._longitude._radians - parentLower._longitude._radians )
                          / parentSector._deltaLongitude._radians
                          * elevationData->getExtentWidth()
                          );

  const double parentY = (
                          ( position._latitude._radians - parentLower._latitude._radians )
                          / parentSector._deltaLatitude._radians
                          * elevationData->getExtentHeight()
                          );

  return Vector2D(parentX, parentY);
}

double DecimatedSubviewElevationData::getElevationBoxAt(const ElevationData* elevationData,
                                                        double x0, double y0,
                                                        double x1, double y1) const {
  const IMathUtils* mu = IMathUtils::instance();

  const double floorY0 = mu->floor(y0);
  const double ceilY1  = mu->ceil(y1);
  const double floorX0 = mu->floor(x0);
  const double ceilX1  = mu->ceil(x1);

  const int parentHeight = elevationData->getExtentHeight();
  const int parentWidth  = elevationData->getExtentWidth();

  if (floorY0 < 0 || ceilY1 >= parentHeight) {
    return 0;
  }
  if (floorX0 < 0 || ceilX1 >= parentWidth) {
    return 0;
  }

  double heightSum = 0;
  double area = 0;

  const double maxX = parentWidth  - 1;
  const double maxY = parentHeight - 1;

  for (double y = floorY0; y <= ceilY1; y++) {
    double ysize = 1.0;
    if (y < y0) {
      ysize *= (1.0 - (y0-y));
    }
    if (y > y1) {
      ysize *= (1.0 - (y-y1));
    }

    const int yy = (int) mu->min(y, maxY);

    for (double x = floorX0; x <= ceilX1; x++) {
      const double height = elevationData->getElevationAt((int) mu->min(x, maxX),
                                                          yy);

      if (ISNAN(height)) {
        return NAND;
      }

      double size = ysize;
      if (x < x0) {
        size *= (1.0 - (x0-x));
      }
      if (x > x1) {
        size *= (1.0 - (x-x1));
      }

      heightSum += height * size;
      area += size;
    }
  }
  
  return heightSum/area;
}
