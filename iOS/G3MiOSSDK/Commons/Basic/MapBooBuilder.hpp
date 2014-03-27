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
class ProtoRenderer;
class GInitializationTask;
class PeriodicalTask;
class Layer;
class LayerSet;
class GPUProgramManager;
class JSONBaseObject;
class JSONObject;
class JSONString;
class JSONArray;
class TimeInterval;
class MapQuestLayer;
class BingMapsLayer;
class CartoDBLayer;
class MapBoxLayer;
class WMSLayer;
class G3MContext;
class IWebSocket;
class MapBoo_Scene;
class ErrorRenderer;
class SceneLighting;
class G3MEventContext;
class Camera;
class Tile;
class MarksRenderer;
class MapBooBuilder;
class Vector2I;
class URLTemplateLayer;
class Sector;

#include <vector>
#include <string>
#include "URL.hpp"
#include "Color.hpp"
#include "Geodetic3D.hpp"


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
  
  virtual void onSceneChanged(const G3MContext* context,
                              MapBoo_Scene* scene) = 0;
  
  virtual void onScenesChanged(const G3MContext* context,
                               const std::vector<MapBoo_Scene*>& scenes) = 0;

  virtual void onCurrentSceneChanged(const G3MContext* context,
                              const std::string& sceneId,
                              const MapBoo_Scene* scene) = 0;

  virtual void onWebSocketOpen(const G3MContext* context) = 0;

  virtual void onWebSocketClose(const G3MContext* context) = 0;

  virtual void onTerrainTouch(MapBooBuilder*         builder,
                              const G3MEventContext* ec,
                              const Vector2I&        pixel,
                              const Camera*          camera,
                              const Geodetic3D&      position,
                              const Tile*            tile) = 0;

};


enum MapBoo_ViewType {
  VIEW_RUNTIME,
  VIEW_EDITION_PREVIEW,
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
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

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
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  MapBoo_MultiImage* deepCopy() const {
    const Color averageColor = Color::fromRGBA(_averageColor._red, _averageColor._green, _averageColor._blue, _averageColor._alpha);
    std::vector<MapBoo_MultiImage_Level*> levels;
    const int levelsSize = _levels.size();
    for (int i = 0; i < levelsSize; i++) {
      const MapBoo_MultiImage_Level* level = _levels.at(i);
      levels.push_back(new MapBoo_MultiImage_Level(level->getUrl(), level->getWidth(), level->getHeight()));
    }
    
    return new MapBoo_MultiImage(averageColor, levels);
  }
};


class MapBoo_CameraPosition {
private:
  const Geodetic3D _position;
  const Angle      _heading;
  const Angle      _pitch;
  const bool       _animated;

public:
  MapBoo_CameraPosition(const Geodetic3D& position,
                        const Angle&      heading,
                        const Angle&      pitch,
                        const bool        animated) :
  _position(position),
  _heading(heading),
  _pitch(pitch),
  _animated(animated)
  {
  }

  ~MapBoo_CameraPosition() {

  }

  const Geodetic3D getPosition() const {
    return _position;
  }

  const Angle getHeading() const {
    return _heading;
  }

  const Angle getPitch() const {
    return _pitch;
  }

  bool isAnimated() const {
    return _animated;
  }

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};


class MapBoo_Scene {
private:
  const std::string            _id;
  const std::string            _name;
  const std::string            _description;
  const MapBoo_MultiImage*     _screenshot;
  const Color                  _backgroundColor;
  const MapBoo_CameraPosition* _cameraPosition;
  const Sector*                _sector;
  Layer*                       _baseLayer;
  Layer*                       _overlayLayer;
  const bool                   _queryable;
  const bool                   _hasWarnings;

public:
  MapBoo_Scene(const std::string&           id,
               const std::string&           name,
               const std::string&           description,
               const MapBoo_MultiImage*     screenshot,
               const Color&                 backgroundColor,
               const MapBoo_CameraPosition* cameraPosition,
               const Sector*                sector,
               Layer*                       baseLayer,
               Layer*                       overlayLayer,
               const bool                   queryable,
               const bool                   hasWarnings) :
  _id(id),
  _name(name),
  _description(description),
  _screenshot(screenshot),
  _backgroundColor(backgroundColor),
  _cameraPosition(cameraPosition),
  _sector(sector),
  _baseLayer(baseLayer),
  _overlayLayer(overlayLayer),
  _queryable(queryable),
  _hasWarnings(hasWarnings)
  {
  }

