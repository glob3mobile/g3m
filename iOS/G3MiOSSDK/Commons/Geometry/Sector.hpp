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
  const Geodetic2D _lower;
  const Geodetic2D _upper;
  
  const Angle _deltaLatitude;
  const Angle _deltaLongitude;

  
public:
  
  Sector(const Geodetic2D& lower,
         const Geodetic2D& upper) :
  _lower(lower),
  _upper(upper),
  _deltaLatitude(upper.latitude().sub(lower.latitude())),
  _deltaLongitude(upper.longitude().sub(lower.longitude()))
  {
  }
  
  const Geodetic2D lower() const {
    return _lower;
  }
  
  const Geodetic2D upper() const {
    return _upper;
  }
  
  bool contains(const Geodetic2D& position) const;
  
  bool touchesWith(const Sector& that) const;
  
  Angle getDeltaLatitude() const {
    return _deltaLatitude;
  }
  
  Angle getDeltaLongitude() const {
    return _deltaLongitude;
  }
  
};


#endif
