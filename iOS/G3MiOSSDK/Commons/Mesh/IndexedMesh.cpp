//
//  IndexedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <stdlib.h>

#include "IndexedMesh.hpp"

IndexedMesh::~IndexedMesh()
{
#ifdef C_CODE
  
  if (_owner){
    delete[] _vertices;
    delete[] _indexes;
    if (_normals != NULL) delete[] _normals;
    if (_colors != NULL) delete[] _colors;
    if (_flatColor != NULL) delete _flatColor;
  }
  
#endif
}

IndexedMesh::IndexedMesh(bool owner,
                         const GLPrimitive primitive,
                         const float* vertices,
                         const unsigned int* indexes,
                         const int numIndex, 
                         const Color* flatColor,
                         const float * colors,
                         const float colorsIntensity,
                         const float* normals):
_owner(owner),
_primitive(primitive),
_vertices(vertices),
_indexes(indexes),
_numIndex(numIndex),
_flatColor(flatColor),
_colors(colors),
_colorsIntensity(colorsIntensity), 
_normals(normals)
{
}

IndexedMesh::IndexedMesh(std::vector<MutableVector3D>& vertices, 
                         const GLPrimitive primitive,
                         std::vector<unsigned int>& indexes,
                         const Color* flatColor,
                         std::vector<Color>* colors,
                         const float colorsIntensity,
                         std::vector<MutableVector3D>* normals):
_owner(true),
_primitive(primitive),
_flatColor(flatColor),
_numIndex(indexes.size()),
_colorsIntensity(colorsIntensity)
{
  float * vert = new float[3* vertices.size()];
  int p = 0;
  for (int i = 0; i < vertices.size(); i++) {
    vert[p++] = vertices[i].x();
    vert[p++] = vertices[i].y();
    vert[p++] = vertices[i].z();
  }
  _vertices = vert;
  
  unsigned int * ind = new unsigned int[indexes.size()];
  for (int i = 0; i < indexes.size(); i++) {
    ind[i] = indexes[i];
  }
  _indexes = ind;
  
  if (normals != NULL) {
    float * norm = new float[3* vertices.size()];
    p = 0;
    for (int i = 0; i < vertices.size(); i++) {
      norm[p++] = (*normals)[i].x();
      norm[p++] = (*normals)[i].y();
      norm[p++] = (*normals)[i].z();
    }
    _normals = norm;
  }
  else {
    _normals = NULL;
  }
  
  if (colors != NULL) {
    float * vertexColor = new float[4* colors->size()];
    for (int i = 0; i < colors->size(); i+=4){
      vertexColor[i] = (*colors)[i].getRed();
      vertexColor[i+1] = (*colors)[i].getGreen();
      vertexColor[i+2] = (*colors)[i].getBlue();
      vertexColor[i+3] = (*colors)[i].getAlpha();
    }
    _colors = vertexColor;
  }
  else {
    _colors = NULL; 
  }
}

void IndexedMesh::render(const RenderContext* rc) const
{
  IGL *gl = rc->getGL();
  
  gl->enableVerticesPosition();
  
  if (_colors != NULL)
    gl->enableVertexColor(_colors, _colorsIntensity);
  else
    gl->disableVertexColor();
  
  if (_flatColor != NULL)
    gl->enableVertexFlatColor(*_flatColor, _colorsIntensity);
  else
    gl->disableVertexFlatColor();
  
  if (_normals != NULL)
    gl->enableVertexNormal(_normals);
  else
    gl->disableVertexNormal();
  
  gl->vertexPointer(3, 0, _vertices);
  
  switch (_primitive) {
    case TriangleStrip:
      gl->drawTriangleStrip(_numIndex, _indexes);
      break;
    case Lines:
      gl->drawLines(_numIndex, _indexes);
    case LineLoop:
      gl->drawLineLoop(_numIndex, _indexes);
    default:
      break;
  }
  
  gl->disableVerticesPosition();
}
