//
//  StarsRenderer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/7/26.
//

#ifndef StarsRenderer_hpp
#define StarsRenderer_hpp

#include "DefaultRenderer.hpp"

class DirectMesh;
class GLState;
class Camera;
class StarsIntensityGLFeature;


// The stars of the Yale Bright Star Catalogue, fixed on the planet's cartesian axes,
// drawn as round discs; the light above white makes a star bigger
class StarsRenderer : public DefaultRenderer {
private:
  const float _starPointSize;
  DirectMesh* _starsMesh;
  GLState*    _glState;
  StarsIntensityGLFeature* _starsIntensityGLFeature;

  DirectMesh* createStarsMesh() const;

  void updateGLState(const Camera* camera);

public:
  // starPointSize: diameter in pixels of a star as bright as white;
  // starsIntensity: multiplies the brightness of every star (1 keeps the catalogue brightness)
  StarsRenderer(float starPointSize,
                float starsIntensity);

  void setStarsIntensity(float starsIntensity);

  ~StarsRenderer();

  void render(const G3MRenderContext* rc,
              GLState* glState);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {
  }

};

#endif
