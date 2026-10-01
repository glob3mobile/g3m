//
//  G3MRasterLayersDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//

#ifndef __G3MApp__G3MRasterLayersDemoScene__
#define __G3MApp__G3MRasterLayersDemoScene__

#include "G3MDemoScene.hpp"

class Layer;
class LayerSet;
class LayerCondition;
class Sector;
class WMSFeatureInfoListener;

class G3MRasterLayersDemoScene : public G3MDemoScene {
private:
  WMSFeatureInfoListener* _featureInfoListener;

  static Layer* createMercatorLayer(const std::string& title,
                                    const std::string& urlTemplate,
                                    const int maxLevel,
                                    const bool isTransparent,
                                    const std::string& attribution);

  Layer* createWMSLayer(const std::string& title,
                        const std::string& mapLayer,
                        const std::string& serverURL,
                        const Sector& dataSector,
                        const std::string& format,
                        const bool isTransparent,
                        const LayerCondition* condition,
                        const float transparency,
                        const bool mercator,
                        const std::string& attribution);

  void createLayerSet(LayerSet* layerSet);

protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex);

public:
  G3MRasterLayersDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Raster Layers", "", 0),
  _featureInfoListener(NULL)
  {
    _options.push_back("Open Street Map");
    _options.push_back("Sentinel-2 cloudless");
    _options.push_back("Sentinel-2 cloudless + labels");
    _options.push_back("Blue Marble (EOX)");
    _options.push_back("Black Marble (EOX)");
    _options.push_back("Terrain Light (EOX)");
    _options.push_back("OpenTopoMap");
    _options.push_back("ESRI World Imagery");
    _options.push_back("Nasa Blue Marble (WMS)");
    _options.push_back("OpenStreetMap (WMS)");
    _options.push_back("Spain PNOA orthoimage (WMS)");
    _options.push_back("Spain Catastro over PNOA (WMS)");
    _options.push_back("Chessboard");
    _options.push_back("Chessboard + Debug tiles");
    _options.push_back("Open Street Map + Debug tiles");
  }

};

#endif
