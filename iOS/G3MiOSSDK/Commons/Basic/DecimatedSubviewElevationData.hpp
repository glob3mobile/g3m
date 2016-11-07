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
                                const Vector2I& extent);

};

#endif
