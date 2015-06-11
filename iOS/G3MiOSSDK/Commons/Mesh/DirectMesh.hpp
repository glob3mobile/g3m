//
//  DirectMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

#ifndef __G3MiOSSDK__DirectMesh__
#define __G3MiOSSDK__DirectMesh__

#include "AbstractMesh.hpp"
#include "ErrorHandling.hpp"

class DirectMesh : public AbstractMesh {
private:
  int _renderVerticesCount;

protected:
  void rawRender(const G3MRenderContext* rc) const;

//  Mesh* createNormalsMesh() const;


public:
  DirectMesh(const int primitive,
             bool owner,
             const Vector3D& center,
             const IFloatBuffer* vertices,
             float lineWidth,
             float pointSize,
             const Color* flatColor = NULL,
             const IFloatBuffer* colors = NULL,
             const float colorsIntensity = 0.0f,
             bool depthTest = true,
             const IFloatBuffer* normals = NULL);

  ~DirectMesh() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void setRenderVerticesCount(int renderVerticesCount) {
    if ((renderVerticesCount < 0) ||
        (renderVerticesCount > getRenderVerticesCount())) {
      THROW_EXCEPTION("Invalid renderVerticesCount");
    }
    _renderVerticesCount = renderVerticesCount;
  }

  int getRenderVerticesCount() const {
    return _renderVerticesCount;
  }

};

#endif
