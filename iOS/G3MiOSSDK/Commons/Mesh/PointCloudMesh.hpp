//
//  PointCloudMesh.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 30/09/14.
//
//

#ifndef __G3MiOSSDK__PointCloudMesh__
#define __G3MiOSSDK__PointCloudMesh__

#include "Mesh.hpp"

#include "Vector3D.hpp"
#include "GLState.hpp"

class MutableMatrix44D;
class IFloatBuffer;
class IByteBuffer;
class Color;

class PointCloudMesh: public Mesh{
private:
  IFloatBuffer*           _points;
  bool                    _ownsPoints;
  IByteBuffer*            _rgbColors;
  bool                    _ownsColors;
  int                     _pointSize;
  bool                    _depthTest;
  
  GLState*                _glState;
  void createGLState();

  
public:
  
  PointCloudMesh(IFloatBuffer* points,
                 bool ownsPoints,
                 IByteBuffer* rgbColors,
                 bool ownsColors,
                 int pointSize = 1.0,
                 bool depthTest = true);
  ~PointCloudMesh();
  
//protected:
//  Vector3D                _center;
//  const MutableMatrix44D* _translationMatrix;
//  IFloatBuffer*           _vertices;
//  const bool              _ownsVertices;
//  IFloatBuffer*           _normals;
//  const bool              _ownsNormals;
//  const float             _lineWidth;
//  const float             _pointSize;
//  const bool              _depthTest;
//  
//  mutable BoundingVolume* _extent;
//  BoundingVolume* computeBoundingVolume() const;
//  
//  AbstractGeometryMesh(const int       primitive,
//                       const Vector3D& center,
//                       IFloatBuffer*   vertices,
//                       bool            ownsVertices,
//                       IFloatBuffer*   normals,
//                       bool            ownsNormals,
//                       float           lineWidth,
//                       float           pointSize,
//                       bool            depthTest);
//  
//  GLState* _glState;
//  
//  void createGLState();
//  
//  virtual void rawRender(const G3MRenderContext* rc) const = 0;
//  
//  mutable bool _showNormals;
//  mutable Mesh* _normalsMesh;
//  Mesh* createNormalsMesh() const;
//  
//public:
//  ~AbstractGeometryMesh();
//  
//  BoundingVolume* getBoundingVolume() const;
//  
//  int getVertexCount() const;
//  
//  const Vector3D getVertex(int i) const;
//  
//  bool isTransparent(const G3MRenderContext* rc) const {
//    return false; //TODO: CHECK
//  }
//  
//  void rawRender(const G3MRenderContext* rc,
//                 const GLState* parentGLState) const;
//  
//  void showNormals(bool v) const {
//    _showNormals = v;
//  }
  
  
  
  
};

#endif /* defined(__G3MiOSSDK__PointCloudMesh__) */
