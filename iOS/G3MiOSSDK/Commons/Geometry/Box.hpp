//
//  Box.h
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 16/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_Box
#define G3MiOSSDK_Box

#include "BoundingVolume.hpp"
#include "Vector3D.hpp"
#include "Vector3F.hpp"
#include "Frustum.hpp"

#include "GLState.hpp"

class Vector2D;
class Mesh;
class Color;

class Box: public BoundingVolume {
private:
  const Vector3D _lower;
  const Vector3D _upper;

#ifdef JAVA_CODE
  private java.util.ArrayList<Vector3D> _cornersD = null; // cache for getCorners() method
  private java.util.ArrayList<Vector3F> _cornersF = null; // cache for getCornersF() method
#endif

  mutable Mesh *_mesh;
  void createMesh(Color* flatColor) const;

public:
  Box(const Vector3D& lower,
      const Vector3D& upper):
  _lower(lower),
  _upper(upper),
  _mesh(NULL)
  {
  }

  ~Box();
  
  bool touchesFrustum(const Frustum* frustum) const {
    return frustum->touchesWithBox(this);
  };

  Vector3D getLower() const { return _lower; }
  Vector3D getUpper() const { return _upper; }

  inline const std::vector<Vector3D> getCorners() const;
  inline const std::vector<Vector3F> getCornersF() const;


  double projectedArea(const G3MRenderContext* rc) const;
  Vector2F projectedExtent(const G3MRenderContext* rc) const;


  Vector3D intersectionWithRay(const Vector3D& origin,
                               const Vector3D& direction) const;

  void render(const G3MRenderContext* rc, const GLState& parentState) const;

  bool touches(const BoundingVolume* that) const {
    if (that == NULL) {
      return false;
    }
    return that->touchesBox(this);
  }

  bool touchesBox(const Box* that) const;
  bool touchesSphere(const Sphere* that) const;

  BoundingVolume* mergedWith(const BoundingVolume* that) const {
    if (that == NULL) {
      return NULL;
    }
    return that->mergedWithBox(this);
  }

  BoundingVolume* mergedWithBox(const Box* that) const;
  BoundingVolume* mergedWithSphere(const Sphere* that) const;

  Vector3D closestPoint(const Vector3D& point) const;

  bool contains(const Vector3D& p) const;

  bool fullContains(const BoundingVolume* that) const {
    return that->fullContainedInBox(this);
  }
  
  bool fullContainedInBox(const Box* that) const;
  bool fullContainedInSphere(const Sphere* that) const;

  Sphere* createSphere() const;

#ifdef JAVA_CODE
  private Vector3F[] _cornersArray = null;
  public Vector3F[] getCornersArray() {
    if (_cornersArray == null) {
      _cornersArray = new Vector3F[8];

      _cornersArray[0] = new Vector3F((float) _lower._x, (float) _lower._y, (float) _lower._z);
      _cornersArray[1] = new Vector3F((float) _lower._x, (float) _lower._y, (float) _upper._z);
      _cornersArray[2] = new Vector3F((float) _lower._x, (float) _upper._y, (float) _lower._z);
      _cornersArray[3] = new Vector3F((float) _lower._x, (float) _upper._y, (float) _upper._z);
      _cornersArray[4] = new Vector3F((float) _upper._x, (float) _lower._y, (float) _lower._z);
      _cornersArray[5] = new Vector3F((float) _upper._x, (float) _lower._y, (float) _upper._z);
      _cornersArray[6] = new Vector3F((float) _upper._x, (float) _upper._y, (float) _lower._z);
      _cornersArray[7] = new Vector3F((float) _upper._x, (float) _upper._y, (float) _upper._z);
    }
    return _cornersArray;
  }
#endif

};

#endif
