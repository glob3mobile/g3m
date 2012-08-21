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
#include "TexturesHandler.cpp"
#include "IDownloader.hpp"
#include "Effects.hpp"
#include "Context.hpp"
#include "CameraConstraints.hpp"
#include "FrameTasksExecutor.hpp"


G3MWidget::G3MWidget(FrameTasksExecutor* frameTasksExecutor,
                     IFactory*         factory,
                     ILogger*          logger,
                     GL*               gl,
                     TexturesHandler*  texturesHandler,
                     IDownloader*      downloader,
                     const Planet*     planet,
                     std::vector<ICameraConstrainer *> cameraConstraint,
                     Renderer*         renderer,
                     Renderer*         busyRenderer,
                     EffectsScheduler* effectsScheduler,
                     int               width,
                     int               height,
                     Color             backgroundColor,
                     const bool        logFPS,
                     const bool        logDownloaderStatistics):
_frameTasksExecutor(frameTasksExecutor),
_factory(factory),
_logger(logger),
_gl(gl),
_texturesHandler(texturesHandler),
_planet(planet),
_cameraConstraint(cameraConstraint),
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
_logDownloaderStatistics(logDownloaderStatistics)
{
  initializeGL();
  
  InitializationContext ic(_factory, _logger, _planet, _downloader, _effectsScheduler);
  _effectsScheduler->initialize(&ic);
  _renderer->initialize(&ic);
  _busyRenderer->initialize(&ic);
  _currentCamera->initialize(&ic);
  _nextCamera->initialize(&ic);
  
  _downloader->start();
}


G3MWidget* G3MWidget::create(FrameTasksExecutor* frameTasksExecutor,
                             IFactory*           factory,
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
  
  ILogger::setInstance(logger);
  
  return new G3MWidget(frameTasksExecutor,
                       factory,
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
  _gl.enableCullFace(CullFace.Back);
#endif
}

G3MWidget::~G3MWidget() { 
  delete _factory;
  delete _logger;
  delete _gl;
  delete _planet;
  delete _renderer;
  delete _busyRenderer;
  delete _effectsScheduler;
  delete _currentCamera;
  delete _nextCamera;
  delete _texturesHandler;
  delete _timer;
  delete _downloader;
  
  for (unsigned int n=0; n<_cameraConstraint.size(); n++) {
    delete _cameraConstraint[n];
  }
  
  delete _frameTasksExecutor;
}

void G3MWidget::onTouchEvent(const TouchEvent* myEvent) {
  if (_rendererReady) {
    EventContext ec(_factory, _logger, _planet, _downloader, _effectsScheduler);
    
    _renderer->onTouchEvent(&ec, myEvent);
  }
}

void G3MWidget::onResizeViewportEvent(int width, int height) {
  if (_rendererReady) {
    EventContext ec(_factory, _logger, _planet, _downloader, _effectsScheduler);
    
    _renderer->onResizeViewportEvent(&ec, width, height);
  }
}

int G3MWidget::render() {
  _timer->start();
  _renderCounter++;
  
  
  // copy next camera to current camera
  bool acceptedCamera = true;
  for (int n = 0; n < _cameraConstraint.size(); n++) {
    if (!_cameraConstraint[n]->acceptsCamera(_nextCamera, _planet)) {
      acceptedCamera = false;
      break;
    }
  }
  if (acceptedCamera) {
    _currentCamera->copyFrom(*_nextCamera);
  }
  else {
    _nextCamera->copyFrom(*_currentCamera);
  }
  
//  int __removePrint;
//  printf("Camera Position=%s\n" ,
//         _planet->toGeodetic3D(_currentCamera->getPosition()).description().c_str());
  
  // create RenderContext
  RenderContext rc(_frameTasksExecutor,
                   _factory,
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
  

  // Clear the scene
  _gl->clearScreen(_backgroundColor);
  
  const int timeToRedraw = _selectedRenderer->render(&rc);
  
//  _frameTasksExecutor->doPostRenderCycle(&rc);
  
  const TimeInterval elapsedTime = _timer->elapsedTime();
  if (elapsedTime.milliseconds() > 100) {
    _logger->logWarning("Frame took too much time: %dms" , elapsedTime.milliseconds());
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
    const std::string cacheStatistics = _downloader->statistics();
    
    if (cacheStatistics != _lastCacheStatistics) {
      _logger->logInfo("%s" , cacheStatistics.c_str());
      _lastCacheStatistics = cacheStatistics;
    }
  }
  
  return timeToRedraw;

}
