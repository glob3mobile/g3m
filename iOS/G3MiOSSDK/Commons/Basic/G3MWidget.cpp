//
//  G3MWidget.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "G3MWidget.hpp"

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
#include "GoToPositionEffect.hpp"
#include "CameraRenderer.hpp"
#include "CPUTextureBuilder.hpp"
#include "IStorage.hpp"
#include "OrderedRenderable.hpp"
#include <math.h>

void G3MWidget::initSingletons(ILogger*            logger,
                               IFactory*           factory,
                               const IStringUtils* stringUtils,
                               IStringBuilder*     stringBuilder,
                               IMathUtils*         mathUtils,
                               IJSONParser*        jsonParser) {
  if (ILogger::instance() == NULL) {
    ILogger::setInstance(logger);
    IFactory::setInstance(factory);
    IStringUtils::setInstance(stringUtils);
    IStringBuilder::setInstance(stringBuilder);
    IMathUtils::setInstance(mathUtils);
    IJSONParser::setInstance(jsonParser);
  }
  else {
    ILogger::instance()->logWarning("Singletons already set");
  }
}

G3MWidget::G3MWidget(GL*                              gl,
                     IStorage*                        storage,
                     IDownloader*                     downloader,
                     IThreadUtils*                    threadUtils,
                     const Planet*                    planet,
                     std::vector<ICameraConstrainer*> cameraConstrainers,
                     CameraRenderer*                  cameraRenderer,
                     Renderer*                        mainRenderer,
                     Renderer*                        busyRenderer,
                     int                              width,
                     int                              height,
                     Color                            backgroundColor,
                     const bool                       logFPS,
                     const bool                       logDownloaderStatistics,
                     GTask*                           initializationTask,
                     bool                             autoDeleteInitializationTask,
                     std::vector<PeriodicalTask*>     periodicalTasks):
_rootState(GLState::newDefault()),
_frameTasksExecutor( new FrameTasksExecutor() ),
_effectsScheduler( new EffectsScheduler() ),
_gl(gl),
/*
 =======
_gl( new GL(nativeGL, false) ),
>>>>>>> origin/webgl-port
 */
_downloader(downloader),
_storage(storage),
_threadUtils(threadUtils),
_texturesHandler( new TexturesHandler(_gl, false) ),
_textureBuilder( new CPUTextureBuilder() ),
_planet(planet),
_cameraConstrainers(cameraConstrainers),
_cameraRenderer(cameraRenderer),
_mainRenderer(mainRenderer),
_busyRenderer(busyRenderer),
_currentCamera(new Camera(width, height)),
_nextCamera(new Camera(width, height)),
_backgroundColor(backgroundColor),
_timer(IFactory::instance()->createTimer()),
_renderCounter(0),
_totalRenderTime(0),
_logFPS(logFPS),
_mainRendererReady(false), // false until first call to G3MWidget::render()
_selectedRenderer(NULL),
_renderStatisticsTimer(NULL),
_logDownloaderStatistics(logDownloaderStatistics),
_userData(NULL),
_initializationTask(initializationTask),
_autoDeleteInitializationTask(autoDeleteInitializationTask),
_context(new G3MContext(IFactory::instance(),
                        IStringUtils::instance(),
                        threadUtils,
                        ILogger::instance(),
                        IMathUtils::instance(),
                        IJSONParser::instance(),
                        _planet,
                        downloader,
                        _effectsScheduler,
                        storage)),
_paused(false)
{
  initializeGL();

  _effectsScheduler->initialize(_context);
  _cameraRenderer->initialize(_context);
  _mainRenderer->initialize(_context);
  _busyRenderer->initialize(_context);
  _currentCamera->initialize(_context);
  _nextCamera->initialize(_context);

  if (_threadUtils != NULL) {
    _threadUtils->initialize(_context);
  }

  if (_storage != NULL) {
    _storage->initialize(_context);
  }

  if (_downloader != NULL){
    _downloader->initialize(_context);
    _downloader->start();
  }

  for (int i = 0; i < periodicalTasks.size(); i++) {
    addPeriodicalTask(periodicalTasks[i]);
  }
}


G3MWidget* G3MWidget::create(GL*                              gl,
                             IStorage*                        storage,
                             IDownloader*                     downloader,
                             IThreadUtils*                    threadUtils,
                             const Planet*                    planet,
                             std::vector<ICameraConstrainer*> cameraConstrainers,
                             CameraRenderer*                  cameraRenderer,
                             Renderer*                        mainRenderer,
                             Renderer*                        busyRenderer,
                             int                              width,
                             int                              height,
                             Color                            backgroundColor,
                             const bool                       logFPS,
                             const bool                       logDownloaderStatistics,
                             GTask*                           initializationTask,
                             bool                             autoDeleteInitializationTask,
                             std::vector<PeriodicalTask*>     periodicalTasks) {

  return new G3MWidget(gl,
                       storage,
                       downloader,
                       threadUtils,
                       planet,
                       cameraConstrainers,
                       cameraRenderer,
                       mainRenderer,
                       busyRenderer,
                       width, height,
                       backgroundColor,
                       logFPS,
                       logDownloaderStatistics,
                       initializationTask,
                       autoDeleteInitializationTask,
                       periodicalTasks);
}

