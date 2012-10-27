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

class Mesh;
class Planet;

class Trail {
private:
  bool _visible;
  const unsigned long _maxSteps;
  bool _positionsDirty;

  Color _color;
  const float _lineWidth;

  std::vector<Geodetic3D*> _positions;

  Mesh* createMesh(const Planet* planet);

  Mesh* _mesh;
  Mesh* getMesh(const Planet* planet);

public:
  Trail(int maxSteps,
        Color color,
        float lineWidth):
  _maxSteps(maxSteps),
  _visible(true),
  _positionsDirty(true),
  _mesh(NULL),
  _color(color),
  _lineWidth(lineWidth)
  {
  }

  ~Trail();

  void render(const RenderContext* rc);

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
        // const int lastIndex = _positions.size() - 1;
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
  TrailsRenderer() {
  }

  void addTrail(Trail* trail);

  virtual ~TrailsRenderer();

  void onResume(const InitializationContext* ic) {

  }

  void onPause(const InitializationContext* ic) {

  }

  void initialize(const InitializationContext* ic) {

  }

  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }

  bool onTouchEvent(const EventContext* ec,
                    const TouchEvent* touchEvent) {
    return false;
  }

  void onResizeViewportEvent(const EventContext* ec,
                             int width, int height) {

  }

  void start() {

  }

  void stop() {

  }

  void render(const RenderContext* rc);
  
};

#endif
