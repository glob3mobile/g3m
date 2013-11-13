//
//  OrientedBox.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 22/10/13.
//
//

#ifndef __G3MiOSSDK__OrientedBox__
#define __G3MiOSSDK__OrientedBox__

#include "BoundingVolume.hpp"
#include "Frustum.hpp"
#include <vector>

class Vector3D;
class MutableMatrix44D;
class Mesh;
class Color;


class OrientedBox: public BoundingVolume {
  
private:
  const double _lowerX, _lowerY, _lowerZ;
  const double _upperX, _upperY, _upperZ;
  const MutableMatrix44D* _transformMatrix;
  
#ifdef JAVA_CODE
  private java.util.ArrayList<Vector3D> _cornersD = null; // cache for getCorners() method
#endif
  
  mutable Mesh *_mesh;
  void createMesh(Color* flatColor) const;
  
public:
  OrientedBox(const Vector3D& lower,
              const Vector3D& upper,
              const MutableMatrix44D& transformMatrix):
  _lowerX(lower._x),
  _lowerY(lower._y),
  _lowerZ(lower._z),
  _upperX(upper._x),
  _upperY(upper._y),
  _upperZ(upper._z),
  _transformMatrix(new MutableMatrix44D(transformMatrix)),
  _mesh(NULL)
  {}
  
  ~OrientedBox();

  
  double projectedArea(const G3MRenderContext* rc) const {
    int __TODO_OrientedBox_projectedArea;
    return 0;
  }
  
  void render(const G3MRenderContext* rc,
              const GLState& parentState) const;
  
  bool touches(const BoundingVolume* that) const  {
    int __TODO_OrientedBox_touches;
    return true;
  }

  bool touchesBox(const Box* that) const  {
    int __TODO_OrientedBox_touchesBox;
    return true;
  }

  bool touchesSphere(const Sphere* that) const  {
    int __TODO_OrientedBox_touchesSphere;
    return true;
  }

  
  bool touchesFrustum(const Frustum* frustum) const {
    return frustum->touchesWithOrientedBox(this);
  };
  
  bool contains(const Vector3D& point) const  {
    int __TODO_OrientedBox_contains;
    return true;
  }

  bool fullContains(const BoundingVolume* that) const  {
    int __TODO_OrientedBox_fullContains;
    return true;
  }

  bool fullContainedInBox(const Box* that) const  {
    int __TODO_OrientedBox_fullContainedInBox;
    return true;
  }

  bool fullContainedInSphere(const Sphere* that) const  {
    int __TODO_OrientedBox_fullContainedInSphere;
    return true;
  }

  
  BoundingVolume* mergedWith(const BoundingVolume* that) const {
    int __TODO_OrientedBox_mergedWith;
    return (BoundingVolume*) NULL;
  }
  

  BoundingVolume* mergedWithBox(const Box* that) const {
    int __TODO_OrientedBox_mergedWithBox;
    return (BoundingVolume*) NULL;
  }
  

  BoundingVolume* mergedWithSphere(const Sphere* that) const {
    int __TODO_OrientedBox_mergedWithSphere;
    return (BoundingVolume*) NULL;
  }
  

  
  Sphere* createSphere() const {
    int __TODO_OrientedBox_createSphere;
    return (Sphere*) NULL;
  }
  

  
  const std::vector<Vector3D> getCorners() const;
  std::vector<double> intersectionsDistances(const Vector3D& origin,
                                             const Vector3D& direction) const;
};


#endif /* defined(__G3MiOSSDK__OrientedBox__) */
