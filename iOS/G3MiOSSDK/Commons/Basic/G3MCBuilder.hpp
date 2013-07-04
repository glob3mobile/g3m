//
//  G3MCBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

#ifndef __G3MiOSSDK__G3MCBuilder__
#define __G3MiOSSDK__G3MCBuilder__

#include <stddef.h>

class GL;
class G3MWidget;
class TileRenderer;
class IStorage;
class IDownloader;
class IThreadUtils;
class Planet;
class ICameraConstrainer;
class CameraRenderer;
class Renderer;
class GInitializationTask;
class PeriodicalTask;
class Layer;
class LayerSet;
class G3MCSceneDescription;
class Color;
class JSONBaseObject;
class JSONObject;
class TimeInterval;
class MapQuestLayer;
class BingMapsLayer;
class CartoDBLayer;
class MapBoxLayer;
class WMSLayer;
class G3MContext;
class IWebSocket;

#include "URL.hpp"

#include <vector>
#include <string>


class G3MCSceneChangeListener {
public:
  virtual ~G3MCSceneChangeListener() {

  }

  virtual void onSceneChanged(const std::string& sceneId) = 0;

  virtual void onBaseLayerChanged(Layer* baseLayer) = 0;
  
  virtual void onOverlayLayerChanged(Layer* overlayLayer) = 0;

  virtual void onUserChanged(const std::string& user) = 0;

  virtual void onNameChanged(const std::string& name) = 0;

  virtual void onDescriptionChanged(const std::string& description) = 0;

  virtual void onBackgroundColorChanged(const Color& backgroundColor) = 0;
  
};


class G3MCBuilderScenesDescriptionsListener {
public:
  virtual ~G3MCBuilderScenesDescriptionsListener() {

  }

  virtual void onDownload(std::vector<G3MCSceneDescription*>* scenesDescriptions) = 0;

  virtual void onError() = 0;
  
};


class G3MCBuilder {
private:

#ifdef C_CODE
  const URL         _serverURL;
  const URL         _tubesURL;
#endif
#ifdef JAVA_CODE
  private final URL _serverURL;
  private final URL _tubesURL;
#endif

  const bool _useWebSockets;

  G3MCSceneChangeListener* _sceneListener;

  std::string _sceneId;
  int         _sceneTimestamp;
  Layer*      _sceneBaseLayer;
  Layer*      _sceneOverlayLayer;
  std::string _sceneUser;
  std::string _sceneName;
  std::string _sceneDescription;
  Color*      _sceneBackgroundColor;


  GL* _gl;
  G3MWidget* _g3mWidget;
  IStorage* _storage;

  IWebSocket* _sceneTubeWebSocket;
  bool        _isSceneTubeOpen;

  LayerSet* _layerSet;
  TileRenderer* createTileRenderer();

  std::vector<ICameraConstrainer*>* createCameraConstraints();
  
  CameraRenderer* createCameraRenderer();

  Renderer* createBusyRenderer();

  std::vector<PeriodicalTask*>* createPeriodicalTasks();

  const URL createScenesDescriptionsURL() const;

  void recreateLayerSet();

  IThreadUtils* _threadUtils;
  IThreadUtils* getThreadUtils();

  IDownloader* _downloader;
  IDownloader* getDownloader();

  void resetScene(const std::string& sceneId);

  void resetG3MWidget();

  GInitializationTask* createInitializationTask();



  Layer* parseLayer(const JSONBaseObject* jsonBaseObjectLayer) const;
  
  MapQuestLayer* parseMapQuestLayer(const JSONObject* jsonBaseLayer,
                                    const TimeInterval& timeToCache) const;

  BingMapsLayer* parseBingMapsLayer(const JSONObject* jsonBaseLayer,
                                    const TimeInterval& timeToCache);

  CartoDBLayer* parseCartoDBLayer(const JSONObject* jsonBaseLayer,
                                  const TimeInterval& timeToCache) const;

  BingMapsLayer* parseBingMapsLayer(const JSONObject* jsonBaseLayer,
                                    const TimeInterval& timeToCache) const;

  MapBoxLayer* parseMapBoxLayer(const JSONObject* jsonBaseLayer,
                                const TimeInterval& timeToCache) const;

  WMSLayer* parseWMSLayer(const JSONObject* jsonBaseLayer) const;

protected:
  G3MCBuilder(const URL& serverURL,
              const URL& tubesURL,
              bool useWebSockets,
              const std::string& sceneId,
              G3MCSceneChangeListener* sceneListener);
  
  virtual ~G3MCBuilder() {
  }

  void setGL(GL *gl);

  GL* getGL();

  G3MWidget* create();

  const Planet* createPlanet();

  IStorage* getStorage();

  virtual IStorage* createStorage() = 0;

  virtual IDownloader* createDownloader() = 0;

  virtual IThreadUtils* createThreadUtils() = 0;
  
public:
  /** Private to G3M, don't call it */
  int getSceneTimestamp() const;

  /** Private to G3M, don't call it */
  void setSceneTimestamp(const int timestamp);

  /** Private to G3M, don't call it */
  void setSceneBaseLayer(Layer* baseLayer);
  
  /** Private to G3M, don't call it */
  void setSceneOverlayLayer(Layer* overlayLayer);

  /** Private to G3M, don't call it */
  void setSceneUser(const std::string& user);

  /** Private to G3M, don't call it */
  void setSceneName(const std::string& name);

  /** Private to G3M, don't call it */
  void setSceneDescription(const std::string& description);

  /** Private to G3M, don't call it */
  void setSceneBackgroundColor(const Color& backgroundColor);

  /** Private to G3M, don't call it */
  const URL createPollingSceneDescriptionURL() const;

  /** Private to G3M, don't call it */
  const URL createSceneTubeURL() const;

  /** Private to G3M, don't call it */
  void rawChangeScene(const std::string& sceneId);

  /** Private to G3M, don't call it */
  void requestScenesDescriptions(G3MCBuilderScenesDescriptionsListener* listener,
                                 bool autoDelete = true);

  /** Private to G3M, don't call it */
  void parseSceneDescription(const std::string& json,
                             const URL& url);

  /** Private to G3M, don't call it */
  void openSceneTube(const G3MContext* context);

  void setSceneTubeOpened(bool open);

  bool isSceneTubeOpen() const {
    return _isSceneTubeOpen;
  }

  void changeScene(const std::string& sceneId);
};

#endif
