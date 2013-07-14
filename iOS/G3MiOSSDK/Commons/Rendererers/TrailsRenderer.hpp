//
//  TrailsRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/23/12.
//
//

#ifndef __G3MiOSSDK__TrailsRenderer__
#define __G3MiOSSDK__TrailsRenderer__

#include "LeafRenderer.hpp"
#include "Geodetic3D.hpp"
#include <vector>
#include "Color.hpp"
#include "GPUProgramState.hpp"
#include "GLState.hpp"

class Mesh;
class Planet;

class Trail {
private:
  bool _visible;
  const unsigned long _maxSteps;
  bool _positionsDirty;

  Color _color;
  const float _ribbonWidth;

  std::vector<Geodetic3D*> _positions;

  Mesh* createMesh(const Planet* planet);

  Mesh* _mesh;
  Mesh* getMesh(const Planet* planet);
  
  GLState _glState;
  void createGLState() const;

  void updateGLState(const G3MRenderContext* rc);
  ProjectionGLFeature* _projection;
  ModelGLFeature*      _model;

public:
  Trail(int maxSteps,
        Color color,
        float ribbonWidth):
  _maxSteps(maxSteps),
  _visible(true),
  _positionsDirty(true),
  _mesh(NULL),
  _color(color),
  _ribbonWidth(ribbonWidth),
  _projection(NULL),
  _model(NULL)
  {
    createGLState();
  }

  ~Trail();

  void render(const G3MRenderContext* rc);

  void setVisible(bool visible) {
    _visible = visible;
  }

  bool isVisible() const {
    return _visible;
  }

  void addPosition(const Geodetic3D& position) {
    _positionsDirty = true;

    if (_maxSteps > 0) {
      while (_positions.size() >= _maxSteps) {
        const int index = 0;
        delete _positions[index];

#ifdef C_CODE
        _positions.erase( _positions.begin() + index );
#endif
#ifdef JAVA_CODE
        _positions.remove( index );
#endif
      }
    }

    _positions.push_back(new Geodetic3D(position));
  }

};


class TrailsRenderer : public LeafRenderer {
private:
  std::vector<Trail*> _trails;

public:
  TrailsRenderer()
  {
  }

  void addTrail(Trail* trail);

  virtual ~TrailsRenderer();

  void onResume(const G3MContext* context) {

  }

  void onPause(const G3MContext* context) {

  }

  void onDestroy(const G3MContext* context) {

  }

  void initialize(const G3MContext* context);

  bool isReadyToRender(const G3MRenderContext* rc) {
    return true;
  }

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent) {
    return false;
  }

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  void start(const G3MRenderContext* rc) {

  }

  void stop(const G3MRenderContext* rc) {

  }

  void render(const G3MRenderContext* rc);
  
};

#endif
