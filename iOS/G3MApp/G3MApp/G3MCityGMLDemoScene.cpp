//
//  G3MCityGMLDemoScene.cpp
//  G3MApp
//
//  Created by Jose Miguel SN on 29/3/16.
//  Copyright © 2016 Igo Software SL. All rights reserved.
//

#include "G3MCityGMLDemoScene.hpp"

#include <G3MiOSSDK/IFactory.hpp>
#include <G3MiOSSDK/IXMLNode.hpp>
#include <G3MiOSSDK/IDownloader.hpp>
#include <G3MiOSSDK/IBufferDownloadListener.hpp>
#include <G3MiOSSDK/G3MContext.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>
#include <G3MiOSSDK/TimeInterval.hpp>
#include <G3MiOSSDK/IStringUtils.hpp>
#include <G3MiOSSDK/CityGMLParser.hpp>
#include <G3MiOSSDK/MeshRenderer.hpp>

#include <G3MiOSSDK/EllipsoidalPlanet.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/DeviceAttitudeCameraHandler.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>

#include <G3MiOSSDK/TerrainTouchListener.hpp>
#include <G3MiOSSDK/ColorLegend.hpp>
#include <G3MiOSSDK/BuildingDataParser.hpp>

class MyTerrainTL: public TerrainTouchListener {
  
  G3MWidget* _widget;
  
  
public:
  
  
  MyTerrainTL(G3MWidget* widget):_widget(widget){
    
  }
  
  bool onTerrainTouch(const G3MEventContext* ec,
                      const Vector2F&        pixel,
                      const Camera*          camera,
                      const Geodetic3D&      position,
                      const Tile*            tile){
    class LM: public ILocationModifier{
      Geodetic3D modify(const Geodetic3D& location){
        return Geodetic3D::fromDegrees(location._latitude._degrees, location._longitude._degrees, 12);
      }
    };
    
    DeviceAttitudeCameraHandler* dac = new DeviceAttitudeCameraHandler(true, new LM());
    _widget->getCameraRenderer()->addHandler(dac);
    
    return true;
  }
  
};

class ColouringCityGMLDemoSceneBDL : public IBufferDownloadListener {
private:
  G3MCityGMLDemoScene* _demo;
  std::vector<CityGMLBuilding*> _buildings;
public:
  ColouringCityGMLDemoSceneBDL(G3MCityGMLDemoScene* demo, std::vector<CityGMLBuilding*> buildings) :
  _demo(demo),
  _buildings(buildings)
  {
  }
  
  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    
    std::string s = buffer->getAsString();
    delete buffer;
    BuildingDataParser::includeDataInBuildingSet(s, _buildings);
  }
  
  void onError(const URL& url) {
    ILogger::instance()->logError("Error downloading \"%s\"", url.getPath().c_str());
  }
  
  void onCancel(const URL& url) {
    // do nothing
  }
  
  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
    // do nothing
  }
  
};

void G3MCityGMLDemoScene::colorBuildings(CityGMLBuildingColorProvider* cp){
  
  for (size_t i = 0; i < _buildings.size(); i++) {
    CityGMLBuilding* b = _buildings.at(i);
    Color c = cp->getColor(b);
    b->changeColorOfBuildingInBoundedMesh(c);
  }
  
}

class MyCityGMLListener: public CityGMLListener{
  
private:
  G3MCityGMLDemoScene* _demo;
public:
  
  MyCityGMLListener(G3MCityGMLDemoScene* demo):_demo(demo){
    
  }
  
  virtual void onBuildingsCreated(const std::vector<CityGMLBuilding*>& buildings){
    
    for (int i = 0; i < buildings.size(); i++) {
      _demo->_buildings.push_back(buildings[i]);
    }
    
    _demo->getModel()->getG3MWidget()->getG3MContext()
    ->getDownloader()->requestBuffer(URL("file:///karlsruhe_data.geojson"), 1000, TimeInterval::forever(), true,
                                     new ColouringCityGMLDemoSceneBDL(_demo, _demo->_buildings),
                                     true);
    
  }
  
  virtual void onError(){
    
  }
};


