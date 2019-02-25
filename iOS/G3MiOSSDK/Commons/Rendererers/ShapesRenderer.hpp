//
//  ShapesRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#ifndef __G3MiOSSDK__ShapesRenderer__
#define __G3MiOSSDK__ShapesRenderer__

#include "DefaultRenderer.hpp"

#include <vector>

#include "URL.hpp"
#include "TimeInterval.hpp"
#include "AltitudeMode.hpp"
#include "DownloadPriority.hpp"
#include "MutableVector3D.hpp"
#include "SceneJSParserParameters.hpp"
#include "Geodetic3D.hpp"

class Shape;
class Geodetic3D;
class Camera;
class Planet;


class ShapeDistance {
public:
  double _distance;
  Shape* _shape;

  ShapeDistance(double distance,
                Shape* shape):
  _distance(distance),
  _shape(shape)
  {
  }
};


class SGShape;

class ShapeLoadListener {
public:
#ifdef C_CODE
  virtual ~ShapeLoadListener() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  virtual void onBeforeAddShape(SGShape* shape) = 0;
  virtual void onAfterAddShape(SGShape* shape) = 0;
};


class ShapesRenderer : public DefaultRenderer {
private:
  class LoadQueueItem {
  public:
    const URL                     _url;
    const TimeInterval            _timeToCache;
#ifdef C_CODE
    const SceneJSParserParameters _parameters;
#endif
#ifdef JAVA_CODE
    public final SceneJSParserParameters _parameters;
#endif
    const long long               _priority;
    const bool                    _readExpired;
    const std::string             _uriPrefix;
    const bool                    _isTransparent;
    const Geodetic3D              _position;
    const AltitudeMode            _altitudeMode;
    ShapeLoadListener*            _listener;
    const bool                    _deleteListener;
    const bool                    _isBSON;

    LoadQueueItem(const URL&                     url,
                  long long                      priority,
                  const TimeInterval&            timeToCache,
                  bool                           readExpired,
                  const std::string&             uriPrefix,
                  bool                           isTransparent,
                  const SceneJSParserParameters& parameters,
                  const Geodetic3D&              position,
                  AltitudeMode                   altitudeMode,
                  ShapeLoadListener*             listener,
                  bool                           deleteListener,
                  bool                           isBSON) :
    _url(url),
    _priority(priority),
    _timeToCache(timeToCache),
    _readExpired(readExpired),
    _uriPrefix(uriPrefix),
    _isTransparent(isTransparent),
    _parameters(parameters),
    _position(position),
    _altitudeMode(altitudeMode),
    _listener(listener),
    _deleteListener(deleteListener),
    _isBSON(isBSON)
    {

    }

    ~LoadQueueItem() {
    }
  };


  const bool _renderNotReadyShapes;

  std::vector<Shape*> _shapes;

#ifdef C_CODE
  const Camera*     _lastCamera;
#endif
#ifdef JAVA_CODE
  private Camera    _lastCamera;
#endif

  GLState* _glState;
  GLState* _glStateTransparent;

  void updateGLState(const Camera* camera);

  std::vector<LoadQueueItem*> _loadQueue;

  void drainLoadQueue();
  
  void cleanLoadQueue();

  MutableVector3D _currentCameraPosition;

  void requestBuffer(const URL&                     url,
                     long long                      priority,
                     const TimeInterval&            timeToCache,
                     bool                           readExpired,
                     const std::string&             uriPrefix,
                     bool                           isTransparent,
                     const SceneJSParserParameters& parameters,
                     const Geodetic3D&              position,
                     AltitudeMode                   altitudeMode,
                     ShapeLoadListener*             listener,
                     bool                           deleteListener,
                     bool                           isBSON);

public:

  ShapesRenderer(bool renderNotReadyShapes=true);

  ~ShapesRenderer();

  void addShape(Shape* shape);

  void removeShape(Shape* shape);

  void removeAllShapes(bool deleteShapes=true);

  void enableAll();

  void disableAll();


  void onResume(const G3MContext* context) {
    _context = context;
  }

  void onChangedContext();
  
  void onLostContext();

  RenderState getRenderState(const G3MRenderContext* rc);

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {
  }

  void render(const G3MRenderContext* rc, GLState* glState);

  std::vector<ShapeDistance> intersectionsDistances(const Planet* planet,
                                                    const Vector3D& origin,
                                                    const Vector3D& direction) const;

  void loadJSONSceneJS(const URL&                     url,
                       long long                      priority,
                       const TimeInterval&            timeToCache,
                       bool                           readExpired,
                       const std::string&             uriPrefix,
                       bool                           isTransparent,
                       const SceneJSParserParameters& parameters,
                       const Geodetic3D&              position,
                       AltitudeMode                   altitudeMode,
                       ShapeLoadListener*             listener = NULL,
                       bool                           deleteListener = true);

  void loadJSONSceneJS(const URL&                     url,
                       const std::string&             uriPrefix,
                       bool                           isTransparent,
                       const SceneJSParserParameters& parameters,
                       const Geodetic3D&              position,
                       AltitudeMode                   altitudeMode,
                       ShapeLoadListener*             listener = NULL,
                       bool                           deleteListener = true) {
    loadJSONSceneJS(url,
                    DownloadPriority::MEDIUM,
                    TimeInterval::fromDays(30),
                    true,
                    uriPrefix,
                    isTransparent,
                    parameters,
                    position,
                    altitudeMode,
                    listener,
                    deleteListener);
  }

  void loadBSONSceneJS(const URL&                     url,
                       long long                      priority,
                       const TimeInterval&            timeToCache,
                       bool                           readExpired,
                       const std::string&             uriPrefix,
                       bool                           isTransparent,
                       const SceneJSParserParameters& parameters,
                       const Geodetic3D&              position,
                       AltitudeMode                   altitudeMode,
                       ShapeLoadListener*             listener = NULL,
                       bool                           deleteListener = true);

  void loadBSONSceneJS(const URL&                     url,
                       const std::string&             uriPrefix,
                       bool                           isTransparent,
                       const SceneJSParserParameters& parameters,
                       const Geodetic3D&              position,
                       AltitudeMode                   altitudeMode,
                       ShapeLoadListener*             listener = NULL,
                       bool                           deleteListener = true) {
    loadBSONSceneJS(url,
                    DownloadPriority::MEDIUM,
                    TimeInterval::fromDays(30),
                    true,
                    uriPrefix,
                    isTransparent,
                    parameters,
                    position,
                    altitudeMode,
                    listener,
                    deleteListener);
  }
  
};

#endif
