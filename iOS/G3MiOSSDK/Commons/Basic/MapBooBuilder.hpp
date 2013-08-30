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

class SceneLighting;

#include "URL.hpp"
#include "Color.hpp"

#include <vector>
#include <string>


class MapBooApplicationChangeListener {
public:
  virtual ~MapBooApplicationChangeListener() {
  }

  virtual void onNameChanged(const G3MContext* context,
                             const std::string& name) = 0;

  virtual void onWebsiteChanged(const G3MContext* context,
                                const std::string& website) = 0;

  virtual void onEMailChanged(const G3MContext* context,
                              const std::string& eMail) = 0;

  virtual void onAboutChanged(const G3MContext* context,
                              const std::string& about) = 0;

  virtual void onIconChanged(const G3MContext* context,
                             const std::string& icon) = 0;

  virtual void onScenesChanged(const G3MContext* context,
                               const std::vector<MapBoo_Scene*>& scenes) = 0;

  virtual void onSceneChanged(const G3MContext* context,
                              int sceneIndex,
                              const MapBoo_Scene* scene) = 0;

};


enum MapBoo_ViewType {
  VIEW_RUNTIME,
  VIEW_PRESENTATION
};


class MapBoo_MultiImage_Level {
private:
#ifdef C_CODE
  const URL _url;
#endif
#ifdef JAVA_CODE
  private final URL _url;
#endif
  const int _width;
  const int _height;

public:
  MapBoo_MultiImage_Level(const URL& url,
                          int width,
                          int height) :
  _url(url),
  _width(width),
  _height(height)
  {
  }

  const URL getUrl() const {
    return _url;
  }

  int getWidth() const {
    return _width;
  }

  int getHeight() const {
    return _height;
  }

  const std::string description() const;

  ~MapBoo_MultiImage_Level() {

  }

};


class MapBoo_MultiImage {
private:
  const Color                           _averageColor;
  std::vector<MapBoo_MultiImage_Level*> _levels;

public:
  MapBoo_MultiImage(const Color&                          averageColor,
                    std::vector<MapBoo_MultiImage_Level*> levels) :
  _averageColor(averageColor),
  _levels(levels)
  {
  }

  ~MapBoo_MultiImage() {
    const int levelsSize = _levels.size();
    for (int i = 0; i < levelsSize; i++) {
      MapBoo_MultiImage_Level* level = _levels[i];
      delete level;
    }
  }

  const Color getAverageColor() const {
    return _averageColor;
  }

  std::vector<MapBoo_MultiImage_Level*> getLevels() const {
    return _levels;
  }

  MapBoo_MultiImage_Level* getBestLevel(int width) const;

  const std::string description() const;

};


class MapBoo_Scene {
private:
  const std::string        _name;
  const std::string        _description;
  const MapBoo_MultiImage* _screenshot;
  const Color              _backgroundColor;
  Layer*                   _baseLayer;
  Layer*                   _overlayLayer;

  const bool               _hasWarnings;

public:
  MapBoo_Scene(const std::string& name,
               const std::string& description,
               MapBoo_MultiImage* screenshot,
               const Color&       backgroundColor,
               Layer*             baseLayer,
               Layer*             overlayLayer,
               const bool         hasWarnings) :
  _name(name),
  _description(description),
  _screenshot(screenshot),
  _backgroundColor(backgroundColor),
  _baseLayer(baseLayer),
  _overlayLayer(overlayLayer),
  _hasWarnings(hasWarnings)
  {
  }

  const std::string getName() const {
    return _name;
  }

  const std::string getDescription() const {
    return _description;
  }

  const MapBoo_MultiImage* getScreenshot() const {
    return _screenshot;
  }

  Color getBackgroundColor() const {
    return _backgroundColor;
  }

  bool hasWarnings() const {
    return _hasWarnings;
  }

  void fillLayerSet(LayerSet* layerSet) const;

  ~MapBoo_Scene();

  const std::string description() const;

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

  MapBoo_ViewType _viewType;

  const bool _useWebSockets;

  MapBooApplicationChangeListener* _applicationListener;

  std::string _applicationId;
  std::string _applicationName;
  std::string _applicationWebsite;
  std::string _applicationEMail;
  std::string _applicationAbout;
  int         _applicationTimestamp;

