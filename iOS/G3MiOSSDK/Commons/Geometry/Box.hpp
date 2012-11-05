//
//  Box.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 16/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_Box_h
#define G3MiOSSDK_Box_h

#include "Extent.hpp"
#include "Vector3D.hpp"
#include "Frustum.hpp"
#include "IndexedMesh.hpp"

#include <vector>

class Vector2D;


class Box: public Extent {
  
public:
  Box(const Vector3D& lower,
      const Vector3D& upper):
  _lower(lower),
  _upper(upper),
  _mesh(NULL)
  {}
  
  ~Box() {
    delete _mesh;
  };
  
  bool touches(const Frustum* frustum) const {
    return frustum->touchesWithBox(this);
  };
  
  Vector3D getLower() const { return _lower; }
  Vector3D getUpper() const { return _upper; }
  
  inline const std::vector<Vector3D> getCorners() const;
  
  double squaredProjectedArea(const RenderContext* rc) const;
  Vector2I projectedExtent(const RenderContext* rc) const;
  
  bool contains(const Vector3D& p) const;
  
  Vector3D intersectionWithRay(const Vector3D& origin, const Vector3D& direction) const;
  
  void render(const RenderContext* rc);
  
  bool touchesBox(const Box* box) const;

  
private:
  const Vector3D _lower;
  const Vector3D _upper;
  
#ifdef JAVA_CODE
  java.util.ArrayList<Vector3D> _corners = null; // cache for getCorners() method
#endif
  
  Mesh *_mesh;  
  void createMesh();

};

#endif
