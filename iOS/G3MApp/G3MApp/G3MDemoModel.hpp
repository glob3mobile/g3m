//
//  G3MDemoModel.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MDemoModel__
#define __G3MApp__G3MDemoModel__

#include <vector>
#include <string>

class G3MDemoListener;
class G3MDemoScene;
class LayerSet;
class GEORenderer;

class G3MDemoModel {
private:
  G3MDemoListener* _listener;

  LayerSet*    _layerSet;
  GEORenderer* _geoRenderer;

  G3MDemoScene* _selectedScene;
  std::vector<G3MDemoScene*> _scenes;

public:

  G3MDemoModel(G3MDemoListener* listener,
               LayerSet* layerSet,
               GEORenderer* geoRenderer);

  LayerSet* getLayerSet() const {
    return _layerSet;
  }

  GEORenderer* getGEORenderer() const {
    return _geoRenderer;
  }

  int getScenesCount() const {
    return _scenes.size();
  }

  const G3MDemoScene* getScene(int index) {
    return _scenes[index];
  }

  G3MDemoScene* getSceneByName(const std::string& sceneName) const;

  void selectScene(const std::string& sceneName);

  void selectScene(G3MDemoScene* scene);

  bool isSelectedScene(const G3MDemoScene* scene) const {
    return _selectedScene == scene;
  }

  void reset();
  
};

#endif
