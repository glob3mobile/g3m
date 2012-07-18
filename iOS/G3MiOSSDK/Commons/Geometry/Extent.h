//
//  Extent.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 16/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_Extent_h
#define G3MiOSSDK_Extent_h

#include "Context.hpp"

class Frustum;

class Extent {
public:
  virtual ~Extent() { }
  
  virtual bool touches(const Frustum *frustum) const = 0;
  
  virtual int projectedSize(const RenderContext* rc) const {
    return sqrt(squaredProjectedSize(rc));
  }

  virtual int squaredProjectedSize(const RenderContext* rc) const = 0;
};


#endif
