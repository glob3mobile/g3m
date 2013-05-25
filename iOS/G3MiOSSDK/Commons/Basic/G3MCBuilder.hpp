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

#include "URL.hpp"

#include <vector>
#include <string>

class G3MCBuilder {
private:
  const URL         _serverURL;
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

  LayerSet* getLayerSet();
  void recreateLayerSet();


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

};

#endif
