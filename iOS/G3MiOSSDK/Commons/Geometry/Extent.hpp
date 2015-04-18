//
//  Extent.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 16/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_Extent
#define G3MiOSSDK_Extent

#include "Context.hpp"
#include "IMathUtils.hpp"
#include "Vector2I.hpp"

class Vector2D;
class Vector3D;

class Frustum;
class Box;
class GLState;

class Extent {
public:
  virtual ~Extent() { }
  
  
  
  virtual double projectedArea(const G3MRenderContext* rc) const {
    return IMathUtils::instance()->sqrt(squaredProjectedArea(rc));
  }

  virtual double squaredProjectedArea(const G3MRenderContext* rc) const = 0;
  
  virtual Vector2I projectedExtent(const G3MRenderContext* rc) const = 0;
  
  virtual Vector3D intersectionWithRay(const Vector3D& origin, const Vector3D& direction) const = 0;
  
  virtual void render(const G3MRenderContext* rc, const GLState* parentState) = 0;

  virtual bool touches(const Frustum *frustum) const = 0;

  virtual bool touchesBox(const Box* box) const = 0;

  virtual bool fullContains(const Extent* that) const = 0;

  virtual bool fullContainedInBox(const Box* box) const = 0;

  virtual Extent* mergedWith(const Extent* that) const = 0;

  virtual Extent* mergedWithBox(const Box* that) const = 0;

};


#endif
