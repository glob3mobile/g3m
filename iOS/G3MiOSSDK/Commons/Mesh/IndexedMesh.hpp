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
#include "MutableVector2D.hpp"
#include "MutableVector3D.hpp"
#include "Geodetic3D.hpp"
#include "Planet.hpp"

#include <vector>

#include "INativeGL.hpp"
//enum GLPrimitive {
//  TriangleStrip,
//  Lines,
//  LineLoop
//};

enum CenterStrategy {
  NoCenter,
  AveragedVertex,
  FirstVertex,
  GivenCenter
};


class IndexedMesh : public Mesh {
private:
  IndexedMesh(std::vector<MutableVector3D>& vertices,
              const GLPrimitive primitive,
              CenterStrategy strategy,
              Vector3D center,
              std::vector<int>& indexes,
              const Color* flatColor = NULL,
              std::vector<Color>* colors = NULL,
              const float colorsIntensity = 0.0,
              std::vector<MutableVector3D>* normals = NULL);
  
  IndexedMesh(bool owner,
              const GLPrimitive primitive,
              CenterStrategy strategy,
              Vector3D center,
              const int numVertices,
              const float* vertices,
              const int* indexes,
              const int numIndex, 
              const Color* flatColor = NULL,
              const float * colors = NULL,
              const float colorsIntensity = 0.0,
              const float* normals = NULL);


  const bool           _owner;
  
  const GLPrimitive    _primitive; 
  
  const float*         _vertices;
  const int            _numVertices;
  
  const int*           _indexes;
  const int            _numIndex;
  
  const float*         _normals;
  
  const Color*         _flatColor;
  const float*         _colors;
  const float          _colorsIntensity;
  
  mutable Extent*      _extent;
  
  Extent* computeExtent() const;
  
  CenterStrategy       _centerStrategy;
  Vector3D             _center;
  
public:
  
  ~IndexedMesh();

    
  static IndexedMesh* createFromVector3D(bool owner,
                                         const GLPrimitive primitive,
                                         CenterStrategy strategy,
                                         Vector3D center,
                                         const int numVertices,
                                         const float* vertices,
                                         const int* indexes,
                                         const int numIndex, 
                                         const Color* flatColor = NULL,
                                         const float * colors = NULL,
                                         const float colorsIntensity = 0.0,
                                         const float* normals = NULL) {
    return new IndexedMesh(owner, primitive, strategy, center, numVertices, vertices,
                           indexes, numIndex, flatColor, colors, colorsIntensity, normals);
  }

    
  static IndexedMesh* createFromVector3D(std::vector<MutableVector3D>& vertices,
                                         const GLPrimitive primitive,
                                         CenterStrategy strategy,
                                         Vector3D center,
                                         std::vector<int>& indexes,
                                         const Color* flatColor = NULL,
                                         std::vector<Color>* colors = NULL,
                                         const float colorsIntensity = 0.0,
                                         std::vector<MutableVector3D>* normals = NULL) {
    return new IndexedMesh(vertices, primitive, strategy, center, indexes,
                           flatColor, colors, colorsIntensity, normals);
  }

  
  static IndexedMesh* createFromGeodetic3D(const Planet *planet,
                                           bool owner,
                                           const GLPrimitive primitive,
                                           CenterStrategy strategy,
                                           Vector3D center,
                                           const int numVertices,
                                           float* vertices,
                                           const int* indexes,
                                           const int numIndex, 
                                           const Color* flatColor = NULL,
                                           const float * colors = NULL,
                                           const float colorsIntensity = 0.0,
                                           const float* normals = NULL) {
    // convert vertices to latlon coordinates
    for (unsigned int n=0; n<numVertices*3; n+=3) {
      Geodetic3D g(Angle::fromDegrees(vertices[n]), Angle::fromDegrees(vertices[n+1]), vertices[n+2]);
      Vector3D v = planet->toVector3D(g);
      vertices[n]   = (float) v.x();
      vertices[n+1] = (float) v.y();
      vertices[n+2] = (float) v.z();
    }
    
    // create indexed mesh
    return new IndexedMesh(owner, primitive, strategy, center, numVertices, vertices,
                           indexes, numIndex, flatColor, colors, colorsIntensity, normals);
  }

    
  virtual void render(const RenderContext* rc) const;
  
  Extent* getExtent() const;
  
  int getVertexCount() const {
    return _numVertices;
  }
  
  const Vector3D getVertex(int i) const {
    const int p = i * 3;
    return Vector3D(_vertices[p  ] + _center.x(),
                    _vertices[p+1] + _center.y(),
                    _vertices[p+2] + _center.z());
  }

  
};

#endif
