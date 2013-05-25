//
//  G3MCBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

#include "G3MCBuilder.hpp"

#include "ILogger.hpp"
#include "CompositeRenderer.hpp"
#include "TileRenderer.hpp"

#include "EllipsoidalTileTessellator.hpp"
#include "MultiLayerTileTexturizer.hpp"
#include "TilesRenderParameters.hpp"
#include "DownloadPriority.hpp"
#include "G3MWidget.hpp"
#include "SimpleCameraConstrainer.hpp"
#include "CameraRenderer.hpp"
#include "CameraSingleDragHandler.hpp"
#include "CameraDoubleDragHandler.hpp"
#include "CameraRotationHandler.hpp"
#include "CameraDoubleTapHandler.hpp"
#include "BusyMeshRenderer.hpp"
#include "GInitializationTask.hpp"
#include "PeriodicalTask.hpp"
#include "IDownloader.hpp"
#include "IBufferDownloadListener.hpp"
#include "IJSONParser.hpp"
#include "JSONObject.hpp"
#include "JSONString.hpp"

#include "OSMLayer.hpp"
#include "MapQuestLayer.hpp"

G3MCBuilder::G3MCBuilder(const URL& serverURL,
                         const std::string& sceneId) :
_serverURL(serverURL),
_sceneId(sceneId),
_gl(NULL),
_glob3Created(false),
_storage(NULL),
_layerSet(NULL),
_baseLayer(NULL)
{
  
}


void G3MCBuilder::setGL(GL *gl) {
  if (_gl) {
    ILogger::instance()->logError("LOGIC ERROR: _gl already initialized");
    return;
  }
  if (!gl) {
    ILogger::instance()->logError("LOGIC ERROR: _gl cannot be NULL");
    return;
  }
  _gl = gl;
}

GL* G3MCBuilder::getGL() {
  if (!_gl) {
    ILogger::instance()->logError("Logic Error: _gl not initialized");
  }

  return _gl;
}

LayerSet* G3MCBuilder::getLayerSet() {
  if (_layerSet == NULL) {
    _layerSet = new LayerSet();
    recreateLayerSet();
  }
  return _layerSet;
}

TileRenderer* G3MCBuilder::createTileRenderer() {
  const TileTessellator* tessellator = new EllipsoidalTileTessellator(true);

  ElevationDataProvider* elevationDataProvider = NULL;
  const float verticalExaggeration = 1;
  TileTexturizer* texturizer = new MultiLayerTileTexturizer();
  //LayerSet* layerSet = new LayerSet();
  LayerSet* layerSet = getLayerSet();

  const bool renderDebug = false;
  const bool useTilesSplitBudget = true;
  const bool forceFirstLevelTilesRenderOnStart = true;
  const bool incrementalTileQuality = false;
  const TilesRenderParameters* parameters = new TilesRenderParameters(renderDebug,
                                                                      useTilesSplitBudget,
                                                                      forceFirstLevelTilesRenderOnStart,
                                                                      incrementalTileQuality);

  const bool showStatistics = false;
  long long texturePriority = DownloadPriority::HIGHER;

  return new TileRenderer(tessellator,
                          elevationDataProvider,
                          verticalExaggeration,
                          texturizer,
                          layerSet,
                          parameters,
                          showStatistics,
                          texturePriority);
}

const Planet* G3MCBuilder::createPlanet() {
  return Planet::createEarth();
}

std::vector<ICameraConstrainer*>* G3MCBuilder::createCameraConstraints() {
  std::vector<ICameraConstrainer*>* cameraConstraints = new std::vector<ICameraConstrainer*>;
  SimpleCameraConstrainer* scc = new SimpleCameraConstrainer();
  cameraConstraints->push_back(scc);

  return cameraConstraints;
}

