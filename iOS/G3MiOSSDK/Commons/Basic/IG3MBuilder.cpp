//
//  IG3MBuilder.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 20/11/12.
//
//

#include "IG3MBuilder.hpp"
#include "G3MWidget.hpp"
#include "GL.hpp"
#include "IStorage.hpp"
#include "IDownloader.hpp"
#include "IThreadUtils.hpp"
#include "ICameraActivityListener.hpp"
#include "GInitializationTask.hpp"
#include "PeriodicalTask.hpp"
#include "CameraSingleDragHandler.hpp"
#include "CameraDoubleDragHandler.hpp"
#include "CameraRotationHandler.hpp"
#include "CameraDoubleTapHandler.hpp"
#include "PlanetRendererBuilder.hpp"
#include "BusyMeshRenderer.hpp"
#include "CompositeRenderer.hpp"
#include "SimpleCameraConstrainer.hpp"
#include "GPUProgramManager.hpp"
#include "GPUProgramFactory.hpp"
#include "SceneLighting.hpp"
#include "Sector.hpp"
#include "SectorAndHeightCameraConstrainer.hpp"
#include "GEORenderer.hpp"
#include "MeshRenderer.hpp"
#include "ShapesRenderer.hpp"
#include "MarksRenderer.hpp"
#include "HUDErrorRenderer.hpp"

IG3MBuilder::IG3MBuilder() :
_gl(NULL),
_storage(NULL),
_downloader(NULL),
_threadUtils(NULL),
_cameraActivityListener(NULL),
_planet(NULL),
_cameraConstraints(NULL),
_cameraRenderer(NULL), 
_backgroundColor(NULL),
_planetRendererBuilder(NULL),
_busyRenderer(NULL),
_errorRenderer(NULL),
_hudRenderer(NULL),
_renderers(NULL),
_initializationTask(NULL),
_autoDeleteInitializationTask(true),
_periodicalTasks(NULL),
_logFPS(false),
_logDownloaderStatistics(false),
_userData(NULL),
_sceneLighting(NULL),
_shownSector(NULL)
{
}

IG3MBuilder::~IG3MBuilder() {
  delete _gl;
  delete _storage;
  delete _downloader;
  delete _threadUtils;
  delete _cameraActivityListener;
  delete _planet;
  if (_cameraConstraints) {
    for (int i = 0; i < _cameraConstraints->size(); i++) {
      delete _cameraConstraints->at(i);
    }
    delete _cameraConstraints;
  }
  delete _cameraRenderer;
  if (_renderers) {
    for (int i = 0; i < _renderers->size(); i++) {
      delete _renderers->at(i);
    }
    delete _renderers;
  }
  delete _busyRenderer;
  delete _errorRenderer;
  delete _hudRenderer;
  delete _backgroundColor;
  delete _initializationTask;
  if (_periodicalTasks) {
    for (int i = 0; i < _periodicalTasks->size(); i++) {
      delete _periodicalTasks->at(i);
    }
    delete _periodicalTasks;
  }
  delete _userData;
  delete _planetRendererBuilder;
  delete _shownSector;
}

/**
 * Returns the _gl.
 *
 * @return _gl: GL*
 */
GL* IG3MBuilder::getGL() {
  if (!_gl) {
    ILogger::instance()->logError("LOGIC ERROR: gl not initialized");
  }
  
  return _gl;
}

/**
 * Returns the _storage. If it does not exist, it will be default initializated.
 *
 * @return _storage: IStorage*
 */
IStorage* IG3MBuilder::getStorage() {
  if (!_storage) {
    _storage = createDefaultStorage();
  }
  
  return _storage;
}

/**
 * Returns the _downloader. If it does not exist, it will be default initializated.
 *
 * @return _downloader: IDownloader*
 */
IDownloader* IG3MBuilder::getDownloader() {
  if (!_downloader) {
    _downloader = createDefaultDownloader();
  }
  
  return _downloader;
}

