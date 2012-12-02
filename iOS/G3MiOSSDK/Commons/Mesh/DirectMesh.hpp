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

class DirectMesh : public AbstractMesh {
protected:
  void rawRender(const G3MRenderContext* rc) const;


public:
  DirectMesh(const int primitive,
             bool owner,
             const Vector3D& center,
             IFloatBuffer* vertices,
             float lineWidth,
             Color* flatColor = NULL,
             IFloatBuffer* colors = NULL,
             const float colorsIntensity = (float)0.0);

  ~DirectMesh() {

  }

};

#endif