  const std::string getId() const {
    return _id;
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

  const MapBoo_CameraPosition* getCameraPosition() const {
    return _cameraPosition;
  }

  const Sector* getSector() const {
    return _sector;
  }
  
  Layer* getBaseLayer() const {
    return _baseLayer;
  }
  
  Layer* getOverlayLayer() const {
    return _overlayLayer;
  }

  bool isQueryable() const {
    return _queryable;

  }

  bool hasWarnings() const {
    return _hasWarnings;
  }

  LayerSet* createLayerSet() const;

  ~MapBoo_Scene();

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};

class MapBoo_Notification {
private:
  const Geodetic2D             _position;
  const MapBoo_CameraPosition* _cameraPosition;
  const std::string            _message;
  const URL*                   _iconURL;

public:

  MapBoo_Notification(const Geodetic2D&            position,
                      const MapBoo_CameraPosition* cameraPosition,
                      const std::string&           message,
                      const URL*                   iconURL) :
  _position(position),
  _cameraPosition(cameraPosition),
  _message(message),
  _iconURL(iconURL)
  {
  }

  ~MapBoo_Notification() {
    delete _iconURL;
    delete _cameraPosition;
  }

  const Geodetic2D getPosition() const {
    return _position;
  }

  const MapBoo_CameraPosition* getCameraPosition() const {
    return _cameraPosition;
  }

  const std::string getMessage() const {
    return _message;
  }

  const URL* getIconURL() const {
    return _iconURL;
  }

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

  MapBooApplicationChangeListener* _applicationListener;

  const bool _enableNotifications;

  std::string                _applicationId;
  std::string                _applicationName;
  std::string                _applicationWebsite;
  std::string                _applicationEMail;
  std::string                _applicationAbout;
  int                        _applicationTimestamp;
  std::vector<MapBoo_Scene*> _applicationScenes;
  std::string                _applicationCurrentSceneId;
  std::string                _lastApplicationCurrentSceneId;
  
  int                        _applicationEventId;

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

  std::vector<ICameraConstrainer*>* createCameraConstraints(const Planet* planet,
                                                            PlanetRenderer* planetRenderer);

  CameraRenderer* createCameraRenderer();

  ProtoRenderer* createBusyRenderer();

  ErrorRenderer* createErrorRenderer();

  std::vector<PeriodicalTask*>* createPeriodicalTasks();

  void recreateLayerSet();

  IThreadUtils* _threadUtils;
  IThreadUtils* getThreadUtils();

  IDownloader* _downloader;
  IDownloader* getDownloader();

  GPUProgramManager* _gpuProgramManager;
  GPUProgramManager* getGPUProgramManager();


  Layer* parseLayer(const JSONBaseObject* jsonBaseObjectLayer) const;

  MapQuestLayer* parseMapQuestLayer(const JSONObject* jsonLayer,
                                    const TimeInterval& timeToCache) const;

  BingMapsLayer* parseBingMapsLayer(const JSONObject* jsonLayer,
                                    const TimeInterval& timeToCache) const;

  CartoDBLayer* parseCartoDBLayer(const JSONObject* jsonLayer,
                                  const TimeInterval& timeToCache) const;


  MapBoxLayer* parseMapBoxLayer(const JSONObject* jsonLayer,
                                const TimeInterval& timeToCache) const;

  WMSLayer* parseWMSLayer(const JSONObject* jsonLayer) const;

  URLTemplateLayer* parseURLTemplateLayer(const JSONObject* jsonLayer) const;

  const std::string getApplicationCurrentSceneId();
  const MapBoo_Scene* getApplicationCurrentScene();

  Color getCurrentBackgroundColor();

//  const std::string parseSceneId(const JSONObject* jsonObject) const;
  MapBoo_Scene* parseScene(const JSONObject* json) const;
  
  Color         parseColor(const JSONString* jsonColor) const;
  Sector*       parseSector(const JSONBaseObject* jsonBaseObjectLayer) const;

  MapBoo_MultiImage*       parseMultiImage(const JSONObject* jsonObject) const;
  MapBoo_MultiImage_Level* parseMultiImageLevel(const JSONObject* jsonObject) const;
  const MapBoo_CameraPosition* parseCameraPosition(const JSONObject* jsonObject) const;

  void changedCurrentScene();
  
  void updateVisibleScene(const bool cameraPositionChanged);

  const std::string getApplicationCurrentSceneCommand() const;

  const std::string getSendNotificationCommand(const Geodetic2D&  position,
                                               const Camera*      camera,
                                               const std::string& message,
                                               const URL*         iconURL) const;

  const std::string getSendNotificationCommand(const Geodetic2D&            position,
                                               const MapBoo_CameraPosition* cameraPosition,
                                               const std::string&           message,
                                               const URL*                   iconURL) const;

  const std::string escapeString(const std::string& str) const;

