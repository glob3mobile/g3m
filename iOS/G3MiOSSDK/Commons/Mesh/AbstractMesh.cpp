//
//  AbstractMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

#include "AbstractMesh.hpp"

#include "GLFeature.hpp"
#include "GLState.hpp"
#include "Box.hpp"


AbstractMesh::~AbstractMesh() {
  if (_owner) {
    delete _vertices;
    delete _colors;
    delete _normals;
  }

  delete _flatColor;

  delete _boundingVolume;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

AbstractMesh::AbstractMesh(const int primitive,
                           bool owner,
                           const Vector3D& center,
                           const IFloatBuffer* vertices,
                           float lineWidth,
                           float pointSize,
                           const Color* flatColor,
                           const IFloatBuffer* colors,
                           bool depthTest,
                           const IFloatBuffer* normals,
                           bool polygonOffsetFill,
                           float polygonOffsetFactor,
                           float polygonOffsetUnits) :
TransformableMesh(center),
_primitive(primitive),
_owner(owner),
_vertices(vertices),
_flatColor(flatColor),
_colors(colors),
_boundingVolume(NULL),
_lineWidth(lineWidth),
_pointSize(pointSize),
_depthTest(depthTest),
_normals(normals),
_polygonOffsetFactor(polygonOffsetFactor),
_polygonOffsetUnits(polygonOffsetUnits),
_polygonOffsetFill(polygonOffsetFill)
{
}

void AbstractMesh::userTransformMatrixChanged() {
  delete _boundingVolume;
  _boundingVolume = NULL;
}

BoundingVolume* AbstractMesh::computeBoundingVolume() const {
  if (hasUserTransform()) {
    return NULL;
  }

  const size_t vertexCount = getVertexCount();

  if (vertexCount == 0) {
    return NULL;
  }

  const IMathUtils* mu = IMathUtils::instance();

  double minX = mu->maxDouble();
  double minY = mu->maxDouble();
  double minZ = mu->maxDouble();

  double maxX = mu->minDouble();
  double maxY = mu->minDouble();
  double maxZ = mu->minDouble();

  for (int i=0; i < vertexCount; i++) {
    const int i3 = i * 3;

    const double x = _vertices->get(i3    ) + _center._x;
    const double y = _vertices->get(i3 + 1) + _center._y;
    const double z = _vertices->get(i3 + 2) + _center._z;

    if (x < minX) { minX = x; }
    if (y < minY) { minY = y; }
    if (z < minZ) { minZ = z; }

    if (x > maxX) { maxX = x; }
    if (y > maxY) { maxY = y; }
    if (z > maxZ) { maxZ = z; }
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

const Vector3D AbstractMesh::getVertex(const size_t index) const {
  const size_t p = index * 3;
  return Vector3D(_vertices->get(p  ) + _center._x,
                  _vertices->get(p+1) + _center._y,
                  _vertices->get(p+2) + _center._z);
}

size_t AbstractMesh::getVertexCount() const {
  return _vertices->size() / 3;
}

bool AbstractMesh::isTransparent(const G3MRenderContext* rc) const {
  if (_flatColor == NULL) {
    return false;
  }
  return _flatColor->isTransparent();
}

void AbstractMesh::rawRender(const G3MRenderContext* rc,
                             const GLState* parentGLState) const {
  GLState* glState = getGLState();
  glState->setParent(parentGLState);
  renderMesh(rc, glState);
}

void AbstractMesh::initializeGLState(GLState* glState) const {
  TransformableMesh::initializeGLState(glState);

  glState->addGLFeature(new GeometryGLFeature(_vertices,            // The attribute is a float vector of 4 elements
                                              3,                    // Our buffer contains elements of 3
                                              0,                    // Index 0
                                              false,                // Not normalized
                                              0,                    // Stride 0
                                              _depthTest,
                                              false,                // cullFace
                                              0,                    // culledFace
                                              _polygonOffsetFill,
                                              _polygonOffsetFactor,
                                              _polygonOffsetUnits,
                                              _lineWidth,
                                              true,                 // needsPointSize
                                              _pointSize),
                        false);

  if (_normals != NULL) {
    glState->addGLFeature(new VertexNormalGLFeature(_normals, 3, 0, false, 0),
                          false);
  }

  if ((_flatColor != NULL) && (_colors == NULL)) {
    glState->addGLFeature(new FlatColorGLFeature(*_flatColor,
                                                 _flatColor->isTransparent(),
                                                 GLBlendFactor::srcAlpha(),
                                                 GLBlendFactor::oneMinusSrcAlpha()),
                          false);

    return;
  }

  if (_colors != NULL) {
    glState->addGLFeature(new ColorGLFeature(_colors,      // The attribute is a float vector of 4 elements RGBA
                                             4,            // Our buffer contains elements of 4
                                             0,            // Index 0
                                             false,        // Not normalized
                                             0,            // Stride 0
                                             true,
                                             GLBlendFactor::srcAlpha(),
                                             GLBlendFactor::oneMinusSrcAlpha()),
                          false);
  }
  
}
