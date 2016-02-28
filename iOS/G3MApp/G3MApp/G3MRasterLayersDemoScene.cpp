//
//  G3MRasterLayersDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//

#include "G3MRasterLayersDemoScene.hpp"

#include "G3MDemoModel.hpp"

#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/MapBoxLayer.hpp>
#include <G3MiOSSDK/MapQuestLayer.hpp>
#include <G3MiOSSDK/WMSLayer.hpp>
#include <G3MiOSSDK/LevelTileCondition.hpp>
#include <G3MiOSSDK/OSMLayer.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/URLTemplateLayer.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/TimeInterval.hpp>
#include <G3MiOSSDK/MercatorTiledLayer.hpp>



// temp
#include <G3MiOSSDK/MeshRenderer.hpp>
#include <G3MiOSSDK/Frustum.hpp>
#include <G3MiOSSDK/Sphere.hpp>
#include <G3MiOSSDK/OrientedBox.hpp>
#include <G3MiOSSDK/Box.hpp>



void G3MRasterLayersDemoScene::createLayerSet(LayerSet* layerSet) {
  MapBoxLayer* mboxOSMLayer = new MapBoxLayer("examples.map-cnkhv76j",
                                              TimeInterval::fromDays(30),
                                              true,
                                              2);
  mboxOSMLayer->setTitle("Map Box OSM");
  mboxOSMLayer->setEnable(true);
  layerSet->addLayer(mboxOSMLayer);


  MapQuestLayer* mqOSM = MapQuestLayer::newOSM(TimeInterval::fromDays(30));
  mqOSM->setEnable(false);
  mqOSM->setTitle("MapQuest OSM");
  layerSet->addLayer(mqOSM);


  MapQuestLayer* mqlAerial = MapQuestLayer::newOpenAerial(TimeInterval::fromDays(30));
  mqlAerial->setTitle("MapQuest Aerial");
  mqlAerial->setEnable(false);
  layerSet->addLayer(mqlAerial);


  MapBoxLayer* mboxAerialLayer = new MapBoxLayer("examples.map-m0t0lrpu",
                                                 TimeInterval::fromDays(30),
                                                 true,
                                                 2);
  mboxAerialLayer->setTitle("Map Box Aerial");
  mboxAerialLayer->setEnable(false);
  layerSet->addLayer(mboxAerialLayer);


  MapBoxLayer* mboxTerrainLayer = new MapBoxLayer("examples.map-qogxobv1",
                                                  TimeInterval::fromDays(30),
                                                  true,
                                                  2);
  mboxTerrainLayer->setTitle("Map Box Terrain");
  mboxTerrainLayer->setEnable(false);
  layerSet->addLayer(mboxTerrainLayer);


  WMSLayer* blueMarble = new WMSLayer("bmng200405",
                                      URL("http://www.nasa.network.com/wms?"),
                                      WMS_1_1_0,
                                      Sector::fullSphere(),
                                      "image/jpeg",
                                      "EPSG:4326",
                                      "",
                                      false,
                                      new LevelTileCondition(0, 18),
                                      TimeInterval::fromDays(30),
                                      true);
  blueMarble->setTitle("Nasa Blue Marble (WMS)");
  blueMarble->setEnable(false);
  layerSet->addLayer(blueMarble);


  OSMLayer* osmLayer = new OSMLayer(TimeInterval::fromDays(30));
  osmLayer->setTitle("Open Street Map");
  osmLayer->setEnable(false);
  layerSet->addLayer(osmLayer);


  BingMapsLayer* bingMapsAerialLayer = new BingMapsLayer(BingMapType::Aerial(),
                                                         "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                                         TimeInterval::fromDays(30));
  bingMapsAerialLayer->setTitle("Bing Aerial");
  bingMapsAerialLayer->setEnable(false);
  layerSet->addLayer(bingMapsAerialLayer);


  BingMapsLayer* bingMapsAerialWithLabels = new BingMapsLayer(BingMapType::AerialWithLabels(),
                                                              "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                                              TimeInterval::fromDays(30));
  bingMapsAerialWithLabels->setTitle("Bing Aerial With Labels");
  bingMapsAerialWithLabels->setEnable(false);
  layerSet->addLayer(bingMapsAerialWithLabels);

  BingMapsLayer* bingMapsCollinsBart = new BingMapsLayer(BingMapType::CollinsBart(),
                                                         "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                                         TimeInterval::fromDays(30));
  bingMapsCollinsBart->setTitle("MapQuest OSM");
  bingMapsCollinsBart->setEnable(false);
  layerSet->addLayer(bingMapsCollinsBart);


  std::vector<std::string> subdomains;
  subdomains.push_back("0.");
  subdomains.push_back("1.");
  subdomains.push_back("2.");
  subdomains.push_back("3.");
  MercatorTiledLayer* meteoritesLayer = new MercatorTiledLayer("http://",
                                                               "tiles.cartocdn.com/osm2/tiles/meteoritessize",
                                                               subdomains,
                                                               "png",
                                                               TimeInterval::fromDays(90),
                                                               false,
                                                               2,
                                                               17,
                                                               true // isTransparent
                                                               );
  meteoritesLayer->setTitle("CartoDB Meteorites");
  meteoritesLayer->setEnable(false);
  layerSet->addLayer(meteoritesLayer);


  URLTemplateLayer* arcGISOverlayLayerTest = URLTemplateLayer::newMercator("http://www.fairfaxcounty.gov/gis/rest/services/DMV/DMV/MapServer/export?dpi=96&transparent=true&format=png8&bbox={west}%2C{south}%2C{east}%2C{north}&bboxSR=3857&imageSR=3857&size={width}%2C{height}&f=image",
                                                                           Sector::fullSphere(),
                                                                           true,
                                                                           2,
                                                                           18,
                                                                           TimeInterval::fromDays(30),
                                                                           true,
                                                                           1,
                                                                           new LevelTileCondition(12, 18));
  arcGISOverlayLayerTest->setTitle("ESRI ArcGis Online");
  arcGISOverlayLayerTest->setEnable(false);
  layerSet->addLayer(arcGISOverlayLayerTest);


  { // Uruguay (WMS)

    WMSLayer* unidadesPaisajisticas = new WMSLayer("Unidades_Paisajisticas",
                                                   URL("http://www.dinama.gub.uy/sia/ecosistemas/ecosistemas.cgi"),
                                                   WMS_1_1_0,
                                                   Sector::fromDegrees(-34.9739999999999966, -58.4393000000000029,
                                                                       -30.0854999999999997, -53.1811000000000007),
                                                   "image/png", // format
                                                   "EPSG:4326",
                                                   "",          // style
                                                   true,        // isTransparent
                                                   NULL,        // condition,
                                                   TimeInterval::fromDays(30),
                                                   true,        // readExpired
                                                   NULL,        // parameters
                                                   0.85f         // transparency
                                                   );
    unidadesPaisajisticas->setTitle("Uruguay_Unidades_Paisajisticas");
    unidadesPaisajisticas->setEnable(false);
    layerSet->addLayer(unidadesPaisajisticas);


    WMSLayer* ambientesAcuaticos = new WMSLayer("ambientes_acuaticos",
                                                URL("http://www.dinama.gub.uy/sia/ecosistemas/ecosistemas.cgi"),
                                                WMS_1_1_0,
                                                Sector::fromDegrees(-40.0005999999999986, -59.4996000000000009,
                                                                    -32.9996000000000009, -49.4981000000000009),
                                                "image/png", // format
                                                "EPSG:4326",
                                                "",          // style
                                                true,        // isTransparent
                                                NULL,        // condition,
                                                TimeInterval::fromDays(30),
                                                true,        // readExpired
                                                NULL,        // parameters
                                                0.85f         // transparency
                                                );
    ambientesAcuaticos->setTitle("Uruguay_Ambientes_Acuaticos");
    ambientesAcuaticos->setEnable(false);
    layerSet->addLayer(ambientesAcuaticos);
  }
}


void G3MRasterLayersDemoScene::rawActivate(const G3MContext* context) {
  createLayerSet( getModel()->getLayerSet() );
  
  
  // FOLLOWING CODE IS FOR TESTING INTERSECTION SPHERE FRUSTUM

  // creating sample frustum
  Frustum frustum0(FrustumData(-1500,1500,-2000,2000,8000,20000));
  Frustum* frustum = frustum0.transformedBy_P(MutableMatrix44D::createTranslationMatrix(6.38e6, 0, 0.7e6).inversed());
  MeshRenderer* meshRenderer = getModel()->getMeshRenderer();
  meshRenderer->addMesh(frustum->createWireFrameMesh());
  
  // creating oriented boxes
  Vector3D lower(6383750+1000, -5000-1000, 680000-1000);
  OrientedBox obb(lower, Vector3D(5000,1000,0), Vector3D(-1000,5000,0), Vector3D(0,0,3000));
  //OrientedBox obb(lower, Vector3D(2000,0,0), Vector3D(0,2000,0), Vector3D(0,0,3000));
  meshRenderer->addMesh(obb.createMesh(Color::fromRGBA(0, 0, 0, 1)));

  // creating sphere
  Vector3D point = lower.add(Vector3D(1000,1000,1000));
  Sphere sphere(point, 3000);
  meshRenderer->addMesh(sphere.createWireframeMesh(Color::fromRGBA(0, 0, 0, 1),20));
  
  Box* box = sphere.mergedWithOrientedBox(&obb);
  meshRenderer->addMesh(box->createMesh(Color::fromRGBA(0, 0, 1, 1)));
  
  
  Polygon3D polA(Vector3D(-1.634844, -2.612539, -1.881418),
                 Vector3D(-2.800236, -2.940148, 4.924435),
                 Vector3D(-6.618011, -7.054529, 4.072652),
                 Vector3D(-5.452620, -6.726920, -2.733201));
  Polygon3D polB(Vector3D(-4.460555, -4.088862, -4.504524),
                 Vector3D(-8.965107, -8.868035, 54.606972),
                 Vector3D(-11.516288, -12.901808, 54.086430),
                 Vector3D(-7.011736, -8.122636, -5.025066));
  printf("resultados=%d\n", polA.touchesPolygon3D(polB));
  
  /*
  // creating spheres
  {
    // outside, close to a frustum corner
    Sphere sphere(Vector3D(6383750+1000, -5000-1000, 680000-1000), 1000);
    Color color =  (sphere.touchesFrustum(frustum))? Color::fromRGBA(0, 1, 0, 1) : Color::fromRGBA(1, 0, 0, 1);
    meshRenderer->addMesh(sphere.createWireframeMesh(color, 50));
  }
  {
    // outside, close to a frustum edge
    Sphere sphere(Vector3D(6380000, -5000-800, 680000-800), 800);
    Color color =  (sphere.touchesFrustum(frustum))? Color::fromRGBA(0, 1, 0, 1) : Color::fromRGBA(1, 0, 0, 1);
    meshRenderer->addMesh(sphere.createWireframeMesh(color, 50));
  }
  {
    // outside, close to a frustum face
    Sphere sphere(Vector3D(6384000, 0, 686000), 800);
    Color color =  (sphere.touchesFrustum(frustum))? Color::fromRGBA(0, 1, 0, 1) : Color::fromRGBA(1, 0, 0, 1);
    meshRenderer->addMesh(sphere.createWireframeMesh(color, 50));
  }
  /*{
    // outside, in the center of proyection of the frustum
    Sphere sphere(Vector3D(6380000, 0, 700000), 1000);
    Color color =  (sphere.touchesFrustum(frustum))? Color::fromRGBA(0, 1, 0, 1) : Color::fromRGBA(1, 0, 0, 1);
    meshRenderer->addMesh(sphere.createWireframeMesh(color, 50));
  }*/
  /*
  {
    // inside the frustum center
    Sphere sphere(Vector3D(6380000, 0, 686000), 800);
    Color color =  (sphere.touchesFrustum(frustum))? Color::fromRGBA(0, 1, 0, 1) : Color::fromRGBA(1, 0, 0, 1);
    meshRenderer->addMesh(sphere.createWireframeMesh(color, 50));
  }
  {
    // behind the center of proyection, with big radius
    Sphere sphere(Vector3D(6375000, 0, 700000), 8500);
    Color color =  (sphere.touchesFrustum(frustum))? Color::fromRGBA(0, 1, 0, 1) : Color::fromRGBA(1, 0, 0, 1);
    meshRenderer->addMesh(sphere.createWireframeMesh(color, 50));
  }*/

  
  // locating camera
  G3MWidget*    g3mWidget = getModel()->getG3MWidget();
  g3mWidget->setCameraPosition(Geodetic3D(Angle::fromDegrees(5.88), Angle::fromDegrees(0.5), 71600));
  g3mWidget->setCameraHeading(Angle::fromDegrees(59.30));
  g3mWidget->setCameraPitch(Angle::fromDegrees(-29.15));

  
  
  
}

void G3MRasterLayersDemoScene::rawSelectOption(const std::string& option,
                                               int optionIndex) {
  LayerSet* layerSet = getModel()->getLayerSet();
  layerSet->disableAllLayers();

  if (option == "MapBox OSM") {
    layerSet->getLayerByTitle("Map Box OSM")->setEnable(true);
  }
  else if (option == "Open Street Map") {
    layerSet->getLayerByTitle("Open Street Map")->setEnable(true);
  }
  else if (option == "MapBox Terrain") {
    layerSet->getLayerByTitle("Map Box Terrain")->setEnable(true);
  }
  else if (option == "MapBox Aerial") {
    layerSet->getLayerByTitle("Map Box Aerial")->setEnable(true);
  }
  else if (option == "CartoDB Meteorites") {
    layerSet->getLayerByTitle("MapQuest OSM")->setEnable(true);
    layerSet->getLayerByTitle("CartoDB Meteorites")->setEnable(true);
  }
  else if (option == "MapQuest Aerial") {
    layerSet->getLayerByTitle("Map Box Aerial")->setEnable(true);
  }
  else if (option == "MapQuest OSM") {
    layerSet->getLayerByTitle("MapQuest OSM")->setEnable(true);
  }
  else if (option == "Nasa Blue Marble (WMS)") {
    layerSet->getLayerByTitle("Nasa Blue Marble (WMS)")->setEnable(true);
  }
  else if (option == "ESRI ArcGis Online") {
    layerSet->getLayerByTitle("Map Box Aerial")->setEnable(true);
    layerSet->getLayerByTitle("ESRI ArcGis Online")->setEnable(true);

    getModel()->getG3MWidget()->setAnimatedCameraPosition(Geodetic3D::fromDegrees(38.6,
                                                                                  -77.2,
                                                                                  30000));
  }
  else if (option == "Bing Aerial") {
    layerSet->getLayerByTitle("Bing Aerial")->setEnable(true);
  }
  else if (option == "Bing Aerial with Labels") {
    layerSet->getLayerByTitle("Bing Aerial With Labels")->setEnable(true);
  }
  else if (option == "Uruguay (WMS)") {
    layerSet->getLayerByTitle("Nasa Blue Marble (WMS)")->setEnable(true);
    layerSet->getLayerByTitle("Uruguay_Unidades_Paisajisticas")->setEnable(true);
    layerSet->getLayerByTitle("Uruguay_Ambientes_Acuaticos")->setEnable(true);

    // just uruguay land
    //    getModel()->getG3MWidget()->setAnimatedCameraPosition(Geodetic3D::fromDegrees(-32.568042356920749114,
    //                                                                                  -56.383545687724506479,
    //                                                                                  1190419));

    // land and water area
    getModel()->getG3MWidget()->setAnimatedCameraPosition(Geodetic3D::fromDegrees(-35.093636856649169431,
                                                                                  -56.098973158344456635,
                                                                                  2139700));
  }
  else {
    ILogger::instance()->logError("option \"%s\" not supported", option.c_str());
  }
}
