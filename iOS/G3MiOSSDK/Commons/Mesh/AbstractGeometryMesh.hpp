//
//  AbstractGeometryMesh.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//

#ifndef __G3MiOSSDK__AbstractGeometryMesh__
#define __G3MiOSSDK__AbstractGeometryMesh__

#include "Mesh.hpp"

#include "Vector3D.hpp"
#include "GLState.hpp"

class MutableMatrix44D;
class IFloatBuffer;
class Color;

class AbstractGeometryMesh : public Mesh {
  
protected:
  const int               _primitive;
  Vector3D                _center;
  const MutableMatrix44D* _translationMatrix;
  IFloatBuffer*           _vertices;
  const bool              _ownsVertices;
  IFloatBuffer*           _normals;
  const bool              _ownsNormals;
  const float             _lineWidth;
  const float             _pointSize;
  const bool              _depthTest;
  
  mutable BoundingVolume* _extent;
  BoundingVolume* computeBoundingVolume() const;
  
  AbstractGeometryMesh(const int       primitive,
                       const Vector3D& center,
                       IFloatBuffer*   vertices,
                       bool            ownsVertices,
                       IFloatBuffer*   normals,
                       bool            ownsNormals,
                       float           lineWidth,
                       float           pointSize,
                       bool            depthTest);
  
  GLState* _glState;
  
  void createGLState();
  
  virtual void rawRender(const G3MRenderContext* rc) const = 0;

  mutable bool _showNormals;
  mutable Mesh* _normalsMesh;
  Mesh* createNormalsMesh() const;
  
public:
  ~AbstractGeometryMesh();
  
  BoundingVolume* getBoundingVolume() const;
  
  int getVertexCount() const;
  
  const Vector3D getVertex(int i) const;
  
  bool isTransparent(const G3MRenderContext* rc) const{
    return false; //TODO: CHECK
  }
  
  void rawRender(const G3MRenderContext* rc,
                 const GLState* parentGLState) const;

  void showNormals(bool v) const{
    _showNormals = v;
  }
  
};

#endif
