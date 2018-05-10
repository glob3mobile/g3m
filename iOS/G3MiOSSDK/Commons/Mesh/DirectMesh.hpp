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
  size_t _renderVerticesCount;

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
             const IFloatBuffer* normals = NULL,
             bool polygonOffsetFill = false,
             float polygonOffsetFactor = 0,
             float polygonOffsetUnits = 0,
             IFloatBuffer* valuesInColorRange = NULL,
             const Color* colorRangeAt0 = NULL,
             const Color* colorRangeAt1 = NULL);

  ~DirectMesh() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void setRenderVerticesCount(size_t renderVerticesCount) {
    if (renderVerticesCount > getRenderVerticesCount()) {
      THROW_EXCEPTION("Invalid renderVerticesCount");
    }
    _renderVerticesCount = renderVerticesCount;
  }

  size_t getRenderVerticesCount() const {
    return _renderVerticesCount;
  }

};

#endif