CameraRenderer* G3MCBuilder::createCameraRenderer() {
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

Renderer* G3MCBuilder::createBusyRenderer() {
  return new BusyMeshRenderer(Color::newFromRGBA(0, 0, 0, 1));
}


class G3MCSceneDescriptionBufferListener : public IBufferDownloadListener {
private:
  G3MCBuilder* _builder;

  Layer* parseLayer(const JSONObject* jsonBaseLayer) const {
    const std::string layerType = jsonBaseLayer->getAsString("layer", "<layer not present>");
    if (layerType.compare("OSM") == 0) {
      return new OSMLayer(TimeInterval::fromDays(30));
    }
    if (layerType.compare("MapQuest") == 0) {
      const std::string imagery = jsonBaseLayer->getAsString("imagery", "<imagery not present>");
      if (imagery.compare("OpenAerial") == 0) {
        return MapQuestLayer::newOpenAerial(TimeInterval::fromDays(30));
      }
      else if (imagery.compare("OSM") == 0) {
        return MapQuestLayer::newOSM(TimeInterval::fromDays(30));
      }
      else {
        ILogger::instance()->logError("Unsupported MapQuest imagery \"%s\"",
                                      imagery.c_str());
        return NULL;
      }
    }
    else {
      ILogger::instance()->logError("Unsupported layer type \"%s\"",
                                    layerType.c_str());
      return NULL;
    }
  }

public:
  G3MCSceneDescriptionBufferListener(G3MCBuilder* builder) :
  _builder(builder)
  {
    
  }
  

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {

    const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(buffer);

    if (jsonBaseObject == NULL) {
      ILogger::instance()->logError("Can't parse SceneJSON from %s",
                                    url.getPath().c_str());
    }
    else {
      const JSONObject* jsonObject = jsonBaseObject->asObject();
      if (jsonObject == NULL) {
        ILogger::instance()->logError("Invalid SceneJSON (1)");
      }
      else {
        const JSONString* error = jsonObject->getAsString("error");
        if (error == NULL) {

          const std::string user = jsonObject->getAsString("user",
                                                           "<user not present>");
          const std::string name = jsonObject->getAsString("name",
                                                           "<name not present>");

          const JSONObject* jsonBaseLayer = jsonObject->getAsObject("baseLayer");

          if (jsonBaseLayer == NULL) {
            ILogger::instance()->logError("Attribute 'baseLayer' not found in SceneJSON");
          }
          else {
            Layer* baseLayer = parseLayer(jsonBaseLayer);

            _builder->setBaseLayer(baseLayer);
          }
        }
        else {
          ILogger::instance()->logError("Server Error: %s",
                                        error->value().c_str());
        }
      }

      delete jsonBaseObject;
    }

    delete buffer;

    int __TODO_flag_initialization_task_as_initialized;
//    _initializationTask->setInitialized(true);
  }

  void onError(const URL& url) {
    ILogger::instance()->logError("Can't download SceneJSON from %s",
                                  url.getPath().c_str());
  }

  void onCancel(const URL& url) {
    // do nothing
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
    // do nothing
  }

};


class G3MCInitializationTask : public GInitializationTask {
private:
  G3MCBuilder* _builder;
  const URL    _sceneDescriptionURL;

  bool _isInitialized;

public:
  G3MCInitializationTask(G3MCBuilder* builder,
                         const URL& sceneDescriptionURL) :
  _builder(builder),
  _sceneDescriptionURL(sceneDescriptionURL),
  _isInitialized(false)
  {

  }

  void run(const G3MContext* context) {
    IDownloader* downloader = context->getDownloader();

    downloader->requestBuffer(_sceneDescriptionURL,
                              DownloadPriority::HIGHEST,
                              TimeInterval::zero(),
                              true,
                              new G3MCSceneDescriptionBufferListener(_builder),
                              true);
  }

  bool isDone(const G3MContext* context) {
    //return _isInitialized;
    int __FIX_IT;
    return true;
  }
};

void G3MCBuilder::recreateLayerSet() {
  if (_layerSet == NULL) {
    ILogger::instance()->logError("Can't recreate the LayerSet before creating the widget");
  }
  else {
    _layerSet->removeAllLayers();
    if (_baseLayer != NULL) {
      _layerSet->addLayer(_baseLayer);
    }
  }
}

void G3MCBuilder::setBaseLayer(Layer* baseLayer) {
  if (_baseLayer != baseLayer) {
    if (_baseLayer != NULL) {
      delete _baseLayer;
      _baseLayer = NULL;
    }
    _baseLayer = baseLayer;
  }
  recreateLayerSet();
}


const URL G3MCBuilder::createSceneDescriptionURL() const {
  std::string serverPath = _serverURL.getPath();

  return URL(serverPath + "/scenes/" + _sceneId, false);
}


GInitializationTask* G3MCBuilder::createInitializationTask() {
  return new G3MCInitializationTask(this, createSceneDescriptionURL());
}

std::vector<PeriodicalTask*>* G3MCBuilder::createPeriodicalTasks() {
  std::vector<PeriodicalTask*>* periodicalTasks = new std::vector<PeriodicalTask*>();

  return periodicalTasks;
}

IStorage* G3MCBuilder::getStorage() {
  if (_storage == NULL) {
    _storage = createStorage();
  }
  return _storage;
}


G3MWidget* G3MCBuilder::create() {
  if (_glob3Created) {
    ILogger::instance()->logError("The G3MWidget was already created, can't create more than one");
    return NULL;
  }
  _glob3Created = true;


  CompositeRenderer* mainRenderer = new CompositeRenderer();


  TileRenderer* tileRenderer = createTileRenderer();
  mainRenderer->addRenderer(tileRenderer);


  std::vector<ICameraConstrainer*>* cameraConstraints = createCameraConstraints();

  Color backgroundColor = Color::fromRGBA(0, 0, 0, 1);

  std::vector<PeriodicalTask*>* periodicalTasks = createPeriodicalTasks();

  G3MWidget * g3mWidget = G3MWidget::create(getGL(),
                                            getStorage(),
                                            createDownloader(),
                                            createThreadUtils(),
                                            createPlanet(),
                                            *cameraConstraints,
                                            createCameraRenderer(),
                                            mainRenderer,
                                            createBusyRenderer(),
                                            backgroundColor,
                                            false, // logFPS
                                            false, // logDownloaderStatistics
                                            createInitializationTask(),
                                            true, // autoDeleteInitializationTask
                                            *periodicalTasks);

  //  g3mWidget->setUserData(getUserData());

  delete cameraConstraints;
  delete periodicalTasks;

  return g3mWidget;
}
