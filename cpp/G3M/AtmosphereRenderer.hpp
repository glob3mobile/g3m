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

class DirectMesh;
class IFloatBuffer;
class CameraPositionGLFeature;
class Camera;
class Color;

class AtmosphereRenderer : public DefaultRenderer {
private:
  const bool               _groundHazePass;
  GLState*                 _glState;
  DirectMesh*              _directMesh;
  IFloatBuffer*            _vertices;
  CameraPositionGLFeature* _camPosGLF;

  void updateGLState(const Camera* camera,
                     const Color& spaceColor);

  void updateVerticesOfZNearPlaneRelativeToCamera(const Camera* camera);

  AtmosphereRenderer(bool groundHazePass);

public:
  // the sky and the space, drawn before the PlanetRenderer
  static AtmosphereRenderer* createSky();

  // the air in front of the ground, drawn after the PlanetRenderer
  static AtmosphereRenderer* createGroundHaze();

  ~AtmosphereRenderer();

  void start(const G3MRenderContext* rc);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {
  }

  void render(const G3MRenderContext* rc, GLState* glState);
  
};

#endif
