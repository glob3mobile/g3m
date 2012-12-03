//
//  AbstractMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

#include "AbstractMesh.hpp"

#include "IFloatBuffer.hpp"
#include "Color.hpp"
#include "GL.hpp"
#include "Box.hpp"

AbstractMesh::~AbstractMesh() {
  if (_owner) {
    delete _vertices;
    delete _colors;
    delete _flatColor;
  }

  delete _extent;
  delete _translationMatrix;
}

AbstractMesh::AbstractMesh(const int primitive,
                           bool owner,
                           const Vector3D& center,
                           IFloatBuffer* vertices,
                           float lineWidth,
                           Color* flatColor,
                           IFloatBuffer* colors,
                           const float colorsIntensity) :
_primitive(primitive),
_owner(owner),
_vertices(vertices),
_flatColor(flatColor),
_colors(colors),
_colorsIntensity(colorsIntensity),
_extent(NULL),
_center(center),
_translationMatrix(( center.isNan() || center.isZero() )
                   ? NULL
                   : new MutableMatrix44D(MutableMatrix44D::createTranslationMatrix(center)) ),
_lineWidth(lineWidth)
{
}

Extent* AbstractMesh::computeExtent() const {

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

Extent* AbstractMesh::getExtent() const {
  if (_extent == NULL) {
    _extent = computeExtent();
  }
  return _extent;
}

const Vector3D AbstractMesh::getVertex(int i) const {
  const int p = i * 3;
  return Vector3D(_vertices->get(p  ) + _center._x,
                  _vertices->get(p+1) + _center._y,
                  _vertices->get(p+2) + _center._z);
}

int AbstractMesh::getVertexCount() const {
  return _vertices->size() / 3;
}

bool AbstractMesh::isTransparent(const G3MRenderContext* rc) const {
  if (_flatColor == NULL) {
    return false;
  }
  return _flatColor->isTransparent();
}


void AbstractMesh::render(const G3MRenderContext *rc,
                          const GLState& parentState) const {
  GL* gl = rc->getGL();

  GLState state(parentState);
  state.enableVerticesPosition();
  if (_colors) {
    state.enableVertexColor(_colors, _colorsIntensity);
  }

  if (_flatColor) {
    state.enableFlatColor(*_flatColor, _colorsIntensity);
    if (_flatColor->isTransparent()) {
      state.enableBlend();
      gl->setBlendFuncSrcAlpha();
    }
  }
//  else {
//    state.disableFlatColor();
//  }

  gl->vertexPointer(3, 0, _vertices);

  gl->lineWidth(_lineWidth);

  if (_translationMatrix != NULL){
    gl->pushMatrix();
    gl->multMatrixf(*_translationMatrix);
  }

  gl->setState(state);
  rawRender(rc, state);

  if (_translationMatrix != NULL) {
    gl->popMatrix();
  }
  
}
