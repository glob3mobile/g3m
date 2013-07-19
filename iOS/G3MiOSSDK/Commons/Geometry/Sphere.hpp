//
//  Sphere.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 04/06/13.
//
//

#ifndef __G3MiOSSDK__Sphere__
#define __G3MiOSSDK__Sphere__

#include "Vector3D.hpp"
#include "BoundingVolume.hpp"
#include "Mesh.hpp"
#include "Color.hpp"


class Sphere : public BoundingVolume {
private:

  mutable Mesh *_mesh;
  void createWireframeMesh(Color* flatColor,
                           short resolution) const;

 
public:
  const Vector3D _center;
  const double   _radius;
  const double   _radiusSquared;


  Sphere(const Vector3D& center,
         double radius):
  _center(center),
  _radius(radius),
  _radiusSquared(radius * radius),
  _mesh(NULL)
  {
  }

  Sphere(const Sphere& that) :
  _center(that._center),
  _radius(that._radius),
  _radiusSquared(that._radiusSquared),
  _mesh(NULL)
  {
  }

  Vector3D getCenter() const{
    return _center;
  }

  double getRadius() const {
    return _radius;
  }

  double getRadiusSquared() const {
    return _radiusSquared;
  }

  double projectedArea(const G3MRenderContext* rc) const;
//  Vector2I projectedExtent(const G3MRenderContext* rc) const;

  void render(const G3MRenderContext* rc,
              const GLState& parentState) const;

  bool touches(const BoundingVolume* that) const {
    if (that == NULL) {
      return false;
    }
    return that->touchesSphere(this);
  }

  bool touchesBox(const Box* that) const;
  bool touchesFrustum(const Frustum* frustum) const;
  bool touchesSphere(const Sphere* that) const;

  BoundingVolume* mergedWith(const BoundingVolume* that) const {
    if (that == NULL) {
      return NULL;
    }
    return that->mergedWithSphere(this);
  }

  BoundingVolume* mergedWithBox(const Box* that) const;
  BoundingVolume* mergedWithSphere(const Sphere* that) const;

  bool contains(const Vector3D& point) const;

  bool fullContains(const BoundingVolume* that) const {
    return that->fullContainedInSphere(this);
  }

  bool fullContainedInBox(const Box* that) const;
  bool fullContainedInSphere(const Sphere* that) const;

  Sphere* createSphere() const;

};

#endif