void G3MWidget::initializeGL() {
  //_gl->enableDepthTest();

  //_gl->enableCullFace(GLCullFace::back());
}

G3MWidget::~G3MWidget() {
  delete _userData;

  delete _gl;
#ifdef C_CODE
  delete _planet;
#endif
  delete _cameraRenderer;
  delete _mainRenderer;
  delete _busyRenderer;
  delete _effectsScheduler;
  delete _currentCamera;
  delete _nextCamera;
  delete _texturesHandler;
  delete _timer;

  if (_downloader != NULL) {
    _downloader->stop();
    delete _downloader;
  }

  delete _storage;
  delete _threadUtils;

#ifdef C_CODE
  for (unsigned int n=0; n<_cameraConstrainers.size(); n++)
    delete _cameraConstrainers[n];
#endif
  delete _frameTasksExecutor;

#ifdef C_CODE
  for (int i = 0; i < _periodicalTasks.size(); i++){
    //    _periodicalTasks[i].releaseTask();

    PeriodicalTask* periodicalTask =  _periodicalTasks[i];
    delete periodicalTask;
  }
#endif

  delete _context;

  delete _rootState;
}

void G3MWidget::onTouchEvent(const TouchEvent* touchEvent) {
  if (_mainRendererReady) {
    G3MEventContext ec(IFactory::instance(),
                       IStringUtils::instance(),
                       _threadUtils,
                       ILogger::instance(),
                       IMathUtils::instance(),
                       IJSONParser::instance(),
                       _planet,
                       _downloader,
                       _effectsScheduler,
                       _storage);

    bool handled = false;
    if (_mainRenderer->isEnable()) {
      handled = _mainRenderer->onTouchEvent(&ec, touchEvent);
    }

    if (!handled) {
      _cameraRenderer->onTouchEvent(&ec, touchEvent);
    }
  }
}

void G3MWidget::onResizeViewportEvent(int width, int height) {
  if (_mainRendererReady) {
    G3MEventContext ec(IFactory::instance(),
                       IStringUtils::instance(),
                       _threadUtils,
                       ILogger::instance(),
                       IMathUtils::instance(),
                       IJSONParser::instance(),
                       _planet,
                       _downloader,
                       _effectsScheduler,
                       _storage);

    _nextCamera->resizeViewport(width, height);

    _cameraRenderer->onResizeViewportEvent(&ec, width, height);

    if (_mainRenderer->isEnable()) {
      _mainRenderer->onResizeViewportEvent(&ec, width, height);
    }
  }
}

