//
//  Extent.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 16/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_Extent_h
#define G3MiOSSDK_Extent_h

#include "Frustum.h"

class Extent {
public:
  virtual ~Extent() { }
  
  virtual bool touches(const Frustum *frustum) const = 0;
  //virtual projectedSize(const RenderContext* rc) const = 0;
};


#endif
