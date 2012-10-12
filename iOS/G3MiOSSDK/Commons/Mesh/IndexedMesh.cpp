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
#include "IFloatBuffer.hpp"
#include "IIntBuffer.hpp"

IndexedMesh::~IndexedMesh()
{
//#ifdef C_CODE
  if (_owner){
    delete _vertices;
    delete _indices;
    if (_colors != NULL) delete _colors;
#ifdef C_CODE
    if (_flatColor != NULL) delete _flatColor;
#endif
  }
  
  if (_extent != NULL) delete _extent;
  if (_translationMatrix != NULL) delete _translationMatrix;
//#endif
}

IndexedMesh::IndexedMesh(const int primitive,
                         bool owner,
                         const Vector3D& center,
                         IFloatBuffer* vertices,
                         IIntBuffer* indices,
                         const Color* flatColor,
                         IFloatBuffer* colors,
                         const float colorsIntensity) :
_primitive(primitive),
_owner(owner),
_vertices(vertices),
_indices(indices),
_flatColor(flatColor),
_colors(colors),
_colorsIntensity(colorsIntensity),
_extent(NULL),
_center(center),
_translationMatrix(center.isNan()? NULL:
                   new MutableMatrix44D(MutableMatrix44D::createTranslationMatrix(center)))
{
}

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
  
  if (_translationMatrix != NULL){
    gl->pushMatrix();
    gl->multMatrixf(*_translationMatrix);
  }
  
  if (_primitive == GLPrimitive::triangleStrip()) {
    gl->drawTriangleStrip(_indices);
  }
  else if (_primitive == GLPrimitive::lines()) {
    gl->drawLines(_indices);
  }
  else if (_primitive == GLPrimitive::lineLoop()) {
    gl->drawLineLoop(_indices);
  }
  else if (_primitive == GLPrimitive::points()) {
    gl->drawPoints(_indices);
  }
  
  if (_translationMatrix != NULL) {
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
    
    const double x = _vertices->get(p  ) + _center._x;
    const double y = _vertices->get(p+1) + _center._y;
    const double z = _vertices->get(p+2) + _center._z;
    
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
  return Vector3D(_vertices->get(p  ) + _center._x,
                  _vertices->get(p+1) + _center._y,
                  _vertices->get(p+2) + _center._z);
}

int IndexedMesh::getVertexCount() const {
  return _vertices->size() / 3;
}