/**
 * Returns the _threadUtils. If it does not exist, it will be default initializated.
 *
 * @return _threadUtils: IThreadUtils*
 */
IThreadUtils* IG3MBuilder::getThreadUtils() {
  if (!_threadUtils) {
    _threadUtils = createDefaultThreadUtils();
  }
  
  return _threadUtils;
}

/**
 * Returns the _cameraActivityListener. If it does not exist, it will be default initializated.
 *
 * @return _threadUtils: IThreadUtils*
 */
ICameraActivityListener* IG3MBuilder::getCameraActivityListener() {
  return _cameraActivityListener;
}


/**
 * Returns the _planet. If it does not exist, it will be default initializated.
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
 * Returns the _cameraConstraints list. If it does not exist, it will be default initializated.
 * @see IG3MBuilder#createDefaultCameraConstraints() 
 *
 * @return _cameraConstraints: std::vector<ICameraConstrainer*>
 */
std::vector<ICameraConstrainer*>* IG3MBuilder::getCameraConstraints() {
  if (!_cameraConstraints) {
    _cameraConstraints = createDefaultCameraConstraints();
  }
  
  return _cameraConstraints;
}

/**
 * Returns the _cameraRenderer. If it does not exist, it will be default initializated.
 * @see IG3MBuilder#createDefaultCameraRenderer()
 *
 * @return _cameraRenderer: CameraRenderer*
 */
CameraRenderer* IG3MBuilder::getCameraRenderer() {
  if (!_cameraRenderer) {
    _cameraRenderer = createDefaultCameraRenderer();
  }
  
  return _cameraRenderer;
}

/**
 * Returns the _busyRenderer. If it does not exist, it will be default initializated.
 *
 * @return _busyRenderer: Renderer*
 */
ProtoRenderer* IG3MBuilder::getBusyRenderer() {
  if (!_busyRenderer) {
    _busyRenderer = new BusyMeshRenderer(Color::newFromRGBA((float)0, (float)0, (float)0, (float)1));
  }
  
  return _busyRenderer;
}

ErrorRenderer* IG3MBuilder::getErrorRenderer() {
  if (!_errorRenderer) {
    _errorRenderer = new HUDErrorRenderer();
  }

  return _errorRenderer;
}

Renderer* IG3MBuilder::getHUDRenderer() const {
  return _hudRenderer;
}

/**
 * Returns the _backgroundColor. If it does not exist, it will be default initializated.
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
 * Returns the _planetRendererBuilder. If it does not exist, it will be default initializated. 
 *
 * @return _planetRendererBuilder: PlanetRendererBuilder*
 */
PlanetRendererBuilder* IG3MBuilder::getPlanetRendererBuilder() {
  if (!_planetRendererBuilder) {
    _planetRendererBuilder = new PlanetRendererBuilder();
  }
  
  return _planetRendererBuilder;
}

/**
 * Returns the renderers list. If it does not exist, it will be default initializated.
 * @see IG3MBuilder#createDefaultRenderers()
 *
 * @return _renderers: std::vector<Renderer*>
 */
std::vector<Renderer*>* IG3MBuilder::getRenderers() {
  if (!_renderers) {
    _renderers = createDefaultRenderers();
  }
  return _renderers;
}

/**
 * Returns the value of _logFPS flag.
 *
 * @return _logFPS: bool
 */
bool IG3MBuilder::getLogFPS() {
  return _logFPS;
}

/**
 * Returns the value of _logDownloaderStatistics flag.
 *
 * @return _logDownloaderStatistics: bool
 */
bool IG3MBuilder::getLogDownloaderStatistics() {
  return _logDownloaderStatistics;
}

/**
 * Returns the initialization task.
 *
 * @return _logDownloaderStatistics: GInitializationTask*
 */
GInitializationTask* IG3MBuilder::getInitializationTask() {
  return _initializationTask;
}

/**
 * Returns the value of _autoDeleteInitializationTask flag.
 *
 * @return _autoDeleteInitializationTask: bool
 */
