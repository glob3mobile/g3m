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
class MapBooApplicationDescription;
class Color;
class GPUProgramManager;
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


class MapBooApplicationChangeListener {
public:
  virtual ~MapBooApplicationChangeListener() {

  }

  virtual void onNameChanged(const std::string& name) = 0;

  virtual void onDescriptionChanged(const std::string& description) = 0;

  virtual void onIconChanged(const std::string& icon) = 0;

//  virtual void onIdChanged(const std::string& applicationId) = 0;
//  virtual void onBaseLayerChanged(Layer* baseLayer) = 0;
//  virtual void onOverlayLayerChanged(Layer* overlayLayer) = 0;
//  virtual void onUserChanged(const std::string& user) = 0;
//  virtual void onBackgroundColorChanged(const Color& backgroundColor) = 0;

};


class MapBooBuilderApplicationsDescriptionsListener {
public:
  virtual ~MapBooBuilderApplicationsDescriptionsListener() {

  }

  virtual void onDownload(std::vector<MapBooApplicationDescription*>* ApplicationsDescriptions) = 0;

  virtual void onError() = 0;

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
  int         _applicationTimestamp;
//  Layer*      _applicationBaseLayer;
//  Layer*      _applicationOverlayLayer;
  std::string _applicationUser;
  std::string _applicationName;
  std::string _applicationDescription;
//  Color*      _applicationBackgroundColor;


  GL* _gl;
  G3MWidget* _g3mWidget;
  IStorage* _storage;

  IWebSocket* _applicationTubeWebSocket;
  bool        _isApplicationTubeOpen;

  LayerSet* _layerSet;
  PlanetRenderer* createPlanetRenderer();

  std::vector<ICameraConstrainer*>* createCameraConstraints();

  CameraRenderer* createCameraRenderer();

  Renderer* createBusyRenderer();

  std::vector<PeriodicalTask*>* createPeriodicalTasks();

  const URL createApplicationsDescriptionsURL() const;

  void recreateLayerSet();

  IThreadUtils* _threadUtils;
  IThreadUtils* getThreadUtils();

  IDownloader* _downloader;
  IDownloader* getDownloader();

  GPUProgramManager* _gpuProgramManager;
  GPUProgramManager* getGPUProgramManager();

  void resetApplication(const std::string& applicationId);

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
  void setApplicationBaseLayer(Layer* baseLayer);

  /** Private to G3M, don't call it */
  void setApplicationOverlayLayer(Layer* overlayLayer);

  /** Private to G3M, don't call it */
  void setApplicationUser(const std::string& user);

  /** Private to G3M, don't call it */
  void setApplicationName(const std::string& name);

  /** Private to G3M, don't call it */
  void setApplicationDescription(const std::string& description);

  /** Private to G3M, don't call it */
  void setApplicationBackgroundColor(const Color& backgroundColor);

  /** Private to G3M, don't call it */
  const URL createPollingApplicationDescriptionURL() const;

  /** Private to G3M, don't call it */
  const URL createApplicationTubeURL() const;

  /** Private to G3M, don't call it */
  void rawChangeApplication(const std::string& applicationId);

  /** Private to G3M, don't call it */
  void requestApplicationsDescriptions(MapBooBuilderApplicationsDescriptionsListener* listener,
                                 bool autoDelete = true);

  /** Private to G3M, don't call it */
  void parseApplicationDescription(const std::string& json,
                             const URL& url);

  /** Private to G3M, don't call it */
  void openApplicationTube(const G3MContext* context);

  void setApplicationTubeOpened(bool open);
  
  bool isApplicationTubeOpen() const {
    return _isApplicationTubeOpen;
  }
  
  void changeApplication(const std::string& applicationId);
};

#endif
