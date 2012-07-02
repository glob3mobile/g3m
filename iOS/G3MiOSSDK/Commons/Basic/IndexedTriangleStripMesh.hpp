//
//  IndexedTriangleStripMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IndexedTriangleStripMesh_h
#define G3MiOSSDK_IndexedTriangleStripMesh_h

#include "Mesh.hpp"
#include "Color.hpp"
#include "MutableVector2D.hpp"
#include "MutableVector3D.hpp"


class IndexedTriangleStripMesh : public Mesh {
private:
  const bool           _owner;
  
  const float*         _vertices;
  
  const unsigned char* _indexes;
  const int            _numIndex;
  
  const float*         _normals;
  
  const Color          _color;
  const float *        _colors;
  
  const float*         _texCoords;
  const int            _textureId;
  
public:
  
  ~IndexedTriangleStripMesh();
  
  //NOT TEXTURED
  IndexedTriangleStripMesh(bool owner,
                           const float* vertices,
                           const unsigned char* indexes,
                           const int numIndex, 
                           const Color& color,
                           const float* normals = NULL);
  
  IndexedTriangleStripMesh(bool owner,
                           const float* vertices,
                           const unsigned char* indexes,
                           const int numIndex,
                           const float * colors,
                           const float* normals = NULL);
  
  IndexedTriangleStripMesh(std::vector<MutableVector3D>& vertices,
                           std::vector<unsigned char>& indexes,
                           const Color& color,
                           std::vector<MutableVector3D>* normals = NULL);
  
  //TEXTURED
  IndexedTriangleStripMesh(bool owner,
                           const float* vertices,
                           const unsigned char* indexes,
                           const int numIndex, 
                           const int texID,
                           const float* texCoords,
                           const float* normals = NULL);
  
  IndexedTriangleStripMesh(bool owner,
                           const float* vertices,
                           const unsigned char* indexes,
                           const int numIndex, 
                           const float * colors,
                           const int texID,
                           const float* texCoords,
                           const float* normals = NULL);
  
  IndexedTriangleStripMesh(std::vector<MutableVector3D>& vertices,
                           std::vector<unsigned char>& indexes,
                           const int texID,
                           std::vector<MutableVector2D>& texCoords,
                           std::vector<MutableVector3D>* normals = NULL);
  
  virtual void render(const RenderContext* rc) const;
  
};

#endif
