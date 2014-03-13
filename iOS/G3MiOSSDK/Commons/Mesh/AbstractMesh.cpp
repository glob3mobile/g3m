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

#include "DirectMesh.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "CompositeMesh.hpp"
#include "Sphere.hpp"

#include "Camera.hpp"

#include "GLFeature.hpp"

AbstractMesh::~AbstractMesh() {
  if (_owner) {
    delete _vertices;
    delete _colors;
    delete _flatColor;
    delete _normals;
  }

  delete _boundingVolume;
  delete _translationMatrix;

  _glState->_release();

  delete _normalsMesh;

#ifdef JAVA_CODE
  super.dispose();
#endif

}

AbstractMesh::AbstractMesh(const int primitive,
                           bool owner,
                           const Vector3D& center,
                           IFloatBuffer* vertices,
                           float lineWidth,
                           float pointSize,
                           const Color* flatColor,
                           IFloatBuffer* colors,
                           const float colorsIntensity,
                           bool depthTest,
                           IFloatBuffer* normals) :
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
_depthTest(depthTest),
_glState(new GLState()),
_normals(normals),
_normalsMesh(NULL),
_showNormals(false)
{
  createGLState();
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

void AbstractMesh::createGLState() {

  _glState->addGLFeature(new GeometryGLFeature(_vertices,   // The attribute is a float vector of 4 elements
                                               3,            // Our buffer contains elements of 3
                                               0,            // Index 0
                                               false,        // Not normalized
                                               0,            // Stride 0
                                               _depthTest,   // Depth test
                                               false, 0,
                                               false, 0.0f, 0.0f,
                                               _lineWidth,
                                               true,
                                               _pointSize),
                         false);   //POINT SIZE

  if (_normals != NULL) {
    _glState->addGLFeature(new VertexNormalGLFeature(_normals, 3, 0, false, 0),
                           false);
  }

  if (_translationMatrix != NULL) {
    _glState->addGLFeature(new ModelTransformGLFeature(_translationMatrix->asMatrix44D()), false);
  }

  if (_flatColor != NULL && _colors == NULL) {  //FlatColorMesh Shader

    _glState->addGLFeature(new FlatColorGLFeature(*_flatColor,
                                                  _flatColor->isTransparent(),
                                                  GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha()),
                           false);




    return;
  }

  if (_colors != NULL) {
    _glState->addGLFeature(new ColorGLFeature(_colors,   //The attribute is a float vector of 4 elements RGBA
                                              4,            //Our buffer contains elements of 4
                                              0,            //Index 0
                                              false,        //Not normalized
                                              0,            //Stride 0
                                              true, GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha()), false);

  }

}

void AbstractMesh::rawRender(const G3MRenderContext* rc,
                             const GLState* parentGLState) const{
  _glState->setParent(parentGLState);
  rawRender(rc);

  //RENDERING NORMALS
  if (_normals != NULL) {
    if (_showNormals) {
      if (_normalsMesh == NULL) {
        //_normalsMesh = createNormalsMesh();
      }
      if (_normalsMesh != NULL) {
        //_normalsMesh->render(rc, parentGLState);
      }
    } else{
      if (_normalsMesh != NULL) {
        delete _normalsMesh;
        _normalsMesh = NULL;
      }
    }
  }
}


Mesh* AbstractMesh::createNormalsMesh() const{

  DirectMesh* verticesMesh = new DirectMesh(GLPrimitive::points(),
                                            false,
                                            _center,
                                            _vertices,
                                            (float)1.0,
                                            (float)2.0,
                                            new Color(Color::red()),
                                            NULL,
                                            (float)1.0,
                                            false,
                                            NULL);

  FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();

  BoundingVolume* volume = getBoundingVolume();
  Sphere* sphere = volume->createSphere();
  double normalsSize = sphere->getRadius() / 100.0;
  delete sphere;

  const int size = _vertices->size();
  for (int i = 0; i < size; i+=3) {

    Vector3D v(_vertices->get(i), _vertices->get(i+1), _vertices->get(i+2));
    Vector3D n(_normals->get(i), _normals->get(i+1), _normals->get(i+2));

    Vector3D v_n = v.add(n.normalized().times(normalsSize));

    fbb->add(v);
    fbb->add(v_n);
  }

  DirectMesh* normalsMesh = new DirectMesh(GLPrimitive::lines(),
                                           true,
                                           _center,
                                           fbb->create(),
                                           (float)2.0,
                                           (float)1.0,
                                           new Color(Color::blue()));

  delete fbb;

  CompositeMesh* compositeMesh = new CompositeMesh();
  compositeMesh->addMesh(verticesMesh);
  compositeMesh->addMesh(normalsMesh);

  return compositeMesh;
  
}




