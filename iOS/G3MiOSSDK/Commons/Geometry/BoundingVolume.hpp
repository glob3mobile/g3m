//
//  BoundingVolume.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 16/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_BoundingVolume
#define G3MiOSSDK_BoundingVolume

#include "Context.hpp"
//#include "IMathUtils.hpp"
#include "Vector2I.hpp"

class Vector2D;
class Vector3D;

class Frustum;
class Box;
class Sphere;
class GLState;

class BoundingVolume {
public:
  virtual ~BoundingVolume() {

  }

  virtual double projectedArea(const G3MRenderContext* rc) const = 0;
  //virtual Vector2I projectedExtent(const G3MRenderContext* rc) const = 0;

  //virtual Vector3D intersectionWithRay(const Vector3D& origin,
  //                                     const Vector3D& direction) const = 0;

  virtual void render(const G3MRenderContext* rc,
                      const GLState& parentState) const = 0;

  virtual bool touches(const BoundingVolume* that) const = 0;
  virtual bool touchesBox(const Box* that) const = 0;
  virtual bool touchesSphere(const Sphere* that) const = 0;

  virtual bool touchesFrustum(const Frustum* that) const = 0;

  virtual bool contains(const Vector3D& point) const = 0;

  virtual bool fullContains(const BoundingVolume* that) const = 0;
  virtual bool fullContainedInBox(const Box* that) const = 0;
  virtual bool fullContainedInSphere(const Sphere* that) const = 0;

  virtual BoundingVolume* mergedWith(const BoundingVolume* that) const = 0;
  virtual BoundingVolume* mergedWithBox(const Box* that) const = 0;
  virtual BoundingVolume* mergedWithSphere(const Sphere* that) const = 0;

  virtual Sphere* createSphere() const = 0;
  
};

#endif
