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

class IFloatBuffer;
class Color;
class ModelTransformGLFeature;


class AbstractMesh : public Mesh {
private:
  MutableMatrix44D*        _transformMatrix;
  ModelTransformGLFeature* _transformGLFeature;
  MutableMatrix44D*        _userTransformMatrix;
  MutableMatrix44D* getTransformMatrix();
  MutableMatrix44D* createTransformMatrix() const;

  void createGLState();

  mutable bool _showNormals;
  mutable Mesh* _normalsMesh;
  Mesh* createNormalsMesh() const;

protected:
  const int           _primitive;
  const bool          _owner;
  const Vector3D      _center;
  const IFloatBuffer* _vertices;
  const Color*        _flatColor;
  const IFloatBuffer* _colors;
  const float         _lineWidth;
  const float         _pointSize;
  const bool          _depthTest;
  const IFloatBuffer* _normals;
  const bool          _polygonOffsetFill;
  const float         _polygonOffsetFactor;
  const float         _polygonOffsetUnits;

  GLState* _glState;

  mutable BoundingVolume* _boundingVolume;
  BoundingVolume* computeBoundingVolume() const;

  AbstractMesh(const int primitive,
               bool owner,
               const Vector3D& center,
               const IFloatBuffer* vertices,
               float lineWidth,
               float pointSize,
               const Color* flatColor,
               const IFloatBuffer* colors,
               bool depthTest,
               const IFloatBuffer* normals,
               bool polygonOffsetFill,
               float polygonOffsetFactor,
               float polygonOffsetUnits);

  virtual void rawRender(const G3MRenderContext* rc) const = 0;

public:
  ~AbstractMesh();

  BoundingVolume* getBoundingVolume() const;

  size_t getVertexCount() const;

  const Vector3D getVertex(size_t i) const;

  bool isTransparent(const G3MRenderContext* rc) const;

  void rawRender(const G3MRenderContext* rc,
                 const GLState* parentGLState) const;

  void showNormals(bool v) const {
    _showNormals = v;
  }

  void setUserTransformMatrix(MutableMatrix44D* userTransformMatrix);
  
};

#endif
