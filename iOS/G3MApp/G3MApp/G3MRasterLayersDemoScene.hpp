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
  static Layer* createMercatorLayer(const std::string& title,
                                    const std::string& urlTemplate,
                                    const int maxLevel,
                                    const bool isTransparent,
                                    const std::string& attribution);

  G3MRasterLayersDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Raster Layers", "", 0),
  _featureInfoListener(NULL)
  {
    addOption("Open Street Map");
    addOption("Sentinel-2 cloudless");
    addOption("Sentinel-2 cloudless + labels");
    addOption("Blue Marble (EOX)");
    addOption("Black Marble (EOX)");
    addOption("Terrain Light (EOX)");
    addOption("OpenTopoMap");
    addOption("ESRI World Imagery");
    addOption("Nasa Blue Marble (WMS)");
    addOption("OpenStreetMap (WMS)");
    addOption("Spain PNOA orthoimage (WMS)");
    addOption("Spain Catastro over PNOA (WMS)");
    addOption("Chessboard");
    addOption("Chessboard + Debug tiles");
    addOption("Open Street Map + Debug tiles");
  }

};

#endif
