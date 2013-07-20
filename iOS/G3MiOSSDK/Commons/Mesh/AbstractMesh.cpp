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

  delete _boundingVolume;
  delete _translationMatrix;
}

AbstractMesh::AbstractMesh(const int primitive,
                           bool owner,
                           const Vector3D& center,
                           IFloatBuffer* vertices,
                           float lineWidth,
                           float pointSize,
                           Color* flatColor,
                           IFloatBuffer* colors,
                           const float colorsIntensity,
                           bool depthTest) :
_primitive(primitive),
_owner(owner),
_vertices(vertices),
_flatColor(flatColor),
_colors(colors),
_colorsIntensity(colorsIntensity),
_boundingVolume(NULL),
_center(center),
_translationMatrix(( center.isNan() || center.isZero() )
                   ? NULL
                   : new MutableMatrix44D(MutableMatrix44D::createTranslationMatrix(center)) ),
_lineWidth(lineWidth),
_pointSize(pointSize),
_depthTest(depthTest)
{

}

BoundingVolume* AbstractMesh::computeBoundingVolume() const {
  const int vertexCount = getVertexCount();

  if (vertexCount <= 0) {
    return NULL;
  }

  double minX = 1e12;
  double minY = 1e12;
  double minZ = 1e12;

  double maxX = -1e12;
  double maxY = -1e12;
  double maxZ = -1e12;

  for (int i=0; i < vertexCount; i++) {
    const int i3 = i * 3;

    const double x = _vertices->get(i3    ) + _center._x;
    const double y = _vertices->get(i3 + 1) + _center._y;
    const double z = _vertices->get(i3 + 2) + _center._z;

    if (x < minX) minX = x;
    if (x > maxX) maxX = x;

    if (y < minY) minY = y;
    if (y > maxY) maxY = y;

    if (z < minZ) minZ = z;
    if (z > maxZ) maxZ = z;
  }

  return new Box(Vector3D(minX, minY, minZ),
                 Vector3D(maxX, maxY, maxZ));
}

BoundingVolume* AbstractMesh::getBoundingVolume() const {
  if (_boundingVolume == NULL) {
    _boundingVolume = computeBoundingVolume();
  }
  return _boundingVolume;
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
  state.setLineWidth(_lineWidth);
  state.setPointSize(_pointSize);
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

  if (!_depthTest) {
    state.disableDepthTest();
  }

  gl->vertexPointer(3, 0, _vertices);

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
