//
//  AbstractMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

#ifndef __G3MiOSSDK__AbstractMesh__
#define __G3MiOSSDK__AbstractMesh__

#include "Mesh.hpp"

#include "Vector3D.hpp"
#include "GLState.hpp"

class MutableMatrix44D;
class IFloatBuffer;
class Color;

class AbstractMesh : public Mesh {
protected:
  const int               _primitive;
  const bool              _owner;
  Vector3D                _center;
  const MutableMatrix44D* _translationMatrix;
  IFloatBuffer*           _vertices;
  const Color*            _flatColor;
  IFloatBuffer*           _colors;
  const float             _colorsIntensity;
  const float             _lineWidth;
  const float             _pointSize;
  const bool              _depthTest;
  IFloatBuffer*           _normals;

  mutable BoundingVolume* _boundingVolume;
  BoundingVolume* computeBoundingVolume() const;

  AbstractMesh(const int primitive,
               bool owner,
               const Vector3D& center,
               IFloatBuffer* vertices,
               float lineWidth,
               float pointSize,
               const Color* flatColor,
               IFloatBuffer* colors,
               const float colorsIntensity,
               bool depthTest,
               IFloatBuffer* normals);

  virtual void rawRender(const G3MRenderContext* rc) const = 0;
//  virtual void rawRender(const G3MRenderContext* rc, const GLState* parentGLState) const = 0;
  
  GLState* _glState;
  
  void createGLState();

  mutable bool _showNormals;
  mutable Mesh* _normalsMesh;
  Mesh* createNormalsMesh() const;

public:
  ~AbstractMesh();
  
  BoundingVolume* getBoundingVolume() const;

  int getVertexCount() const;

  const Vector3D getVertex(int i) const;

  bool isTransparent(const G3MRenderContext* rc) const;
  
  void rawRender(const G3MRenderContext* rc,
                 const GLState* parentGLState) const;

  void showNormals(bool v) const{
    _showNormals = v;
  }
  
};

#endif
