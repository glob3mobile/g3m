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
class ColorLook;
class Vector3F;
class Matrix44D;

class AtmosphereRenderer : public DefaultRenderer {
private:
  const bool               _groundHazePass;
  GLState*                 _glState;
  DirectMesh*              _directMesh;
  IFloatBuffer*            _vertices;
  CameraPositionGLFeature* _camPosGLF;
  ColorLook*               _colorLook;

  void updateGLState(const Camera* camera,
                     const Color& spaceColor);

  void updateVerticesOfZNearPlaneRelativeToCamera(const Camera* camera);

  AtmosphereRenderer(bool groundHazePass);

  // the horizon of Google Earth seen from the ground
  static Color defaultHorizonColor();

  // Rayleigh scattering at sea level for red, green and blue (680, 550, 440 nm), in 1e-3 / km
  static Vector3F defaultSkyRayleighScattering();

  static Color gradedHorizonColor(const Matrix44D& colorMatrix,
                                  const Color& horizonColor);

  void applyColorLook();

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

  // Grades the air like the tiles graded with the same look (PlanetRenderer::setColorMatrix):
  // the haze exactly, the sky approximately (its light is not linear in its colours)
  void setColorLook(const ColorLook& look);
  
};

#endif
