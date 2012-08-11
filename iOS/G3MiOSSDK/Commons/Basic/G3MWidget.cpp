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
#include "Downloader.hpp"
#include "IDownloader.hpp"
#include "Effects.hpp"
#include "Context.hpp"
#include "CameraConstraints.hpp"

G3MWidget::G3MWidget(IFactory*         factory,
                     ILogger*          logger,
                     GL*               gl,
                     TexturesHandler*  texturesHandler,
                     Downloader*       downloaderOLD,
                     IDownloader*      downloader,
                     const Planet*     planet,
                     std::vector<ICameraConstrainer *> cameraConstraint,
                     Renderer*         renderer,
                     Renderer*         busyRenderer,
                     EffectsScheduler* scheduler,
                     int               width,
                     int               height,
                     Color             backgroundColor,
                     const bool        logFPS):
_factory(factory),
_logger(logger),
_gl(gl),
_texturesHandler(texturesHandler),
_planet(planet),
_cameraConstraint(cameraConstraint),
_renderer(renderer),
_busyRenderer(busyRenderer),
_scheduler(scheduler),
_currentCamera(new Camera(width, height)),
_nextCamera(new Camera(width, height)),
_backgroundColor(backgroundColor),
_timer(factory->createTimer()),
_renderCounter(0),
_totalRenderTime(0),
_logFPS(logFPS),
_downloaderOLD(downloaderOLD),
_downloader(downloader),
_rendererReady(false) // false until first call to G3MWidget::render()
{
  initializeGL();
  
  InitializationContext ic(_factory, _logger, _planet, _downloaderOLD, _downloader, _scheduler);
  _scheduler->initialize(&ic);
  _renderer->initialize(&ic);
  _busyRenderer->initialize(&ic);
  _currentCamera->initialize(&ic);
  _nextCamera->initialize(&ic);
  
  _downloader->start();
}


G3MWidget* G3MWidget::create(IFactory*         factory,
                             ILogger*          logger,
                             GL*               gl,
                             TexturesHandler*  texturesHandler,
                             Downloader *      downloaderOLD,
                             IDownloader*      downloader,
                             const Planet*     planet,
                             std::vector<ICameraConstrainer *> cameraConstraint,
                             Renderer*         renderer,
                             Renderer*         busyRenderer,
                             EffectsScheduler* scheduler,
                             int               width,
                             int               height,
                             Color             backgroundColor,
                             const bool        logFPS) {
  if (logger != NULL) {
    logger->logInfo("Creating G3MWidget...");
  }
  
  ILogger::setInstance(logger);
  
  return new G3MWidget(factory,
                       logger,
                       gl,
                       texturesHandler,
                       downloaderOLD,
                       downloader,
                       planet,
                       cameraConstraint,
                       renderer,
                       busyRenderer,
                       scheduler,
                       width, height,
                       backgroundColor,
                       logFPS);
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
  delete _scheduler;
  delete _currentCamera;
  delete _nextCamera;
  delete _texturesHandler;
  delete _timer;
  delete _downloaderOLD;
  delete _downloader;
  
  for (unsigned int n=0; n<_cameraConstraint.size(); n++)
    delete _cameraConstraint[n];
}

void G3MWidget::onTouchEvent(const TouchEvent* myEvent) {
  if (_rendererReady) {
    EventContext ec(_factory, _logger, _planet, _downloaderOLD, _downloader, _scheduler);
    
    _renderer->onTouchEvent(&ec, myEvent);
  }
}

void G3MWidget::onResizeViewportEvent(int width, int height) {
  if (_rendererReady) {
    EventContext ec(_factory, _logger, _planet, _downloaderOLD, _downloader, _scheduler);
    
    _renderer->onResizeViewportEvent(&ec, width, height);
  }
}

int G3MWidget::render() {
  _timer->start();
  _renderCounter++;
  
  // copy next camera to current camera
  bool acceptCamera = true;
  for (unsigned int n=0; n<_cameraConstraint.size(); n++) {
    if (!_cameraConstraint[n]->acceptsCamera(_nextCamera, _planet)) {
      acceptCamera = false;
    }
  }
  if (acceptCamera) {
    _currentCamera->copyFrom(*_nextCamera);
  }
  else {
    _nextCamera->copyFrom(*_currentCamera);
  }
  
  // create RenderContext
  RenderContext rc(_factory,
                   _logger,
                   _planet,
                   _gl,
                   _currentCamera,
                   _nextCamera,
                   _texturesHandler,
                   _downloaderOLD,
                   _downloader,
                   _scheduler,
                   _factory->createTimer());

  _scheduler->doOneCyle(&rc);

  _rendererReady = _renderer->isReadyToRender(&rc);
  Renderer* selectedRenderer = _rendererReady ? _renderer : _busyRenderer;

  // Clear the scene
  _gl->clearScreen(_backgroundColor);
  
  const int timeToRedraw = selectedRenderer->render(&rc);
  
  const TimeInterval elapsedTime = _timer->elapsedTime();
  if (elapsedTime.milliseconds() > 100) {
    _logger->logWarning("Frame took too much time: %dms" , elapsedTime.milliseconds());
  }
  _totalRenderTime += elapsedTime.milliseconds();
  
  if ((_renderCounter % 60) == 0) {
    if (_logFPS) {
      const double averageTimePerRender = (double) _totalRenderTime / _renderCounter;
      const double fps = 1000.0 / averageTimePerRender;
      _logger->logInfo("FPS=%f" , fps);
    }
    
    _renderCounter = 0;
    _totalRenderTime = 0;
  }
  
  return timeToRedraw;

}
