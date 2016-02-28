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
#include "Box.hpp"
#include "Polygon3D.hpp"

class Mesh;
class Sphere;

enum verticesLocation {
  COMPLETELY_OUTSIDE,
  COMPLETELY_INSIDE,
  BOTH_SIDES_OF_THE_PLANE
};



class OrientedBox: public BoundingVolume {
private:

  mutable Mesh* _mesh;
  
  // the eight vertices (N:north, S:south, W:west, E:east, T:top, B:bottom
  const Vector3D _SWB, _SEB, _NWB, _NEB, _SWT, _SET, _NWT, _NET;
  
  // the six faces
  const Polygon3D _westFace, _eastFace, _southFace, _northFace, _bottomFace, _topFace;

  Vector2F projectedExtent(const G3MRenderContext* rc) const;


public:
  
  // the axis limits
  mutable double _minX, _maxX, _minY, _maxY, _minZ, _maxZ;
  
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
  _westFace(Polygon3D(_SWB, _SWT, _NWT, _NWB)),
  _eastFace(Polygon3D(_SEB, _NEB, _NET, _SET)),
  _southFace(Polygon3D(_SEB, _SET, _SWT, _SWB)),
  _northFace(Polygon3D(_NWT, _NET, _NEB, _NWB)),
  _bottomFace(Polygon3D(_NEB, _SEB, _SWB, _NWB)),
  _topFace(Polygon3D(_NET, _NWT, _SWT, _SET)),
  _mesh(NULL)
  {
    _minX = _SWB._x;
    _maxX = _SWB._x;
    _minY = _SWB._y;
    _maxY = _SWB._y;
    _minZ = _SWB._z;
    _maxZ = _SWB._z;

    if (_SWT._x < _minX) _minX = _SWT._x;
    if (_SWT._x > _maxX) _maxX = _SWT._x;
    if (_SWT._y < _minY) _minY = _SWT._y;
    if (_SWT._y > _maxY) _maxY = _SWT._y;
    if (_SWT._z < _minZ) _minZ = _SWT._z;
    if (_SWT._z > _maxZ) _maxZ = _SWT._z;
    
    if (_SEB._x < _minX) _minX = _SEB._x;
    if (_SEB._x > _maxX) _maxX = _SEB._x;
    if (_SEB._y < _minY) _minY = _SEB._y;
    if (_SEB._y > _maxY) _maxY = _SEB._y;
    if (_SEB._z < _minZ) _minZ = _SEB._z;
    if (_SEB._z > _maxZ) _maxZ = _SEB._z;
    
    if (_SET._x < _minX) _minX = _SET._x;
    if (_SET._x > _maxX) _maxX = _SET._x;
    if (_SET._y < _minY) _minY = _SET._y;
    if (_SET._y > _maxY) _maxY = _SET._y;
    if (_SET._z < _minZ) _minZ = _SET._z;
    if (_SET._z > _maxZ) _maxZ = _SET._z;
    
    if (_NWB._x < _minX) _minX = _NWB._x;
    if (_NWB._x > _maxX) _maxX = _NWB._x;
    if (_NWB._y < _minY) _minY = _NWB._y;
    if (_NWB._y > _maxY) _maxY = _NWB._y;
    if (_NWB._z < _minZ) _minZ = _NWB._z;
    if (_NWB._z > _maxZ) _maxZ = _NWB._z;
    
    if (_NWT._x < _minX) _minX = _NWT._x;
    if (_NWT._x > _maxX) _maxX = _NWT._x;
    if (_NWT._y < _minY) _minY = _NWT._y;
    if (_NWT._y > _maxY) _maxY = _NWT._y;
    if (_NWT._z < _minZ) _minZ = _NWT._z;
    if (_NWT._z > _maxZ) _maxZ = _NWT._z;
    
    if (_NEB._x < _minX) _minX = _NEB._x;
    if (_NEB._x > _maxX) _maxX = _NEB._x;
    if (_NEB._y < _minY) _minY = _NEB._y;
    if (_NEB._y > _maxY) _maxY = _NEB._y;
    if (_NEB._z < _minZ) _minZ = _NEB._z;
    if (_NEB._z > _maxZ) _maxZ = _NEB._z;
    
    if (_NET._x < _minX) _minX = _NET._x;
    if (_NET._x > _maxX) _maxX = _NET._x;
    if (_NET._y < _minY) _minY = _NET._y;
    if (_NET._y > _maxY) _maxY = _NET._y;
    if (_NET._z < _minZ) _minZ = _NET._z;
    if (_NET._z > _maxZ) _maxZ = _NET._z;
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

  double projectedArea(const G3MRenderContext* rc) const;
  
  BoundingVolume* mergedWith(const BoundingVolume* that) const;
  Box* mergedWithOrientedBox(const OrientedBox* that) const;
  Box* mergedWithBox(const Box* that) const;
  Box* mergedWithSphere(const Sphere* that) const;


  verticesLocation getVerticesLocationRespectToPlane(const Plane* plane,
                                                     MutableVector3D& inner) const;


  
  // TODO for Agustin!
  bool touches(const BoundingVolume* that) const {}
  bool touchesBox(const Box* that) const {}
  bool touchesSphere(const Sphere* that) const {}
  bool touchesFrustum(const Frustum* frustum) const {}

};

#endif
