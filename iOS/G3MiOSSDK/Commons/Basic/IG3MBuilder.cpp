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

IG3MBuilder::IG3MBuilder() {
  _gl = NULL;
  _storage = NULL;
  _downloader = NULL;
  _threadUtils = NULL;
  _planet = Planet::createEarth();
  _cameraRenderer = createCameraRenderer();
  _backgroundColor = Color::newFromRGBA((float)0, (float)0.1, (float)0.2, (float)1);
  _layerSet = NULL;
  _parameters = NULL;
  _tileRenderer = NULL;
  _busyRenderer = new BusyMeshRenderer();
  _initializationTask = NULL;
  _logFPS = true;
  _logDownloaderStatistics = false;
  _autoDeleteInitializationTask = true;
  _userData = NULL;
}

IG3MBuilder::~IG3MBuilder() {
//#ifdef C_CODE
//  delete _planet;
//  delete _parameters;
//#endif
//  delete _gl;
//  delete _storage;
//  delete _downloader;
//  delete _threadUtils;
//  delete _cameraRenderer;
//  delete _backgroundColor;
//  delete _layerSet;
//  delete _tileRenderer;
//  delete _busyRenderer;
//  delete _initializationTask;
//  delete _userData;
}

G3MWidget* IG3MBuilder::create() {

  if (_gl) {
    if (!_storage) {
      _storage = createStorage();
    }

    if (!_downloader) {
      _downloader = createDownloader();
    }

    if (!_threadUtils) {
      _threadUtils = createThreadUtils();
    }

    if (!_planet) {
      _planet = Planet::createEarth();
    }

    if (_cameraConstraints.size() == 0) {
      _cameraConstraints = createCameraConstraints();
    }

    if (!_cameraRenderer) {
      _cameraRenderer = createCameraRenderer();
    }


    if (!_tileRenderer) {
      TileRendererBuilder tileRendererBuilder;
      if (_layerSet) {
        tileRendererBuilder.setLayerSet(_layerSet);
      }
      if (_parameters) {
        tileRendererBuilder.setTileRendererParameters(_parameters);
      }
      _tileRenderer = tileRendererBuilder.create();
    }
    else {
      if (_layerSet) {
        ILogger::instance()->logWarning("LayerSet will be ignored because TileRenderer was also set");
      }
      if (_parameters) {
        ILogger::instance()->logWarning("TilesRendererParameters will be ignored because TileRenderer was also set");
      }
    }

    Renderer* mainRenderer = NULL;
    if (_renderers.size() > 0) {
      mainRenderer = new CompositeRenderer();
      ((CompositeRenderer *) mainRenderer)->addRenderer(_tileRenderer);

      for (int i = 0; i < _renderers.size(); i++) {
        ((CompositeRenderer *) mainRenderer)->addRenderer(_renderers[i]);
      }
    }
    else {
      mainRenderer = _tileRenderer;
    }

    if (!_busyRenderer) {
      _busyRenderer = new BusyMeshRenderer();
    }

    Color backgroundColor = Color::fromRGBA(_backgroundColor->getRed(), _backgroundColor->getGreen(), _backgroundColor->getBlue(), _backgroundColor->getAlpha());

    G3MWidget * g3mWidget = G3MWidget::create(_gl,
                                              _storage,
                                              _downloader,
                                              _threadUtils,
                                              _planet,
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
  return NULL;
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

void IG3MBuilder::setLayerSet(LayerSet *layerSet) {
  if (!_tileRenderer) {
    if (_layerSet != layerSet) {
      delete _layerSet;
      _layerSet = layerSet;
    }
  }
  else {
    ILogger::instance()->logWarning("LayerSet will be ignored because TileRenderer was previously set");
  }
}

void IG3MBuilder::setTileRendererParameters(TilesRenderParameters *parameters) {
  if (!_tileRenderer) {
    if (_parameters != parameters) {
#ifdef C_CODE
      delete _parameters;
#endif
      _parameters = parameters;
    }
  }
  else {
    ILogger::instance()->logWarning("TilesRendererParameters will be ignored because TileRenderer was previously set");
  }
}

void IG3MBuilder::setTileRenderer(TileRenderer *tileRenderer) {
  if (_tileRenderer != tileRenderer) {
    delete _tileRenderer;
    _tileRenderer = tileRenderer;
  }
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

void IG3MBuilder::setInitializationTask(GInitializationTask *initializationTask,
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

void IG3MBuilder::setUserData(UserData *userData) {
  if (_userData != userData) {
    delete _userData;
    _userData = userData;
  }
}
