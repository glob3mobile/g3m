//
//  PointCloudMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

#ifndef __G3MiOSSDK__PointCloudMesh__
#define __G3MiOSSDK__PointCloudMesh__

#include "Mesh.hpp"

#include "Vector3D.hpp"
#include "GLState.hpp"

class MutableMatrix44D;
class IFloatBuffer;
class Color;

class PointCloudMesh : public Mesh {
protected:
  const bool              _owner;
  const Vector3D                _center;
  const MutableMatrix44D* _translationMatrix;
  const IFloatBuffer*           _vertices;
  const IFloatBuffer*           _colors;
  const float             _pointSize;
  const bool              _depthTest;
  Color _borderColor;
  
  mutable BoundingVolume* _boundingVolume;
  BoundingVolume* computeBoundingVolume() const;
  
  GLState* _glState;
  
  void createGLState();
  
  mutable bool _showNormals;
  mutable Mesh* _normalsMesh;
  Mesh* createNormalsMesh() const;
  
public:
  
  PointCloudMesh(bool owner,
                 const Vector3D& center,
                 const IFloatBuffer* vertices,
                 float pointSize,
                 const IFloatBuffer* colors,
                 bool depthTest,
                 const Color& borderColor);
  
  ~PointCloudMesh();
  
  BoundingVolume* getBoundingVolume() const;
  
  size_t getVertexCount() const;
  
  const Vector3D getVertex(size_t i) const;
  
  bool isTransparent(const G3MRenderContext* rc) const;
  
  void rawRender(const G3MRenderContext* rc,
                 const GLState* parentGLState) const;
  
  void showNormals(bool v) const {
    _showNormals = v;
  }
  
  IFloatBuffer* getColorsFloatBuffer() const{
    return (IFloatBuffer*)_colors;
  }
  
};

#endif
