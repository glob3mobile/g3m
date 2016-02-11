//
//  MapBooOLDBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

#ifndef __G3MiOSSDK__MapBooOLDBuilder__
#define __G3MiOSSDK__MapBooOLDBuilder__

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
class MapBooOLD_Scene;
class ErrorRenderer;
class SceneLighting;
class G3MEventContext;
class Camera;
class Tile;
class MarksRenderer;
class MapBooOLDBuilder;
class Vector2I;
class URLTemplateLayer;
class Sector;
class IByteBuffer;
class IBufferDownloadListener;

#include <vector>
#include <string>
#include "URL.hpp"
#include "Color.hpp"
#include "Geodetic3D.hpp"
#include "HUDRenderer.hpp"
#include "InfoDisplay.hpp"
#include "HUDImageRenderer.hpp"
#include "GroupCanvasElement.hpp"
#include "IBufferDownloadListener.hpp"
class TileVisibilityTester;
class TileLODTester;



class MapBooOLDApplicationChangeListener {
public:
  virtual ~MapBooOLDApplicationChangeListener() {
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
                              MapBooOLD_Scene* scene) = 0;
  
  virtual void onScenesChanged(const G3MContext* context,
                               const std::vector<MapBooOLD_Scene*>& scenes) = 0;

  virtual void onCurrentSceneChanged(const G3MContext* context,
                              const std::string& sceneId,
                              const MapBooOLD_Scene* scene) = 0;

  virtual void onWebSocketOpen(const G3MContext* context) = 0;

  virtual void onWebSocketClose(const G3MContext* context) = 0;

  virtual void onTerrainTouch(MapBooOLDBuilder*         builder,
                              const G3MEventContext* ec,
                              const Vector2F&        pixel,
                              const Camera*          camera,
                              const Geodetic3D&      position,
                              const Tile*            tile) = 0;
  
  virtual void onFeatureInfoReceived(IByteBuffer* buffer) = 0;

};

class FeatureInfoDownloadListener : public IBufferDownloadListener {
private:
  MapBooOLDApplicationChangeListener* _applicationListener;
public:
  
  /**
   Callback method invoked on a successful download.  The buffer has to be deleted in C++ / .disposed() in Java
   */
  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    if (_applicationListener != NULL) {
      _applicationListener->onFeatureInfoReceived(buffer);
    }
  }
  
  /**
   Callback method invoke after an error trying to download url
   */
  void onError(const URL& url) {
    
  }
  
  /**
   Callback method invoke after canceled request
   */
  void onCancel(const URL& url) {
    
  }
  
  /**
   This method will be call, before onCancel, when the data arrived before the cancelation.
   
   The buffer WILL be deleted/disposed after the method finishs.  If you need to keep the buffer, use shallowCopy() to store a copy of the buffer.
   */
  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
    
  }
  
  FeatureInfoDownloadListener(MapBooOLDApplicationChangeListener* applicationListener) : _applicationListener(applicationListener)
  {
    
  }
  
  virtual ~FeatureInfoDownloadListener()
  {
#ifdef JAVA_CODE
    super.dispose();
#endif
    
  }
  
};


enum MapBooOLD_ViewType {
  VIEW_RUNTIME,
  VIEW_EDITION_PREVIEW,
  VIEW_PRESENTATION
};


class MapBooOLD_MultiImage_Level {
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
  MapBooOLD_MultiImage_Level(const URL& url,
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

  ~MapBooOLD_MultiImage_Level() {

  }

};


class MapBooOLD_MultiImage {
private:
  const Color                           _averageColor;
  std::vector<MapBooOLD_MultiImage_Level*> _levels;

public:
  MapBooOLD_MultiImage(const Color&                          averageColor,
                    std::vector<MapBooOLD_MultiImage_Level*> levels) :
  _averageColor(averageColor),
  _levels(levels)
  {
  }

  ~MapBooOLD_MultiImage() {
    const size_t levelsSize = _levels.size();
    for (size_t i = 0; i < levelsSize; i++) {
      MapBooOLD_MultiImage_Level* level = _levels[i];
      delete level;
    }
  }

  const Color getAverageColor() const {
    return _averageColor;
  }

  std::vector<MapBooOLD_MultiImage_Level*> getLevels() const {
    return _levels;
  }

