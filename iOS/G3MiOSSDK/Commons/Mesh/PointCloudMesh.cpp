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
_depthTest(depthTest)
{
  if ((points->size() / 4) != (rgbColors->size() / 3)){
    ILogger::instance()->logError("Wrong parameters for PointCloudMesh()");
  }
}

PointCloudMesh::~PointCloudMesh(){
  if (_ownsPoints){
    delete _points;
  }
  if (_ownsColors){
    delete _rgbColors;
  }
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
                                               true, _pointSize),
                         false);
}