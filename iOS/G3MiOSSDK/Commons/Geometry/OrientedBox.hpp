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

class Mesh;

class OrientedBox: public BoundingVolume {
private:

  mutable Mesh* _mesh;
  Mesh* createMesh(const Color& color) const;

public:
  
  // the eight vertices (N:north, S:south, W:west, E:east, T:top, B:bottom
  const Vector3D _NWT, _NET, _SWT, _SET;
  const Vector3D _NWB, _NEB, _SWB, _SEB;
  const Vector3D _centralAxis;

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
  _centralAxis(heightAxis.normalized()),
  _mesh(NULL)
  {
  }

  ~OrientedBox();

  
  // TODO for Agustin!
  double projectedArea(const G3MRenderContext* rc) const {}
  void render(const G3MRenderContext* rc,
              const GLState* parentState,
              const Color& color) const {}
  bool touches(const BoundingVolume* that) const {}
  bool touchesBox(const Box* that) const {}
  bool touchesSphere(const Sphere* that) const {}
  bool touchesFrustum(const Frustum* frustum) const {}
  bool contains(const Vector3D& p) const {}
  bool fullContains(const BoundingVolume* that) const {}
  bool fullContainedInBox(const Box* that) const {}
  bool fullContainedInSphere(const Sphere* that) const {}
  BoundingVolume* mergedWith(const BoundingVolume* that) const {}
  BoundingVolume* mergedWithBox(const Box* that) const {}
  BoundingVolume* mergedWithSphere(const Sphere* that) const {}
  Sphere* createSphere() const {}

};

#endif
