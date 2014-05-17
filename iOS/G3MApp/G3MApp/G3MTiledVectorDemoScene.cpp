//
//  G3MTiledVectorDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 5/17/14.
//  Copyright (c) 2014 Igo Software SL. All rights reserved.
//

#include "G3MTiledVectorDemoScene.hpp"
#include "G3MDemoModel.hpp"
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/GEORasterSymbolizer.hpp>
#include <G3MiOSSDK/GEO2DLineRasterStyle.hpp>
#include <G3MiOSSDK/GEOGeometry.hpp>
#include <G3MiOSSDK/GEO2DSurfaceRasterStyle.hpp>
#include <G3MiOSSDK/GEOLineRasterSymbol.hpp>
#include <G3MiOSSDK/GEO2DLineStringGeometry.hpp>
#include <G3MiOSSDK/GEOMultiLineRasterSymbol.hpp>
#include <G3MiOSSDK/GEO2DMultiLineStringGeometry.hpp>
#include <G3MiOSSDK/GEOPolygonRasterSymbol.hpp>
#include <G3MiOSSDK/GEO2DPolygonGeometry.hpp>
#include <G3MiOSSDK/GEO2DMultiPolygonGeometry.hpp>
#include <G3MiOSSDK/TiledVectorLayer.hpp>
#include <G3MiOSSDK/LevelTileCondition.hpp>


class SampleRasterSymbolizer : public GEORasterSymbolizer {
private:
  static GEO2DLineRasterStyle createPolygonLineRasterStyle(const GEOGeometry* geometry,
                                                           const Color& baseColor) {
    const Color color = baseColor.muchDarker();

    float dashLengths[] = {};
    int dashCount = 0;

    return GEO2DLineRasterStyle(color,
                                1,
                                CAP_ROUND,
                                JOIN_ROUND,
                                1,
                                dashLengths,
                                dashCount,
                                0);
  }

  static GEO2DSurfaceRasterStyle createPolygonSurfaceRasterStyle(const GEOGeometry* geometry,
                                                                 const Color& baseColor) {
    return GEO2DSurfaceRasterStyle( baseColor );
  }

  static GEO2DLineRasterStyle createLineRasterStyle(const GEOGeometry* geometry,
                                                    const Color& baseColor) {
    const Color color = baseColor.muchDarker();

    float dashLengths[] = {};
    int dashCount = 0;

    return GEO2DLineRasterStyle(color,
                                1,
                                CAP_ROUND,
                                JOIN_ROUND,
                                1,
                                dashLengths,
                                dashCount,
                                0);
  }

protected:
  virtual const Color baseColorForGeometry(const GEOGeometry* geometry) const = 0;

public:

  std::vector<GEORasterSymbol*>* createSymbols(const GEO2DPointGeometry* geometry) const {
    return NULL;
  }

  std::vector<GEORasterSymbol*>* createSymbols(const GEO2DLineStringGeometry* geometry) const {
    std::vector<GEORasterSymbol*>* symbols = new std::vector<GEORasterSymbol*>();

    symbols->push_back( new GEOLineRasterSymbol(geometry->getCoordinates(),
                                                createLineRasterStyle(geometry,
                                                                      baseColorForGeometry(geometry))) );

    return symbols;

  }

  std::vector<GEORasterSymbol*>* createSymbols(const GEO2DMultiLineStringGeometry* geometry) const {
    std::vector<GEORasterSymbol*>* symbols = new std::vector<GEORasterSymbol*>();

    symbols->push_back( new GEOMultiLineRasterSymbol(geometry->getCoordinatesArray(),
                                                     createLineRasterStyle(geometry,
                                                                           baseColorForGeometry(geometry))) );

    return symbols;
  }

  std::vector<GEORasterSymbol*>* createSymbols(const GEO2DPolygonGeometry* geometry) const {
    std::vector<GEORasterSymbol*>* symbols = new std::vector<GEORasterSymbol*>();

    const Color baseColor = baseColorForGeometry(geometry);
    symbols->push_back( new GEOPolygonRasterSymbol(geometry->getPolygonData(),
                                                   createPolygonLineRasterStyle(geometry, baseColor),
                                                   createPolygonSurfaceRasterStyle(geometry, baseColor)) );

    return symbols;
  }

