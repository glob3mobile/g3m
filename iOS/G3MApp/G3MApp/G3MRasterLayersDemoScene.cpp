//
//  G3MRasterLayersDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//

#include "G3MRasterLayersDemoScene.hpp"

#include "G3MDemoModel.hpp"

#include <G3M/LayerSet.hpp>
#include <G3M/Layer.hpp>
#include <G3M/LayerBuilder.hpp>
#include <G3M/URLTemplateLayer.hpp>
#include <G3M/WMSLayer.hpp>
#include <G3M/LevelTileCondition.hpp>
#include <G3M/Sector.hpp>
#include <G3M/TimeInterval.hpp>
#include <G3M/Info.hpp>
#include <G3M/ILogger.hpp>
#include <G3M/G3MWidget.hpp>
#include <G3M/Geodetic3D.hpp>
#include <G3M/Geodetic2D.hpp>
#include <G3M/Angle.hpp>
#include <G3M/PlanetRenderer.hpp>
#include <G3M/PlanetRendererBuilder.hpp>
#include <G3M/ProjectedCornersDistanceTileLODTester.hpp>
#include <G3M/ProjectedGridTileLODTester.hpp>
#include <G3M/URL.hpp>
#include <G3M/ChessboardLayer.hpp>
#include <G3M/DebugTilesLayer.hpp>
#include <G3M/LayerTouchEventListener.hpp>
#include <G3M/IBufferDownloadListener.hpp>
#include <G3M/IDownloader.hpp>
#include <G3M/DownloadPriority.hpp>
#include <G3M/G3MEventContext.hpp>


class FeatureInfoDownloadListener : public IBufferDownloadListener {
private:
  G3MDemoModel*     _model;
  const std::string _title;

public:
  FeatureInfoDownloadListener(G3MDemoModel* model,
                              const std::string& title) :
  _model(model),
  _title(title)
  {
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    _model->showDialog(_title, buffer->getAsString());
    delete buffer;
  }

  void onError(const URL& url) {
    _model->showDialog(_title, "Error downloading " + url._path);
  }

  void onCancel(const URL& url) {
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
  }
};


// Long-press on the terrain asks the WMS server for the feature info under the finger and shows the raw answer
class WMSFeatureInfoListener : public LayerTouchEventListener {
private:
  G3MDemoModel* _model;

public:
  WMSFeatureInfoListener(G3MDemoModel* model) :
  _model(model)
  {
  }

  bool onTerrainTouch(const G3MEventContext* context,
                      const LayerTouchEvent& ev) {
    const Layer* layer = ev.getLayer();
    const URL url = layer->getFeatureInfoURL(ev.getPosition().asGeodetic2D(), ev.getSector());
    if (url.isNull()) {
      return false;
    }
    ILogger::instance()->logInfo("GetFeatureInfo %s", url._path.c_str());
    context->getDownloader()->requestBuffer(url,
                                            DownloadPriority::HIGHEST,
                                            TimeInterval::zero(),
                                            false, // readExpired
                                            new FeatureInfoDownloadListener(_model, layer->getTitle()),
                                            true); // deleteListener
    return true;
  }
};


Layer* G3MRasterLayersDemoScene::createMercatorLayer(const std::string& title,
                                                     const std::string& urlTemplate,
                                                     const int maxLevel,
                                                     const bool isTransparent,
                                                     const std::string& attribution) {
  std::vector<const Info*>* layerInfo = new std::vector<const Info*>();
  layerInfo->push_back(new Info(attribution));

  URLTemplateLayer* layer = URLTemplateLayer::newMercator(urlTemplate,
                                                          Sector::FULL_SPHERE,
                                                          isTransparent,
                                                          2,    // firstLevel
                                                          maxLevel,
                                                          TimeInterval::fromDays(30),
                                                          true, // readExpired
                                                          1,    // transparency
                                                          NULL, // condition
                                                          layerInfo);
  layer->setTitle(title);
  layer->setEnable(false);
  return layer;
}


