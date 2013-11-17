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
//    const JSONObject* properties = geometry->getFeature()->getProperties();

    // final int colorIndex = (int) properties.getAsNumber("mapcolor7", 0);
    const int colorIndex = 5;
    const Color color = Color::fromRGBA(0.7f, 0, 0, 0.5f).wheelStep(7, colorIndex).muchLighter().muchLighter();

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
//    const JSONObject* properties = geometry->getFeature()->getProperties();

    // final int colorIndex = (int) properties.getAsNumber("mapcolor7", 0);
    const int colorIndex = 10;
    const Color color = Color::fromRGBA(0.7f, 0, 0, 0.5f).wheelStep(7, colorIndex);

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
                          5000,
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
  //  MapBoxLayer* layer = new MapBoxLayer("examples.map-qogxobv1",
  //                                       TimeInterval::fromDays(30),
  //                                       true,
  //                                       12);

  MapBoxLayer* layer = new MapBoxLayer("examples.map-qogxobv1",
                                       TimeInterval::fromDays(30),
                                       true);

  G3MDemoModel* model = getModel();

  model->getLayerSet()->addLayer(layer);


  GEORenderer* geoRenderer = model->getGEORenderer();
  geoRenderer->loadJSON(URL("file:///buildings_monaco.geojson"),   new G3MVectorialDemoSymbolizer());
  geoRenderer->loadJSON(URL("file:///roads_monaco.geojson"),       new G3MVectorialDemoSymbolizer());
  geoRenderer->loadJSON(URL("file:///restaurants_monaco.geojson"), new G3MVectorialDemoSymbolizer());

  //  final Geodetic2D lower = new Geodetic2D( //
  //                                          Angle.fromDegrees(43.69200778158779), //
  //                                          Angle.fromDegrees(7.36351850323685));
  //  final Geodetic2D upper = new Geodetic2D( //
  //                                          Angle.fromDegrees(43.7885865186124), //
  //                                          Angle.fromDegrees(7.48617349925817));
  //
  //  final Sector demSector = new Sector(lower, upper);
  //
  //
  //  //NROWS          13
  //  //NCOLS          16
  //  final ElevationDataProvider dem = new SingleBillElevationDataProvider(new URL("file:///monaco-dem.bil", false), demSector,
  //                                                                        new Vector2I(16, 13), DELTA_HEIGHT);
  //
  //  builder.getPlanetRendererBuilder().setElevationDataProvider(dem);
  //  builder.getPlanetRendererBuilder().setVerticalExaggeration(_VerticalExaggeration);
  //
  //
  //  _vectorialRenderer = builder.createGEORenderer(Symbolizer);
  //  _vectorialRenderer.loadJSON(new URL("file:///buildings_monaco.geojson"));
  //  _vectorialRenderer.loadJSON(new URL("file:///roads_monaco.geojson"));
  //  _vectorialRenderer.loadJSON(new URL("file:///restaurants_monaco.geojson"));
  //
  //
  //  //The sector is shrinked to adjust the projection of
  
}