bool IG3MBuilder::getAutoDeleteInitializationTask() {
  return _autoDeleteInitializationTask;
}

/**
 * Returns the array of periodical tasks. If it does not exist, it will be default initializated.
 * @see IG3MBuilder#createDefaultPeriodicalTasks()
 *
 * @return _periodicalTasks: std::vector<PeriodicalTask*>
 */
std::vector<PeriodicalTask*>* IG3MBuilder::getPeriodicalTasks() {
  if (!_periodicalTasks) {
    _periodicalTasks = createDefaultPeriodicalTasks();
  }
  return _periodicalTasks;
}

/**
 * Returns the user data.
 *
 * @return _userData: WidgetUserData*
 */
WidgetUserData* IG3MBuilder::getUserData() {
  return _userData;
}

/**
 * Sets the _gl.
 *
 * @param gl - cannot be NULL.
 */
void IG3MBuilder::setGL(GL *gl) {
  if (_gl) {
    ILogger::instance()->logError("LOGIC ERROR: gl already initialized");
    return;
  }
  if (!gl) {
    ILogger::instance()->logError("LOGIC ERROR: gl cannot be NULL");
    return;
  }
  _gl = gl;
}

/**
 * Sets the _storage.
 *
 * @param storage
 */
void IG3MBuilder::setStorage(IStorage *storage) {
  if (_storage) {
    ILogger::instance()->logError("LOGIC ERROR: storage already initialized");
    return;
  }
  _storage = storage;
}

/**
 * Sets the _downloader
 *
 * @param downloader - cannot be NULL.
 */
void IG3MBuilder::setDownloader(IDownloader *downloader) {
  if (_downloader) {
    ILogger::instance()->logError("LOGIC ERROR: downloader already initialized");
    return;
  }
  if (!downloader) {
    ILogger::instance()->logError("LOGIC ERROR: downloader cannot be NULL");
    return;
  }
  _downloader = downloader;
}

/**
 * Sets the _threadUtils
 *
 * @param threadUtils - cannot be NULL.
 */
void IG3MBuilder::setThreadUtils(IThreadUtils *threadUtils) {
  if (_threadUtils) {
    ILogger::instance()->logError("LOGIC ERROR: threadUtils already initialized");
    return;
  }
  if (!threadUtils) {
    ILogger::instance()->logError("LOGIC ERROR: threadUtils cannot be NULL");
    return;
  }
  _threadUtils = threadUtils;
}

/**
 * Sets the _cameraActivityListener
 *
 * @param cameraActivityListener - cannot be NULL.
 */
void IG3MBuilder::setCameraActivityListener(ICameraActivityListener *cameraActivityListener) {
  if (_cameraActivityListener) {
    ILogger::instance()->logError("LOGIC ERROR: cameraActivityListener already initialized");
    return;
  }
  if (!cameraActivityListener) {
    ILogger::instance()->logError("LOGIC ERROR: cameraActivityListener cannot be NULL");
    return;
  }
  _cameraActivityListener = cameraActivityListener;
}


/**
 * Sets the _planet
 *
 * @param planet - cannot be NULL.
 */
void IG3MBuilder::setPlanet(const Planet *planet) {
  if (_planet) {
    ILogger::instance()->logError("LOGIC ERROR: planet already initialized");
    return;
  }
  if (!planet) {
    ILogger::instance()->logError("LOGIC ERROR: planet cannot be NULL");
    return;
  }
  _planet = planet;
}

/**
 * Adds a new camera constraint to the constraints list.
 * The camera constraint list will be initializated with a default constraints set.
 * @see IG3MBuilder#createDefaultCameraConstraints()
 *
 * @param cameraConstraint - cannot be NULL.
 */
void IG3MBuilder::addCameraConstraint(ICameraConstrainer* cameraConstraint) {
  if (!cameraConstraint) {
    ILogger::instance()->logError("LOGIC ERROR: trying to add a NULL cameraConstraint object");
    return;
  }
  getCameraConstraints()->push_back(cameraConstraint);
}

