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
                                   const Vector2I& extent);

};

#endif
