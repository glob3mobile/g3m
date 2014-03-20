//
//  TrailsRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/23/12.
//
//

#ifndef __G3MiOSSDK__TrailsRenderer__
#define __G3MiOSSDK__TrailsRenderer__

#include "DefaultRenderer.hpp"
#include "Geodetic3D.hpp"
#include <vector>
#include "Color.hpp"
#include "GLState.hpp"

class Mesh;
class Planet;
class Frustum;

class TrailSegment {
private:
  Color _color;
  const float _ribbonWidth;

  bool _positionsDirty;
  std::vector<Geodetic3D*> _positions;
  Geodetic3D* _nextSegmentFirstPosition;
  Geodetic3D* _previousSegmentLastPosition;

  Mesh* createMesh(const Planet* planet);

  Mesh* _mesh;
  Mesh* getMesh(const Planet* planet);


public:
  TrailSegment(Color color,
               float ribbonWidth) :
  _color(color),
  _ribbonWidth(ribbonWidth),
  _positionsDirty(true),
  _mesh(NULL),
  _nextSegmentFirstPosition(NULL),
  _previousSegmentLastPosition(NULL)
  {

  }

  ~TrailSegment();
  
  int getSize() const {
    return _positions.size();
  }

  void addPosition(const Angle& latitude,
                   const Angle& longitude,
                   const double height) {
    _positionsDirty = true;
    _positions.push_back(new Geodetic3D(latitude,
                                        longitude,
                                        height));
  }

  void addPosition(const Geodetic3D& position) {
    addPosition(position._latitude,
                position._longitude,
                position._height);
  }

  void setNextSegmentFirstPosition(const Angle& latitude,
                                   const Angle& longitude,
                                   const double height) {
    _positionsDirty = true;
    delete _nextSegmentFirstPosition;
    _nextSegmentFirstPosition = new Geodetic3D(latitude,
                                               longitude,
                                               height);
  }

  void setPreviousSegmentLastPosition(const Geodetic3D& position) {
    _positionsDirty = true;
    delete _previousSegmentLastPosition;
    _previousSegmentLastPosition = new Geodetic3D(position);
  }

  Geodetic3D getLastPosition() const {
#ifdef C_CODE
    return *(_positions[ _positions.size() - 1]);
#endif
#ifdef JAVA_CODE
    return _positions.get(_positions.size() - 1);
#endif
  }

  Geodetic3D getPreLastPosition() const {
#ifdef C_CODE
    return *(_positions[ _positions.size() - 2]);
#endif
#ifdef JAVA_CODE
    return _positions.get(_positions.size() - 2);
#endif
  }

  void render(const G3MRenderContext* rc,
              const Frustum* frustum, const GLState* state);

};

class Trail {
private:
  bool _visible;

  Color _color;
  const float _ribbonWidth;
  const float _heightDelta;

  std::vector<TrailSegment*> _segments;


public:
  Trail(Color color,
        float ribbonWidth,
        float heightDelta):
  _visible(true),
  _color(color),
  _ribbonWidth(ribbonWidth),
  _heightDelta(heightDelta)
  {
  }

  ~Trail();

  void render(const G3MRenderContext* rc,
              const Frustum* frustum,
              const GLState* state);

  void setVisible(bool visible) {
    _visible = visible;
  }

  bool isVisible() const {
    return _visible;
  }

  void addPosition(const Angle& latitude,
                   const Angle& longitude,
                   const double height);

  void addPosition(const Geodetic3D& position) {
    addPosition(position._latitude, position._longitude, position._height);
  }

  void clear();

};


class TrailsRenderer : public DefaultRenderer {
private:
  std::vector<Trail*> _trails;


  GLState* _glState;

  void updateGLState(const G3MRenderContext* rc);
  ProjectionGLFeature* _projection;
  ModelGLFeature*      _model;

public:
  TrailsRenderer():
  _projection(NULL),
  _model(NULL),
  _glState(new GLState())
  {
  }

  void addTrail(Trail* trail);

  virtual ~TrailsRenderer();

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  void render(const G3MRenderContext* rc, GLState* glState);
  
};

#endif
