//
//  TrailsRenderer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/23/12.
//
//

#ifndef __G3M__TrailsRenderer__
#define __G3M__TrailsRenderer__

#include "DefaultRenderer.hpp"

class Trail;
class Camera;
class ProjectionGLFeature;
class ModelGLFeature;


class TrailsRenderer : public DefaultRenderer {
private:
  std::vector<Trail*> _trails;

  GLState* _glState;

  void updateGLState(const Camera* camera);
  ProjectionGLFeature* _projection;
  ModelGLFeature*      _model;

public:
  TrailsRenderer();

  void addTrail(Trail* trail);

  void removeTrail(Trail* trail,
                   bool deleteTrail = true);

  void removeAllTrails(bool deleteTrails = true);

  ~TrailsRenderer();

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  void render(const G3MRenderContext* rc,
              GLState* glState);
  
};

#endif
