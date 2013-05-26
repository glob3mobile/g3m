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

#include "URL.hpp"

#include <vector>
#include <string>


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

  const std::string _sceneId;

  Layer* _baseLayer;

  GL* _gl;
  bool _glob3Created;
  IStorage* _storage;

  LayerSet* _layerSet;
  TileRenderer* createTileRenderer();

  std::vector<ICameraConstrainer*>* createCameraConstraints();
  
  CameraRenderer* createCameraRenderer();

  Renderer* createBusyRenderer();

  std::vector<PeriodicalTask*>* createPeriodicalTasks();

  const URL createSceneDescriptionURL() const;
  const URL createScenesDescriptionsURL() const;

  LayerSet* getLayerSet();
  void recreateLayerSet();

  IDownloader* _downloader;
  IDownloader* getDownloader();


protected:
  G3MCBuilder(const URL& serverURL,
              const std::string& sceneId);
  
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
  void setBaseLayer(Layer* baseLayer);

  void requestScenesDescriptions(G3MCBuilderScenesDescriptionsListener* listener,
                                 bool autoDelete = true);

};

#endif
