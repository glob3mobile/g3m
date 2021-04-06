//
//  ShapesRenderer.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#include "ShapesRenderer.hpp"

#include "OrderedRenderable.hpp"
#include "Camera.hpp"
#include "TouchEvent.hpp"
#include "IDownloader.hpp"
#include "IBufferDownloadListener.hpp"
#include "IThreadUtils.hpp"
#include "SceneJSShapesParser.hpp"
#include "SGShape.hpp"
#include "G3MEventContext.hpp"
#include "GAsyncTask.hpp"
#include "G3MRenderContext.hpp"
#include "GLState.hpp"
#include "Planet.hpp"
#include "Geodetic3D.hpp"
#include "ShapeFilter.hpp"


class TransparentShapeWrapper : public OrderedRenderable {
private:
  Shape* _shape;
  const double _squaredDistanceFromEye;
  GLState* _parentGLState;
  const bool _renderNotReadyShapes;
public:
  TransparentShapeWrapper(Shape* shape,
                          double squaredDistanceFromEye,
                          GLState* parentGLState,
                          bool renderNotReadyShapes) :
  _shape(shape),
  _squaredDistanceFromEye(squaredDistanceFromEye),
  _parentGLState(parentGLState),
  _renderNotReadyShapes(renderNotReadyShapes)
  {
  }

  double squaredDistanceFromEye() const {
    return _squaredDistanceFromEye;
  }

  void render(const G3MRenderContext* rc) {
    _shape->render(rc, _parentGLState, _renderNotReadyShapes);
  }
};

ShapesRenderer::ShapesRenderer(bool renderNotReadyShapes) :
_renderNotReadyShapes(renderNotReadyShapes),
_glState(new GLState()),
_glStateTransparent(new GLState()),
_lastCamera(NULL)
{
  _context = NULL;
}

ShapesRenderer::~ShapesRenderer() {
  const size_t shapesCount = _shapes.size();
  for (size_t i = 0; i < shapesCount; i++) {
    Shape* shape = _shapes[i];
    delete shape;
  }

  _glState->_release();
  _glStateTransparent->_release();

#ifdef JAVA_CODE
  super.dispose();
#endif
}


RenderState ShapesRenderer::getRenderState(const G3MRenderContext* rc) {
  if (!_renderNotReadyShapes) {
    const size_t shapesCount = _shapes.size();
    for (size_t i = 0; i < shapesCount; i++) {
      Shape* shape = _shapes[i];
      const bool shapeReady = shape->isReadyToRender(rc);
      if (!shapeReady) {
        return RenderState::busy();
      }
    }
  }
  return RenderState::ready();
}

void ShapesRenderer::updateGLState(const Camera* camera) {
  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(camera), true);
  }
  else {
    f->setMatrix(camera->getModelViewMatrix44D());
  }

  f = (ModelViewGLFeature*) _glStateTransparent->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glStateTransparent->addGLFeature(new ModelViewGLFeature(camera), true);
  }
  else {
    f->setMatrix(camera->getModelViewMatrix44D());
  }
}

void ShapesRenderer::render(const G3MRenderContext* rc, GLState* glState) {
  // Saving camera for use in onTouchEvent
  _lastCamera = rc->getCurrentCamera();

  const size_t shapesCount = _shapes.size();
  if (shapesCount > 0) {
    _lastCamera->getCartesianPositionMutable(_currentCameraPosition);

    //Setting camera matrixes
    updateGLState(_lastCamera);

    _glState->setParent(glState);
    _glStateTransparent->setParent(glState);


    for (size_t i = 0; i < shapesCount; i++) {
      Shape* shape = _shapes[i];
      if (shape->isEnable()) {
        if (shape->isTransparent(rc)) {
          const Planet* planet = rc->getPlanet();
          const Vector3D shapePosition = planet->toCartesian( shape->getPosition() );
          const double squaredDistanceFromEye = shapePosition.sub(_currentCameraPosition).squaredLength();

          rc->addOrderedRenderable(new TransparentShapeWrapper(shape,
                                                               squaredDistanceFromEye,
                                                               _glStateTransparent,
                                                               _renderNotReadyShapes));
        }
        else {
          shape->render(rc, _glState, _renderNotReadyShapes);
        }
      }
    }
  }
}

void ShapesRenderer::addShape(Shape* shape) {
  _shapes.push_back(shape);
  if (_context != NULL) {
    shape->initialize(_context);
  }
}

