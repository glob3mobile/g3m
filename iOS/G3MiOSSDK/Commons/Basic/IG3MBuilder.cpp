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
#include "SimpleCameraConstrainer.hpp"

IG3MBuilder::IG3MBuilder() :
_gl(NULL),
_storage(NULL),
_downloader(NULL),
_threadUtils(NULL),
_planet(NULL), 
_cameraRenderer(NULL), 
_backgroundColor(NULL),
_tileRendererBuilder(NULL),
_busyRenderer(NULL),
_initializationTask(NULL),
_autoDeleteInitializationTask(true),
_logFPS(false),
_logDownloaderStatistics(false),
_userData(NULL)
{
}

IG3MBuilder::~IG3MBuilder() {
  delete _gl;
  delete _storage;
  delete _downloader;
  delete _threadUtils;
  delete _planet;
  for (int i = 0; i < _cameraConstraints.size(); i++) {
    delete _cameraConstraints[i];
  }
  delete _cameraRenderer;
  for (int i = 0; i < _renderers.size(); i++) {
    delete _renderers[i];
  }
  delete _busyRenderer;
  delete _backgroundColor;
  delete _initializationTask;
  for (int i = 0; i < _periodicalTasks.size(); i++) {
    delete _periodicalTasks[i];
  }
  delete _userData;
  delete _tileRendererBuilder;
}

/**
 * Returns the _gl.
 * Lazy initialization.
 *
 * @return _gl: GL*
 */
GL* IG3MBuilder::getGL() {
  if (!_gl) {
    ILogger::instance()->logError("Logic Error: _gl not initialized");
  }
  
  return _gl;
}

/**
 * Returns the _storage.
 * Lazy initialization.
 *
 * @return _storage: IStorage*
 */
IStorage* IG3MBuilder::getStorage() {
  if (!_storage) {
    _storage = createStorage();
  }
  
  return _storage;
}

/**
 * Returns the _downloader.
 * Lazy initialization.
 *
 * @return _downloader: IDownloader*
 */
IDownloader* IG3MBuilder::getDownloader() {
  if (!_downloader) {
    _downloader = createDownloader();
  }
  
  return _downloader;
}

/**
 * Returns the _threadUtils.
 * Lazy initialization.
 *
 * @return _threadUtils: IThreadUtils*
 */
IThreadUtils* IG3MBuilder::getThreadUtils() {
  if (!_threadUtils) {
    _threadUtils = createThreadUtils();
  }
  
  return _threadUtils;
}

/**
 * Returns the _planet.
 * Lazy initialization.
 *
 * @return _planet: const Planet*
 */
const Planet* IG3MBuilder::getPlanet() {
  if (!_planet) {
    _planet = Planet::createEarth();
  }
  return _planet;
}

/**
 * Returns the _cameraConstraints.
 * Lazy initialization.
 *
 * @return _cameraConstraints: std::vector<ICameraConstrainer*>
 */
std::vector<ICameraConstrainer*> IG3MBuilder::getCameraConstraints() {
  if (_cameraConstraints.size() == 0) {
    _cameraConstraints = createCameraConstraints();
  }
  
  return _cameraConstraints;
}

/**
 * Returns the _cameraRenderer.
 * Lazy initialization.
 *
 * @return _cameraRenderer: CameraRenderer*
 */
CameraRenderer* IG3MBuilder::getCameraRenderer() {
  if (!_cameraRenderer) {
    _cameraRenderer = createCameraRenderer();
  }
  
  return _cameraRenderer;
}

/**
 * Returns the _busyRenderer.
 * Lazy initialization.
 *
 * @return _busyRenderer: Renderer*
 */
Renderer* IG3MBuilder::getBusyRenderer() {
  if (!_busyRenderer) {
    _busyRenderer = new BusyMeshRenderer();
  }
  
  return _busyRenderer;
}