void G3MCityGMLDemoScene::rawActivate(const G3MContext* context) {
  
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();
  
  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  getModel()->getLayerSet()->addLayer(layer);
  
  
  IDownloader* downloader = context->getDownloader();
  
  std::vector<std::string> cityGMLFiles;
  cityGMLFiles.push_back("file:///innenstadt_ost_4326_lod2.gml");
  //  cityGMLFiles.push_back("file:///innenstadt_west_4326_lod2.gml");
  //  cityGMLFiles.push_back("file:///hagsfeld_4326_lod2.gml");
  //  cityGMLFiles.push_back("file:///durlach_4326_lod2_PART_1.gml");
  //  cityGMLFiles.push_back("file:///durlach_4326_lod2_PART_2.gml");
  //  cityGMLFiles.push_back("file:///hohenwettersbach_4326_lod2.gml");
  //      cityGMLFiles.push_back("file:///bulach_4326_lod2.gml");
  //      cityGMLFiles.push_back("file:///daxlanden_4326_lod2.gml");
  //      cityGMLFiles.push_back("file:///knielingen_4326_lod2_PART_1.gml");
  //      cityGMLFiles.push_back("file:///knielingen_4326_lod2_PART_2.gml");
  //      cityGMLFiles.push_back("file:///knielingen_4326_lod2_PART_3.gml");
  
  getModel()->getPlanetRenderer()->addTerrainTouchListener(new MyTerrainTL(getModel()->getG3MWidget()));
  
  //  downloader->requestBuffer(URL("file:///karlsruhe_data.geojson"), 1000, TimeInterval::forever(), true,
  //                            new ColouringCityGMLDemoSceneBDL(this),
  //                            true);
  
  for (size_t i = 0; i < cityGMLFiles.size(); i++) {
    
    CityGMLParser::addLOD2MeshAndMarksFromFile(cityGMLFiles[i], downloader, context->getPlanet(), getModel()->getMeshRenderer(), getModel()->getMarksRenderer(),
                                               new MyCityGMLListener(this), true);
  }
  
  
  
  
  //Whole city!
  g3mWidget->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
                                       Geodetic3D::fromDegrees(49.07139214735035182, 8.134019638291379195, 22423.46165080198989),
                                       Angle::fromDegrees(-109.452892),
                                       Angle::fromDegrees(-44.938813)
                                       );
}

void G3MCityGMLDemoScene::deactivate(const G3MContext* context) {
  
  G3MDemoScene::deactivate(context);
}

void G3MCityGMLDemoScene::rawSelectOption(const std::string& option,
                                          int optionIndex) {
  
  if (option == "Random Colors"){
    RandomBuildingColorPicker* rcp = new RandomBuildingColorPicker();
    colorBuildings(rcp);
    delete rcp;
  }
  
  
  if (option == "Heat Demand"){
    
    std::vector<ColorLegend::ColorAndValue*> legend;
    legend.push_back(new ColorLegend::ColorAndValue(Color::green(), 6336.0));
    legend.push_back(new ColorLegend::ColorAndValue(Color::white(), 70000.0));
    ColorLegend* cl = new ColorLegend(legend);
    CityGMLBuildingColorProvider* colorProvider = new BuildingDataColorProvider("Heat_Dem_1", cl);
    colorBuildings(colorProvider);
    delete colorProvider;
    
  }
  
  if (option == "Volume"){
    
    std::vector<ColorLegend::ColorAndValue*> legend;
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(213,62,79, 255), 2196.0));
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(252,141,89, 255), 6816.0));
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(254,224,139, 255), 17388.0));
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(230,245,152, 255), 33165.0));
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(153,213,148, 255), 62472.0));
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(50,136,189, 255), 122553.0));
    ColorLegend* cl = new ColorLegend(legend);
    
    BuildingDataColorProvider* colorProvider = new BuildingDataColorProvider("Bui_Volu_1", cl);
    colorBuildings(colorProvider);
    delete colorProvider;
  }
  
  if (option == "QCL"){
    
    int nClasses = 18;
    int colors[] = {103,0,31,
      178,24,43,
      214,96,77,
      244,165,130,
      253,219,199,
      247,247,247,
      209,229,240,
      146,197,222,
      67,147,195,
      33,102,172,
      5,48,97,
      171,221,164,
      102,194,165,
      50,136,189,
      94,79,162,
      227,26,28,
      253,191,111,
      255,127,0};
    
    std::vector<ColorLegend::ColorAndValue*> legend;
    for (int i = 0; i < nClasses;i++) {
      legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(colors[i*3],
                                                                         colors[i*3+1],
                                                                         colors[i*3+2],
                                                                         255),
                                                      (double)(i+1)));
    }
    
    ColorLegend* cl = new ColorLegend(legend);
    CityGMLBuildingColorProvider* colorProvider = new BuildingDataColorProvider("QCL_1", cl);
    colorBuildings(colorProvider);
    delete colorProvider;
    
  }
  
  if (option == "SOM Cluster"){
    
    std::vector<ColorLegend::ColorAndValue*> legend;
    
    int nClasses = 15;
    int colors[] = {103,0,31,
      178,24,43,
      214,96,77,
      244,165,130,
      253,219,199,
      247,247,247,
      209,229,240,
      146,197,222,
      67,147,195,
      33,102,172,
      5,48,97,
      166,219,160,
      90,174,97,
      27,120,55,
      0,68,27};
    
    for (int i = 0; i < nClasses;i++) {
      legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(colors[i*3],
                                                                         colors[i*3+1],
                                                                         colors[i*3+2],
                                                                         255),
                                                      (double)(i+1)));
    }
    
    ColorLegend* cl = new ColorLegend(legend);
    CityGMLBuildingColorProvider* colorProvider = new BuildingDataColorProvider("SOMcluster", cl);
    colorBuildings(colorProvider);
    delete colorProvider;
    
  }
  
  if (option == "Field 2"){
    
    std::vector<ColorLegend::ColorAndValue*> legend;
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(213,62,79, 255), -0.05));
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(252,141,89, 255), 0.05));
    
    ColorLegend* cl = new ColorLegend(legend);
    CityGMLBuildingColorProvider* colorProvider = new BuildingDataColorProvider("Field2_12", cl);
    colorBuildings(colorProvider);
    delete colorProvider;
    
  }
  
}