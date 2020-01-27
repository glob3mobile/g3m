//
//  G3MVectorialDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//

#include "G3MVectorialDemoScene.hpp"

#include "G3MDemoModel.hpp"

#include <G3M/LayerSet.hpp>
#include <G3M/BingMapsLayer.hpp>
#include <G3M/GEORenderer.hpp>
#include <G3M/GEOSymbolizer.hpp>
#include <G3M/GEOPolygonRasterSymbol.hpp>
#include <G3M/GEO2DPolygonGeometry.hpp>
#include <G3M/GEOLineRasterSymbol.hpp>
#include <G3M/GEO2DLineStringGeometry.hpp>
#include <G3M/JSONObject.hpp>
#include <G3M/GEO2DPointGeometry.hpp>
#include <G3M/GEOFeature.hpp>
#include <G3M/Mark.hpp>
#include <G3M/GEOMarkSymbol.hpp>
#include <G3M/G3MWidget.hpp>
#include <G3M/PlanetRenderer.hpp>
#include <G3M/SingleBILElevationDataProvider.hpp>
#include <G3M/MarkTouchListener.hpp>
#include <G3M/GEOVectorLayer.hpp>


class G3MVectorialDemoScene_RestaurantMarkTouchListener : public MarkTouchListener {
private:
  G3MDemoModel*     _model;
  const std::string _name;

public:
  G3MVectorialDemoScene_RestaurantMarkTouchListener(G3MDemoModel* model,
                                                    const std::string& name) :
  _model(model),
  _name(name)
  {
  }

  bool touchedMark(Mark* mark) {
    const std::string name = _name.empty() ? "<no name>" : _name;
    _model->showDialog(name, "Restaurant");
    return true;
  }
};

class G3MVectorialDemoScene_GEOSymbolizer : public GEOSymbolizer {
private:
  G3MDemoModel* _model;

  static GEO2DLineRasterStyle createLineRasterStyle(const GEOGeometry* geometry) {
    const JSONObject* properties = geometry->getFeature()->getProperties();

    const std::string type = properties->getAsString("type", "");

    float dashLengths[] = {};
    int dashCount = 0;

    const JSONString* s = properties->getAsString("ROAD_NAME2");
    if (s != NULL) {
      return GEO2DLineRasterStyle(Color::BLUE,
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
  G3MVectorialDemoScene_GEOSymbolizer(G3MDemoModel* model) :
  _model(model)
  {
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DPointGeometry* geometry) const {
    std::vector<GEOSymbol*>* result = new std::vector<GEOSymbol*>();

    const JSONObject* properties = geometry->getFeature()->getProperties();
    const std::string name = properties->getAsString("name", "");

    Mark* mark = new Mark(URL("file:///restaurant-48x48.png"),
                          Geodetic3D(geometry->getPosition(), 0),
                          RELATIVE_TO_GROUND,
                          15000,
                          NULL,
                          false,
                          new G3MVectorialDemoScene_RestaurantMarkTouchListener(_model, name), // markListener,
                          true);

    result->push_back(new GEOMarkSymbol(mark));
    return result;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO3DPointGeometry* geometry) const {
    return NULL;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DLineStringGeometry* geometry) const {
    std::vector<GEOSymbol*>* symbols = new std::vector<GEOSymbol*>();
    symbols->push_back(new GEOLineRasterSymbol(geometry->getCoordinates(),
                                               createLineRasterStyle(geometry)));
    return symbols;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiLineStringGeometry* geometry) const {
    return NULL;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DPolygonGeometry* geometry) const {
    std::vector<GEOSymbol*>* symbols = new std::vector<GEOSymbol*>();
    symbols->push_back(new GEOPolygonRasterSymbol(geometry->getPolygonData(),
                                                  createPolygonLineRasterStyle(geometry),
                                                  createPolygonSurfaceRasterStyle(geometry)));
    return symbols;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO3DPolygonGeometry* geometry) const {
    return NULL;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiPolygonGeometry* geometry) const {
    return NULL;
  }

};


void G3MVectorialDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  g3mWidget->setBackgroundColor(Color::fromRGBA(0.19f, 0.23f, 0.21f, 1.0f));

//  const std::string&    imagerySet,
//  const std::string&    key,
//  const TimeInterval&   timeToCache,
//  const bool            readExpired    = true,
//  const int             initialLevel   = 2,
  BingMapsLayer* rasterLayer = new BingMapsLayer(BingMapType::Aerial(),
                                                 "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                                 TimeInterval::fromDays(30),
                                                 true, // readExpired
                                                 12    // initialLevel
                                                 );
  model->getLayerSet()->addLayer(rasterLayer);

  GEOVectorLayer* vectorLayer = new GEOVectorLayer();
  model->getLayerSet()->addLayer(vectorLayer);

  GEORenderer* geoRenderer = model->getGEORenderer();
  geoRenderer->setGEOVectorLayer(vectorLayer, false);
  geoRenderer->loadJSON(URL("file:///buildings_monaco.geojson"),   new G3MVectorialDemoScene_GEOSymbolizer(model));
  geoRenderer->loadJSON(URL("file:///roads_monaco.geojson"),       new G3MVectorialDemoScene_GEOSymbolizer(model));
  geoRenderer->loadJSON(URL("file:///restaurants_monaco.geojson"), new G3MVectorialDemoScene_GEOSymbolizer(model));

  const Sector demSector = Sector::fromDegrees(43.69200778158779, 7.36351850323685,
                                               43.7885865186124,  7.48617349925817);
  
  g3mWidget->setRenderedSector(demSector.shrinkedByPercent(0.1f));
}