/**
 * Returns the _backgroundColor.
 * Lazy initialization.
 *
 * @return _backgroundColor: Color*
 */
Color* IG3MBuilder::getBackgroundColor() {
  if (!_backgroundColor) {
    _backgroundColor = Color::newFromRGBA((float)0, (float)0.1, (float)0.2, (float)1);
  }

  return _backgroundColor;
}

/**
 * Returns the _tileRendererBuilder.
 * Lazy initialization.
 *
 * @return _tileRendererBuilder: TileRendererBuilder*
 */
TileRendererBuilder* IG3MBuilder::getTileRendererBuilder() {
  if (!_tileRendererBuilder) {
    _tileRendererBuilder = new TileRendererBuilder();
  }
  
  return _tileRendererBuilder;
}

/**
 * Returns the array of renderers.
 * Method created to keep convention. It is not needed as it does not have to create a default value.
 *
 * @return _renderers: std::vector<Renderer*>
 */
std::vector<Renderer*> IG3MBuilder::getRenderers() {
  return _renderers;
}

/**
 * Returns the value of _logFPS flag.
 * Method created to keep convention. It is not needed as it does not have to create a default value.
 *
 * @return _logFPS: bool
 */
bool IG3MBuilder::getLogFPS() {
  return _logFPS;
}

/**
 * Returns the value of _logDownloaderStatistics flag.
 * Method created to keep convention. It is not needed as it does not have to create a default value.
 *
 * @return _logDownloaderStatistics: bool
 */
bool IG3MBuilder::getLogDownloaderStatistics() {
  return _logDownloaderStatistics;
}

/**
 * Returns the initialization task.
 * Method created to keep convention. It is not needed as it does not have to create a default value.
 *
 * @return _logDownloaderStatistics: GInitializationTask*
 */
GInitializationTask* IG3MBuilder::getInitializationTask() {
  return _initializationTask;
}

/**
 * Returns the value of _autoDeleteInitializationTask flag.
 * Method created to keep convention. It is not needed as it does not have to create a default value.
 *
 * @return _autoDeleteInitializationTask: bool
 */
bool IG3MBuilder::getAutoDeleteInitializationTask() {
  return _autoDeleteInitializationTask;
}

/**
 * Returns the array of periodical tasks.
 * Method created to keep convention. It is not needed as it does not have to create a default value.
 *
 * @return _periodicalTasks: std::vector<PeriodicalTask*>
 */
std::vector<PeriodicalTask*> IG3MBuilder::getPeriodicalTasks() {
  return _periodicalTasks;
}

/**
 * Returns the user data.
 * Method created to keep convention. It is not needed as it does not have to create a default value.
 *
 * @return _userData: WidgetUserData*
 */
WidgetUserData* IG3MBuilder::getUserData() {
  return _userData;
}

void IG3MBuilder::setGL(GL *gl) {
  if (_gl) {
    ILogger::instance()->logError("LOGIC ERROR: _gl already initialized");
    return;
  }
  _gl = gl;
}

void IG3MBuilder::setStorage(IStorage *storage) {
  if (_storage) {
    ILogger::instance()->logError("LOGIC ERROR: _storage already initialized");
    return;
  }
  _storage = storage;
}

void IG3MBuilder::setDownloader(IDownloader *downloader) {
  if (_downloader) {
    ILogger::instance()->logError("LOGIC ERROR: _downloader already initialized");
    return;
  }
  _downloader = downloader;
}

void IG3MBuilder::setThreadUtils(IThreadUtils *threadUtils) {
  if (_threadUtils) {
    ILogger::instance()->logError("LOGIC ERROR: _threadUtils already initialized");
    return;
  }
  _threadUtils = threadUtils;
}

void IG3MBuilder::setPlanet(const Planet *planet) {
  if (_planet) {
    ILogger::instance()->logError("LOGIC ERROR: _planet already initialized");
    return;
  }
  _planet = planet;
}

