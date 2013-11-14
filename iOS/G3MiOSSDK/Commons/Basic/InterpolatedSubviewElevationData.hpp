//
//  InterpolatedSubviewElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/1/13.
//
//

#ifndef __G3MiOSSDK__InterpolatedSubviewElevationData__
#define __G3MiOSSDK__InterpolatedSubviewElevationData__

#include "SubviewElevationData.hpp"

class InterpolatedSubviewElevationData : public SubviewElevationData {
public:

  InterpolatedSubviewElevationData(const ElevationData* elevationData,
                                   const Sector& sector,
                                   const Vector2I& extent) :
  SubviewElevationData(elevationData,
                       sector,
                       extent)
  {
    for (int x = 0; x < _width; x++) {
      const double u = (double) x / (_width - 1);

      const Angle longitude = _sector.getInnerPointLongitude(u);

      for (int y = 0; y < _height; y++) {
        const double v = 1.0 - ( (double) y / (_height - 1) );

        const Angle latitude = _sector.getInnerPointLatitude(v);

        const int index = ((_height-1-y) * _width) + x;

        const double height = elevationData->getElevationAt(latitude,
                                                            longitude);

        _buffer[index] = (float) height;

        if (!_hasNoData) {
          if ( ISNAN(height) ) {
            _hasNoData = true;
          }
        }
      }
    }
  }
};

#endif
