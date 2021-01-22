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


XPCRenderer::XPCRenderer() :
_cloudsSize(0),
_glState(new GLState()),
_timer(NULL),
_lastCamera(NULL),
_renderDebug(false)
{
}


XPCRenderer::~XPCRenderer() {
  for (int i = 0; i < _cloudsSize; i++) {
    XPCPointCloud* cloud = _clouds[i];
    cloud->_release();
  }
  
  _glState->_release();
  delete _timer;
  
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
                                float verticalExaggeration,
                                float deltaHeight,
                                XPCMetadataListener* metadataListener,
                                bool deleteMetadataListener,
                                bool verbose) {
  
  XPCPointCloud* pointCloud = new XPCPointCloud(serverURL,
                                                cloudName,
                                                downloadPriority, timeToCache, readExpired,
                                                pointColorizer, deletePointColorizer,
                                                minProjectedArea,
                                                pointSize, dynamicPointSize,
                                                verticalExaggeration, deltaHeight,
                                                metadataListener, deleteMetadataListener,
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
    const long long nowInMS = _timer->elapsedTimeInMilliseconds();

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
                    _renderDebug);
    }
  }
}



bool XPCRenderer::onTouchEvent(const G3MEventContext* ec,
                               const TouchEvent* touchEvent) {
  _renderDebug = false;

  if (_cloudsSize > 0) {
    if (_lastCamera != NULL) {
      if ((touchEvent->getTouchCount() == 1) &&
          (touchEvent->getTapCount()   == 1) &&
          (touchEvent->getType()       == LongPress /*Down*/)) {

        const Vector2F touchedPixel = touchEvent->getTouch(0)->getPos();
        const Vector3D rayDirection = _lastCamera->pixel2Ray(touchedPixel);

        if (!rayDirection.isNan()) {
//          _renderDebug = true;

          const Vector3D rayOrigin = _lastCamera->getCartesianPosition();

          const Ray ray = Ray(rayOrigin,
                              rayDirection);

          //const Planet* planet = ec->getPlanet();

          for (int i = 0; i < _cloudsSize; i++) {
            XPCPointCloud* cloud = _clouds[i];
            if (cloud->touchesRay(ray)) {
              _renderDebug = true;
              return true;
            }
          }

//          std::vector<ShapeDistance> shapeDistances = intersectionsDistances(planet, origin, direction);
//
//          if (!shapeDistances.empty()) {
//            //        printf ("Found %d intersections with shapes:\n",
//            //                (int)shapeDistances.size());
//            for (int i=0; i<shapeDistances.size(); i++) {
//              //            printf ("   %d: shape %x to distance %f\n",
//              //                    i+1,
//              //                    (unsigned int)shapeDistances[i]._shape,
//              //                    shapeDistances[i]._distance);
//            }
//          }
        }

      }
    }
  }

  return false;
}
