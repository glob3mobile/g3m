//
//  IndexedMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IndexedMesh_h
#define G3MiOSSDK_IndexedMesh_h

#include "Mesh.hpp"
#include "Color.hpp"
#include "Vector3D.hpp"
#include "INativeGL.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"

class IndexedMesh : public Mesh {
private:
  const GLPrimitive _primitive;
  const bool        _owner;
  CenterStrategy    _centerStrategy;
  Vector3D          _center;
  IFloatBuffer*     _vertices;
//  const int         _numVertices;
  IIntBuffer*       _indices;
//  const int         _numIndex;
  const Color*      _flatColor;
  IFloatBuffer*     _colors;
  const float       _colorsIntensity;

  mutable Extent*   _extent;
  
  Extent* computeExtent() const;
  
public:
  IndexedMesh(const GLPrimitive primitive,
              bool owner,
              CenterStrategy centerStrategy,
              Vector3D center,
              IFloatBuffer* vertices,
              IIntBuffer* indices,
              const Color* flatColor = NULL,
              IFloatBuffer* colors = NULL,
              const float colorsIntensity = (float)0.0);

  ~IndexedMesh();

  virtual void render(const RenderContext* rc) const;
  
  Extent* getExtent() const;
  
  int getVertexCount() const;
  
  const Vector3D getVertex(int i) const;
  
};

#endif
