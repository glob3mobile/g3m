//
//  AtmosphereRenderer.hpp
//  G3M
//
//  Created by Jose Miguel SN on 07/10/2016.
//
//

#ifndef AtmosphereRenderer_hpp
#define AtmosphereRenderer_hpp

#include <stdio.h>

#include "DefaultRenderer.hpp"

#include "Color.hpp"

class Mesh;
class IFloatBuffer;
class CameraPositionGLFeature;
class Camera;

class AtmosphereRenderer : public DefaultRenderer {
private:
  const Color  _blueSky;
  const Color  _darkSpace;
  const double _minHeight;

  GLState*                 _glState;
  Mesh*                    _directMesh;
  IFloatBuffer*            _vertices;
  CameraPositionGLFeature* _camPosGLF;
  Color*                   _previousBackgroundColor;
  bool                     _overPrecisionThreshold;

  void updateGLState(const Camera* camera);

public:
  AtmosphereRenderer();

  ~AtmosphereRenderer();

  void start(const G3MRenderContext* rc);

  void stop(const G3MRenderContext* rc);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {
  }

  void render(const G3MRenderContext* rc, GLState* glState);
  
};

#endif
