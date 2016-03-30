//
//  G3MCityGMLDemoScene.cpp
//  G3MApp
//
//  Created by Jose Miguel SN on 29/3/16.
//  Copyright © 2016 Igo Software SL. All rights reserved.
//

#include "G3MCityGMLDemoScene.hpp"

#include <G3MiOSSDK/IFactory.hpp>
#include <G3MiOSSDK/IXMLDocument.hpp>
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



class G3MCityGMLDemoScene_BufferDownloadListener : public IBufferDownloadListener {
private:
  G3MCityGMLDemoScene* _scene;
public:
  G3MCityGMLDemoScene_BufferDownloadListener(G3MCityGMLDemoScene* scene) :
  _scene(scene)
  {
  }
  
  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    
    std::string s = buffer->getAsString();
    
    IXMLDocument* xml = IFactory::instance()->createXMLDocumentFromXML(s);
    
    std::vector<CityGMLBuilding*> buildings = CityGMLParser::parseLOD2Buildings2(xml);
    
    const Planet* planet = EllipsoidalPlanet::createEarth();
    
    Mesh* mesh = CityGMLBuilding::createSingleIndexedMeshWithColorPerVertexForBuildings(buildings, *planet, false);
    
    _scene->getModel()->getMeshRenderer()->addMesh(mesh);
    
//    std::vector<IXMLDocument*> buildings = xml->evaluateXPathAsXMLDocuments("//*[local-name()='Building']");
//    
//    for (int i = 0; i < buildings.size(); i++) {
//      buildings[i]->evaluateXPathAsXMLDocuments("*[local-name()='boundedBy']//*[local-name()='posList']");
//    }
    
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

void G3MCityGMLDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();
  
  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  getModel()->getLayerSet()->addLayer(layer);
  
  
  IDownloader* downloader = context->getDownloader();
  
  
  
  _requestId = downloader->requestBuffer(URL("file:///test_sample_4326_lod2.gml"),
                                         DownloadPriority::HIGHEST,
                                         TimeInterval::fromHours(1),
                                         true,
                                         new G3MCityGMLDemoScene_BufferDownloadListener(this),
                                         true);
  
  g3mWidget->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
                                       Geodetic3D::fromDegrees(49.0158653208903, 8.39695262907557, 1000),
                                       Angle::zero(),
                                       //Angle::fromDegrees(45)
                                       Angle::fromDegrees(45 - 90)
                                       );
  
  
}

void G3MCityGMLDemoScene::deactivate(const G3MContext* context) {

  G3MDemoScene::deactivate(context);
}

void G3MCityGMLDemoScene::rawSelectOption(const std::string& option,
                                        int optionIndex) {
  
}