void ShapesRenderer::removeShape(Shape* shape) {
  int pos = -1;
  const int shapesSize = _shapes.size();
  for (int i = 0; i < shapesSize; i++) {
    if (_shapes[i] == shape) {
      pos = i;
      break;
    }
  }
  if (pos != -1) {
#ifdef C_CODE
    _shapes.erase(_shapes.begin() + pos);
#endif
#ifdef JAVA_CODE
    _shapes.remove(pos);
#endif
  }
}

void ShapesRenderer::removeAllShapes(bool deleteShapes) {
  const size_t shapesCount = _shapes.size();
  if (shapesCount == 0) {
    return;
  }

  if (deleteShapes) {
    for (size_t i = 0; i < shapesCount; i++) {
      Shape* shape = _shapes[i];
      delete shape;
    }
  }

  _shapes.clear();
}


#ifdef C_CODE
class SortShapeDistanceClass {
public:
  bool operator() (ShapeDistance sd1, ShapeDistance sd2) {
    return (sd1._distance < sd2._distance);
  }
} sortShapeDistanceObject;
#endif



std::vector<ShapeDistance> ShapesRenderer::intersectionsDistances(const Planet* planet,
                                                                  const Vector3D& origin,
                                                                  const Vector3D& direction) const
{
  std::vector<ShapeDistance> shapeDistances;
  for (int n=0; n<_shapes.size(); n++) {
    Shape* shape = _shapes[n];
    std::vector<double> distances = shape->intersectionsDistances(planet, origin, direction);
    for (int i=0; i<distances.size(); i++) {
      shapeDistances.push_back(ShapeDistance(distances[i], shape));
    }
  }

  // sort vector
#ifdef C_CODE
  std::sort(shapeDistances.begin(),
            shapeDistances.end(),
            sortShapeDistanceObject);
#endif
#ifdef JAVA_CODE
  java.util.Collections.sort(shapeDistances,
                             new java.util.Comparator<ShapeDistance>() {
                               @Override
                               public int compare(final ShapeDistance sd1,
                                                  final ShapeDistance sd2) {
                                 return Double.compare(sd1._distance, sd2._distance);
                               }
                             });
#endif

  return shapeDistances;
}


bool ShapesRenderer::onTouchEvent(const G3MEventContext* ec,
                                  const TouchEvent* touchEvent) {
  if (_lastCamera != NULL) {
    if ((touchEvent->getTouchCount() == 1) &&
        (touchEvent->getTapCount() == 1) &&
        (touchEvent->getType() == Down)) {
      const Vector3D origin    = _lastCamera->getCartesianPosition();
      const Vector2F pixel     = touchEvent->getTouch(0)->getPos();
      const Vector3D direction = _lastCamera->pixel2Ray(pixel);

      if (!direction.isNan()) {
        const Planet* planet = ec->getPlanet();

        const std::vector<ShapeDistance> shapeDistances = intersectionsDistances(planet, origin, direction);

        if (!shapeDistances.empty()) {
          const ShapeDistance selectedShape = shapeDistances[0];
          return selectedShape._shape->touched(ec);
        }
      }
    }
  }
  
  return false;
}

void ShapesRenderer::drainLoadQueue() {

  const size_t loadQueueSize = _loadQueue.size();
  for (size_t i = 0; i < loadQueueSize; i++) {
    LoadQueueItem* item = _loadQueue[i];
    requestBuffer(item->_url,
                  item->_priority,
                  item->_timeToCache,
                  item->_readExpired,
                  item->_uriPrefix,
                  item->_isTransparent,
                  item->_parameters,
                  item->_position,
                  item->_altitudeMode,
                  item->_listener,
                  item->_deleteListener,
                  item->_isBSON);

    delete item;
  }

  _loadQueue.clear();
}

void ShapesRenderer::cleanLoadQueue() {
  const size_t loadQueueSize = _loadQueue.size();
  for (size_t i = 0; i < loadQueueSize; i++) {
    LoadQueueItem* item = _loadQueue[i];
    delete item;
  }
  _loadQueue.clear();
}

void ShapesRenderer::onChangedContext() {
  if (_context != NULL) {
    const size_t shapesCount = _shapes.size();
    for (size_t i = 0; i < shapesCount; i++) {
      Shape* shape = _shapes[i];
      shape->initialize(_context);
    }

    drainLoadQueue();
  }
}

void ShapesRenderer::onLostContext() {
  if (_context == NULL) {
    cleanLoadQueue();
  }
}