void G3MRasterLayersDemoScene::createLayerSet(LayerSet* layerSet) {
  Layer* osm = LayerBuilder::createOSMLayer();
  osm->setTitle("Open Street Map");
  osm->setEnable(true);
  layerSet->addLayer(osm);

  Layer* sentinel2 = getModel()->createRasterLayer();
  sentinel2->setTitle("Sentinel-2 cloudless");
  sentinel2->setEnable(false);
  layerSet->addLayer(sentinel2);

  Layer* blueMarble = LayerBuilder::createBlueMarbleLayer();
  blueMarble->setTitle("Blue Marble (EOX)");
  blueMarble->setEnable(false);
  layerSet->addLayer(blueMarble);

  layerSet->addLayer( createMercatorLayer("Black Marble (EOX)",
                                          "https://tiles.maps.eox.at/wmts/1.0.0/blackmarble_3857/default/g/{z}/{y}/{x}.jpg",
                                          8,
                                          false,
                                          "Black Marble by NASA, rendered by EOX IT Services GmbH") );

  layerSet->addLayer( createMercatorLayer("Terrain Light (EOX)",
                                          "https://tiles.maps.eox.at/wmts/1.0.0/terrain-light_3857/default/g/{z}/{y}/{x}.jpg",
                                          16,
                                          false,
                                          "Terrain Light by EOX IT Services GmbH, data by OpenStreetMap contributors, NaturalEarth, SRTM") );

  layerSet->addLayer( createMercatorLayer("OpenTopoMap",
                                          "https://tile.opentopomap.org/{z}/{x}/{y}.png",
                                          17,
                                          false,
                                          "Map data: OpenStreetMap contributors, SRTM. Map style: OpenTopoMap (CC-BY-SA)") );

  layerSet->addLayer( createMercatorLayer("Boundaries and labels (EOX)",
                                          "https://tiles.maps.eox.at/wmts/1.0.0/overlay_3857/default/g/{z}/{y}/{x}.png",
                                          14,
                                          true,
                                          "Overlay by EOX IT Services GmbH, data by OpenStreetMap contributors and NaturalEarth") );

  layerSet->addLayer( createMercatorLayer("ESRI World Imagery",
                                          "https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}",
                                          18,
                                          false,
                                          "Esri, Maxar, Earthstar Geographics, and the GIS User Community") );

  layerSet->addLayer( createWMSLayer("Nasa Blue Marble (WMS)",
                                     "BlueMarble_NextGeneration",
                                     "https://gibs.earthdata.nasa.gov/wms/epsg4326/best/wms.cgi?",
                                     Sector::FULL_SPHERE,
                                     "image/jpeg",
                                     false, // isTransparent
                                     NULL,  // condition
                                     1,     // transparency
                                     false, // mercator
                                     "Blue Marble Next Generation by NASA, served by NASA GIBS") );

  layerSet->addLayer( createWMSLayer("OpenStreetMap (WMS)",
                                     "OSM-WMS",
                                     "https://ows.terrestris.de/osm/service?",
                                     Sector::FULL_SPHERE,
                                     "image/png",
                                     false, // isTransparent
                                     NULL,  // condition
                                     1,     // transparency
                                     false, // mercator
                                     "OpenStreetMap WMS by terrestris GmbH, data by OpenStreetMap contributors") );

  const Sector spain = Sector::fromDegrees(27.5, -18.4,
                                           44.0,  4.4);

  layerSet->addLayer( createWMSLayer("Spain PNOA orthoimage (WMS)",
                                     "OI.OrthoimageCoverage",
                                     "https://www.ign.es/wms-inspire/pnoa-ma?",
                                     spain,
                                     "image/png",
                                     true,  // isTransparent, the sea comes transparent so the layer below shows
                                     NULL,  // condition
                                     1,     // transparency
                                     true,  // mercator
                                     "PNOA orthoimage by Instituto Geografico Nacional de Espana (CC-BY 4.0)") );

  layerSet->addLayer( createWMSLayer("Spain Catastro (WMS)",
                                     "Catastro",
                                     "https://ovc.catastro.meh.es/Cartografia/WMS/ServidorWMS.aspx?",
                                     spain,
                                     "image/png",
                                     true,  // isTransparent
                                     new LevelTileCondition(12, 20),
                                     0.85f, // transparency
                                     true,  // mercator
                                     "Cartografia catastral by Direccion General del Catastro de Espana") );

  Layer* chessboard = new ChessboardLayer();
  chessboard->setTitle("Chessboard");
  chessboard->setEnable(false);
  layerSet->addLayer(chessboard);

  Layer* debugTiles = new DebugTilesLayer();
  debugTiles->setTitle("Debug tiles");
  debugTiles->setEnable(false);
  layerSet->addLayer(debugTiles);
}


Layer* G3MRasterLayersDemoScene::createWMSLayer(const std::string& title,
                                                const std::string& mapLayer,
                                                const std::string& serverURL,
                                                const Sector& dataSector,
                                                const std::string& format,
                                                const bool isTransparent,
                                                const LayerCondition* condition,
                                                const float transparency,
                                                const bool mercator,
                                                const std::string& attribution) {
  std::vector<const Info*>* layerInfo = new std::vector<const Info*>();
  layerInfo->push_back(new Info(attribution));

  // the tile pyramid of every enabled layer must match, so the WMS layers meant to go over the Sentinel-2 tiles are mercator too
  WMSLayer* layer = mercator
  ? WMSLayer::newMercator(mapLayer,
                          URL(serverURL),
                          WMS_1_1_0,
                          dataSector,
                          format,
                          "",   // style
                          isTransparent,
                          2,    // firstLevel
                          17,   // maxLevel, the same as the Sentinel-2 layer below
                          condition,
                          TimeInterval::fromDays(30),
                          true, // readExpired
                          transparency,
                          layerInfo)
  : new WMSLayer(mapLayer,
                 URL(serverURL),
                 WMS_1_1_0,
                 dataSector,
                 format,
                 "EPSG:4326",
                 "",   // style
                 isTransparent,
                 condition,
                 TimeInterval::fromDays(30),
                 true, // readExpired
                 NULL, // parameters
                 transparency,
                 layerInfo);
  layer->setTitle(title);
  layer->setEnable(false);

  if (_featureInfoListener == NULL) {
    _featureInfoListener = new WMSFeatureInfoListener(getModel());
  }
  layer->addLayerTouchEventListener(_featureInfoListener);

  return layer;
}


