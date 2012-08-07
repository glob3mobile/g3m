//
//  IndexedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <stdlib.h>

#include "IndexedMesh.hpp"
#include "Box.hpp"
#include "GL.hpp"

#include "INativeGL.hpp"


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
  
  if (_extent != NULL) delete _extent;
  
#endif
}

IndexedMesh::IndexedMesh(bool owner,
                         const GLPrimitive primitive,
                         CenterStrategy strategy,
                         Vector3D center,
                         const int numVertices,
                         const float vertices[],
                         const int indexes[],
                         const int numIndex, 
                         const Color* flatColor,
                         const float colors[],
                         const float colorsIntensity,
                         const float normals[]):
_owner(owner),
_primitive(primitive),
_numVertices(numVertices),
_vertices(vertices),
_indexes(indexes),
_numIndex(numIndex),
_flatColor(flatColor),
_colors(colors),
_colorsIntensity(colorsIntensity), 
_normals(normals),
_extent(NULL),
_centerStrategy(strategy),
_center(center)
{
  if (strategy!=NoCenter) 
    printf ("IndexedMesh array constructor: this center Strategy is not yet implemented\n");
}


IndexedMesh::IndexedMesh(std::vector<MutableVector3D>& vertices, 
                         const GLPrimitive primitive,
                         CenterStrategy strategy,
                         Vector3D center,                         
                         std::vector<int>& indexes,
                         const Color* flatColor,
                         std::vector<Color>* colors,
                         const float colorsIntensity,
                         std::vector<MutableVector3D>* normals):
_owner(true),
_primitive(primitive),
_numVertices(vertices.size()),
_flatColor(flatColor),
_numIndex(indexes.size()),
_colorsIntensity(colorsIntensity),
_extent(NULL),
_centerStrategy(strategy),
_center(center)
{
  float * vert = new float[3* vertices.size()];
  int p = 0;
  
  switch (strategy) {
    case NoCenter:
      for (int i = 0; i < vertices.size(); i++) {
        vert[p++] = (float) vertices[i].x();
        vert[p++] = (float) vertices[i].y();
        vert[p++] = (float) vertices[i].z();
      }      
      break;
      
    case GivenCenter:
      for (int i = 0; i < vertices.size(); i++) {
        vert[p++] = (float) (vertices[i].x() - center.x());
        vert[p++] = (float) (vertices[i].y() - center.y());
        vert[p++] = (float) (vertices[i].z() - center.z());
      }      
      break;
      
    default:
      printf ("IndexedMesh vector constructor: this center Strategy is not yet implemented\n");
  }
  
  _vertices = vert;
  
  int * ind = new int[indexes.size()];
  for (int i = 0; i < indexes.size(); i++) {
    ind[i] = indexes[i];
  }
  _indexes = ind;
  
  if (normals != NULL) {
    float * norm = new float[3* vertices.size()];
    p = 0;
    for (int i = 0; i < vertices.size(); i++) {
      norm[p++] = (float) (*normals)[i].x();
      norm[p++] = (float) (*normals)[i].y();
      norm[p++] = (float) (*normals)[i].z();
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

void IndexedMesh::render(const RenderContext* rc) const {
  GL *gl = rc->getGL();
  
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
  
  if (_centerStrategy!=NoCenter) {
    gl->pushMatrix();
    gl->multMatrixf(MutableMatrix44D::createTranslationMatrix(_center));
  }
  
  switch (_primitive) {
    case TriangleStrip:
      gl->drawTriangleStrip(_numIndex, _indexes);
      break;
    case Lines:
      gl->drawLines(_numIndex, _indexes);
      break;
    case LineLoop:
      gl->drawLineLoop(_numIndex, _indexes);
      break;
    default:
      break;
  }
  
  if (_centerStrategy!=NoCenter) {
    gl->popMatrix();
  }
  
  gl->disableVerticesPosition();
}


Extent* IndexedMesh::computeExtent() const {
  double minx=1e10, miny=1e10, minz=1e10;
  double maxx=-1e10, maxy=-1e10, maxz=-1e10;
  
  for (int i=0; i < _numVertices; i++) {
    const int p = i * 3;
    
    const double x = _vertices[p  ] + _center.x();
    const double y = _vertices[p+1] + _center.y();
    const double z = _vertices[p+2] + _center.z();
    
    if (x < minx) minx = x;
    if (x > maxx) maxx = x;

    if (y < miny) miny = y;
    if (y > maxy) maxy = y;
    
    if (z < minz) minz = z;
    if (z > maxz) maxz = z;
  }
  
  return new Box(Vector3D(minx, miny, minz), Vector3D(maxx, maxy, maxz));
}


Extent* IndexedMesh::getExtent() const {
  if (_extent == NULL) {
    _extent = computeExtent(); 
  }
  return _extent;
}
