//
//  AbstractGeometryMesh.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//

#include "AbstractGeometryMesh.hpp"

#include "Box.hpp"


AbstractGeometryMesh::~AbstractGeometryMesh() {
  if (_ownsVertices) {
    delete _vertices;
  }
  if (_ownsNormals) {
    delete _normals;
  }

  delete _extent;
  delete _translationMatrix;

  _glState->_release();

#ifdef JAVA_CODE
  super.dispose();
#endif
}

AbstractGeometryMesh::AbstractGeometryMesh(const int       primitive,
                                           const Vector3D& center,
                                           IFloatBuffer*   vertices,
                                           bool            ownsVertices,
                                           IFloatBuffer*   normals,
                                           bool            ownsNormals,
                                           float           lineWidth,
                                           float           pointSize,
                                           bool            depthTest,
                                           bool            polygonOffsetFill,
                                           float           polygonOffsetFactor,
                                           float           polygonOffsetUnits,
                                           bool            cullFace,
                                           int             culledFace) :
_primitive(primitive),
_vertices(vertices),
_ownsVertices(ownsVertices),
_normals(normals),
_ownsNormals(ownsNormals),
_extent(NULL),
_center(center),
_translationMatrix(( center.isNan() || center.isZero() )
                   ? NULL
                   : new MutableMatrix44D(MutableMatrix44D::createTranslationMatrix(center)) ),
_lineWidth(lineWidth),
_pointSize(pointSize),
_depthTest(depthTest),
_glState(new GLState()),
_polygonOffsetFactor(polygonOffsetFactor),
_polygonOffsetUnits(polygonOffsetUnits),
_polygonOffsetFill(polygonOffsetFill),
_cullFace(cullFace),
_culledFace(culledFace)
{
  createGLState();
}

BoundingVolume* AbstractGeometryMesh::computeBoundingVolume() const {
  const size_t vertexCount = getVertexCount();

  if (vertexCount == 0) {
    return NULL;
  }

  double minX = 1e12;
  double minY = 1e12;
  double minZ = 1e12;

  double maxX = -1e12;
  double maxY = -1e12;
  double maxZ = -1e12;

  for (size_t i=0; i < vertexCount; i++) {
    const size_t i3 = i * 3;

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

BoundingVolume* AbstractGeometryMesh::getBoundingVolume() const {
  if (_extent == NULL) {
    _extent = computeBoundingVolume();
  }
  return _extent;
}

const Vector3D AbstractGeometryMesh::getVertex(const size_t index) const {
  const size_t p = index * 3;
  return Vector3D(_vertices->get(p  ) + _center._x,
                  _vertices->get(p+1) + _center._y,
                  _vertices->get(p+2) + _center._z);
}

size_t AbstractGeometryMesh::getVertexCount() const {
  return _vertices->size() / 3;
}

void AbstractGeometryMesh::createGLState() {
  _glState->addGLFeature(new GeometryGLFeature(_vertices,    // The attribute is a float vector of 4 elements
                                               3,            // Our buffer contains elements of 3
                                               0,            // Index 0
                                               false,        // Not normalized
                                               0,            // Stride 0
                                               _depthTest,   // Depth test
                                               _cullFace,
                                               _culledFace,
                                               _polygonOffsetFill, _polygonOffsetFactor, _polygonOffsetUnits,  //Polygon Offset
                                               _lineWidth,
                                               true, _pointSize),
                         false);

  if (_normals != NULL) {
    _glState->addGLFeature(new VertexNormalGLFeature(_normals, 3, 0, false, 0),
                           false);
  }

  if (_translationMatrix != NULL) {
    _glState->addGLFeature(new ModelTransformGLFeature(_translationMatrix->asMatrix44D()),
                           false);
  }
}

void AbstractGeometryMesh::rawRender(const G3MRenderContext* rc,
                                     const GLState* parentGLState) const {
  _glState->setParent(parentGLState);
  rawRender(rc);
}
