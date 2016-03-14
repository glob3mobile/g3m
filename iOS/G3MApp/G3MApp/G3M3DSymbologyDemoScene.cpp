//
//  G3M3DSymbologyDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/18/13.
//

#include "G3M3DSymbologyDemoScene.hpp"

#include <G3MiOSSDK/Sector.hpp>
#include <G3MiOSSDK/MapBoxLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/GEORenderer.hpp>
#include <G3MiOSSDK/GEOSymbolizer.hpp>
#include <G3MiOSSDK/JSONObject.hpp>
#include <G3MiOSSDK/GEO2DPointGeometry.hpp>
#include <G3MiOSSDK/GEOFeature.hpp>
#include <G3MiOSSDK/BoxShape.hpp>
#include <G3MiOSSDK/Mark.hpp>
#include <G3MiOSSDK/GEOShapeSymbol.hpp>
#include <G3MiOSSDK/GEOMarkSymbol.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>
#include <G3MiOSSDK/SingleBilElevationDataProvider.hpp>
#include <G3MiOSSDK/GEOVectorLayer.hpp>
#include <G3MiOSSDK/ShapesRenderer.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/DownloaderImageBuilder.hpp>
#include <G3MiOSSDK/IImageBuilderListener.hpp>
#include <G3MiOSSDK/TexturedBoxShape.hpp>
#include <G3MiOSSDK/TexturesHandler.hpp>



#include "G3MDemoModel.hpp"

class USCitiesSymbolizer : public GEOSymbolizer {
public:
  std::vector<GEOSymbol*>* createSymbols(const GEO2DPointGeometry* geometry) const {
    std::vector<GEOSymbol*>* result = new std::vector<GEOSymbol*>();

    const JSONObject* properties = geometry->getFeature()->getProperties();

    const double popMax = properties->getAsNumber("POP_MAX", 0);

    const double boxExtent = 50000;
    const double baseArea = boxExtent * boxExtent;
    const double volume = popMax * boxExtent * 3500;
    const double height = volume / baseArea;

    BoxShape* bs = new BoxShape(new Geodetic3D(geometry->getPosition(), 0),
                                RELATIVE_TO_GROUND,
                                Vector3D(boxExtent / 4, boxExtent / 4, height / 2),
                                2,
                                Color::fromRGBA(0.99f, 0.8f, 0.08f, 1.0f),
                                Color::newFromRGBA(0.35f, 0.28f, 0.03f, 1.0f),
                                true);
    result->push_back(new GEOShapeSymbol(bs));


    const std::string label = IStringUtils::instance()->toString( IMathUtils::instance()->round(popMax / 1000) );
    Mark* mark = new Mark(label,
                          Geodetic3D(geometry->getPosition(),
                                     height / 5),
                          RELATIVE_TO_GROUND);
    result->push_back(new GEOMarkSymbol(mark));

    return result;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DLineStringGeometry* geometry) const {
    return NULL;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiLineStringGeometry* geometry) const {
    return NULL;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DPolygonGeometry* geometry) const {
    return NULL;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiPolygonGeometry* geometry) const {
    return NULL;
  }

};

void G3M3DSymbologyDemoScene::rawSelectOption(const std::string& option,
                                              int optionIndex) {
}

class MyIImageBuilderListener: public IImageBuilderListener{
  
  ShapesRenderer*  _sr;
  
public:
  
  MyIImageBuilderListener(ShapesRenderer* sr):_sr(sr){}
  
  virtual void imageCreated(const IImage*      image,
                            const std::string& imageName){
    
    const double boxExtent = 20000;
    const double baseArea = boxExtent * boxExtent;
    const double volume = 1e7 * boxExtent * 3500;
    const double height = volume / baseArea;
    
    TexturedBoxShape* bs = new TexturedBoxShape(new Geodetic3D(Geodetic3D::fromDegrees(28.124823,-15.430006, 0)),
                                RELATIVE_TO_GROUND,
                                Vector3D(boxExtent / 4, boxExtent / 4, height / 2),
                                2,
                                image,
                                imageName,
                                Vector3F(1,15,1),
                                NULL);
    _sr->addShape(bs);
  }
  
  virtual void onError(const std::string& error){
    
  }
};

void G3M3DSymbologyDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  PlanetRenderer* planetRenderer = model->getPlanetRenderer();
  planetRenderer->setVerticalExaggeration(16);

  ElevationDataProvider* elevationDataProvider = new SingleBilElevationDataProvider(URL("file:///full-earth-2048x1024.bil"),
                                                                                     Sector::fullSphere(),
                                                                                     Vector2I(2048, 1024));
  planetRenderer->setElevationDataProvider(elevationDataProvider, true);


//  MapBoxLayer* rasterLayer = new MapBoxLayer("examples.map-qogxobv1",
//                                             TimeInterval::fromDays(30),
//                                             true,
//                                             3);
//  model->getLayerSet()->addLayer(rasterLayer);
//  
  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);

  GEOVectorLayer* vectorLayer = new GEOVectorLayer();
  model->getLayerSet()->addLayer(vectorLayer);

  g3mWidget->setBackgroundColor( Color::fromRGBA(0.19f, 0.23f, 0.22f, 1.0f) );

  GEORenderer* geoRenderer = model->getGEORenderer();
  geoRenderer->setGEOVectorLayer(vectorLayer, false);

  geoRenderer->loadJSON(URL("file:///uscitieslite.geojson"), new USCitiesSymbolizer());
  
  
  //Texture Wrapping demo
  DownloaderImageBuilder* dib = new DownloaderImageBuilder(URL("http://www.bricksntiles.com/textures/brickwork-texture.jpg"));
  dib->build(g3mWidget->getG3MContext(), new MyIImageBuilderListener(getModel()->getShapesRenderer()), true);
  ///
  

  g3mWidget->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
                                       Geodetic3D::fromDegrees(25.5, -74.52, 1595850),
                                       Angle::zero(),
                                       //Angle::fromDegrees(45)
                                       Angle::fromDegrees(45 - 90)
                                       );
}