void ShapesRenderer::loadJSONSceneJS(const URL&                     url,
                                     long long                      priority,
                                     const TimeInterval&            timeToCache,
                                     bool                           readExpired,
                                     const std::string&             uriPrefix,
                                     bool                           isTransparent,
                                     const SceneJSParserParameters& parameters,
                                     const Geodetic3D&              position,
                                     AltitudeMode                   altitudeMode,
                                     ShapeLoadListener*             listener,
                                     bool                           deleteListener) {
  if (_context == NULL) {
    _loadQueue.push_back(new LoadQueueItem(url,
                                           priority,
                                           timeToCache,
                                           readExpired,
                                           uriPrefix,
                                           isTransparent,
                                           parameters,
                                           position,
                                           altitudeMode,
                                           listener,
                                           deleteListener,
                                           false /* isBson */));
  }
  else {
    requestBuffer(url,
                  priority,
                  timeToCache,
                  readExpired,
                  uriPrefix,
                  isTransparent,
                  parameters,
                  position,
                  altitudeMode,
                  listener,
                  deleteListener,
                  false /* isBson */);
  }
}


void ShapesRenderer::loadBSONSceneJS(const URL&                     url,
                                     long long                      priority,
                                     const TimeInterval&            timeToCache,
                                     bool                           readExpired,
                                     const std::string&             uriPrefix,
                                     bool                           isTransparent,
                                     const SceneJSParserParameters& parameters,
                                     const Geodetic3D&              position,
                                     AltitudeMode                   altitudeMode,
                                     ShapeLoadListener*             listener,
                                     bool                           deleteListener) {
  if (_context == NULL) {
    _loadQueue.push_back(new LoadQueueItem(url,
                                           priority,
                                           timeToCache,
                                           readExpired,
                                           uriPrefix,
                                           isTransparent,
                                           parameters,
                                           position,
                                           altitudeMode,
                                           listener,
                                           deleteListener,
                                           true /* isBson */));
  }
  else {
    requestBuffer(url,
                  priority,
                  timeToCache,
                  readExpired,
                  uriPrefix,
                  isTransparent,
                  parameters,
                  position,
                  altitudeMode,
                  listener,
                  deleteListener,
                  true /* isBson */);
  }
}

class ShapesRenderer_SceneJSParserAsyncTask : public GAsyncTask {
private:
  ShapesRenderer*    _shapesRenderer;
  const URL                     _url;
#ifdef C_CODE
  const SceneJSParserParameters _parameters;
#endif
#ifdef JAVA_CODE
  private final SceneJSParserParameters _parameters;
#endif
  IByteBuffer*       _buffer;
  const std::string  _uriPrefix;
  const bool         _isTransparent;
  const Geodetic3D   _position;
  AltitudeMode       _altitudeMode;
  ShapeLoadListener* _listener;
  const bool         _deleteListener;
  const bool         _isBSON;

  SGShape* _sgShape;

public:
  ShapesRenderer_SceneJSParserAsyncTask(ShapesRenderer*                shapesRenderer,
                                        const URL&                     url,
                                        IByteBuffer*                   buffer,
                                        const std::string&             uriPrefix,
                                        bool                           isTransparent,
                                        const SceneJSParserParameters& parameters,
                                        const Geodetic3D&              position,
                                        AltitudeMode                   altitudeMode,
                                        ShapeLoadListener*             listener,
                                        bool                           deleteListener,
                                        bool                           isBSON) :
  _shapesRenderer(shapesRenderer),
  _url(url),
  _buffer(buffer),
  _uriPrefix(uriPrefix),
  _isTransparent(isTransparent),
  _parameters(parameters),
  _position(position),
  _altitudeMode(altitudeMode),
  _listener(listener),
  _deleteListener(deleteListener),
  _isBSON(isBSON),
  _sgShape(NULL)
  {
  }

  void runInBackground(const G3MContext* context) {
    if (_isBSON) {
      _sgShape = SceneJSShapesParser::parseFromBSON(_buffer,
                                                    _uriPrefix,
                                                    _isTransparent,
                                                    _parameters,
                                                    _position,
                                                    _altitudeMode);
    }
    else {
      _sgShape = SceneJSShapesParser::parseFromJSON(_buffer,
                                                    _uriPrefix,
                                                    _isTransparent,
                                                    _parameters,
                                                    _position,
                                                    _altitudeMode);
    }

    delete _buffer;
    _buffer = NULL;
  }