  std::vector<GEORasterSymbol*>* createSymbols(const GEO2DMultiPolygonGeometry* geometry) const {
    std::vector<GEORasterSymbol*>* symbols = new std::vector<GEORasterSymbol*>();

    const Color baseColor = baseColorForGeometry(geometry);

    const GEO2DLineRasterStyle    lineStyle    = createPolygonLineRasterStyle(geometry, baseColor);
    const GEO2DSurfaceRasterStyle surfaceStyle = createPolygonSurfaceRasterStyle(geometry, baseColor);

    const std::vector<GEO2DPolygonData*>* polygonsData = geometry->getPolygonsData();
    const int polygonsDataSize = polygonsData->size();

    for (int i = 0; i < polygonsDataSize; i++) {
      GEO2DPolygonData* polygonData = polygonsData->at(i);
      symbols->push_back( new GEOPolygonRasterSymbol(polygonData,
                                                     lineStyle,
                                                     surfaceStyle) );
    }

    return symbols;
  }
};

class PinkishRasterSymbolizer : public SampleRasterSymbolizer {
protected:
  const Color baseColorForGeometry(const GEOGeometry* geometry) const {
    return Color::fromRGBA(0.92, 0.6, 0.65, 0.75);
  }

public:
  GEORasterSymbolizer* copy() const {
    return new PinkishRasterSymbolizer();
  }

};

class GreenishRasterSymbolizer : public SampleRasterSymbolizer {
protected:
  const Color baseColorForGeometry(const GEOGeometry* geometry) const {
    return Color::fromRGBA(0.6, 0.92, 0.65, 0.75);
  }

public:
  GEORasterSymbolizer* copy() const {
    return new GreenishRasterSymbolizer();
  }

};


void G3MTiledVectorDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();


  g3mWidget->setBackgroundColor(Color::fromRGBA255(175, 221, 233, 255));


  BingMapsLayer* rasterLayer = new BingMapsLayer(BingMapType::AerialWithLabels(),
                                                 "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                                 TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(rasterLayer);


#warning TODO move vector-tiles data to any internet server
  const std::string urlTemplate = "http://192.168.1.15/vectorial/swiss-buildings/{level}/{x}/{y}.geojson";
  const Sector swissSector = Sector::fromDegrees(45.8176852, 5.956216,
                                                 47.803029, 10.492264);
  const int firstLevel = 2;
  const int maxLevel = 17;

//  const GEORasterSymbolizer* symbolizer = new PinkishRasterSymbolizer();
  const GEORasterSymbolizer* symbolizer = new GreenishRasterSymbolizer();

  _tiledVectorLayer = TiledVectorLayer::newMercator(symbolizer,
                                                    urlTemplate,
                                                    swissSector,
                                                    firstLevel,
                                                    maxLevel,
                                                    TimeInterval::fromDays(30), // timeToCache
                                                    true,                       // readExpired
                                                    1,                          // transparency
                                                    new LevelTileCondition(14, 21),
                                                    ""                          // disclaimerInfo
                                                    );
  model->getLayerSet()->addLayer(_tiledVectorLayer);


  g3mWidget->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
                                       Geodetic3D::fromDegrees(47.371223104914406576, 8.5408702851516871135, 1040),
                                       Angle::zero(),
                                       Angle::fromDegrees(-90));

}

void G3MTiledVectorDemoScene::rawSelectOption(const std::string& option,
                                              int optionIndex) {
  if (option == "Pinkish") {
    _tiledVectorLayer->setSymbolizer( new PinkishRasterSymbolizer(), true );
  }
  else {
    _tiledVectorLayer->setSymbolizer( new GreenishRasterSymbolizer(), true );
  }
}

void G3MTiledVectorDemoScene::deactivate(const G3MContext* context) {
  _tiledVectorLayer = NULL;

  G3MDemoScene::deactivate(context);
}
