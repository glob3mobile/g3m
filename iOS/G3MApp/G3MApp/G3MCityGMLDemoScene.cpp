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



//class G3MCityGMLDemoScene_BufferDownloadListener : public IBufferDownloadListener {
//private:
//  G3MCityGMLDemoScene* _scene;
//  const Planet* _planet;
//  const bool _deleteBuildings;
//public:
//  G3MCityGMLDemoScene_BufferDownloadListener(G3MCityGMLDemoScene* scene, const Planet* planet, bool deleteBuildings) :
//  _scene(scene),_planet(planet), _deleteBuildings(deleteBuildings)
//  {
//  }
//  
//  void onDownload(const URL& url,
//                  IByteBuffer* buffer,
//                  bool expired) {
//    
//    std::string s = buffer->getAsString();
//    
//    IXMLNode* xml = IFactory::instance()->createXMLNodeFromXML(s);
//    
//    std::vector<CityGMLBuilding*> buildings = CityGMLParser::parseLOD2Buildings2(xml);
//    
//    //Adding marks
//    for (size_t i = 0; i < buildings.size(); i++) {
//      _scene->getModel()->getMarksRenderer()->addMark( buildings[i]->createMark(false) );
//    }
//    
//    const Planet* planet = EllipsoidalPlanet::createEarth();
//    
//    //Creating mesh model
//    Mesh* mesh = CityGMLBuilding::createSingleIndexedMeshWithColorPerVertexForBuildings(buildings, *planet, false);
//    
//    _scene->getModel()->getMeshRenderer()->addMesh(mesh);
//    
//    delete xml;
//    
//    delete buffer;
//    
//    if (_deleteBuildings){
//      for (size_t i = 0; i < buildings.size(); i++) {
//        delete buildings[i];
//      }
//    }
//  }
//  
//  void onError(const URL& url) {
//    ILogger::instance()->logError("Error downloading \"%s\"", url.getPath().c_str());
//  }
//  
//  void onCancel(const URL& url) {
//    // do nothing
//  }
//  
//  void onCanceledDownload(const URL& url,
//                          IByteBuffer* buffer,
//                          bool expired) {
//    // do nothing
//  }
//  
//};

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
  
  
  
  for (size_t i = 0; i < cityGMLFiles.size(); i++) {
    
    CityGMLParser::addLOD2MeshAndMarksFromFile(cityGMLFiles[i], downloader, context->getPlanet(), getModel()->getMeshRenderer(), getModel()->getMarksRenderer());
    
//    _requestId = downloader->requestBuffer(URL(cityGMLFiles[i]),
//                                           DownloadPriority::HIGHEST,
//                                           TimeInterval::fromHours(1),
//                                           true,
//                                           new G3MCityGMLDemoScene_BufferDownloadListener(this, context->getPlanet(), true),
//                                           true);
  }
  
  
  
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