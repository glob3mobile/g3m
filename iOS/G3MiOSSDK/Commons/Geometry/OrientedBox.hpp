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


class OrientedBox: public BoundingVolume {
  
private:
  const double _halfExtentX, _halfExtentY, _halfExtentZ;
  const MutableMatrix44D* _transformMatrix;
  
public:
  OrientedBox(const Vector3D& extent, const MutableMatrix44D& transformMatrix):
  _halfExtentX(extent._x/2),
  _halfExtentY(extent._y/2),
  _halfExtentZ(extent._z/2),
  _transformMatrix(new MutableMatrix44D(transformMatrix))
  {}
  
  ~OrientedBox() {
    delete _transformMatrix;
  }
  
  double projectedArea(const G3MRenderContext* rc) const{}
  void render(const G3MRenderContext* rc,
              const GLState& parentState) const{}
  
  bool touches(const BoundingVolume* that) const{}
  bool touchesBox(const Box* that) const{}
  bool touchesSphere(const Sphere* that) const{}
  
  bool touchesFrustum(const Frustum* frustum) const {
    return frustum->touchesWithOrientedBox(this);
  };
  
  bool contains(const Vector3D& point) const{}
  
  bool fullContains(const BoundingVolume* that) const{}
  bool fullContainedInBox(const Box* that) const{}
  bool fullContainedInSphere(const Sphere* that) const{}
  
  BoundingVolume* mergedWith(const BoundingVolume* that) const{}
  BoundingVolume* mergedWithBox(const Box* that) const{}
  BoundingVolume* mergedWithSphere(const Sphere* that) const{}
  
  Sphere* createSphere() const{}
  
  const std::vector<Vector3D> getCorners() const;
  std::vector<double> intersectionsDistances(const Vector3D& origin,
                                             const Vector3D& direction) const;


};


#endif /* defined(__G3MiOSSDK__OrientedBox__) */
