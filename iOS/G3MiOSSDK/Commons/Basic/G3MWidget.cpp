//
//  G3MWidget.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "G3MWidget.hpp"

//#include "ITimer.hpp"
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
#include "IStringBuilder.hpp"
#include "IJSONParser.hpp"

#include "GLConstants.hpp"

#include "PeriodicalTask.hpp"

G3MWidget::G3MWidget(FrameTasksExecutor*              frameTasksExecutor,
                     IFactory*                        factory,
                     const IStringUtils*              stringUtils,
                     IThreadUtils*                    threadUtils,
                     ILogger*                         logger,
                     GL*                              gl,
                     TexturesHandler*                 texturesHandler,
                     TextureBuilder*                  textureBuilder,
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
                     const bool                       logDownloaderStatistics,
                     GTask*                           initializationTask,
                     bool                             autoDeleteInitializationTask):
_frameTasksExecutor(frameTasksExecutor),
_factory(factory),
_stringUtils(stringUtils),
_threadUtils(threadUtils),
_logger(logger),
_gl(gl),
_texturesHandler(texturesHandler),
_textureBuilder(textureBuilder),
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
_userData(NULL),
_initializationTask(initializationTask),
_autoDeleteInitializationTask(autoDeleteInitializationTask)
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


G3MWidget* G3MWidget::create(FrameTasksExecutor*              frameTasksExecutor,
                             IFactory*                        factory,
                             const IStringUtils*              stringUtils,
                             IThreadUtils*                    threadUtils,
                             IStringBuilder*                  stringBuilder,
                             IMathUtils*                      mathUtils,
                             IJSONParser*                     jsonParser,
                             ILogger*                         logger,
                             GL*                              gl,
                             TexturesHandler*                 texturesHandler,
                             TextureBuilder*                  textureBuilder,
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
                             const bool                       logDownloaderStatistics,
                             GTask*                           initializationTask,
                             bool                             autoDeleteInitializationTask) {
  if (logger != NULL) {
    logger->logInfo("Creating G3MWidget...");
  }
  
  IFactory::setInstance(factory);
  IStringUtils::setInstance(stringUtils);
  ILogger::setInstance(logger);
  IThreadUtils::setInstance(threadUtils);
  IStringBuilder::setInstance(stringBuilder);
  IMathUtils::setInstance(mathUtils);
  IJSONParser::setInstance(jsonParser);
  
  return new G3MWidget(frameTasksExecutor,
                       factory,
                       stringUtils,
                       threadUtils,
                       logger,
                       gl,
                       texturesHandler,
                       textureBuilder,
                       downloader,
                       planet,
                       cameraConstrainers,
                       renderer,
                       busyRenderer,
                       effectsScheduler,
                       width, height,
                       backgroundColor,
                       logFPS,
                       logDownloaderStatistics,
                       initializationTask,
                       autoDeleteInitializationTask);
}

void G3MWidget::initializeGL() {
  _gl->enableDepthTest();
  
  _gl->enableCullFace(GLCullFace::back());
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
  
#ifdef C_CODE
  for (int i = 0; i < _periodicalTasks.size(); i++){
    _periodicalTasks[i].releaseTask();
  }
#endif
}

void G3MWidget::onTouchEvent(const TouchEvent* myEvent) {
  if (_rendererReady) {
    if (_renderer->isEnable()) {
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
}

void G3MWidget::onResizeViewportEvent(int width, int height) {
  if (_rendererReady) {
    if (_renderer->isEnable()) {
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
}

void G3MWidget::render() {
  _timer->start();
  _renderCounter++;
  
  //Start periodical task
  for (int i = 0; i < _periodicalTasks.size(); i++) {
    PeriodicalTask& pt = _periodicalTasks[i];
    pt.executeIfNecessary();
  }
  
  // give to the CameraContrainers the opportunity to change the nextCamera
  for (int i = 0; i< _cameraConstrainers.size(); i++) {
    ICameraConstrainer* constrainer =  _cameraConstrainers[i];
    constrainer->onCameraChange(_planet,
                                _currentCamera,
                                _nextCamera);
  }
  _currentCamera->copyFrom(*_nextCamera);
  
  
  if (_initializationTask != NULL) {
    _initializationTask->run();
    if (_autoDeleteInitializationTask) {
      delete _initializationTask;
    }
    _initializationTask = NULL;
  }
  
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
                   _textureBuilder,
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
  
  _gl->clearScreen(_backgroundColor);
  
  if (_selectedRenderer->isEnable()) {
    _selectedRenderer->render(&rc);
  }
  
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

void G3MWidget::onPause() {
  InitializationContext ic(_factory,
                           _stringUtils,
                           _threadUtils,
                           _logger,
                           _planet,
                           _downloader,
                           _effectsScheduler);
  
  _renderer->onPause(&ic);
  _busyRenderer->onPause(&ic);
  
  _effectsScheduler->onPause(&ic);
  
  if (_downloader != NULL) {
    _downloader->onPause(&ic);
  }
}

void G3MWidget::onResume() {
  InitializationContext ic(_factory,
                           _stringUtils,
                           _threadUtils,
                           _logger,
                           _planet,
                           _downloader,
                           _effectsScheduler);
  
  _renderer->onResume(&ic);
  _busyRenderer->onResume(&ic);
  
  _effectsScheduler->onResume(&ic);
  
  if (_downloader != NULL) {
    _downloader->onResume(&ic);
  }
}

void G3MWidget::addPeriodicalTasks(const TimeInterval& interval, GTask* task){
  PeriodicalTask pt(interval, task);
  _periodicalTasks.push_back(pt);
}
