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
class StarsGLFeature;


// The stars of the Yale Bright Star Catalogue, fixed on the planet's cartesian axes, drawn as round discs
// from their real light (as Stellarium): the faint stars fade at the smallest diameter, the bright ones grow
class StarsRenderer : public DefaultRenderer {
private:
  const float _smallestStarDiameter;
  DirectMesh* _starsMesh;
  GLState*    _glState;
  StarsGLFeature* _starsGLFeature;

  DirectMesh* createStarsMesh() const;

  void updateGLState(const Camera* camera);

public:
  // smallestStarDiameter: in pixels, the diameter of the faint stars;
  // fullStarMagnitude: a star of this magnitude is drawn opaque at the smallest diameter (fainter ones fade);
  // starSizeExponent: the diameter of the brighter stars grows as their light to this power
  StarsRenderer(float smallestStarDiameter,
                float fullStarMagnitude,
                float starSizeExponent);

  void setFullStarMagnitude(float fullStarMagnitude);

  ~StarsRenderer();

  void render(const G3MRenderContext* rc,
              GLState* glState);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {
  }

};

#endif
