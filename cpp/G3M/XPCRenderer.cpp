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
//#include "G3MContext.hpp"
#include "G3MRenderContext.hpp"
#include "IFactory.hpp"
#include "Camera.hpp"
#include "ILogger.hpp"
#include "IDownloader.hpp"



XPCRenderer::XPCRenderer() :
_cloudsSize(0),
_glState(new GLState()),
_timer(NULL)
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

void XPCRenderer::render(const G3MRenderContext* rc,
                         GLState* glState) {
  if (_cloudsSize > 0) {
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
      cloud->render(rc, _glState, frustum, nowInMS);
    }
  }
}


void XPCRenderer::addPointCloud(const URL& serverURL,
                                const std::string& cloudName,
                                long long downloadPriority,
                                const TimeInterval& timeToCache,
                                bool readExpired,
                                const XPCPointColorizer* pointColorizer,
                                bool deletePointColorizer,
                                float pointSize,
                                bool dynamicPointSize,
                                float verticalExaggeration,
                                double deltaHeight,
                                XPCMetadataListener* metadataListener,
                                bool deleteMetadataListener,
                                bool verbose) {
  
  XPCPointCloud* pointCloud = new XPCPointCloud(serverURL,
                                                cloudName,
                                                downloadPriority, timeToCache, readExpired,
                                                pointColorizer, deletePointColorizer,
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
