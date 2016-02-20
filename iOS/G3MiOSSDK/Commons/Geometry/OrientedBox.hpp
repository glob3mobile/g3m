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

public:
  
  // the eight vertices (N:north, S:south, W:west, E:east, T:top, B:bottom
  const Vector3D _SWB, _SEB, _NWB, _NEB, _SWT, _SET, _NWT, _NET;

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
  _mesh(NULL)
  {
    printf("SEB=%f %f %f\n",_SEB._x, _SEB._y, _SEB._z);
    printf("NWB=%f %f %f\n",_NWB._x, _NWB._y, _NWB._z);
    Vector3D caca =_SWB.add(latAxis);
    printf("caca=%f %f %f\n",caca._x, caca._y, caca._z);
  }

  ~OrientedBox();

  Mesh* createMesh(const Color& color) const;
  
  void render(const G3MRenderContext* rc,
              const GLState* parentState,
              const Color& color) const;


  
  // TODO for Agustin!
  double projectedArea(const G3MRenderContext* rc) const {}
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
