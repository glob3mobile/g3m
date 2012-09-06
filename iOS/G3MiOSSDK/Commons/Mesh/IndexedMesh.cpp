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
#include "IFloatBuffer.hpp"
#include "IIntBuffer.hpp"

//IndexedMesh* IndexedMesh::createFromGeodetic3D(const Planet *planet,
//                                               bool owner,
//                                               const GLPrimitive primitive,
//                                               CenterStrategy strategy,
//                                               Vector3D center,
//                                               const int numVertices,
//                                               IFloatBuffer* vertices,
//                                               IIntBuffer* indices,
//                                               const int numIndex,
//                                               const Color* flatColor,
//                                               IFloatBuffer* colors,
//                                               const float colorsIntensity) {
//  // convert vertices to latlon coordinates
//  for (unsigned int n=0; n<numVertices*3; n+=3) {
//    const Geodetic3D g(Angle::fromDegrees(vertices->get(n)),
//                       Angle::fromDegrees(vertices->get(n+1)),
//                       vertices->get(n+2));
//    const Vector3D v = planet->toCartesian(g);
//    vertices[n]   = (float) v.x();
//    vertices[n+1] = (float) v.y();
//    vertices[n+2] = (float) v.z();
//  }
//
//  // create indexed mesh
//  return new IndexedMesh(owner, primitive, strategy, center, numVertices, vertices,
//                         indices, numIndex, flatColor, colors, colorsIntensity);
//}

IndexedMesh::~IndexedMesh()
{
#ifdef C_CODE
  if (_owner){
    delete _vertices;
    delete _indices;
    if (_colors != NULL) delete _colors;
    if (_flatColor != NULL) delete _flatColor;
  }
  
  if (_extent != NULL) delete _extent;
  
#endif
}

IndexedMesh::IndexedMesh(const GLPrimitive primitive,
                         bool owner,
                         CenterStrategy centerStrategy,
                         Vector3D center,
                         IFloatBuffer* vertices,
                         IIntBuffer* indices,
                         const Color* flatColor,
                         IFloatBuffer* colors,
                         const float colorsIntensity) :
_primitive(primitive),
_owner(owner),
_centerStrategy(centerStrategy),
_center(center),
_vertices(vertices),
_indices(indices),
_flatColor(flatColor),
_colors(colors),
_colorsIntensity(colorsIntensity),
_extent(NULL)
{
  if (centerStrategy != NoCenter) {
    printf ("IndexedMesh array constructor: this center Strategy is not yet implemented\n");
  }
}

//IndexedMesh::IndexedMesh(std::vector<MutableVector3D>& vertices,
//                         const GLPrimitive primitive,
//                         CenterStrategy strategy,
//                         Vector3D center,
//                         std::vector<int>& indices,
//                         const Color* flatColor,
//                         std::vector<Color>* colors,
//                         const float colorsIntensity,
//                         std::vector<MutableVector3D>* normals):
//_owner(true),
//_primitive(primitive),
//_numVertices(vertices.size()),
//_flatColor(flatColor),
//_numIndex(indices.size()),
//_colorsIntensity(colorsIntensity),
//_extent(NULL),
//_centerStrategy(strategy),
//_center(center)
//{
//  float* vert = new float[3 * vertices.size()];
//  int p = 0;
//
//  switch (strategy) {
//    case NoCenter:
//      for (int i = 0; i < vertices.size(); i++) {
//        vert[p++] = (float) vertices[i].x();
//        vert[p++] = (float) vertices[i].y();
//        vert[p++] = (float) vertices[i].z();
//      }
//      break;
//
//    case GivenCenter:
//      for (int i = 0; i < vertices.size(); i++) {
//        vert[p++] = (float) (vertices[i].x() - center.x());
//        vert[p++] = (float) (vertices[i].y() - center.y());
//        vert[p++] = (float) (vertices[i].z() - center.z());
//      }
//      break;
//
//    default:
//      printf ("IndexedMesh vector constructor: this center Strategy is not yet implemented\n");
//  }
//
//  _vertices = vert;
//
//  int* ind = new int[indices.size()];
//  for (int i = 0; i < indices.size(); i++) {
//    ind[i] = indices[i];
//  }
//  _indices = ind;
//
//  if (normals != NULL) {
//    float* norm = new float[3 * vertices.size()];
//    p = 0;
//    for (int i = 0; i < vertices.size(); i++) {
//      norm[p++] = (float) normals->at(i).x();
//      norm[p++] = (float) normals->at(i).y();
//      norm[p++] = (float) normals->at(i).z();
//    }
//    _normals = norm;
//  }
//  else {
//    _normals = NULL;
//  }
//
//  if (colors != NULL) {
//    float* vertexColor = new float[4 * colors->size()];
//    for (int i = 0; i < colors->size(); i+=4){
//      vertexColor[i] = colors->at(i).getRed();
//      vertexColor[i+1] = colors->at(i).getGreen();
//      vertexColor[i+2] = colors->at(i).getBlue();
//      vertexColor[i+3] = colors->at(i).getAlpha();
//    }
//    _colors = vertexColor;
//  }
//  else {
//    _colors = NULL;
//  }
//}

void IndexedMesh::render(const RenderContext* rc) const {
  GL *gl = rc->getGL();
  
  gl->enableVerticesPosition();
  
  if (_colors == NULL) {
    gl->disableVertexColor();
  }
  else {
    gl->enableVertexColor(_colors, _colorsIntensity);
  }
  
  if (_flatColor == NULL) {
    gl->disableVertexFlatColor();
  }
  else {
    gl->enableVertexFlatColor(*_flatColor, _colorsIntensity);
  }
  
  gl->vertexPointer(3, 0, _vertices);
  
  if (_centerStrategy != NoCenter) {
    gl->pushMatrix();
    gl->multMatrixf(MutableMatrix44D::createTranslationMatrix(_center));
  }
  
  switch (_primitive) {
    case TriangleStrip:
      gl->drawTriangleStrip(_indices);
      break;
    case Lines:
      gl->drawLines(_indices);
      break;
    case LineLoop:
      gl->drawLineLoop(_indices);
      break;
    case Points:
      gl->drawPoints(_indices);
      break;
    default:
      break;
  }
  
  if (_centerStrategy != NoCenter) {
    gl->popMatrix();
  }
  
  gl->disableVerticesPosition();
}


Extent* IndexedMesh::computeExtent() const {
  
  const int vertexCount = getVertexCount();
  
  if (vertexCount <= 0) {
    return NULL;
  }
  
  double minx=1e10, miny=1e10, minz=1e10;
  double maxx=-1e10, maxy=-1e10, maxz=-1e10;
  
  for (int i=0; i < vertexCount; i++) {
    const int p = i * 3;
    
    const double x = _vertices->get(p  ) + _center.x();
    const double y = _vertices->get(p+1) + _center.y();
    const double z = _vertices->get(p+2) + _center.z();
    
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

const Vector3D IndexedMesh::getVertex(int i) const {
  const int p = i * 3;
  return Vector3D(_vertices->get(p  ) + _center.x(),
                  _vertices->get(p+1) + _center.y(),
                  _vertices->get(p+2) + _center.z());
}

int IndexedMesh::getVertexCount() const {
  return _vertices->size() / 3;
}
