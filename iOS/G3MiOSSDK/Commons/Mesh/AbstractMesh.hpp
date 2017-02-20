//
//  AbstractMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

#ifndef __G3MiOSSDK__AbstractMesh__
#define __G3MiOSSDK__AbstractMesh__

#include "TransformableMesh.hpp"


class IFloatBuffer;
class Color;


class AbstractMesh : public TransformableMesh {
private:

protected:
  const int           _primitive;
  const bool          _owner;
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

  virtual void renderMesh(const G3MRenderContext* rc,
                          GLState* glState) const = 0;

  void userTransformMatrixChanged();

  void initializeGLState(GLState* glState) const;

public:
  ~AbstractMesh();

  BoundingVolume* getBoundingVolume() const;

  size_t getVertexCount() const;

  const Vector3D getVertex(const size_t i) const;

  virtual bool isTransparent(const G3MRenderContext* rc) const;

  void rawRender(const G3MRenderContext* rc,
                 const GLState* parentGLState) const;

};

#endif