  MapBooOLD_MultiImage_Level* getBestLevel(int width) const;

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  MapBooOLD_MultiImage* deepCopy() const {
    const Color averageColor = Color::fromRGBA(_averageColor._red, _averageColor._green, _averageColor._blue, _averageColor._alpha);
    std::vector<MapBooOLD_MultiImage_Level*> levels;
    const size_t levelsSize = _levels.size();
    for (size_t i = 0; i < levelsSize; i++) {
      const MapBooOLD_MultiImage_Level* level = _levels.at(i);
      levels.push_back(new MapBooOLD_MultiImage_Level(level->getUrl(), level->getWidth(), level->getHeight()));
    }
    
    return new MapBooOLD_MultiImage(averageColor, levels);
  }
};


class MapBooOLD_CameraPosition {
private:
  const Geodetic3D _position;
  const Angle      _heading;
  const Angle      _pitch;
  const bool       _animated;

public:
  MapBooOLD_CameraPosition(const Geodetic3D& position,
                        const Angle&      heading,
                        const Angle&      pitch,
                        const bool        animated) :
  _position(position),
  _heading(heading),
  _pitch(pitch),
  _animated(animated)
  {
  }

  ~MapBooOLD_CameraPosition() {

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


class MapBooOLD_Scene {
private:
  const std::string            _id;
  const std::string            _name;
  const std::string            _description;
  const MapBooOLD_MultiImage*     _screenshot;
  const Color                  _backgroundColor;
  const MapBooOLD_CameraPosition* _cameraPosition;
  const Sector*                _sector;
  Layer*                       _baseLayer;
  Layer*                       _overlayLayer;
  const bool                   _queryable;
  const bool                   _hasWarnings;

public:
  MapBooOLD_Scene(const std::string&           id,
               const std::string&           name,
               const std::string&           description,
               const MapBooOLD_MultiImage*     screenshot,
               const Color&                 backgroundColor,
               const MapBooOLD_CameraPosition* cameraPosition,
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

  const MapBooOLD_MultiImage* getScreenshot() const {
    return _screenshot;
  }

  Color getBackgroundColor() const {
    return _backgroundColor;
  }

  const MapBooOLD_CameraPosition* getCameraPosition() const {
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

  ~MapBooOLD_Scene();

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};

class MapBooOLD_Notification {
private:
  const Geodetic2D             _position;
  const MapBooOLD_CameraPosition* _cameraPosition;
  const std::string            _message;
  const URL*                   _iconURL;

public:

  MapBooOLD_Notification(const Geodetic2D&            position,
                      const MapBooOLD_CameraPosition* cameraPosition,
                      const std::string&           message,
                      const URL*                   iconURL) :
  _position(position),
  _cameraPosition(cameraPosition),
  _message(message),
  _iconURL(iconURL)
  {
  }

  ~MapBooOLD_Notification() {
    delete _iconURL;
    delete _cameraPosition;
  }

  const Geodetic2D getPosition() const {
    return _position;
  }

  const MapBooOLD_CameraPosition* getCameraPosition() const {
    return _cameraPosition;
  }

  const std::string getMessage() const {
    return _message;
  }

  const URL* getIconURL() const {
    return _iconURL;
  }

};


class HUDInfoRenderer_ImageFactory : public HUDImageRenderer::CanvasImageFactory {
private:
  std::vector<const Info*> _info;
  
protected:
  
  void drawOn(ICanvas* canvas,
              int width,
              int height);
  
  bool isEquals(const std::vector<const Info*>& v1,
                const std::vector<const Info*>& v2) const;
  
public:
  ~HUDInfoRenderer_ImageFactory() {
  }
  
  bool setInfo(const std::vector<const Info*>& info);
};

class MapBooOLD_HUDRenderer : public DefaultRenderer {
private:
  HUDImageRenderer* _hudImageRenderer;
public:
  MapBooOLD_HUDRenderer();
  
  ~MapBooOLD_HUDRenderer();
  
  void updateInfo(const std::vector<const Info*>& info);
  
  void initialize(const G3MContext* context);
  
  void render(const G3MRenderContext* rc,
              GLState* glState);
  
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height);
  
  void start(const G3MRenderContext* rc);
  
  void stop(const G3MRenderContext* rc);
  
  
  void onResume(const G3MContext* context);
  
  void onPause(const G3MContext* context);
  
  void onDestroy(const G3MContext* context);
};


class MapBooOLD_HUDRendererInfoDisplay : public InfoDisplay {
private:
  MapBooOLD_HUDRenderer* _mapBooHUDRenderer;
public:
  
  MapBooOLD_HUDRendererInfoDisplay(MapBooOLD_HUDRenderer* mapBooHUDRenderer):
  _mapBooHUDRenderer(mapBooHUDRenderer)
  {
    
  }
  
  void changedInfo(const std::vector<const Info*>& info) {
    _mapBooHUDRenderer->updateInfo(info);
    
  }
  
  void showDisplay() {
    _mapBooHUDRenderer->setEnable(true);
  }
  
  void hideDisplay() {
    _mapBooHUDRenderer->setEnable(false);
  }
  
  bool isShowing() {
    return _mapBooHUDRenderer->isEnable();
  }
  
#ifdef C_CODE
  virtual ~MapBooOLD_HUDRendererInfoDisplay() { }
#endif
#ifdef JAVA_CODE
  public void dispose() { }
#endif

};

class MapBooOLD_ErrorRenderer : public DefaultRenderer {
private:
  std::vector<std::string> _errors;
public:
  MapBooOLD_ErrorRenderer() {}
  ~MapBooOLD_ErrorRenderer() {};
  void setErrors(const std::vector<std::string>& errors);
  RenderState getRenderState(const G3MRenderContext* rc);
  void render(const G3MRenderContext* rc,
              GLState* glState) {}
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {}
};

class MapBooOLDBuilder {
private:

#ifdef C_CODE
  const URL         _serverURL;
  const URL         _tubesURL;
#endif
#ifdef JAVA_CODE
  private final URL _serverURL;
  private final URL _tubesURL;
#endif

  MapBooOLD_ViewType _viewType;

  MapBooOLDApplicationChangeListener* _applicationListener;
  
  FeatureInfoDownloadListener* _featureInfoDownloadListener;

  const bool _enableNotifications;

  std::string                _applicationId;
  std::string                _applicationName;
  std::string                _applicationWebsite;
  std::string                _applicationEMail;
  std::string                _applicationAbout;
  int                        _applicationTimestamp;
  std::vector<MapBooOLD_Scene*> _applicationScenes;
  std::string                _applicationCurrentSceneId;
  std::string                _lastApplicationCurrentSceneId;
  
  int                        _applicationEventId;
  const std::string          _token;

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
  
  TileLODTester*        _tileLODTester;
  TileVisibilityTester* _tileVisibilityTester;
    
  MapBooOLD_ErrorRenderer* _mbErrorRenderer;

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
                                  const bool transparent,
                                  const TimeInterval& timeToCache) const;


  MapBoxLayer* parseMapBoxLayer(const JSONObject* jsonLayer,
                                const TimeInterval& timeToCache) const;

  WMSLayer* parseWMSLayer(const JSONObject* jsonLayer,
                          const bool transparent) const;

  URLTemplateLayer* parseURLTemplateLayer(const JSONObject* jsonLayer,
                                          const bool transparent) const;

  const std::string getApplicationCurrentSceneId();
  const MapBooOLD_Scene* getApplicationCurrentScene();

  Color getCurrentBackgroundColor();

//  const std::string parseSceneId(const JSONObject* jsonObject) const;
  MapBooOLD_Scene* parseScene(const JSONObject* json) const;
  
  Color         parseColor(const JSONString* jsonColor) const;
  Sector*       parseSector(const JSONBaseObject* jsonBaseObjectLayer) const;

  MapBooOLD_MultiImage*       parseMultiImage(const JSONObject* jsonObject) const;
  MapBooOLD_MultiImage_Level* parseMultiImageLevel(const JSONObject* jsonObject) const;
  const MapBooOLD_CameraPosition* parseCameraPosition(const JSONObject* jsonObject) const;

  void changedCurrentScene();
  
  void updateVisibleScene(const bool cameraPositionChanged);

  const std::string getApplicationCurrentSceneCommand() const;

  const std::string getSendNotificationCommand(const Geodetic2D&  position,
                                               const Camera*      camera,
                                               const std::string& message,
                                               const URL*         iconURL) const;

  const std::string getSendNotificationCommand(const Geodetic2D&            position,
                                               const MapBooOLD_CameraPosition* cameraPosition,
                                               const std::string&           message,
                                               const URL*                   iconURL) const;

  const std::string escapeString(const std::string& str) const;

  const std::string toCameraPositionJSON(const Camera* camera) const;
  const std::string toCameraPositionJSON(const MapBooOLD_CameraPosition* cameraPosition) const;

  MapBooOLD_Notification* parseNotification(const JSONObject* jsonNotification) const;
  std::vector<MapBooOLD_Notification*>* parseNotifications(const JSONArray* jsonArray) const;

  void addApplicationNotification(MapBooOLD_Notification* notification);
  void addApplicationNotifications(const std::vector<MapBooOLD_Notification*>* notifications);

  const URL* parseURL(const JSONString* jsonString) const;

  MarksRenderer* _marksRenderer;
  MarksRenderer* getMarksRenderer();

  bool _hasParsedApplication;
  
  bool _initialParse;
  
  void fireOnScenesChanged();
  
  void setCameraPosition(const MapBooOLD_CameraPosition* cameraPosition, const bool animated);
  void setCameraPosition(const MapBooOLD_CameraPosition* cameraPosition);
  
  const std::string getViewAsString() const;
  
  const URL createApplicationCurrentSceneURL() const;

  const URL createGetFeatureInfoRestURL(const Tile* tile,
                                        const Vector2I& size,
                                        const Vector2I& pixel,
                                        const Geodetic3D& position);
protected:
  MapBooOLDBuilder(const URL& serverURL,
                const URL& tubesURL,
                const std::string& applicationId,
                MapBooOLD_ViewType viewType,
                MapBooOLDApplicationChangeListener* applicationListener,
                bool enableNotifications,
                const std::string& token);

  virtual ~MapBooOLDBuilder();

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

  const URL createApplicationPollURL() const;
  
  const Sector parseSector(const JSONObject* jsonObject, const std::string& paramName) const;
  
  TileLODTester* createDefaultTileLODTester() const;
  TileVisibilityTester* createDefaultTileVisibilityTester() const;

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
  void addApplicationScene(MapBooOLD_Scene* scene, const int position);

  /** Private to MapbooBuilder, don't call it */
  void deleteApplicationScene(const std::string& sceneId);
  
  /** Private to MapbooBuilder, don't call it */
  void setApplicationScenes(const std::vector<MapBooOLD_Scene*>& applicationScenes);

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
                      const Vector2F&        pixel,
                      const Camera*          camera,
                      const Geodetic3D&      position,
                      const Tile*            tile);

  /** Private to MapbooBuilder, don't call it */
  void setHasParsedApplication();

  /** Private to MapbooBuilder, don't call it */
  bool hasParsedApplication() const;


  const MapBooOLD_Notification* createNotification(const Geodetic2D&  position,
                                                const Camera*      camera,
                                                const std::string& message,
                                                const URL*         iconURL) const;

  void sendNotification(const Geodetic2D&  position,
                        const Camera*      camera,
                        const std::string& message,
                        const URL*         iconURL) const;

  void sendNotification(const Geodetic2D&            position,
                        const MapBooOLD_CameraPosition* cameraPosition,
                        const std::string&           message,
                        const URL*                   iconURL) const;

  void changeScene(const std::string& sceneId);
  
  void changeScene(const MapBooOLD_Scene* scene);
  
  
  const bool isQueryableCurrentScene() {
    return getApplicationCurrentScene()->isQueryable();
  }

  const URL getServerURL() const {
    return _serverURL;
  }
  
  const void requestGetFeatureInfo(const Tile* tile,
                                   const Vector2I& size,
                                   const Vector2I& pixel,
                                   const Geodetic3D& position);
  
  /** Private to MapbooBuilder, don't call it */
  void pollApplicationDataFromServer(const G3MContext* context);
  
  const std::string getApplicationId();
  
  void setTileLODTester(TileLODTester* tlt);
  
  TileLODTester* getTileLODTester();

  TileVisibilityTester* getTileVisibilityTester();

};

#endif