/**
 * Sets the camera constraints list, ignoring the default camera constraints list 
 * and the camera constraints previously added, if added.
 *
 * @param cameraConstraints - std::vector<ICameraConstrainer*>
 */
void IG3MBuilder::setCameraConstrainsts(std::vector<ICameraConstrainer*> cameraConstraints) {
  if (_cameraConstraints) {
    ILogger::instance()->logWarning("LOGIC WARNING: camera contraints previously set will be ignored and deleted");
    for (unsigned int i = 0; i < _cameraConstraints->size(); i++) {
      delete _cameraConstraints->at(i);
    }
    _cameraConstraints->clear();
  }
  else {
    _cameraConstraints = new std::vector<ICameraConstrainer*>;
  }
  for (unsigned int i = 0; i < cameraConstraints.size(); i++) {
    _cameraConstraints->push_back(cameraConstraints[i]);
  }
}

/**
 * Sets the _cameraRenderer
 *
 * @param cameraRenderer - cannot be NULL.
 */
void IG3MBuilder::setCameraRenderer(CameraRenderer *cameraRenderer) {
  if (_cameraRenderer) {
    ILogger::instance()->logError("LOGIC ERROR: cameraRenderer already initialized");
    return;
  }
  if (!cameraRenderer) {
    ILogger::instance()->logError("LOGIC ERROR: cameraRenderer cannot be NULL");
    return;
  }
  _cameraRenderer = cameraRenderer;
}

/**
 * Sets the _backgroundColor
 *
 * @param backgroundColor - cannot be NULL.
 */
void IG3MBuilder::setBackgroundColor(Color* backgroundColor) {
  if (_backgroundColor) {
    ILogger::instance()->logError("LOGIC ERROR: backgroundColor already initialized");
    return;
  }
  if (!backgroundColor) {
    ILogger::instance()->logError("LOGIC ERROR: backgroundColor cannot be NULL");
    return;
  }
  _backgroundColor = backgroundColor;
}

/**
 * Sets the _busyRenderer
 *
 * @param busyRenderer - cannot be NULL.
 */
void IG3MBuilder::setBusyRenderer(ProtoRenderer* busyRenderer) {
  if (_busyRenderer) {
    ILogger::instance()->logError("LOGIC ERROR: busyRenderer already initialized");
    return;
  }
  if (!busyRenderer) {
    ILogger::instance()->logError("LOGIC ERROR: busyRenderer cannot be NULL");
    return;
  }
  _busyRenderer = busyRenderer;
}

void IG3MBuilder::setErrorRenderer(ErrorRenderer* errorRenderer) {
  if (_errorRenderer) {
    ILogger::instance()->logError("LOGIC ERROR: errorRenderer already initialized");
    return;
  }
  if (!errorRenderer) {
    ILogger::instance()->logError("LOGIC ERROR: errorRenderer cannot be NULL");
    return;
  }
  _errorRenderer = errorRenderer;
}

void IG3MBuilder::setHUDRenderer(Renderer* hudRenderer) {
  if (_hudRenderer) {
    ILogger::instance()->logError("LOGIC ERROR: hudRenderer already initialized");
    return;
  }
  if (!hudRenderer) {
    ILogger::instance()->logError("LOGIC ERROR: hudRenderer cannot be NULL");
    return;
  }
  _hudRenderer = hudRenderer;
}

/**
 * Adds a new renderer to the renderers list.
 * The renderers list will be initializated with a default renderers set (empty set at the moment).
 *
 * @param renderer - cannot be either NULL or an instance of PlanetRenderer
 */
void IG3MBuilder::addRenderer(Renderer *renderer) {
  if (!renderer) {
    ILogger::instance()->logError("LOGIC ERROR: trying to add a NULL renderer object");
    return;
  }
  if (renderer->isPlanetRenderer()) {
    ILogger::instance()->logError("LOGIC ERROR: a new PlanetRenderer is not expected to be added");
    return;
  }
  getRenderers()->push_back(renderer);
}

