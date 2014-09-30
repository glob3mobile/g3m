//
//  PointCloudMesh.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 30/09/14.
//
//

#include "PointCloudMesh.hpp"


#include "IFloatBuffer.hpp"
#include "IByteBuffer.hpp"
#include "Color.hpp"
#include "GL.hpp"
#include "Box.hpp"


#include "DirectMesh.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "CompositeMesh.hpp"
#include "Sphere.hpp"

#include "Camera.hpp"

#include "GLState.hpp"

PointCloudMesh::PointCloudMesh(IFloatBuffer* points,
                               bool ownsPoints,
                               IByteBuffer* rgbColors,
                               bool ownsColors,
                               int pointSize,
                               bool depthTest):
_points(points),
_ownsPoints(ownsPoints),
_rgbColors(rgbColors),
_ownsColors(ownsColors),
_pointSize(pointSize),
_depthTest(depthTest),
_nPoints(points->size() / 3),
_boundingVolume(NULL),
_glState(new GLState())
{
  if (_nPoints != (rgbColors->size() / 3)){
    ILogger::instance()->logError("Wrong parameters for PointCloudMesh()");
  }
  
  createGLState();
}

PointCloudMesh::~PointCloudMesh(){
  if (_ownsPoints){
    delete _points;
  }
  if (_ownsColors){
    delete _rgbColors;
  }
  _glState->_release();
  delete _boundingVolume;
}

void PointCloudMesh::createGLState() {
  _glState->addGLFeature(new GeometryGLFeature(_points,    //The attribute is a float vector of 4 elements
                                               3,            //Our buffer contains elements of 3
                                               0,            //Index 0
                                               false,        //Not normalized
                                               0,            //Stride 0
                                               _depthTest,         //Depth test
                                               false, 0,
                                               false, 0, 0,
                                               1.0,
                                               true,
                                               _pointSize),
                         false);
  
  _glState->addGLFeature(new ColorGLFeature(_rgbColors,// The attribute is a byte vector of 3 elements RGB
                                            3,            // Our buffer contains elements of 3
                                            0,            // Index 0
                                            false,        // Not normalized
                                            0,            // Stride 0
                                            true, GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha()),
                         false);
}

void PointCloudMesh::rawRender(const G3MRenderContext* rc,
                             const GLState* parentGLState) const {
  _glState->setParent(parentGLState);
  GL* gl = rc->getGL();
  gl->drawArrays(GLPrimitive::points(),
                 0,
                 _nPoints,
                 _glState,
                 *rc->getGPUProgramManager());
}

BoundingVolume* PointCloudMesh::computeBoundingVolume() const {
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
    
    const double x = _points->get(i3    );// + _center._x;
    const double y = _points->get(i3 + 1);// + _center._y;
    const double z = _points->get(i3 + 2);// + _center._z;
    
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

