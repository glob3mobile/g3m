//
//  G3MWidget.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "G3MWidget.hpp"

#include "ITimer.hpp"
#include "Renderer.hpp"
#include "Camera.hpp"
#include "GL.hpp"
#include "TexturesHandler.hpp"
#include "IDownloader.hpp"
#include "Effects.hpp"
#include "Context.hpp"
#include "CameraConstraints.hpp"
#include "FrameTasksExecutor.hpp"
#include "IStringUtils.hpp"
#include "IThreadUtils.hpp"

G3MWidget::G3MWidget(FrameTasksExecutor*              frameTasksExecutor,
                     IFactory*                        factory,
                     const IStringUtils*              stringUtils,
                     IThreadUtils*                    threadUtils,
                     ILogger*                         logger,
                     GL*                              gl,
                     TexturesHandler*                 texturesHandler,
                     IDownloader*                     downloader,
                     const Planet*                    planet,
                     std::vector<ICameraConstrainer*> cameraConstrainers,
                     Renderer*                        renderer,
                     Renderer*                        busyRenderer,
                     EffectsScheduler*                effectsScheduler,
                     int                              width,
                     int                              height,
                     Color                            backgroundColor,
                     const bool                       logFPS,
                     const bool                       logDownloaderStatistics):
_frameTasksExecutor(frameTasksExecutor),
_factory(factory),
_stringUtils(stringUtils),
_threadUtils(threadUtils),
_logger(logger),
_gl(gl),
_texturesHandler(texturesHandler),
_planet(planet),
_cameraConstrainers(cameraConstrainers),
_renderer(renderer),
_busyRenderer(busyRenderer),
_effectsScheduler(effectsScheduler),
_currentCamera(new Camera(width, height)),
_nextCamera(new Camera(width, height)),
_backgroundColor(backgroundColor),
_timer(factory->createTimer()),
_renderCounter(0),
_totalRenderTime(0),
_logFPS(logFPS),
_downloader(downloader),
_rendererReady(false), // false until first call to G3MWidget::render()
_selectedRenderer(NULL),
_renderStatisticsTimer(NULL),
_logDownloaderStatistics(logDownloaderStatistics),
_userData(NULL)
{
  initializeGL();
  
  InitializationContext ic(_factory,
                           _stringUtils,
                           _threadUtils,
                           _logger,
                           _planet,
                           _downloader,
                           _effectsScheduler);
  
  _effectsScheduler->initialize(&ic);
  _renderer->initialize(&ic);
  _busyRenderer->initialize(&ic);
  _currentCamera->initialize(&ic);
  _nextCamera->initialize(&ic);
  
  if (_downloader != NULL){
    _downloader->start();
  }
}


G3MWidget* G3MWidget::create(FrameTasksExecutor* frameTasksExecutor,
                             IFactory*           factory,
                             const IStringUtils* stringUtils,
                             IThreadUtils*       threadUtils,
                             ILogger*            logger,
                             GL*                 gl,
                             TexturesHandler*    texturesHandler,
                             IDownloader*        downloader,
                             const Planet*       planet,
                             std::vector<ICameraConstrainer*> cameraConstraint,
                             Renderer*           renderer,
                             Renderer*           busyRenderer,
                             EffectsScheduler*   scheduler,
                             int                 width,
                             int                 height,
                             Color               backgroundColor,
                             const bool          logFPS,
                             const bool          logDownloaderStatistics) {
  if (logger != NULL) {
    logger->logInfo("Creating G3MWidget...");
  }
  
  IStringUtils::setInstance(stringUtils);
  ILogger::setInstance(logger);
  IThreadUtils::setInstance(threadUtils);
  
  return new G3MWidget(frameTasksExecutor,
                       factory,
                       stringUtils,
                       threadUtils,
                       logger,
                       gl,
                       texturesHandler,
                       downloader,
                       planet,
                       cameraConstraint,
                       renderer,
                       busyRenderer,
                       scheduler,
                       width, height,
                       backgroundColor,
                       logFPS,
                       logDownloaderStatistics);
}

void G3MWidget::initializeGL() {
  _gl->enableDepthTest();
#ifdef C_CODE
  _gl->enableCullFace(Back);
#endif
#ifdef JAVA_CODE
  _gl.enableCullFace(GLCullFace.Back);
#endif
}

G3MWidget::~G3MWidget() {
  if (_userData != NULL) {
    delete _userData;
  }
  
  delete _factory;
  delete _logger;
  delete _gl;
#ifdef C_CODE
  delete _planet;
#endif
  delete _renderer;
  delete _busyRenderer;
  delete _effectsScheduler;
  delete _currentCamera;
  delete _nextCamera;
  delete _texturesHandler;
  delete _timer;

  if (_downloader != NULL) {
    _downloader->stop();
#ifdef C_CODE
    delete _downloader;
#endif
  }
  
#ifdef C_CODE
  for (unsigned int n=0; n<_cameraConstrainers.size(); n++)
    delete _cameraConstrainers[n];
#endif
  delete _frameTasksExecutor;
}