/**
 * Sets the renderers list, ignoring the default renderers list and the renderers
 * previously added, if added.
 * The renderers list must contain at least an instance of the PlanetRenderer class.
 *
 * @param renderers - std::vector<Renderer*>
 */
void IG3MBuilder::setRenderers(std::vector<Renderer*> renderers) {
  if (!containsPlanetRenderer(renderers)) {
    ILogger::instance()->logError("LOGIC ERROR: renderers list must contain at least an instance of the PlanetRenderer class");
    return;
  }
  if (_renderers) {
    ILogger::instance()->logWarning("LOGIC WARNING: renderers previously set will be ignored and deleted");
    for (unsigned int i = 0; i < _renderers->size(); i++) {
      delete _renderers->at(i);
    }
    _renderers->clear();
  }
  else {
    _renderers = new std::vector<Renderer*>;
  }
  for (unsigned int i = 0; i < renderers.size(); i++) {
    _renderers->push_back(renderers[i]);
  }
}

void IG3MBuilder::pvtSetInitializationTask(GInitializationTask *initializationTask,
                                           const bool autoDeleteInitializationTask) {
  if (_initializationTask) {
    ILogger::instance()->logError("LOGIC ERROR: initializationTask already initialized");
    return;
  }
  if (!initializationTask) {
    ILogger::instance()->logError("LOGIC ERROR: initializationTask cannot be NULL");
    return;
  }
  _initializationTask = initializationTask;
  _autoDeleteInitializationTask = autoDeleteInitializationTask;
}

/**
 * Adds a new periodical task to the periodical tasks list.
 * The periodical tasks list will be initializated with a default periodical task set (empty set at the moment).
 *
 * @param periodicalTasks - cannot be NULL
 */
void IG3MBuilder::addPeriodicalTask(PeriodicalTask* periodicalTask) {
  if (!periodicalTask) {
    ILogger::instance()->logError("LOGIC ERROR: trying to add a NULL periodicalTask object");
    return;
  }
  getPeriodicalTasks()->push_back(periodicalTask);
}

/**
 * Sets the periodical tasks list, ignoring the default periodical tasks list and the
 * periodical tasks previously added, if added.
 *
 * @param periodicalTasks - std::vector<PeriodicalTask*>
 */
void IG3MBuilder::setPeriodicalTasks(std::vector<PeriodicalTask*> periodicalTasks) {
  if (_periodicalTasks) {
    ILogger::instance()->logWarning("LOGIC WARNING: periodical tasks previously set will be ignored and deleted");
    for (unsigned int i = 0; i < _periodicalTasks->size(); i++) {
      delete _periodicalTasks->at(i);
    }
    _periodicalTasks->clear();
  }
  else {
    _periodicalTasks = new std::vector<PeriodicalTask*>;
  }
  for (unsigned int i = 0; i < periodicalTasks.size(); i++) {
    _periodicalTasks->push_back(periodicalTasks[i]);
  }
}

/**
 * Sets the _logFPS
 *
 * @param logFPS
 */
void IG3MBuilder::setLogFPS(const bool logFPS) {
  _logFPS = logFPS;
}

/**
 * Sets the _logDownloaderStatistics
 *
 * @param logDownloaderStatistics
 */
void IG3MBuilder::setLogDownloaderStatistics(const bool logDownloaderStatistics) {
  _logDownloaderStatistics = logDownloaderStatistics;
}

/**
 * Sets the _userData
 *
 * @param userData - cannot be NULL.
 */
void IG3MBuilder::setUserData(WidgetUserData *userData) {
  if (_userData) {
    ILogger::instance()->logError("LOGIC ERROR: userData already initialized");
    return;
  }
  if (!userData) {
    ILogger::instance()->logError("LOGIC ERROR: userData cannot be NULL");
    return;
  }
  _userData = userData;
}

