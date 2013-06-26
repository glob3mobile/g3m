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
private:
  
  virtual void rawRender(const G3MRenderContext* rc, const GLState* parentGLState) const = 0;
  
protected:
  const int               _primitive;
  const bool              _owner;
  Vector3D                _center;
  const MutableMatrix44D* _translationMatrix;
  IFloatBuffer*           _vertices;
  const float             _lineWidth;
  const float             _pointSize;
  const bool              _depthTest;
  
  mutable Extent* _extent;
  Extent* computeExtent() const;
  
  AbstractGeometryMesh(const int primitive,
                       bool owner,
                       const Vector3D& center,
                       IFloatBuffer* vertices,
                       float lineWidth,
                       float pointSize,
                       bool depthTest);
  
  GLState _glState;
  
  void createGLState();
  
public:
  ~AbstractGeometryMesh();
  
  void render(const G3MRenderContext* rc) const{}
  
  Extent* getExtent() const;
  
  int getVertexCount() const;
  
  const Vector3D getVertex(int i) const;
  
  bool isTransparent(const G3MRenderContext* rc) const{
    return false; //TODO: CHECK
  }
  
  void render(const G3MRenderContext* rc, const GLState* parentGLState);
  
};

#endif /* defined(__G3MiOSSDK__AbstractGeometryMesh__) */
