//
//  Box.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 16/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_Box_h
#define G3MiOSSDK_Box_h

#include "Extent.h"
#include "Vector3D.hpp"


class Box: public Extent {
  
public:
  Box(const Vector3D& min, const Vector3D& max):
  _min(min),
  _max(max)
  {}
  
  bool touches(const Frustum *frustum) const {return true;};
  
private:
  Vector3D _min, _max;
  
};

#endif
