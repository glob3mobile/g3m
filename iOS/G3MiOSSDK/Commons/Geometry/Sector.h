//
//  Sector.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Sector_h
#define G3MiOSSDK_Sector_h

#include <vector>

#include "Geodetic2D.hpp"

class Sector {
  
private:
   const Geodetic2D _min, _max;
  
public:

  Sector(const Angle& minLat, const Angle& minLon, 
         const Angle& maxLat, const Angle& maxLon):
  _min(minLat, minLon), _max(maxLat, maxLon) {}
  
  
  const Geodetic2D min() const {
    return _min;
  }
  
  const Geodetic2D max() const {
    return _max;
  }
  
};


#endif
