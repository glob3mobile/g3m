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
public:
  ColouringCityGMLDemoSceneBDL(G3MCityGMLDemoScene* demo) :
  _demo(demo)
  {
  }
  
  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    
    std::string s = buffer->getAsString();
    delete buffer;
    
    std::vector<ColorLegend::ColorAndValue*> legend;
    legend.push_back(new ColorLegend::ColorAndValue(Color::blue(), 6336.0));
    legend.push_back(new ColorLegend::ColorAndValue(Color::red(), 70000.0));
    //    double gap = 1012376.75;
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(254,240,217,255), 6336.0 + gap * 0));
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(253,212,158,255), 6336.0 + gap * 1));
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(253,187,132,255), 6336.0 + gap * 2));
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(252,141,89,255), 6336.0 + gap * 3));
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(179,0,0,255), 6336.0 + gap * 4));
    ColorLegend* cl = new ColorLegend(legend);
    
    
    _demo->_colorProvider = new GeoJSONDataBuildingColorPicker(s, cl, GeoJSONDataBuildingColorPicker::BUILDING_PROPERTY::HEAT_DEMAND);
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

void G3MCityGMLDemoScene::colorBuildings(GeoJSONDataBuildingColorPicker::BUILDING_PROPERTY prop){
  _colorProvider->_activeProperty = prop;
  for (size_t i = 0; i < _buildings->size(); i++) {
    _buildings->at(i)->changeColorOfBuildingInBoundedMesh(*_colorProvider);
  }
  
}


void G3MCityGMLDemoScene::rawActivate(const G3MContext* context) {
  
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();
  
  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  getModel()->getLayerSet()->addLayer(layer);
  
  
  IDownloader* downloader = context->getDownloader();
  
  std::vector<std::string> cityGMLFiles;
//  cityGMLFiles.push_back("file:///innenstadt_ost_4326_lod2.gml");
  cityGMLFiles.push_back("file:///innenstadt_west_4326_lod2.gml");
//    cityGMLFiles.push_back("file:///hagsfeld_4326_lod2.gml");
//    cityGMLFiles.push_back("file:///durlach_4326_lod2_PART_1.gml");
//    cityGMLFiles.push_back("file:///durlach_4326_lod2_PART_2.gml");
//    cityGMLFiles.push_back("file:///hohenwettersbach_4326_lod2.gml");
//    cityGMLFiles.push_back("file:///bulach_4326_lod2.gml");
//    cityGMLFiles.push_back("file:///daxlanden_4326_lod2.gml");
//    cityGMLFiles.push_back("file:///knielingen_4326_lod2_PART_1.gml");
//    cityGMLFiles.push_back("file:///knielingen_4326_lod2_PART_2.gml");
//    cityGMLFiles.push_back("file:///knielingen_4326_lod2_PART_3.gml");
  
  getModel()->getPlanetRenderer()->addTerrainTouchListener(new MyTerrainTL(getModel()->getG3MWidget()));
  
  downloader->requestBuffer(URL("file:///Innenstadt_west_all_data.geojson"), 1000, TimeInterval::forever(), true,
                                                          new ColouringCityGMLDemoSceneBDL(this),
                                                          true);
  
  
  _buildings = new std::vector<CityGMLBuilding*>();
  
    for (size_t i = 0; i < cityGMLFiles.size(); i++) {
  
      CityGMLParser::addLOD2MeshAndMarksFromFile(cityGMLFiles[i], downloader, context->getPlanet(), getModel()->getMeshRenderer(), getModel()->getMarksRenderer(), NULL, _buildings);
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
  
  
//  IDownloader* downloader = getModel()->getG3MWidget()->getG3MContext()->getDownloader();
  if (option == "Random Colors"){
    RandomBuildingColorPicker* rcp = new RandomBuildingColorPicker();
    
    for (size_t i = 0; i < _buildings->size(); i++) {
      _buildings->at(i)->changeColorOfBuildingInBoundedMesh(*rcp);
    }
    
    rcp->_release();
  }
  
  
  if (option == "Heat Demand"){
    
    std::vector<ColorLegend::ColorAndValue*> legend;
    legend.push_back(new ColorLegend::ColorAndValue(Color::green(), 6336.0));
    legend.push_back(new ColorLegend::ColorAndValue(Color::white(), 70000.0));
    ColorLegend* cl = new ColorLegend(legend);
    _colorProvider->setLegend(cl);
    
//    downloader->requestBuffer(URL("file:///Innenstadt_west_all_data.geojson"), 1000, TimeInterval::forever(), true,
//                              new ColouringCityGMLDemoSceneBDL(_buildings,
//                                                               GeoJSONDataBuildingColorPicker::BUILDING_PROPERTY::HEAT_DEMAND),
//                              true);
    
    colorBuildings(GeoJSONDataBuildingColorPicker::BUILDING_PROPERTY::HEAT_DEMAND);
    
  }
  
  if (option == "Volume"){
    
    std::vector<ColorLegend::ColorAndValue*> legend;
    legend.push_back(new ColorLegend::ColorAndValue(Color::blue(), 6336.0));
    legend.push_back(new ColorLegend::ColorAndValue(Color::red(), 70000.0));
    ColorLegend* cl = new ColorLegend(legend);
    _colorProvider->setLegend(cl);
    
//    downloader->requestBuffer(URL("file:///Innenstadt_west_all_data.geojson"), 1000, TimeInterval::forever(), true,
//                              new ColouringCityGMLDemoSceneBDL(_buildings,
//                                                               GeoJSONDataBuildingColorPicker::BUILDING_PROPERTY::VOLUME),
//                              true);
    
    colorBuildings(GeoJSONDataBuildingColorPicker::BUILDING_PROPERTY::VOLUME);
    
  }
  
}