void IG3MBuilder::addCameraConstraint(ICameraConstrainer* cameraConstraint) {
  _cameraConstraints.push_back(cameraConstraint);
}

void IG3MBuilder::setCameraRenderer(CameraRenderer *cameraRenderer) {
  if (_cameraRenderer) {
    ILogger::instance()->logError("LOGIC ERROR: _cameraRenderer already initialized");
    return;
  }
  _cameraRenderer = cameraRenderer;
}

void IG3MBuilder::setBackgroundColor(Color* backgroundColor) {
  if (_backgroundColor) {
    ILogger::instance()->logError("LOGIC ERROR: _backgroundColor already initialized");
    return;
  }
  _backgroundColor = backgroundColor;
}

void IG3MBuilder::setBusyRenderer(Renderer *busyRenderer) {
  if (_busyRenderer) {
    ILogger::instance()->logError("LOGIC ERROR: _busyRenderer already initialized");
    return;
  }
  _busyRenderer = busyRenderer;
}

void IG3MBuilder::addRenderer(Renderer *renderer) {
  if (!renderer->isTileRenderer()) {
    _renderers.push_back(renderer);
  }
  else {
    ILogger::instance()->logError("LOGIC ERROR: a new TileRenderer is not expected to be added");
  }
}

void IG3MBuilder::pvtSetInitializationTask(GInitializationTask *initializationTask,
                                           const bool autoDeleteInitializationTask) {
  if (_initializationTask) {
    ILogger::instance()->logError("LOGIC ERROR: _initializationTask already initialized");
    return;
  }
  _initializationTask = initializationTask;
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
  if (_userData) {
    ILogger::instance()->logError("LOGIC ERROR: _userData already initialized");
    return;
  }
  _userData = userData;
}

G3MWidget* IG3MBuilder::create() {
  Renderer* mainRenderer = NULL;
  TileRenderer* tileRenderer = getTileRendererBuilder()->create();
  if (getRenderers().size() > 0) {
    mainRenderer = new CompositeRenderer();
    ((CompositeRenderer *) mainRenderer)->addRenderer(tileRenderer);
    
    for (int i = 0; i < getRenderers().size(); i++) {
      ((CompositeRenderer *) mainRenderer)->addRenderer(getRenderers()[i]);
    }
  }
  else {
    mainRenderer = tileRenderer;
  }
  
  Color backgroundColor = Color::fromRGBA(getBackgroundColor()->getRed(),
                                          getBackgroundColor()->getGreen(),
                                          getBackgroundColor()->getBlue(),
                                          getBackgroundColor()->getAlpha());
  
  G3MWidget * g3mWidget = G3MWidget::create(getGL(),
                                            getStorage(),
                                            getDownloader(),
                                            getThreadUtils(),
                                            getPlanet(),
                                            getCameraConstraints(),
                                            getCameraRenderer(),
                                            mainRenderer,
                                            getBusyRenderer(),
                                            backgroundColor,
                                            getLogFPS(),
                                            getLogDownloaderStatistics(),
                                            getInitializationTask(),
                                            getAutoDeleteInitializationTask(),
                                            getPeriodicalTasks());
  
  g3mWidget->setUserData(getUserData());
  
  _gl = NULL;
  _storage = NULL;
  _downloader = NULL;
  _threadUtils = NULL;
  _planet = NULL;
  for (int i = 0; i < _cameraConstraints.size(); i++) {
    _cameraConstraints[i] = NULL;
  }
  _cameraRenderer = NULL;
  for (int i = 0; i < _renderers.size(); i++) {
    _renderers[i] = NULL;
  }
  _busyRenderer = NULL;
  _backgroundColor = NULL;
  _initializationTask = NULL;
  for (int i = 0; i < _periodicalTasks.size(); i++) {
    _periodicalTasks[i] = NULL;
  }
  _userData = NULL;
  _tileRendererBuilder = NULL;
  
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
