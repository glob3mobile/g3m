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

class MutableMatrix44D;
class IFloatBuffer;


class AbstractGeometryMesh : public Mesh {
  
protected:
  const int               _primitive;
  const Vector3D          _center;
  const MutableMatrix44D* _translationMatrix;
  const IFloatBuffer*     _vertices;
  const bool              _ownsVertices;
  const IFloatBuffer*     _normals;
  const bool              _ownsNormals;
  const float             _lineWidth;
  const float             _pointSize;
  const bool              _depthTest;

  const bool _polygonOffsetFill;
  const float _polygonOffsetFactor;
  const float _polygonOffsetUnits;

  const bool _cullFace;
  const int  _culledFace;

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
                       bool            depthTest,
                       bool            polygonOffsetFill,
                       float           polygonOffsetFactor,
                       float           polygonOffsetUnits,
                       bool            cullFace,
                       int             culledFace);
  
  GLState* _glState;
  
  void createGLState();
  
  virtual void rawRender(const G3MRenderContext* rc) const = 0;

public:
  ~AbstractGeometryMesh();
  
  BoundingVolume* getBoundingVolume() const;
  
  size_t getVertexCount() const;
  
  const Vector3D getVertex(const size_t index) const;
  
  bool isTransparent(const G3MRenderContext* rc) const {
    return false; //TODO: CHECK
  }
  
  void rawRender(const G3MRenderContext* rc,
                 const GLState* parentGLState) const;
  
};

#endif