/**
 * Creates the generic widget using all the parameters previously set.
 *
 * @return G3MWidget*
 */
G3MWidget* IG3MBuilder::create() {


  Sector shownSector = getShownSector();
  getPlanetRendererBuilder()->setRenderedSector(shownSector); //Shown sector

  /**
   * If any renderers were set or added, the main renderer will be a composite renderer.
   *    If the renderers list does not contain a planetRenderer, it will be created and added.
   *    The renderers contained in the list, will be added to the main renderer.
   * If not, the main renderer will be made up of an only renderer (planetRenderer).
   */
  Renderer* mainRenderer = NULL;
  if (getRenderers()->size() > 0) {
    mainRenderer = new CompositeRenderer();
    if (!containsPlanetRenderer(*getRenderers())) {
      ((CompositeRenderer *) mainRenderer)->addRenderer(getPlanetRendererBuilder()->create());
    }
    for (unsigned int i = 0; i < getRenderers()->size(); i++) {
      ((CompositeRenderer *) mainRenderer)->addRenderer(getRenderers()->at(i));
    }
  }
  else {
    mainRenderer = getPlanetRendererBuilder()->create();
  }

  const Geodetic3D initialCameraPosition = getPlanet()->getDefaultCameraPosition(shownSector);
  addCameraConstraint(new RenderedSectorCameraConstrainer(mainRenderer->getPlanetRenderer(),
                                                          initialCameraPosition._height * 1.2));

  InitialCameraPositionProvider* icpp = new SimpleInitialCameraPositionProvider();

  G3MWidget * g3mWidget = G3MWidget::create(getGL(),
                                            getStorage(),
                                            getDownloader(),
                                            getThreadUtils(),
                                            getCameraActivityListener(),
                                            getPlanet(),
                                            *getCameraConstraints(),
                                            getCameraRenderer(),
                                            mainRenderer,
                                            getBusyRenderer(),
                                            getErrorRenderer(),
                                            getHUDRenderer(),
                                            *getBackgroundColor(),
                                            getLogFPS(),
                                            getLogDownloaderStatistics(),
                                            getInitializationTask(),
                                            getAutoDeleteInitializationTask(),
                                            *getPeriodicalTasks(),
                                            getGPUProgramManager(),
                                            getSceneLighting(),
                                            icpp);
  
  g3mWidget->setUserData(getUserData());

  _gl = NULL;
  _storage = NULL;
  _downloader = NULL;
  _threadUtils = NULL;
  _cameraActivityListener = NULL;
  _planet = NULL;
  delete _cameraConstraints;
  _cameraConstraints = NULL;
  _cameraRenderer = NULL;
  delete _renderers;
  _renderers = NULL;
  _busyRenderer = NULL;
  _errorRenderer = NULL;
  _hudRenderer = NULL;
  _initializationTask = NULL;
  delete _periodicalTasks;
  _periodicalTasks = NULL;
  _userData = NULL;

  delete _shownSector;
  _shownSector = NULL;
  
  return g3mWidget;
}

std::vector<ICameraConstrainer*>* IG3MBuilder::createDefaultCameraConstraints() {
  std::vector<ICameraConstrainer*>* cameraConstraints = new std::vector<ICameraConstrainer*>;
  SimpleCameraConstrainer* scc = new SimpleCameraConstrainer();
  cameraConstraints->push_back(scc);
  
  return cameraConstraints;
}

CameraRenderer* IG3MBuilder::createDefaultCameraRenderer() {
  CameraRenderer* cameraRenderer = new CameraRenderer();
  const bool useInertia = true;
  cameraRenderer->addHandler(new CameraSingleDragHandler(useInertia));
  cameraRenderer->addHandler(new CameraDoubleDragHandler());
  cameraRenderer->addHandler(new CameraRotationHandler());
  cameraRenderer->addHandler(new CameraDoubleTapHandler());
  
  return cameraRenderer;
}