  std::vector<MapBoo_Scene*> _applicationScenes;
  int                        _applicationCurrentSceneIndex;
  int                        _lastApplicationCurrentSceneIndex;
  
  GL* _gl;
  G3MWidget* _g3mWidget;
  IStorage*  _storage;

  IWebSocket* _webSocket;

#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

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

  GInitializationTask* createInitializationTask();



  Layer* parseLayer(const JSONBaseObject* jsonBaseObjectLayer) const;

  MapQuestLayer* parseMapQuestLayer(const JSONObject* jsonLayer,
                                    const TimeInterval& timeToCache) const;

  BingMapsLayer* parseBingMapsLayer(const JSONObject* jsonLayer,
                                    const TimeInterval& timeToCache);

  CartoDBLayer* parseCartoDBLayer(const JSONObject* jsonLayer,
                                  const TimeInterval& timeToCache) const;

  BingMapsLayer* parseBingMapsLayer(const JSONObject* jsonLayer,
                                    const TimeInterval& timeToCache) const;

  MapBoxLayer* parseMapBoxLayer(const JSONObject* jsonLayer,
                                const TimeInterval& timeToCache) const;

  WMSLayer* parseWMSLayer(const JSONObject* jsonLayer) const;


  const int getApplicationCurrentSceneIndex();
  const MapBoo_Scene* getApplicationCurrentScene();

  Color getCurrentBackgroundColor();

  MapBoo_Scene* parseScene(const JSONObject* json) const;
  Color         parseColor(const JSONString* jsonColor) const;

  MapBoo_MultiImage*       parseMultiImage(const JSONObject* jsonObject) const;
  MapBoo_MultiImage_Level* parseMultiImageLevel(const JSONObject* jsonObject) const;

  void changedCurrentScene();

  const std::string getApplicationCurrentSceneCommand() const;


protected:
  MapBooBuilder(const URL& serverURL,
                const URL& tubesURL,
                bool useWebSockets,
                const std::string& applicationId,
                MapBoo_ViewType viewType,
                MapBooApplicationChangeListener* ApplicationListener);

  virtual ~MapBooBuilder();

  void setGL(GL *gl);

  GL* getGL();

  G3MWidget* create();

  const Planet* createPlanet();

  IStorage* getStorage();

  virtual IStorage* createStorage() = 0;

  virtual IDownloader* createDownloader() = 0;

  virtual IThreadUtils* createThreadUtils() = 0;

  virtual GPUProgramManager* createGPUProgramManager() = 0;

  SceneLighting* createSceneLighting();

public:
  /** Private to MapbooBuilder, don't call it */
  int getApplicationTimestamp() const;

  /** Private to MapbooBuilder, don't call it */
  void setApplicationTimestamp(const int timestamp);

  /** Private to MapbooBuilder, don't call it */
  void setApplicationName(const std::string& name);

  /** Private to MapbooBuilder, don't call it */
  void setApplicationWebsite(const std::string& website);

  /** Private to MapbooBuilder, don't call it */
  void setApplicationEMail(const std::string& eMail);

  /** Private to MapbooBuilder, don't call it */
  void setApplicationAbout(const std::string& about);

  /** Private to MapbooBuilder, don't call it */
  void setApplicationScenes(const std::vector<MapBoo_Scene*>& applicationScenes);

  /** Private to MapbooBuilder, don't call it */
  const URL createPollingApplicationDescriptionURL() const;

  /** Private to MapbooBuilder, don't call it */
  const URL createApplicationTubeURL() const;

  /** Private to MapbooBuilder, don't call it */
  void parseApplicationJSON(const std::string& json,
                            const URL& url);

  /** Private to MapbooBuilder, don't call it */
  void openApplicationTube(const G3MContext* context);

  /** Private to MapbooBuilder, don't call it */
  void setApplicationCurrentSceneIndex(int currentSceneIndex);

  /** Private to MapbooBuilder, don't call it */
  void rawChangeScene(int sceneIndex);

  /** Private to MapbooBuilder, don't call it */
  void setContext(const G3MContext* context);

  /** Private to MapbooBuilder, don't call it */
  void setApplicationTubeOpened(bool open);

  /** Private to MapbooBuilder, don't call it */
  bool isApplicationTubeOpen() const {
    return _isApplicationTubeOpen;
  }
  
  void changeScene(int sceneIndex);
  
  void changeScene(const MapBoo_Scene* scene);
  
};

#endif
