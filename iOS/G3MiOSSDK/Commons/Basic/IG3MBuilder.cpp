//
//  IG3MBuilder.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 20/11/12.
//
//

#include "IG3MBuilder.hpp"
#include "CameraSingleDragHandler.hpp"
#include "CameraDoubleDragHandler.hpp"
#include "CameraRotationHandler.hpp"
#include "CameraDoubleTapHandler.hpp"
#include "TileRendererBuilder.hpp"
#include "BusyMeshRenderer.hpp"
#include "CompositeRenderer.hpp"

IG3MBuilder::IG3MBuilder() :
_gl(NULL),
_storage(NULL),
_downloader(NULL),
_threadUtils(NULL),
_planet(NULL), //Planet::createEarth();
_cameraRenderer(NULL), //createCameraRenderer();
_backgroundColor(Color::newFromRGBA((float)0, (float)0.1, (float)0.2, (float)1)),
_tileRendererBuilder(new TileRendererBuilder()),
_busyRenderer(NULL), // new BusyMeshRenderer()),
_initializationTask(NULL),
_autoDeleteInitializationTask(true),
_logFPS(false),
_logDownloaderStatistics(false),
_userData(NULL)
{
  /*
   _gl = NULL;
   _storage = NULL;
   _downloader = NULL;
   _threadUtils = NULL;
   _planet = NULL;
   _cameraRenderer = NULL;
   _backgroundColor = Color::newFromRGBA((float)0, (float)0.1, (float)0.2, (float)1);
   _layerSet = NULL;
   _parameters = NULL;
   _tileRenderer = NULL;
   _busyRenderer = new BusyMeshRenderer();
   _initializationTask = NULL;
   _logFPS = false;
   _logDownloaderStatistics = false;
   _autoDeleteInitializationTask = true;
   _userData = NULL;
   */
}

IG3MBuilder::~IG3MBuilder() {
  delete _backgroundColor;

  //#ifdef C_CODE
  //  delete _planet;
  //  delete _parameters;
  //#endif
  //  delete _gl;
  //  delete _storage;
  //  delete _downloader;
  //  delete _threadUtils;
  //  delete _cameraRenderer;
  //  delete _layerSet;
  //  delete _tileRenderer;
  //  delete _busyRenderer;
  //  delete _initializationTask;
  //  delete _userData;
}

const Planet* IG3MBuilder::getPlanet() {
  if (!_planet) {
    _planet = Planet::createEarth();
  }
  return _planet;
}


G3MWidget* IG3MBuilder::create() {

  if (_gl == NULL) {
    ILogger::instance()->logError("Logic Error: _gl not initialized");
    return NULL;
  }

  if (!_storage) {
    _storage = createStorage();
  }

  if (!_downloader) {
    _downloader = createDownloader();
  }

  if (!_threadUtils) {
    _threadUtils = createThreadUtils();
  }

  if (_cameraConstraints.size() == 0) {
    _cameraConstraints = createCameraConstraints();
  }

  if (!_cameraRenderer) {
    _cameraRenderer = createCameraRenderer();
  }
  
  Renderer* mainRenderer = NULL;
  TileRenderer* tileRenderer = _tileRendererBuilder->create();
  if (_renderers.size() > 0) {
    mainRenderer = new CompositeRenderer();
    ((CompositeRenderer *) mainRenderer)->addRenderer(tileRenderer);

    for (int i = 0; i < _renderers.size(); i++) {
      ((CompositeRenderer *) mainRenderer)->addRenderer(_renderers[i]);
    }
  }
  else {
    mainRenderer = tileRenderer;
  }

  if (!_busyRenderer) {
    _busyRenderer = new BusyMeshRenderer();
  }

  Color backgroundColor = Color::fromRGBA(_backgroundColor->getRed(),
                                          _backgroundColor->getGreen(),
                                          _backgroundColor->getBlue(),
                                          _backgroundColor->getAlpha());

  G3MWidget * g3mWidget = G3MWidget::create(_gl,
                                            _storage,
                                            _downloader,
                                            _threadUtils,
                                            getPlanet(),
                                            _cameraConstraints,
                                            _cameraRenderer,
                                            mainRenderer,
                                            _busyRenderer,
                                            backgroundColor,
                                            _logFPS,
                                            _logDownloaderStatistics,
                                            _initializationTask,
                                            _autoDeleteInitializationTask,
                                            _periodicalTasks);

  g3mWidget->setUserData(_userData);

  return g3mWidget;

}

