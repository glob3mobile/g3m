//
//  PointCloudMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

#include "PointCloudMesh.hpp"

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

PointCloudMesh::~PointCloudMesh() {
  if (_owner) {
    delete _vertices;
    delete _colors;
    
//    for (size_t i = 0; i < _colorsCollection.size(); i++){
//      delete _colorsCollection[i];
//    }
    
  }
  
  delete _boundingVolume;
  delete _translationMatrix;
  
  _glState->_release();
  
#ifdef JAVA_CODE
  super.dispose();
#endif
}

PointCloudMesh::PointCloudMesh(bool owner,
                               const Vector3D& center,
                               const IFloatBuffer* vertices,
                               float pointSize,
                               const IFloatBuffer* colors,
                               bool depthTest,
                               const Color& borderColor) :
_owner(owner),
_vertices(vertices),
_boundingVolume(NULL),
_center(center),
_translationMatrix(( center.isNan() || center.isZero() )
                   ? NULL
                   : new MutableMatrix44D(MutableMatrix44D::createTranslationMatrix(center)) ),
_pointSize(pointSize),
_depthTest(depthTest),
_glState(new GLState()),
_borderColor(borderColor),
_colors(colors)
{
  createGLState();
}

BoundingVolume* PointCloudMesh::computeBoundingVolume() const {
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

BoundingVolume* PointCloudMesh::getBoundingVolume() const {
  if (_boundingVolume == NULL) {
    _boundingVolume = computeBoundingVolume();
  }
  return _boundingVolume;
}

const Vector3D PointCloudMesh::getVertex(size_t i) const {
  const size_t p = i * 3;
  return Vector3D(_vertices->get(p  ) + _center._x,
                  _vertices->get(p+1) + _center._y,
                  _vertices->get(p+2) + _center._z);
}

size_t PointCloudMesh::getVertexCount() const {
  return _vertices->size() / 3;
}

bool PointCloudMesh::isTransparent(const G3MRenderContext* rc) const {
  return false;
}

void PointCloudMesh::createGLState() {
  
  _glState->addGLFeature(new GeometryGLFeature(_vertices,    //The attribute is a float vector of 4 elements
                                               3,            //Our buffer contains elements of 3
                                               0,            //Index 0
                                               false,        //Not normalized
                                               0,            //Stride 0
                                               _depthTest,         //Depth test
                                               false, 0,     //Cull and culled face
                                               false, 0.0, 0.0,  //Polygon Offset
                                               1.0,
                                               true, _pointSize),
                         false);
  
  _glState->addGLFeature(new PointShapeGLFeature(_borderColor), false);
  
  if (_translationMatrix != NULL) {
    _glState->addGLFeature(new ModelTransformGLFeature(_translationMatrix->asMatrix44D()), false);
  }
  
  applyColor();
}

void PointCloudMesh::rawRender(const G3MRenderContext* rc,
                               const GLState* parentGLState) const {
  _glState->setParent(parentGLState);
  
  GL* gl = rc->getGL();
  
  gl->drawArrays(GLPrimitive::points(),
                 0,
                 (int)_vertices->size() / 3,
                 _glState,
                 *rc->getGPUProgramManager());
}

void PointCloudMesh::applyColor(){
  _glState->clearGLFeatureGroup(COLOR_GROUP);
  
  ColorGLFeature* c = new ColorGLFeature(_colors,      // The attribute is a float vector of 4 elements RGBA
                                         4,            // Our buffer contains elements of 4
                                         0,            // Index 0
                                         false,        // Not normalized
                                         0,            // Stride 0
                                         true, GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());
  _glState->addGLFeature(c, false);
}

void PointCloudMesh::changeToColors(IFloatBuffer* colors){
  if (_colors == colors){
    return;
  }
  
  if (_owner) {
    delete _colors;
    _colors = colors;
  }
  
  applyColor();
}




