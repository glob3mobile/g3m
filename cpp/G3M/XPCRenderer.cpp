//
//  XPCRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#include "XPCRenderer.hpp"

#include "XPCPointColorizer.hpp"
#include "XPCMetadataListener.hpp"
#include "XPCPointCloud.hpp"

#include "GLState.hpp"
#include "ITimer.hpp"
#include "G3MRenderContext.hpp"
#include "IFactory.hpp"
#include "Camera.hpp"
#include "ILogger.hpp"
#include "IDownloader.hpp"
#include "TouchEvent.hpp"
#include "G3MEventContext.hpp"
#include "Ray.hpp"
#include "XPCSelectionResult.hpp"


XPCRenderer::XPCRenderer() :
_cloudsSize(0),
_glState(new GLState()),
_timer(NULL),
_lastCamera(NULL),
_renderDebug(false),
_selectionResult(NULL)
{
}


XPCRenderer::~XPCRenderer() {
  for (int i = 0; i < _cloudsSize; i++) {
    XPCPointCloud* cloud = _clouds[i];
    cloud->_release();
  }
  
  _glState->_release();
  delete _timer;

  delete _selectionResult;
  
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void XPCRenderer::removeAllPointClouds() {
  for (int i = 0; i < _cloudsSize; i++) {
    XPCPointCloud* cloud = _clouds[i];
    cloud->_release();
  }
  _clouds.clear();
  _cloudsSize = _clouds.size();

  delete _selectionResult;
  _selectionResult = NULL;
}


void XPCRenderer::addPointCloud(const URL& serverURL,
                                const std::string& cloudName,
                                long long downloadPriority,
                                const TimeInterval& timeToCache,
                                bool readExpired,
                                XPCPointColorizer* pointColorizer,
                                bool deletePointColorizer,
                                const double minProjectedArea,
                                float pointSize,
                                bool dynamicPointSize,
                                const bool depthTest,
                                float verticalExaggeration,
                                float deltaHeight,
                                XPCMetadataListener* metadataListener,
                                bool deleteMetadataListener,
                                XPCPointSelectionListener* pointSelectionListener,
                                bool deletePointSelectionListener,
                                bool verbose) {
  
  XPCPointCloud* pointCloud = new XPCPointCloud(serverURL,
                                                cloudName,
                                                downloadPriority, timeToCache, readExpired,
                                                pointColorizer, deletePointColorizer,
                                                minProjectedArea,
                                                pointSize, dynamicPointSize, depthTest,
                                                verticalExaggeration, deltaHeight,
                                                metadataListener, deleteMetadataListener,
                                                pointSelectionListener, deletePointSelectionListener,
                                                verbose);
  
  if (_context != NULL) {
    pointCloud->initialize(_context);
  }
  _clouds.push_back(pointCloud);
  _cloudsSize = _clouds.size();
}


void XPCRenderer::onChangedContext() {
  for (int i = 0; i < _cloudsSize; i++) {
    XPCPointCloud* cloud = _clouds[i];
    cloud->initialize(_context);
  }
}


RenderState XPCRenderer::getRenderState(const G3MRenderContext* rc) {
  _errors.clear();
  bool busyFlag  = false;
  bool errorFlag = false;

  for (int i = 0; i < _cloudsSize; i++) {
    XPCPointCloud* cloud = _clouds[i];
    const RenderState childRenderState = cloud->getRenderState(rc);

    const RenderState_Type childRenderStateType = childRenderState._type;

    if (childRenderStateType == RENDER_ERROR) {
      errorFlag = true;

      const std::vector<std::string> childErrors = childRenderState.getErrors();
#ifdef C_CODE
      _errors.insert(_errors.end(),
                     childErrors.begin(),
                     childErrors.end());
#endif
#ifdef JAVA_CODE
      _errors.addAll(childErrors);
#endif
    }
    else if (childRenderStateType == RENDER_BUSY) {
      busyFlag = true;
    }
  }

  if (errorFlag) {
    return RenderState::error(_errors);
  }
  else if (busyFlag) {
    return RenderState::busy();
  }
  else {
    return RenderState::ready();
  }
}

void XPCRenderer::render(const G3MRenderContext* rc,
                         GLState* glState) {
  if (_cloudsSize > 0) {
    _lastCamera = rc->getCurrentCamera();

    if (_timer == NULL) {
      _timer = rc->getFactory()->createTimer();
    }
    const long long nowInMS = _timer->nowInMilliseconds();

    const Camera* camera = rc->getCurrentCamera();
    ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
    if (f == NULL) {
      _glState->addGLFeature(new ModelViewGLFeature(camera), true);
    }
    else {
      f->setMatrix(camera->getModelViewMatrix44D());
    }

    _glState->setParent(glState);

    const Frustum* frustum = camera->getFrustumInModelCoordinates();
    for (int i = 0; i < _cloudsSize; i++) {
      XPCPointCloud* cloud = _clouds[i];
      cloud->render(rc,
                    _glState,
                    frustum,
                    nowInMS,
                    _renderDebug,
                    _selectionResult);
    }

//    if (_selectionResult != NULL) {
//      _selectionResult->render(rc, _glState);
//    }
  }
}

bool XPCRenderer::onTouchEvent(const G3MEventContext* ec,
                               const TouchEvent* touchEvent) {
  _renderDebug = false;

  if (_cloudsSize > 0) {
    if (_lastCamera != NULL) {
      if (touchEvent->getType() == LongPress) {
        const Vector2F touchedPixel = touchEvent->getTouch(0)->getPos();
        const Vector3D rayDirection = _lastCamera->pixel2Ray(touchedPixel);

        if (!rayDirection.isNan()) {
          // _renderDebug = true;

          const Vector3D rayOrigin = _lastCamera->getCartesianPosition();

          XPCSelectionResult* selectionResult = new XPCSelectionResult(new Ray(rayOrigin, rayDirection));

          bool selectedPoints = false;
          for (size_t i = 0; i < _cloudsSize; i++) {
            XPCPointCloud* cloud = _clouds[i];
            if (cloud->selectPoints(selectionResult)) {
              selectedPoints = true;
            }
          }

          if (selectedPoints) {
            delete _selectionResult;
            _selectionResult = selectionResult;

            return _selectionResult->notifyPointCloud(ec->getPlanet());
          }

          delete selectionResult;
          selectionResult = NULL;
        }
      }
    }
  }

  if (touchEvent->getType() == LongPress) {
    delete _selectionResult;
    _selectionResult = NULL;
  }

  //  if (!_renderDebug) {
  //    delete _ray;
  //    _ray = NULL;
  //  }

  return false;
}
