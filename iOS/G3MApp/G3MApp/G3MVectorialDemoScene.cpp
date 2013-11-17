//
//  G3MVectorialDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3MVectorialDemoScene.hpp"

#include "G3MDemoModel.hpp"

#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/MapBoxLayer.hpp>
#include <G3MiOSSDK/GEORenderer.hpp>
#include <G3MiOSSDK/GEOSymbolizer.hpp>
#include <G3MiOSSDK/GEORasterPolygonSymbol.hpp>
#include <G3MiOSSDK/GEO2DPolygonGeometry.hpp>
#include <G3MiOSSDK/GEORasterLineSymbol.hpp>
#include <G3MiOSSDK/GEO2DLineStringGeometry.hpp>
#include <G3MiOSSDK/JSONObject.hpp>
#include <G3MiOSSDK/GEO2DPointGeometry.hpp>
#include <G3MiOSSDK/GEOFeature.hpp>
#include <G3MiOSSDK/Mark.hpp>
#include <G3MiOSSDK/GEOMarkSymbol.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>


class G3MVectorialDemoSymbolizer : public GEOSymbolizer {
private:

  static GEO2DLineRasterStyle createLineRasterStyle(const GEOGeometry* geometry) {
    const JSONObject* properties = geometry->getFeature()->getProperties();

    const std::string type = properties->getAsString("type", "");

    float dashLengths[] = {};
    int dashCount = 0;

    const JSONString* s = properties->getAsString("ROAD_NAME2");
    if (s != NULL) {
      return GEO2DLineRasterStyle(Color::blue(),
                                  8,
                                  CAP_ROUND,
                                  JOIN_ROUND,
                                  1,
                                  dashLengths,
                                  dashCount,
                                  0);
    }

    const Color color = (type == "Water Indicator") ? Color::fromRGBA(1, 1, 1, 0.9f) : Color::fromRGBA(1, 1, 0, 0.9f);

    return GEO2DLineRasterStyle(color,
                                2,
                                CAP_ROUND,
                                JOIN_ROUND,
                                1,
                                dashLengths,
                                dashCount,
                                0);
  }

  static GEO2DLineRasterStyle createPolygonLineRasterStyle(const GEOGeometry* geometry) {
    const Color color = Color::fromRGBA(0.63f, 0.48f, 1.0f, 0.5f);
    float dashLengths[] = {};
    const int dashCount = 0;

    return GEO2DLineRasterStyle(color,
                                2,
                                CAP_ROUND,
                                JOIN_ROUND,
                                1,
                                dashLengths,
                                dashCount,
                                0);
  }


  static GEO2DSurfaceRasterStyle createPolygonSurfaceRasterStyle(const GEOGeometry* geometry) {
    const Color color = Color::fromRGBA(0.0f, 0.7f, 0.4f, 0.5f);

    return GEO2DSurfaceRasterStyle(color);
  }


public:

  std::vector<GEOSymbol*>* createSymbols(const GEO2DPointGeometry* geometry) const {
    std::vector<GEOSymbol*>* result = new std::vector<GEOSymbol*>();

    //    const JSONObject* properties = geometry->getFeature()->getProperties();
    //    const std::string name = properties->getAsString("name", "");

    Mark* mark = new Mark(URL("file:///restaurant-48x48.png"),
                          Geodetic3D(geometry->getPosition(), 0),
                          RELATIVE_TO_GROUND,
                          15000,
                          NULL,
                          false,
                          NULL, // markListener,
                          true);

    result->push_back(new GEOMarkSymbol(mark));
    return result;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DLineStringGeometry* geometry) const {
    std::vector<GEOSymbol*>* symbols = new std::vector<GEOSymbol*>();
    symbols->push_back(new GEORasterLineSymbol(geometry->getCoordinates(),
                                               createLineRasterStyle(geometry)));
    return symbols;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiLineStringGeometry* geometry) const {
    return NULL;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DPolygonGeometry* geometry) const {
    std::vector<GEOSymbol*>* symbols = new std::vector<GEOSymbol*>();
    symbols->push_back(new GEORasterPolygonSymbol(geometry->getPolygonData(),
                                                  createPolygonLineRasterStyle(geometry),
                                                  createPolygonSurfaceRasterStyle(geometry)));
    return symbols;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiPolygonGeometry* geometry) const {
    return NULL;
  }

};


void G3MVectorialDemoScene::activate() {
#warning Diego at work!

  G3MDemoModel* model = getModel();

  MapBoxLayer* layer = new MapBoxLayer("examples.map-qogxobv1",
                                       TimeInterval::fromDays(30),
                                       true,
                                       13);
  model->getLayerSet()->addLayer(layer);

  GEORenderer* geoRenderer = model->getGEORenderer();
  geoRenderer->loadJSON(URL("file:///buildings_monaco.geojson"),   new G3MVectorialDemoSymbolizer());
  geoRenderer->loadJSON(URL("file:///roads_monaco.geojson"),       new G3MVectorialDemoSymbolizer());
  geoRenderer->loadJSON(URL("file:///restaurants_monaco.geojson"), new G3MVectorialDemoSymbolizer());

  const Sector demSector = Sector::fromDegrees(43.69200778158779, 7.36351850323685,
                                               43.7885865186124,  7.48617349925817);

  model->getG3MWidget()->setShownSector(demSector.shrinkedByPercent(0.1f));

  //  final ElevationDataProvider dem = new SingleBillElevationDataProvider(new URL("file:///monaco-dem.bil", false), demSector,
  //                                                                        new Vector2I(16, 13), DELTA_HEIGHT);
  //
  //  builder.getPlanetRendererBuilder().setElevationDataProvider(dem);
  //  builder.getPlanetRendererBuilder().setVerticalExaggeration(_VerticalExaggeration);
  
}
