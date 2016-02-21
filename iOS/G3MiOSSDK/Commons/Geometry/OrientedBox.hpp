//
//  OrientedBox.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 19/02/16.
//

#ifndef G3MiOSSDK_OrientedBox
#define G3MiOSSDK_OrientedBox

#include "BoundingVolume.hpp"
#include "Vector3D.hpp"
#include "Plane.hpp"

class Mesh;
class Sphere;
class Box;

class OrientedBox: public BoundingVolume {
private:

  mutable Mesh* _mesh;
  
  // the eight vertices (N:north, S:south, W:west, E:east, T:top, B:bottom
  const Vector3D _SWB, _SEB, _NWB, _NEB, _SWT, _SET, _NWT, _NET;

  
#ifdef C_CODE
  const Plane _westPlane;
  const Plane _eastPlane;
  const Plane _southPlane;
  const Plane _northPlane;
  const Plane _bottomPlane;
  const Plane _topPlane;
#endif

#ifdef JAVA_CODE
  private final Plane _westPlane;
  private final Plane _eastPlane;
  private final Plane _southPlane;
  private final Plane _northPlane;
  private final Plane _bottomPlane;
  private final Plane _topPlane;
#endif


public:
  
  OrientedBox(const Vector3D& lower,
              const Vector3D& lonAxis,
              const Vector3D& latAxis,
              const Vector3D& heightAxis):
  _SWB(lower),
  _SEB(_SWB.add(lonAxis)),
  _NWB(_SWB.add(latAxis)),
  _NEB(_SEB.add(latAxis)),
  _SWT(_SWB.add(heightAxis)),
  _SET(_SEB.add(heightAxis)),
  _NWT(_NWB.add(heightAxis)),
  _NET(_NEB.add(heightAxis)),
  _westPlane(Plane::fromPoints(_SWB, _SWT, _NWT)),
  _eastPlane(Plane::fromPoints(_SEB, _NEB, _NET)),
  _southPlane(Plane::fromPoints(_SEB, _SET, _SWT)),
  _northPlane(Plane::fromPoints(_NWT, _NET, _NEB)),
  _bottomPlane(Plane::fromPoints(_NEB, _SEB, _SWB)),
  _topPlane(Plane::fromPoints(_NET, _NWT, _SWT)),
  _mesh(NULL)
  {
  }

  ~OrientedBox();

  Mesh* createMesh(const Color& color) const;
  
  void render(const G3MRenderContext* rc,
              const GLState* parentState,
              const Color& color) const;

  bool contains(const Vector3D& point) const;
  bool fullContainsSphere(const Sphere* that) const;
  bool fullContainsBox(const Box* that) const;
  bool fullContains(const BoundingVolume* that) const {
    return that->fullContainedInOrientedBox(this);
  }
  bool fullContainedInBox(const Box* that) const;
  bool fullContainedInSphere(const Sphere* that) const;
  bool fullContainedInOrientedBox(const OrientedBox* that) const;

  Sphere* createSphere() const;


  
  // TODO for Agustin!
  double projectedArea(const G3MRenderContext* rc) const {}
  bool touches(const BoundingVolume* that) const {}
  bool touchesBox(const Box* that) const {}
  bool touchesSphere(const Sphere* that) const {}
  bool touchesFrustum(const Frustum* frustum) const {}
  BoundingVolume* mergedWith(const BoundingVolume* that) const {}
  BoundingVolume* mergedWithBox(const Box* that) const {}
  BoundingVolume* mergedWithSphere(const Sphere* that) const {}

};

#endif