void G3MRasterLayersDemoScene::rawActivate(const G3MContext* context) {
  createLayerSet( getModel()->getLayerSet() );
  getModel()->getPlanetRenderer()->setShowStatistics(true);
}

void G3MRasterLayersDemoScene::rawSelectGroupOption(size_t groupIndex,
                                                    const std::string& option,
                                                    int optionIndex) {
  if (groupIndex == _anisotropyGroupIndex) {
    const float maxAnisotropy = (option == "Anisotropic 16x") ? 16 : (option == "Anisotropic 8x") ? 8 : 1;
    getModel()->getG3MWidget()->setTextureMaxAnisotropy(maxAnisotropy);
  }
  else if (groupIndex == _atmosphereGroupIndex) {
    G3MDemoModel* model = getModel();
    model->setAtmosphereEnable(option != "No atmosphere");
    model->setGroundHazeEnable(option == "Atmosphere & haze");
  }
  else if (groupIndex == _lodGroupIndex) {
    TileLODTester* projectedSizeTester;
    if (option == "LOD: grid") {
      projectedSizeTester = new ProjectedGridTileLODTester(1);
    }
    else if (option == "LOD: grid x1.15") {
      projectedSizeTester = new ProjectedGridTileLODTester(1.15);
    }
    else if (option == "LOD: grid x1.2") {
      projectedSizeTester = new ProjectedGridTileLODTester(1.2);
    }
    else if (option == "LOD: grid x1.3") {
      projectedSizeTester = new ProjectedGridTileLODTester(1.3);
    }
    else {
      projectedSizeTester = new ProjectedCornersDistanceTileLODTester();
    }
    getModel()->getPlanetRenderer()->setTileLODTester( PlanetRendererBuilder::createDefaultTileLODTesterChain(projectedSizeTester) );
  }
  else {
    rawSelectOption(option, optionIndex);
  }
}

void G3MRasterLayersDemoScene::rawSelectOption(const std::string& option,
                                               int optionIndex) {
  LayerSet* layerSet = getModel()->getLayerSet();
  layerSet->disableAllLayers();

  if (option == "Spain Catastro over PNOA (WMS)") {
    layerSet->getLayerByTitle("Sentinel-2 cloudless")->setEnable(true);
    layerSet->getLayerByTitle("Spain PNOA orthoimage (WMS)")->setEnable(true);
    layerSet->getLayerByTitle("Spain Catastro (WMS)")->setEnable(true);
    getModel()->getG3MWidget()->setAnimatedCameraPosition(Geodetic3D::fromDegrees(40.4168, -3.7038, 4000));
    return;
  }

  if (option == "ESRI World Imagery") {
    layerSet->getLayerByTitle("ESRI World Imagery")->setEnable(true);
    // Manhattan's street grid at a grazing angle, where anisotropic filtering shows the most
    getModel()->getG3MWidget()->setAnimatedCameraPosition(Geodetic3D::fromDegrees(40.7000, -74.0150, 1500),
                                                          Angle::fromDegrees(29),
                                                          Angle::fromDegrees(-8));
    return;
  }

  if (option == "Sentinel-2 cloudless + labels") {
    layerSet->getLayerByTitle("Sentinel-2 cloudless")->setEnable(true);
    layerSet->getLayerByTitle("Boundaries and labels (EOX)")->setEnable(true);
  }
  else if (option == "Spain PNOA orthoimage (WMS)") {
    layerSet->getLayerByTitle("Sentinel-2 cloudless")->setEnable(true);
    layerSet->getLayerByTitle("Spain PNOA orthoimage (WMS)")->setEnable(true);
  }
  else if (option == "Chessboard + Debug tiles") {
    layerSet->getLayerByTitle("Chessboard")->setEnable(true);
    layerSet->getLayerByTitle("Debug tiles")->setEnable(true);
  }
  else if (option == "Open Street Map + Debug tiles") {
    layerSet->getLayerByTitle("Open Street Map")->setEnable(true);
    layerSet->getLayerByTitle("Debug tiles")->setEnable(true);
  }
  else {
    Layer* layer = layerSet->getLayerByTitle(option);
    if (layer == NULL) {
      ILogger::instance()->logError("option \"%s\" not supported", option.c_str());
      return;
    }
    layer->setEnable(true);
  }

  // whole Spain in view, Canary Islands included
  getModel()->getG3MWidget()->setAnimatedCameraPosition(Geodetic3D::fromDegrees(35.3392, -9.4129, 3761535));
}
