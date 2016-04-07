//
//  CityGMLParser.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 30/3/16.
//
//

#include "CityGMLParser.hpp"

#include "ILogger.hpp"
#include "IStringUtils.hpp"

#include "IFactory.hpp"
#include "IDownloader.hpp"
#include "IBufferDownloadListener.hpp"
#include "G3MContext.hpp"
#include "DownloadPriority.hpp"
#include "TimeInterval.hpp"
#include "IStringUtils.hpp"
#include "CityGMLParser.hpp"
#include "MeshRenderer.hpp"
#include "URL.hpp"


class G3MCityGMLDemoScene_BufferDownloadListener : public IBufferDownloadListener {
private:
  const Planet* _planet;
  MeshRenderer* _meshRenderer;
  MarksRenderer* _marksRenderer;
  
  CityGMLBuildingColorProvider* _colorProvider;
  
  std::vector<CityGMLBuilding*>* _buildings;
public:
  G3MCityGMLDemoScene_BufferDownloadListener(const Planet* planet,
                                             MeshRenderer* meshRenderer,
                                             MarksRenderer* marksRenderer,
                                             CityGMLBuildingColorProvider* colorProvider,
                                             std::vector<CityGMLBuilding*>* buildings) :
  _planet(planet),
  _meshRenderer(meshRenderer),
  _marksRenderer(marksRenderer),
  _colorProvider(colorProvider),
  _buildings(buildings)
  {
    if (_colorProvider != NULL){
      _colorProvider->_retain();
    }
  }
  
  ~G3MCityGMLDemoScene_BufferDownloadListener(){
    if (_colorProvider != NULL){
      _colorProvider->_release();
    }
  }
  
  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    
    std::string s = buffer->getAsString();
    delete buffer;
    
    IXMLNode* xml = IFactory::instance()->createXMLNodeFromXML(s);
    
    std::vector<CityGMLBuilding*> buildings = CityGMLParser::parseLOD2Buildings2(xml);
    
    //Adding marks
    for (size_t i = 0; i < buildings.size(); i++) {
      _marksRenderer->addMark( buildings[i]->createMark(false) );
    }
    
    //Checking walls visibility
    int n = CityGMLBuilding::checkWallsVisibility(buildings);
    ILogger::instance()->logInfo("Removed %d invisible walls from the model.", n);
    
    //Creating mesh model
    Mesh* mesh = CityGMLBuilding::createMesh(buildings, *_planet, false, false, _colorProvider);
    
    _meshRenderer->addMesh(mesh);
    
    delete xml;
    
    if (_buildings == NULL){
    for (size_t i = 0; i < buildings.size(); i++) {
      delete buildings[i];
    }
    } else{
      for (size_t i = 0; i < buildings.size(); i++) {
        _buildings->push_back( buildings[i] );
      }
    }
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

void CityGMLParser::addLOD2MeshAndMarksFromFile(const std::string& url,
                                                IDownloader* downloader,
                                                const Planet* planet,
                                                MeshRenderer* meshRenderer,
                                                MarksRenderer* marksRenderer,
                                                CityGMLBuildingColorProvider* colorProvider,
                                                std::vector<CityGMLBuilding*>* buildings){
  downloader->requestBuffer(URL(url),
                            DownloadPriority::HIGHEST,
                            TimeInterval::fromHours(1),
                            true,
                            new G3MCityGMLDemoScene_BufferDownloadListener(planet, meshRenderer, marksRenderer, colorProvider, buildings),
                            true);
}

std::vector<CityGMLBuilding*> CityGMLParser::parseLOD2Buildings2(IXMLNode* cityGMLDoc) {
  
  std::vector<CityGMLBuilding*> buildings;
  
  const std::vector<IXMLNode*> buildingsXML = cityGMLDoc->evaluateXPathAsXMLNodes("//*[local-name()='Building']");
  //      ILogger.instance().logInfo("N Buildings %d", buildingsXML.size());
  for (size_t i = 0; i < buildingsXML.size(); i++) {
    
    IXMLNode* b = buildingsXML[i];
    
    //Name
    std::string* name = b->getAttribute("id");
    if (name == NULL){
      name = new std::string("NO NAME");
    }
    
    std::vector<CityGMLBuildingSurface*> surfaces;
    
    //Grounds
    const std::vector<IXMLNode*> grounds = b->evaluateXPathAsXMLNodes(" *[local-name()='boundedBy']//*[local-name()='GroundSurface']//*[local-name()='posList']");
    for (int j = 0; j < grounds.size(); j++) {
      std::string* str = grounds[j]->getTextContent();
      if (str != NULL){
        //ILogger::instance()->logInfo("%s", str->c_str() );
        
        std::vector<double> coors = IStringUtils::instance()->parseDoubles(*str, " ");
        if (coors.size() % 3 != 0){
          ILogger::instance()->logError("Problem parsing wall coordinates.");
        }
        
        surfaces.push_back(CityGMLBuildingSurface::createFromArrayOfCityGMLWGS84Coordinates(coors, GROUND));
        
        delete str;
      }
      
      delete grounds[j];
    }
    
    //Walls
    const std::vector<IXMLNode*> walls = b->evaluateXPathAsXMLNodes("*[local-name()='boundedBy']//*[local-name()='WallSurface']//*[local-name()='posList']");
    for (int j = 0; j < walls.size(); j++) {
      std::string* str = walls[j]->getTextContent();
      if (str != NULL){
        //ILogger::instance()->logInfo("%s", str->c_str() );
        
        std::vector<double> coors = IStringUtils::instance()->parseDoubles(*str, " ");
        if (coors.size() % 3 != 0){
          ILogger::instance()->logError("Problem parsing wall coordinates.");
        }
        
        surfaces.push_back(CityGMLBuildingSurface::createFromArrayOfCityGMLWGS84Coordinates(coors, WALL));
        
        delete str;
      }
      
      delete walls[j];
    }
    
    //Roofs
    const std::vector<IXMLNode*> roofs = b->evaluateXPathAsXMLNodes("*[local-name()='boundedBy']//*[local-name()='RoofSurface']//*[local-name()='posList']");
    for (int j = 0; j < roofs.size(); j++) {
      std::string* str = roofs[j]->getTextContent();
      if (str != NULL){
        //ILogger::instance()->logInfo("%s", str->c_str() );
        
        std::vector<double> coors = IStringUtils::instance()->parseDoubles(*str, " ");
        if (coors.size() % 3 != 0){
          ILogger::instance()->logError("Problem parsing wall coordinates.");
        }
        
        surfaces.push_back(CityGMLBuildingSurface::createFromArrayOfCityGMLWGS84Coordinates(coors, ROOF));
        
        delete str;
      }
      
      delete roofs[j];
    }
    
    CityGMLBuilding* nb = new CityGMLBuilding(*name, 1, surfaces);
    
    buildings.push_back(nb);
    
    delete name;
    delete b;
    
  }
  
  return buildings;
}

