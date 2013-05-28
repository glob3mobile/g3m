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
class ICameraActivityListener;
class Planet;
class ICameraConstrainer;
class CameraRenderer;
class Renderer;
class GInitializationTask;
class PeriodicalTask;
class Layer;
class LayerSet;
class G3MCSceneDescription;

#include "URL.hpp"

#include <vector>
#include <string>


class G3MCSceneChangeListener {
public:
  virtual ~G3MCSceneChangeListener() {

  }

  virtual void onBaseLayerChanged(Layer* baseLayer) = 0;

  virtual void onUserChanged(const std::string& user) = 0;

  virtual void onNameChanged(const std::string& name) = 0;
  
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
#endif
#ifdef JAVA_CODE
  private final URL _serverURL;
#endif

  G3MCSceneChangeListener* _sceneListener;

  int _sceneTimestamp;
  std::string _sceneId;
  std::string _sceneUser;
  std::string _sceneName;

  Layer* _baseLayer;

  GL* _gl;
//  bool _glob3Created;
  G3MWidget* _g3mWidget;
  IStorage* _storage;

  LayerSet* _layerSet;
  TileRenderer* createTileRenderer();

  std::vector<ICameraConstrainer*>* createCameraConstraints();
  
  CameraRenderer* createCameraRenderer();

  Renderer* createBusyRenderer();

  std::vector<PeriodicalTask*>* createPeriodicalTasks();

  const URL createScenesDescriptionsURL() const;

//  LayerSet* getLayerSet();
  void recreateLayerSet();

  IThreadUtils* _threadUtils;
  IThreadUtils* getThreadUtils();

  IDownloader* _downloader;
  IDownloader* getDownloader();
  
  ICameraActivityListener* _cameraActivityListener;
  ICameraActivityListener* getCameraActivityListener();


protected:
  G3MCBuilder(const URL& serverURL,
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
  
  virtual ICameraActivityListener* createCameraActivityListener() = 0;

public:

  /** Private to G3M, don't call it */
  void changeBaseLayer(Layer* baseLayer);

  /** Private to G3M, don't call it */
  int getSceneTimestamp() const;

  /** Private to G3M, don't call it */
  void setSceneTimestamp(const int timestamp);

  /** Private to G3M, don't call it */
  void setSceneUser(const std::string& user);

  /** Private to G3M, don't call it */
  void setSceneName(const std::string& name);

  /** Private to G3M, don't call it */
  const URL createSceneDescriptionURL() const;

  /** Private to G3M, don't call it */
  void rawChangeScene(const std::string& sceneId);

  void changeScene(const std::string& sceneId);

  void requestScenesDescriptions(G3MCBuilderScenesDescriptionsListener* listener,
                                 bool autoDelete = true);

};

#endif
