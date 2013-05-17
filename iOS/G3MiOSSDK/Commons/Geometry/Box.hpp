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
#include "Vector3F.hpp"
#include "Frustum.hpp"

class Vector2D;
class Mesh;
class Color;

class Box: public Extent {
private:
  const Vector3D _lower;
  const Vector3D _upper;

#ifdef JAVA_CODE
  private java.util.ArrayList<Vector3D> _cornersD = null; // cache for getCorners() method
  private java.util.ArrayList<Vector3F> _cornersF = null; // cache for getCornersF() method
#endif

  Mesh *_mesh;
  void createMesh(Color* flatColor);

public:
  Box(const Vector3D& lower,
      const Vector3D& upper):
  _lower(lower),
  _upper(upper),
  _mesh(NULL)
  {
  }

  ~Box();
  
  bool touches(const Frustum* frustum) const {
    return frustum->touchesWithBox(this);
  };

  Vector3D getLower() const { return _lower; }
  Vector3D getUpper() const { return _upper; }

  inline const std::vector<Vector3D> getCorners() const;
  inline const std::vector<Vector3F> getCornersF() const;

  double squaredProjectedArea(const G3MRenderContext* rc) const;
  Vector2I projectedExtent(const G3MRenderContext* rc) const;

  bool contains(const Vector3D& p) const;

  Vector3D intersectionWithRay(const Vector3D& origin, const Vector3D& direction) const;

  void render(const G3MRenderContext* rc,
              const GLGlobalState& parentState,
              const GPUProgramState* parentProgramState);

  bool touchesBox(const Box* box) const;

  Extent* mergedWith(const Extent* that) const {
    if (that == NULL) {
      return NULL;
    }
    return that->mergedWithBox(this);
  }

  Extent* mergedWithBox(const Box* that) const;
  
  bool fullContains(const Extent* that) const;

  bool fullContainedInBox(const Box* box) const;

  
};

#endif
