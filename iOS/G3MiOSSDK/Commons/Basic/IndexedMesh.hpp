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

enum GLPrimitive{ TriangleStrip, Lines, LineLoop };


class IndexedMesh : public Mesh {
private:
  const bool           _owner;
  
  const GLPrimitive    _primitive; 
  
  const float*         _vertices;
  
  const unsigned int*  _indexes;
  const int            _numIndex;
  
  const float*         _normals;
  
  const Color *        _flatColor;
  const float *        _colors;
  
  const float*         _texCoords;
  const int            _textureId;
  
public:
  
  ~IndexedMesh();
  
  //NOT TEXTURED
  IndexedMesh(bool owner,
              const GLPrimitive primitive,
                           const float* vertices,
                           const unsigned int* indexes,
                           const int numIndex, 
                           const Color* flatColor,
                           const float * colors,
                           const float* normals = NULL);
  
  IndexedMesh(std::vector<MutableVector3D>& vertices,
              const GLPrimitive primitive,
                           std::vector<unsigned int>& indexes,
                           const Color* flatColor,
                           std::vector<Color>* colors = NULL,
                           std::vector<MutableVector3D>* normals = NULL);
  
  //TEXTURED
  IndexedMesh(bool owner,
              const GLPrimitive primitive,
                           const float* vertices,
                           const unsigned int* indexes,
                           const int numIndex,
                           const Color* flatColor,
                           const float * colors,
                           const int texID,
                           const float* texCoords,
                           const float* normals = NULL);
  
  IndexedMesh(std::vector<MutableVector3D>& vertices,
              const GLPrimitive primitive,
                           std::vector<unsigned int>& indexes,
                           const int texID,
                           std::vector<MutableVector2D>& texCoords,
                           const Color* flatColor = NULL,
                           std::vector<Color>* colors = NULL,
                           std::vector<MutableVector3D>* normals = NULL);
  
  virtual void render(const RenderContext* rc) const;
  
};

#endif
