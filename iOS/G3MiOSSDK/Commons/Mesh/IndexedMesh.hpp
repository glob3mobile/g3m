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
#ifdef C_CODE
  const GLPrimitive _primitive;
#endif
#ifdef JAVA_CODE
  private final GLPrimitive _primitive;
#endif
  const bool        _owner;
  Vector3D          _center;
  const MutableMatrix44D* _translationMatrix;
  IFloatBuffer*     _vertices;
  IIntBuffer*       _indices;
  const Color*      _flatColor;
  IFloatBuffer*     _colors;
  const float       _colorsIntensity;
  
  mutable Extent*   _extent;
  
  Extent* computeExtent() const;
  
  
public:
  IndexedMesh(const GLPrimitive primitive,
              bool owner,
              const Vector3D& center,
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
