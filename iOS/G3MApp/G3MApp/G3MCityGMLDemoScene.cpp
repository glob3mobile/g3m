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
  cityGMLFiles.push_back("file:///innenstadt_west_4326_lod2.gml");
  cityGMLFiles.push_back("file:///hagsfeld_4326_lod2.gml");
  cityGMLFiles.push_back("file:///durlach_4326_lod2_PART_1.gml");
  cityGMLFiles.push_back("file:///durlach_4326_lod2_PART_2.gml");
  cityGMLFiles.push_back("file:///hohenwettersbach_4326_lod2.gml");
  cityGMLFiles.push_back("file:///bulach_4326_lod2.gml");
  cityGMLFiles.push_back("file:///daxlanden_4326_lod2.gml");
  cityGMLFiles.push_back("file:///knielingen_4326_lod2_PART_1.gml");
  cityGMLFiles.push_back("file:///knielingen_4326_lod2_PART_2.gml");
  cityGMLFiles.push_back("file:///knielingen_4326_lod2_PART_3.gml");
  
  getModel()->getPlanetRenderer()->addTerrainTouchListener(new MyTerrainTL(getModel()->getG3MWidget()));
  
  for (size_t i = 0; i < cityGMLFiles.size(); i++) {
    
    CityGMLParser::addLOD2MeshAndMarksFromFile(cityGMLFiles[i], downloader, context->getPlanet(), getModel()->getMeshRenderer(), getModel()->getMarksRenderer());
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
  
}