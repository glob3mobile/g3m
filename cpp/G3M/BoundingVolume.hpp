//
//  BoundingVolume.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 16/07/12.
//

#ifndef G3M_BoundingVolume
#define G3M_BoundingVolume

class G3MRenderContext;
class GLState;
class Color;
class Box;
class Sphere;
class Frustum;
class Vector3D;


class BoundingVolume {
public:
  virtual ~BoundingVolume() {

  }

  virtual double projectedArea(const G3MRenderContext* rc) const = 0;

  virtual void render(const G3MRenderContext* rc,
                      const GLState* parentState,
                      const Color& color) const = 0;

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

  virtual BoundingVolume* copy() const = 0;

};

#endif