  const std::string toCameraPositionJSON(const Camera* camera) const;
  const std::string toCameraPositionJSON(const MapBoo_CameraPosition* cameraPosition) const;

  MapBoo_Notification* parseNotification(const JSONObject* jsonNotification) const;
  std::vector<MapBoo_Notification*>* parseNotifications(const JSONArray* jsonArray) const;

  void addApplicationNotification(MapBoo_Notification* notification);
  void addApplicationNotifications(const std::vector<MapBoo_Notification*>* notifications);

  const URL* parseURL(const JSONString* jsonString) const;

  MarksRenderer* _marksRenderer;
  MarksRenderer* getMarksRenderer();

  bool _hasParsedApplication;
  
  bool _initialParse;
  
  void fireOnScenesChanged();
  
  void setCameraPosition(const MapBoo_CameraPosition* cameraPosition, const bool animated);
  void setCameraPosition(const MapBoo_CameraPosition* cameraPosition);

protected:
  MapBooBuilder(const URL& serverURL,
                const URL& tubesURL,
                const std::string& applicationId,
                MapBoo_ViewType viewType,
                MapBooApplicationChangeListener* applicationListener,
                bool enableNotifications);

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

  const URL createApplicationRestURL() const;

public:
  /** Private to MapbooBuilder, don't call it */
  int getApplicationEventId() const;
  
  /** Private to MapbooBuilder, don't call it */
  void setApplicationEventId(const int eventId);
  
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
  void addApplicationScene(MapBoo_Scene* scene, const int position);

  /** Private to MapbooBuilder, don't call it */
  void deleteApplicationScene(const std::string& sceneId);
  
  /** Private to MapbooBuilder, don't call it */
  void setApplicationScenes(const std::vector<MapBoo_Scene*>& applicationScenes);

  /** Private to MapbooBuilder, don't call it */
  void saveApplicationData() const;

  /** Private to MapbooBuilder, don't call it */
  const URL createApplicationTubeURL() const;
  
  /** Private to MapbooBuilder, don't call it */
  void parseApplicationJSON(const std::string& json,
                            const URL& url);
  
  /** Private to MapbooBuilder, don't call it */
  void parseApplicationJSON(const JSONObject* jsonBaseObjectLayer,
                            const URL& url);
  
  /** Private to MapbooBuilder, don't call it */
  void parseApplicationEventsJSON(const std::string& json,
                            const URL& url);

  /** Private to MapbooBuilder, don't call it */
  void parseSceneEventAndUpdateScene(const JSONObject* jsonObject);
                                     
  /** Private to MapbooBuilder, don't call it */
  void openApplicationTube(const G3MContext* context);

  /** Private to MapbooBuilder, don't call it */
  void setApplicationCurrentSceneId(const std::string& currentSceneId);

  /** Private to MapbooBuilder, don't call it */
  void rawChangeScene(const std::string& sceneId);

  /** Private to MapbooBuilder, don't call it */
  void setContext(const G3MContext* context);

  /** Private to MapbooBuilder, don't call it */
  void setApplicationTubeOpened(bool open);

  /** Private to MapbooBuilder, don't call it */
  bool isApplicationTubeOpen() const {
    return _isApplicationTubeOpen;
  }

  /** Private to MapbooBuilder, don't call it */
  bool onTerrainTouch(const G3MEventContext* ec,
                      const Vector2I&        pixel,
                      const Camera*          camera,
                      const Geodetic3D&      position,
                      const Tile*            tile);

  /** Private to MapbooBuilder, don't call it */
  void setHasParsedApplication();

  /** Private to MapbooBuilder, don't call it */
  bool hasParsedApplication() const;


  const MapBoo_Notification* createNotification(const Geodetic2D&  position,
                                                const Camera*      camera,
                                                const std::string& message,
                                                const URL*         iconURL) const;

  void sendNotification(const Geodetic2D&  position,
                        const Camera*      camera,
                        const std::string& message,
                        const URL*         iconURL) const;

  void sendNotification(const Geodetic2D&            position,
                        const MapBoo_CameraPosition* cameraPosition,
                        const std::string&           message,
                        const URL*                   iconURL) const;

  void changeScene(const std::string& sceneId);
  
  void changeScene(const MapBoo_Scene* scene);
  
  
  const bool isQueryableCurrentScene() {
    return getApplicationCurrentScene()->isQueryable();
  }

  const URL getServerURL() const {
    return _serverURL;
  }
  
  const URL createGetFeatureInfoRestURL(const Tile* tile,
                                        const Vector2I& size,
                                        const Vector2I& pixel,
                                        const Geodetic3D& position);

  /** Private to MapbooBuilder, don't call it */
  void pollApplicationDataFromServer(const G3MContext* context);
};

#endif
