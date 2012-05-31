//
//  Angle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Angle_hpp
#define G3MiOSSDK_Angle_hpp

Angle::clampedTo(const Angle& min,
                 const Angle& max) const {
  if (_degrees < min._degrees) {
    return min;
  }
  
  if (_degrees > max._degrees) {
    return max;
  }
  
  return *this;
}


#endif