void G3MWidget::onTouchEvent(const TouchEvent* myEvent) {
  if (_rendererReady) {
    EventContext ec(_factory,
                    _stringUtils,
                    _threadUtils,
                    _logger,
                    _planet,
                    _downloader,
                    _effectsScheduler);
    
    _renderer->onTouchEvent(&ec, myEvent);
  }
}

void G3MWidget::onResizeViewportEvent(int width, int height) {
  if (_rendererReady) {
    EventContext ec(_factory,
                    _stringUtils,
                    _threadUtils,
                    _logger,
                    _planet,
                    _downloader,
                    _effectsScheduler);
    
    _renderer->onResizeViewportEvent(&ec, width, height);
  }
}

//const double clamp(const double value,
//                   const double lower,
//                   const double upper) {
//  if (value < lower) {
//    return lower;
//  }
//  if (value > upper) {
//    return upper;
//  }
//  return value;
//}

void G3MWidget::render() {
  _timer->start();
  _renderCounter++;
  
  // give to the CameraContrainers the opportunity to change the nextCamera
  for (int i = 0; i< _cameraConstrainers.size(); i++) {
    ICameraConstrainer* constrainer =  _cameraConstrainers[i];
    constrainer->onCameraChange(_planet,
                                _currentCamera,
                                _nextCamera);
  }
  _currentCamera->copyFrom(*_nextCamera);
  
  //  int __removePrint;
  //  printf("Camera Position=%s\n" ,
  //         _planet->toGeodetic3D(_currentCamera->getCartesianPosition()).description().c_str());
  
  // create RenderContext
  RenderContext rc(_frameTasksExecutor,
                   _factory,
                   _stringUtils,
                   _threadUtils,
                   _logger,
                   _planet,
                   _gl,
                   _currentCamera,
                   _nextCamera,
                   _texturesHandler,
                   _downloader,
                   _effectsScheduler,
                   _factory->createTimer());
  
  _effectsScheduler->doOneCyle(&rc);
  
  _frameTasksExecutor->doPreRenderCycle(&rc);
  
  _rendererReady = _renderer->isReadyToRender(&rc);
  
  Renderer* selectedRenderer = _rendererReady ? _renderer : _busyRenderer;
  if (selectedRenderer != _selectedRenderer) {
    if (_selectedRenderer != NULL) {
      _selectedRenderer->stop();
    }
    _selectedRenderer = selectedRenderer;
    _selectedRenderer->start();
  }
  
//  const Vector3D ray = _currentCamera->getCenter();
//  const Vector3D origin = _currentCamera->getPosition();
//  
//  const Vector3D intersection = _planet->closestIntersection(origin, ray);
//  if (!intersection.isNan()) {
//    const Vector3D cameraPosition = _currentCamera->getPosition();
//    
//    const double minDistance = 1000;
//    const double maxDistance = 20000;
//    
//    const double distanceToTerrain = clamp(intersection.sub(cameraPosition).length(),
//                                           minDistance,
//                                           maxDistance + minDistance) - minDistance;
//    
//    printf("Camera to terrain distance=%f\n", distanceToTerrain);
//    
//    const float factor = (float) (distanceToTerrain / maxDistance);
//    
//    // Clear the scene
//    const Color dayColor = Color::fromRGBA((float) 0.5, (float) 0.5, 1, 1);
//    _gl->clearScreen(_backgroundColor.mixedWith(dayColor, factor));
//    //    _gl->clearScreen(_backgroundColor);
//    
//  }
//  else {
//    // Clear the scene
//    _gl->clearScreen(_backgroundColor);
//  }
  _gl->clearScreen(_backgroundColor);

  _selectedRenderer->render(&rc);
  
  //  _frameTasksExecutor->doPostRenderCycle(&rc);
  
  const TimeInterval elapsedTime = _timer->elapsedTime();
  if (elapsedTime.milliseconds() > 100) {
    _logger->logWarning("Frame took too much time: %dms" ,
                        elapsedTime.milliseconds());
  }
  
  if (_logFPS) {
    _totalRenderTime += elapsedTime.milliseconds();
    
    if ((_renderStatisticsTimer == NULL) ||
        (_renderStatisticsTimer->elapsedTime().seconds() > 2)) {
      const double averageTimePerRender = (double) _totalRenderTime / _renderCounter;
      const double fps = 1000.0 / averageTimePerRender;
      _logger->logInfo("FPS=%f" , fps);
      
      _renderCounter = 0;
      _totalRenderTime = 0;
      
      if (_renderStatisticsTimer == NULL) {
        _renderStatisticsTimer = _factory->createTimer();
      } 
      else {
        _renderStatisticsTimer->start();
      }
    }
  }
  
  if (_logDownloaderStatistics) {
    std::string cacheStatistics = "";
    
    if (_downloader != NULL){
      cacheStatistics = _downloader->statistics();
    }
    
    if (cacheStatistics != _lastCacheStatistics) {
      _logger->logInfo("%s" , cacheStatistics.c_str());
      _lastCacheStatistics = cacheStatistics;
    }
  }
  
}
