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
#include "IMathUtils.hpp"
#include "Vector2I.hpp"

class Vector2D;
class Vector3D;

class Frustum;
class Box;

class Extent {
public:
  virtual ~Extent() { }
  
  virtual bool touches(const Frustum *frustum) const = 0;
  
  
  virtual double projectedArea(const RenderContext* rc) const {
    return GMath.sqrt(squaredProjectedArea(rc));
  }

  virtual double squaredProjectedArea(const RenderContext* rc) const = 0;
  
  virtual Vector2I projectedExtent(const RenderContext* rc) const = 0;
  
  virtual Vector3D intersectionWithRay(const Vector3D& origin, const Vector3D& direction) const = 0;
  
  virtual void render(const RenderContext* rc) = 0;
  
  virtual bool touchesBox(const Box *box) const = 0;

};


#endif