void G3MWidget::render() {
  if (_paused) {
    return;
  }

  _timer->start();
  _renderCounter++;

  //Start periodical task
  const int periodicalTasksCount = _periodicalTasks.size();
  for (int i = 0; i < periodicalTasksCount; i++) {
    PeriodicalTask* pt = _periodicalTasks[i];
    pt->executeIfNecessary(_context);
  }

  // give to the CameraContrainers the opportunity to change the nextCamera
  const int cameraConstrainersCount = _cameraConstrainers.size();
  for (int i = 0; i< cameraConstrainersCount; i++) {
    ICameraConstrainer* constrainer = _cameraConstrainers[i];
    constrainer->onCameraChange(_planet,
                                _currentCamera,
                                _nextCamera);
  }
  _currentCamera->copyFrom(*_nextCamera);

  G3MRenderContext rc(_frameTasksExecutor,
                      IFactory::instance(),
                      IStringUtils::instance(),
                      _threadUtils,
                      ILogger::instance(),
                      IMathUtils::instance(),
                      IJSONParser::instance(),
                      _planet,
                      _gl,
                      _currentCamera,
                      _nextCamera,
                      _texturesHandler,
                      _textureBuilder,
                      _downloader,
                      _effectsScheduler,
                      IFactory::instance()->createTimer(),
                      _storage);

  _mainRendererReady = _mainRenderer->isReadyToRender(&rc);

  if (_mainRendererReady) {
    if (_initializationTask != NULL) {
      _initializationTask->run(_context);
      if (_autoDeleteInitializationTask) {
        delete _initializationTask;
      }
      _initializationTask = NULL;
    }
  }

  _effectsScheduler->doOneCyle(&rc);

  _frameTasksExecutor->doPreRenderCycle(&rc);


  Renderer* selectedRenderer = _mainRendererReady ? _mainRenderer : _busyRenderer;
  if (selectedRenderer != _selectedRenderer) {
    if (_selectedRenderer != NULL) {
      _selectedRenderer->stop();
    }
    _selectedRenderer = selectedRenderer;
    _selectedRenderer->start();
  }

  _gl->clearScreen(_backgroundColor);

  if (_mainRendererReady) {
    _cameraRenderer->render(&rc, *_rootState);
  }

  if (_selectedRenderer->isEnable()) {
    _selectedRenderer->render(&rc, *_rootState);
  }

  std::vector<OrderedRenderable*>* orderedRenderables = rc.getSortedOrderedRenderables();
  if (orderedRenderables != NULL) {
    const int orderedRenderablesCount = orderedRenderables->size();
    for (int i = 0; i < orderedRenderablesCount; i++) {
      OrderedRenderable* orderedRenderable = orderedRenderables->at(i);
      orderedRenderable->render(&rc, *_rootState);
      delete orderedRenderable;
    }
  }

  //  _frameTasksExecutor->doPostRenderCycle(&rc);

  const TimeInterval elapsedTime = _timer->elapsedTime();
  if (elapsedTime.milliseconds() > 100) {
    ILogger::instance()->logWarning("Frame took too much time: %dms",
                                    elapsedTime.milliseconds());
  }

  if (_logFPS) {
    _totalRenderTime += elapsedTime.milliseconds();

    if ((_renderStatisticsTimer == NULL) ||
        (_renderStatisticsTimer->elapsedTime().seconds() > 2)) {
      const double averageTimePerRender = (double) _totalRenderTime / _renderCounter;
      const double fps = 1000.0 / averageTimePerRender;
      ILogger::instance()->logInfo("FPS=%f" , fps);

      _renderCounter = 0;
      _totalRenderTime = 0;

      if (_renderStatisticsTimer == NULL) {
        _renderStatisticsTimer = IFactory::instance()->createTimer();
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
      ILogger::instance()->logInfo("%s" , cacheStatistics.c_str());
      _lastCacheStatistics = cacheStatistics;
    }
  }

}

void G3MWidget::onPause() {
  _paused = true;
  
  _threadUtils->onPause(_context);

  _effectsScheduler->onPause(_context);

  _mainRenderer->onPause(_context);
  _busyRenderer->onPause(_context);

  _downloader->onPause(_context);
  _storage->onPause(_context);
}

void G3MWidget::onResume() {
  _paused = false;
  
  _storage->onResume(_context);

  _downloader->onResume(_context);

  _mainRenderer->onResume(_context);
  _busyRenderer->onResume(_context);

  _effectsScheduler->onResume(_context);

  _threadUtils->onResume(_context);
}

void G3MWidget::onDestroy() {
  _threadUtils->onDestroy(_context);

  _effectsScheduler->onDestroy(_context);

  _mainRenderer->onDestroy(_context);
  _busyRenderer->onDestroy(_context);

  _downloader->onDestroy(_context);
  _storage->onDestroy(_context);
}

void G3MWidget::addPeriodicalTask(PeriodicalTask* periodicalTask) {
  _periodicalTasks.push_back(periodicalTask);
}

void G3MWidget::addPeriodicalTask(const TimeInterval& interval,
                                  GTask* task) {
  addPeriodicalTask( new PeriodicalTask(interval, task) );
}

void G3MWidget::setCameraHeading(const Angle& angle) {
  getNextCamera()->setHeading(angle);
}

void G3MWidget::setCameraPitch(const Angle& angle) {
  getNextCamera()->setPitch(angle);
}

void G3MWidget::setCameraPosition(const Geodetic3D& position) {
  getNextCamera()->setPosition(position);
}

void G3MWidget::setAnimatedCameraPosition(const Geodetic3D& position) {
  setAnimatedCameraPosition(position, TimeInterval::fromSeconds(3));
}

void G3MWidget::setAnimatedCameraPosition(const Geodetic3D& position,
                                          const TimeInterval& interval) {

  const Geodetic3D startPosition = _planet->toGeodetic3D( _currentCamera->getCartesianPosition() );

  double finalLat = position.latitude()._degrees;
  double finalLon = position.longitude()._degrees;

  //Fixing final latitude
  while (finalLat > 90) {
    finalLat -= 360;
  }
  while (finalLat < -90) {
    finalLat += 360;
  }

  //Fixing final longitude
  while (finalLon > 360) {
    finalLon -= 360;
  }
  while (finalLon < 0) {
    finalLon += 360;
  }
  if (fabs(finalLon - startPosition.longitude()._degrees) > 180) {
    finalLon -= 360;
  }

  const Geodetic3D endPosition = Geodetic3D::fromDegrees(finalLat, finalLon, position.height());

  EffectTarget* target = _nextCamera->getEffectTarget();
  _effectsScheduler->cancellAllEffectsFor(target);

  _effectsScheduler->startEffect(new GoToPositionEffect(interval, startPosition, endPosition),
                                 target);
}

void G3MWidget::resetCameraPosition() {
  getNextCamera()->resetPosition();
}
