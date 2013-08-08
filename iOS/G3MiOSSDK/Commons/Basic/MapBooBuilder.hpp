//
//  MapBooBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

#ifndef __G3MiOSSDK__MapBooBuilder__
#define __G3MiOSSDK__MapBooBuilder__

#include <stddef.h>

class GL;
class G3MWidget;
class PlanetRenderer;
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
class GPUProgramManager;
class JSONBaseObject;
class JSONObject;
class JSONString;
class TimeInterval;
class MapQuestLayer;
class BingMapsLayer;
class CartoDBLayer;
class MapBoxLayer;
class WMSLayer;
class G3MContext;
class IWebSocket;
class MapBoo_Scene;

#include "URL.hpp"
#include "Color.hpp"

#include <vector>
#include <string>


class MapBooApplicationChangeListener {
public:
  virtual ~MapBooApplicationChangeListener() {

  }

  virtual void onNameChanged(const std::string& name) = 0;

  virtual void onDescriptionChanged(const std::string& description) = 0;

  virtual void onIconChanged(const std::string& icon) = 0;

  virtual void onScenesChanged(const std::vector<MapBoo_Scene*>& applicationScenes) = 0;

//  virtual void onWarningsChanged() = 0;

};

class MapBoo_Scene {
private:
  const std::string _name;
  const std::string _description;
  const std::string _icon;
  const Color       _backgroundColor;
  Layer*            _baseLayer;
  Layer*            _overlayLayer;

public:
  MapBoo_Scene(const std::string& name,
               const std::string& description,
               const std::string& icon,
               const Color&       backgroundColor,
               Layer*             baseLayer,
               Layer*             overlayLayer) :
  _name(name),
  _description(description),
  _icon(icon),
  _backgroundColor(backgroundColor),
  _baseLayer(baseLayer),
  _overlayLayer(overlayLayer)
  {
  }

  const std::string getName() const {
    return _name;
  }

  const std::string getDescription() const {
    return _description;
  }

  const std::string getIcon() const {
    return _icon;
  }

  Color getBackgroundColor() const {
    return _backgroundColor;
  }

  void recreateLayerSet(LayerSet* layerSet) const;

  ~MapBoo_Scene();
};


class MapBooBuilder {
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

  MapBooApplicationChangeListener* _applicationListener;

  std::string _applicationId;
  std::string _applicationName;
  std::string _applicationDescription;
  int         _applicationTimestamp;

  std::vector<MapBoo_Scene*> _applicationScenes;
  int                        _applicationCurrentSceneIndex;
  int                        _applicationDefaultSceneIndex;

  GL* _gl;
  G3MWidget* _g3mWidget;
  IStorage* _storage;

//  IWebSocket* _applicationTubeWebSocket;
  bool        _isApplicationTubeOpen;

  LayerSet* _layerSet;
  PlanetRenderer* createPlanetRenderer();

  std::vector<ICameraConstrainer*>* createCameraConstraints();

  CameraRenderer* createCameraRenderer();

  Renderer* createBusyRenderer();

  std::vector<PeriodicalTask*>* createPeriodicalTasks();

  void recreateLayerSet();

  IThreadUtils* _threadUtils;
  IThreadUtils* getThreadUtils();

  IDownloader* _downloader;
  IDownloader* getDownloader();

  GPUProgramManager* _gpuProgramManager;
  GPUProgramManager* getGPUProgramManager();

//  void resetApplication(const std::string& applicationId);

//  void resetG3MWidget();

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


  const int getApplicationCurrentSceneIndex();
  const MapBoo_Scene* getApplicationCurrentScene();

  Color getCurrentBackgroundColor();

  MapBoo_Scene* parseScene(const JSONObject* json) const;
  Color         parseColor(const JSONString* jsonColor) const;

protected:
  MapBooBuilder(const URL& serverURL,
                const URL& tubesURL,
                bool useWebSockets,
                const std::string& applicationId,
                MapBooApplicationChangeListener* ApplicationListener);

  virtual ~MapBooBuilder() {
  }

  void setGL(GL *gl);

  GL* getGL();

  G3MWidget* create();

  const Planet* createPlanet();

  IStorage* getStorage();

  virtual IStorage* createStorage() = 0;

  virtual IDownloader* createDownloader() = 0;

  virtual IThreadUtils* createThreadUtils() = 0;

  virtual GPUProgramManager* createGPUProgramManager() = 0;

public:
  /** Private to G3M, don't call it */
  int getApplicationTimestamp() const;

  /** Private to G3M, don't call it */
  void setApplicationTimestamp(const int timestamp);

  /** Private to G3M, don't call it */
  void setApplicationName(const std::string& name);

  /** Private to G3M, don't call it */
  void setApplicationDescription(const std::string& description);

  /** Private to G3M, don't call it */
  void setApplicationScenes(const std::vector<MapBoo_Scene*>& applicationScenes);

  /** Private to G3M, don't call it */
  const URL createPollingApplicationDescriptionURL() const;

  /** Private to G3M, don't call it */
  const URL createApplicationTubeURL() const;

//  /** Private to G3M, don't call it */
//  void rawChangeApplication(const std::string& applicationId);

  /** Private to G3M, don't call it */
  void parseApplicationDescription(const std::string& json,
                                   const URL& url);

  /** Private to G3M, don't call it */
  void openApplicationTube(const G3MContext* context);

  void setApplicationTubeOpened(bool open);
  
  bool isApplicationTubeOpen() const {
    return _isApplicationTubeOpen;
  }
  
//  void changeApplication(const std::string& applicationId);
};

#endif