  ~ShapesRenderer_SceneJSParserAsyncTask() {
    if (_deleteListener) {
      delete _listener;
    }
    delete _buffer;
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void onPostExecute(const G3MContext* context) {
    if (_sgShape == NULL) {
      ILogger::instance()->logError("Error parsing SceneJS from \"%s\"", _url._path.c_str());
    }
    else {
      if (_listener != NULL) {
        _listener->onBeforeAddShape(_sgShape);
      }

      ILogger::instance()->logInfo("Adding SGShape to _shapesRenderer");
      _shapesRenderer->addShape(_sgShape);

      if (_listener != NULL) {
        _listener->onAfterAddShape(_sgShape);
      }

      _sgShape = NULL;
    }
  }

};

class ShapesRenderer_SceneJSBufferDownloadListener : public IBufferDownloadListener {
private:
  ShapesRenderer*               _shapesRenderer;
  const std::string             _uriPrefix;
  const bool                    _isTransparent;
#ifdef C_CODE
  const SceneJSParserParameters _parameters;
#endif
#ifdef JAVA_CODE
  private final SceneJSParserParameters _parameters;
#endif
  const Geodetic3D              _position;
  AltitudeMode                  _altitudeMode;
  ShapeLoadListener*            _listener;
  bool                          _deleteListener;
  const IThreadUtils*           _threadUtils;
  bool                          _isBSON;

public:

  ShapesRenderer_SceneJSBufferDownloadListener(ShapesRenderer*                shapesRenderer,
                                               const std::string&             uriPrefix,
                                               bool                           isTransparent,
                                               const SceneJSParserParameters& parameters,
                                               const Geodetic3D&              position,
                                               AltitudeMode                   altitudeMode,
                                               ShapeLoadListener*             listener,
                                               bool                           deleteListener,
                                               const IThreadUtils*            threadUtils,
                                               bool                           isBSON) :
  _shapesRenderer(shapesRenderer),
  _uriPrefix(uriPrefix),
  _isTransparent(isTransparent),
  _parameters(parameters),
  _position(position),
  _altitudeMode(altitudeMode),
  _listener(listener),
  _deleteListener(deleteListener),
  _threadUtils(threadUtils),
  _isBSON(isBSON)
  {
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    ILogger::instance()->logInfo("Downloaded SceneJS buffer from \"%s\" (%db)",
                                 url._path.c_str(),
                                 buffer->size());

    _threadUtils->invokeAsyncTask(new ShapesRenderer_SceneJSParserAsyncTask(_shapesRenderer,
                                                                            url,
                                                                            buffer,
                                                                            _uriPrefix,
                                                                            _isTransparent,
                                                                            _parameters,
                                                                            _position,
                                                                            _altitudeMode,
                                                                            _listener,
                                                                            _deleteListener,
                                                                            _isBSON),
                                  true);

  }

  void onError(const URL& url) {
    ILogger::instance()->logError("Error downloading \"%s\"", url._path.c_str());

    if (_deleteListener) {
      delete _listener;
    }
  }

  void onCancel(const URL& url) {
    ILogger::instance()->logInfo("Canceled download of \"%s\"", url._path.c_str());

    if (_deleteListener) {
      delete _listener;
    }
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
    // do nothing
  }
};

void ShapesRenderer::requestBuffer(const URL&                     url,
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
                                   bool                           isBSON) {

  IDownloader* downloader = _context->getDownloader();
  downloader->requestBuffer(url,
                            priority,
                            timeToCache,
                            readExpired,
                            new ShapesRenderer_SceneJSBufferDownloadListener(this,
                                                                             uriPrefix,
                                                                             isTransparent,
                                                                             parameters,
                                                                             position,
                                                                             altitudeMode,
                                                                             listener,
                                                                             deleteListener,
                                                                             _context->getThreadUtils(),
                                                                             isBSON),
                            true);
  
}

void ShapesRenderer::enableAll() {
  const size_t shapesCount = _shapes.size();
  for (size_t i = 0; i < shapesCount; i++) {
    Shape* shape = _shapes[i];
    shape->setEnable(true);
  }
}

void ShapesRenderer::disableAll() {
  const size_t shapesCount = _shapes.size();
  for (size_t i = 0; i < shapesCount; i++) {
    Shape* shape = _shapes[i];
    shape->setEnable(false);
  }
}

size_t ShapesRenderer::removeAllShapes(const ShapeFilter& filter,
                                       bool deleteShapes) {
  size_t removed = 0;
  const size_t shapesSize = _shapes.size();

  std::vector<Shape*> surviving;
  for (size_t i = 0; i < shapesSize; i++) {
    Shape* shape = _shapes[i];
    if (filter.test(shape)) {
      if (deleteShapes) {
        delete shape;
      }
      removed++;
    }
    else {
      surviving.push_back(shape);
    }
  }

  if (removed > 0) {
    _shapes = surviving;
  }

  return removed;
}