std::vector<PeriodicalTask*>* IG3MBuilder::createDefaultPeriodicalTasks() {
  std::vector<PeriodicalTask*>* periodicalTasks = new std::vector<PeriodicalTask*>;
  
  return periodicalTasks;
}

std::vector<Renderer*>* IG3MBuilder::createDefaultRenderers() {
  std::vector<Renderer*>* renderers = new std::vector<Renderer*>;
  
  return renderers;
}

/**
 * Returns TRUE if the given renderer list contains, at least, an instance of 
 * the PlanetRenderer class. Returns FALSE if not.
 *
 * @return bool
 */
bool IG3MBuilder::containsPlanetRenderer(std::vector<Renderer*> renderers) {
  for (unsigned int i = 0; i < renderers.size(); i++) {
    if (renderers[i]->isPlanetRenderer()) {
      return true;
    }
  }
  return false;
}

void IG3MBuilder::addGPUProgramSources(const GPUProgramSources& s) {
  _sources.push_back(s);
}

void IG3MBuilder::setSceneLighting(SceneLighting* sceneLighting) {
  _sceneLighting = sceneLighting;
}

GPUProgramManager* IG3MBuilder::getGPUProgramManager() {
  //GPU Program Manager
  GPUProgramFactory * gpuProgramFactory = new GPUProgramFactory();
  for(int i = 0; i < _sources.size(); i++) {
    gpuProgramFactory->add(_sources[i]);
  }
  GPUProgramManager * gpuProgramManager = new GPUProgramManager(gpuProgramFactory);
  return gpuProgramManager;
}

SceneLighting* IG3MBuilder::getSceneLighting() {
  if (_sceneLighting == NULL) {
    //_sceneLighting = new DefaultSceneLighting();
    _sceneLighting = new CameraFocusSceneLighting(Color::fromRGBA((float)0.3, (float)0.3, (float)0.3, (float)1.0),
                                                  Color::yellow());
  }
  return _sceneLighting;
}

void IG3MBuilder::setShownSector(const Sector& sector) {
  if (_shownSector != NULL) {
    ILogger::instance()->logError("LOGIC ERROR: shownSector already initialized");
    return;
  }
  _shownSector = new Sector(sector);
}

Sector IG3MBuilder::getShownSector() const{
  if (_shownSector == NULL) {
    return Sector::fullSphere();
  }
  return *_shownSector;
}

MeshRenderer* IG3MBuilder::createMeshRenderer() {
  MeshRenderer* meshRenderer = new MeshRenderer();
  addRenderer(meshRenderer);
  return meshRenderer;
}

ShapesRenderer* IG3MBuilder::createShapesRenderer() {
  ShapesRenderer* shapesRenderer = new ShapesRenderer();
  addRenderer(shapesRenderer);
  return shapesRenderer;
}

MarksRenderer* IG3MBuilder::createMarksRenderer() {
  MarksRenderer* marksRenderer = new MarksRenderer(false);
  addRenderer(marksRenderer);
  return marksRenderer;
}

GEORenderer* IG3MBuilder::createGEORenderer(GEOSymbolizer* symbolizer,
                                            bool createMeshRenderer,
                                            bool createShapesRenderer,
                                            bool createMarksRenderer,
                                            bool createGEOTileRasterizer) {

  MeshRenderer*      meshRenderer      = createMeshRenderer      ? this->createMeshRenderer() : NULL;
  ShapesRenderer*    shapesRenderer    = createShapesRenderer    ? this->createShapesRenderer() : NULL;
  MarksRenderer*     marksRenderer     = createMarksRenderer     ? this->createMarksRenderer() : NULL;
  GEOTileRasterizer* geoTileRasterizer = createGEOTileRasterizer ? getPlanetRendererBuilder()->createGEOTileRasterizer() : NULL;

  GEORenderer* geoRenderer = new GEORenderer(symbolizer,
                                             meshRenderer,
                                             shapesRenderer,
                                             marksRenderer,
                                             geoTileRasterizer);
  addRenderer(geoRenderer);

  return geoRenderer;
}
