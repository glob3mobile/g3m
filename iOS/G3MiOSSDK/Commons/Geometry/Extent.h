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
  
  
  virtual double projectedArea(const RenderContext* rc) const {
    return sqrt(squaredProjectedArea(rc));
  }

  virtual double squaredProjectedArea(const RenderContext* rc) const = 0;
  
  virtual Vector2D projectedExtent(const RenderContext* rc) const = 0;

};


#endif
