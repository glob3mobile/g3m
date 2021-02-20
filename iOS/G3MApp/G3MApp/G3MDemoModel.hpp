//
//  G3MDemoModel.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//

#ifndef __G3MApp__G3MDemoModel__
#define __G3MApp__G3MDemoModel__

#include <vector>
#include <string>

class G3MDemoListener;
class G3MDemoScene;
class LayerSet;
class G3MWidget;
class G3MContext;
class GEOVectorLayer;
class MarksRenderer;
class MeshRenderer;
class ShapesRenderer;
class PlanetRenderer;
class GEORenderer;
class OLDPointCloudsRenderer;
class XPCRenderer;
class HUDRenderer;
class NonOverlappingMarksRenderer;
class VectorStreamingRenderer;
class CompositeRenderer;


class G3MDemoModel {
private:
  G3MWidget* _g3mWidget;

  G3MDemoListener* _listener;

  LayerSet*                    _layerSet;
  MeshRenderer*                _meshRenderer;
  ShapesRenderer*              _shapesRenderer;
  MarksRenderer*               _marksRenderer;
  GEORenderer*                 _geoRenderer;
  OLDPointCloudsRenderer*      _oldPointCloudsRenderer;
  XPCRenderer*                 _xpcRenderer;
  HUDRenderer*                 _hudRenderer;
  NonOverlappingMarksRenderer* _nonOverlappingMarksRenderer;
  VectorStreamingRenderer*     _vectorStreamingRenderer;
  CompositeRenderer*           _compositeRenderer;

  G3MDemoScene*              _selectedScene;
  std::vector<G3MDemoScene*> _scenes;

  const G3MContext* _context;

public:

  G3MDemoModel(G3MDemoListener*             listener,
               LayerSet*                    layerSet,
               MeshRenderer*                meshRenderer,
               ShapesRenderer*              shapesRenderer,
               MarksRenderer*               marksRenderer,
               GEORenderer*                 geoRenderer,
               OLDPointCloudsRenderer*      oldPointCloudsRenderer,
               XPCRenderer*                 xpcloudsRenderer,
               HUDRenderer*                 hudRenderer,
               NonOverlappingMarksRenderer* nonOverlappingMarksRenderer,
               VectorStreamingRenderer*     vectorStreamingRenderer,
               CompositeRenderer*           compositeRenderer);


  void initializeG3MWidget(G3MWidget* g3mWidget);

  void initializeG3MContext(const G3MContext* context);

  G3MWidget* getG3MWidget() const {
    return _g3mWidget;
  }

  LayerSet* getLayerSet() const {
    return _layerSet;
  }

  GEORenderer* getGEORenderer() const {
    return _geoRenderer;
  }

  MarksRenderer* getMarksRenderer() const {
    return _marksRenderer;
  }

  MeshRenderer* getMeshRenderer() const {
    return _meshRenderer;
  }

  ShapesRenderer* getShapesRenderer() const {
    return _shapesRenderer;
  }

  OLDPointCloudsRenderer* getOLDPointCloudsRenderer() const {
    return _oldPointCloudsRenderer;
  }

  XPCRenderer* getXPCRenderer() const {
    return _xpcRenderer;
  }

  HUDRenderer* getHUDRenderer() const {
    return _hudRenderer;
  }

  NonOverlappingMarksRenderer* getNonOverlappingMarksRenderer() const {
    return _nonOverlappingMarksRenderer;
  }

  VectorStreamingRenderer* getVectorStreamingRenderer() const {
    return _vectorStreamingRenderer;
  }
  
  CompositeRenderer* getCompositeRenderer() const {
    return _compositeRenderer;
  }


  PlanetRenderer* getPlanetRenderer() const;

  size_t getScenesCount() const {
    return _scenes.size();
  }

  const G3MDemoScene* getScene(size_t index) {
    return _scenes[index];
  }

  G3MDemoScene* getSceneByName(const std::string& sceneName) const;

  void selectScene(const std::string& sceneName);

  void selectScene(G3MDemoScene* scene);

  bool isSelectedScene(const G3MDemoScene* scene) const {
    return _selectedScene == scene;
  }

  G3MDemoScene* getSelectedScene() const {
    return _selectedScene;
  }

  void reset();

  void onChangeSceneOption(G3MDemoScene* scene,
                           const std::string& option,
                           int optionIndex);

  void showDialog(const std::string& title,
                  const std::string& message) const;

};

#endif
