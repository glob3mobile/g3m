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
#include <math.h>

void G3MWidget::initSingletons(ILogger*            logger,
                               IFactory*           factory,
                               const IStringUtils* stringUtils,
                               IThreadUtils*       threadUtils,
                               IStringBuilder*     stringBuilder,
                               IMathUtils*         mathUtils,
                               IJSONParser*        jsonParser) {
  if (ILogger::instance() == NULL) {
    ILogger::setInstance(logger);
    IFactory::setInstance(factory);
    IStringUtils::setInstance(stringUtils);
    IThreadUtils::setInstance(threadUtils);
    IStringBuilder::setInstance(stringBuilder);
    IMathUtils::setInstance(mathUtils);
    IJSONParser::setInstance(jsonParser);
  }
  else {
    ILogger::instance()->logWarning("Singletons already set");
  }
}

G3MWidget::G3MWidget(INativeGL*                       nativeGL,
                     IDownloader*                     downloader,
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
_frameTasksExecutor( new FrameTasksExecutor() ),
_effectsScheduler( new EffectsScheduler() ),
_gl( new GL(nativeGL) ),
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
_downloader(downloader),
_mainRendererReady(false), // false until first call to G3MWidget::render()
_selectedRenderer(NULL),
_renderStatisticsTimer(NULL),
_logDownloaderStatistics(logDownloaderStatistics),
_userData(NULL),
_initializationTask(initializationTask),
_autoDeleteInitializationTask(autoDeleteInitializationTask),
_initializationContext(new InitializationContext(IFactory::instance(),
                                                 IStringUtils::instance(),
                                                 IThreadUtils::instance(),
                                                 ILogger::instance(),
                                                 IMathUtils::instance(),
                                                 IJSONParser::instance(),
                                                 _planet,
                                                 _downloader,
                                                 _effectsScheduler))
{
  initializeGL();

  _effectsScheduler->initialize(_initializationContext);
  _cameraRenderer->initialize(_initializationContext);
  _mainRenderer->initialize(_initializationContext);
  _busyRenderer->initialize(_initializationContext);
  _currentCamera->initialize(_initializationContext);
  _nextCamera->initialize(_initializationContext);

  if (_downloader != NULL){
    _downloader->start();
  }

  for (int i = 0; i < periodicalTasks.size(); i++) {
    addPeriodicalTask(periodicalTasks[i]);
  }
}


G3MWidget* G3MWidget::create(INativeGL*                       nativeGL,
                             IDownloader*                     downloader,
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

  return new G3MWidget(nativeGL,
                       downloader,
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
  _gl->enableDepthTest();

  _gl->enableCullFace(GLCullFace::back());
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
    //    _periodicalTasks[i].releaseTask();

    PeriodicalTask* periodicalTask =  _periodicalTasks[i];
    delete periodicalTask;
  }
#endif

  delete _initializationContext;
}

void G3MWidget::onTouchEvent(const TouchEvent* touchEvent) {
  if (_mainRendererReady) {
    EventContext ec(IFactory::instance(),
                    IStringUtils::instance(),
                    IThreadUtils::instance(),
                    ILogger::instance(),
                    IMathUtils::instance(),
                    IJSONParser::instance(),
                    _planet,
                    _downloader,
                    _effectsScheduler);

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
    EventContext ec(IFactory::instance(),
                    IStringUtils::instance(),
                    IThreadUtils::instance(),
                    ILogger::instance(),
                    IMathUtils::instance(),
                    IJSONParser::instance(),
                    _planet,
                    _downloader,
                    _effectsScheduler);

    _cameraRenderer->onResizeViewportEvent(&ec, width, height);

    if (_mainRenderer->isEnable()) {
      _mainRenderer->onResizeViewportEvent(&ec, width, height);
    }
  }
}

void G3MWidget::render() {
  _timer->start();
  _renderCounter++;

  //Start periodical task
  for (int i = 0; i < _periodicalTasks.size(); i++) {
    PeriodicalTask* pt = _periodicalTasks[i];
    pt->executeIfNecessary();
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
                   IFactory::instance(),
                   IStringUtils::instance(),
                   IThreadUtils::instance(),
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
                   IFactory::instance()->createTimer()
                   );

  _effectsScheduler->doOneCyle(&rc);

  _frameTasksExecutor->doPreRenderCycle(&rc);

  _mainRendererReady = _mainRenderer->isReadyToRender(&rc);

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
    _cameraRenderer->render(&rc);
  }

  if (_selectedRenderer->isEnable()) {
    _selectedRenderer->render(&rc);
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
  _mainRenderer->onPause(_initializationContext);
  _busyRenderer->onPause(_initializationContext);

  _effectsScheduler->onPause(_initializationContext);

  if (_downloader != NULL) {
    _downloader->onPause(_initializationContext);
  }
}

void G3MWidget::onResume() {
  _mainRenderer->onResume(_initializationContext);
  _busyRenderer->onResume(_initializationContext);

  _effectsScheduler->onResume(_initializationContext);

  if (_downloader != NULL) {
    _downloader->onResume(_initializationContext);
  }
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

  Geodetic3D startPosition = _planet->toGeodetic3D( _currentCamera->getCartesianPosition() );

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