std::vector<ICameraConstrainer*> IG3MBuilder::createCameraConstraints() {
  std::vector<ICameraConstrainer*> cameraConstraints;
  SimpleCameraConstrainer* scc = new SimpleCameraConstrainer();
  cameraConstraints.push_back(scc);

  return cameraConstraints;
}

CameraRenderer* IG3MBuilder::createCameraRenderer() {
  CameraRenderer* cameraRenderer = new CameraRenderer();
  const bool useInertia = true;
  cameraRenderer->addHandler(new CameraSingleDragHandler(useInertia));
  const bool processRotation = true;
  const bool processZoom = true;
  cameraRenderer->addHandler(new CameraDoubleDragHandler(processRotation,
                                                         processZoom));
  cameraRenderer->addHandler(new CameraRotationHandler());
  cameraRenderer->addHandler(new CameraDoubleTapHandler());

  return cameraRenderer;
}

void IG3MBuilder::setGL(GL *gl) {
  if (_gl != gl) {
    delete _gl;
    _gl = gl;
  }
}

void IG3MBuilder::setStorage(IStorage *storage) {
  if (_storage != storage) {
    delete _storage;
    _storage = storage;
  }
}

void IG3MBuilder::setDownloader(IDownloader *downloader) {
  if (_downloader != downloader) {
    delete _downloader;
    _downloader = downloader;
  }
}

void IG3MBuilder::setThreadUtils(IThreadUtils *threadUtils) {
  if (_threadUtils != threadUtils) {
    delete _threadUtils;
    _threadUtils = threadUtils;
  }
}

void IG3MBuilder::setPlanet(const Planet *planet) {
  if (_planet != planet) {
#ifdef C_CODE
    delete _planet;
#endif
    _planet = planet;
  }
}

void IG3MBuilder::addCameraConstraint(ICameraConstrainer* cameraConstraint) {
  _cameraConstraints.push_back(cameraConstraint);
}

void IG3MBuilder::setCameraRenderer(CameraRenderer *cameraRenderer) {
  if (_cameraRenderer != cameraRenderer) {
    delete _cameraRenderer;
    _cameraRenderer = cameraRenderer;
  }
}

void IG3MBuilder::setBackgroundColor(Color* backgroundColor) {
  if (_backgroundColor != backgroundColor) {
    delete _backgroundColor;
    _backgroundColor = backgroundColor;
  }
}

TileRendererBuilder* IG3MBuilder::getTileRendererBuilder() {
  return _tileRendererBuilder;
}

void IG3MBuilder::setBusyRenderer(Renderer *busyRenderer) {
  if (_busyRenderer != busyRenderer) {
    delete _busyRenderer;
    _busyRenderer = busyRenderer;
  }
}

void IG3MBuilder::addRenderer(Renderer *renderer) {
  _renderers.push_back(renderer);
}

void IG3MBuilder::pvtSetInitializationTask(GInitializationTask *initializationTask,
                                           const bool autoDeleteInitializationTask) {
  if (_initializationTask != initializationTask) {
    delete _initializationTask;
    _initializationTask = initializationTask;
  }
  _autoDeleteInitializationTask = autoDeleteInitializationTask;
}

void IG3MBuilder::addPeriodicalTask(PeriodicalTask* periodicalTask) {
  _periodicalTasks.push_back(periodicalTask);
}

void IG3MBuilder::setLogFPS(const bool logFPS) {
  _logFPS = logFPS;
}

void IG3MBuilder::setLogDownloaderStatistics(const bool logDownloaderStatistics) {
  _logDownloaderStatistics = logDownloaderStatistics;
}

void IG3MBuilder::setUserData(WidgetUserData *userData) {
  if (_userData != userData) {
    delete _userData;
    _userData = userData;
  }
}
