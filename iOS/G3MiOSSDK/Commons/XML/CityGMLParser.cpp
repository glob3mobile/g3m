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

#include "CityGMLBuildingTessellator.hpp"
#include "IThreadUtils.hpp"


//#import <mach/mach.h>


class CityGMLParser_BufferDownloadListener : public IBufferDownloadListener {
private:
  CityGMLListener* _listener;
  bool _deleteListener;
  const IThreadUtils* _threadUtils;
  
  class ParsingTask: public GAsyncTask {
    const std::string _s;
    std::vector<CityGMLBuilding*> _buildings;
    
    CityGMLListener* _listener;
    bool _deleteListener;
  public:
    
    ParsingTask(std::string& s,
                CityGMLListener* listener,
                bool deleteListener):
    _s(s),
    _listener(listener),
    _deleteListener(deleteListener)
    {}
    
    //    vm_size_t report_memory(void) {
    //      struct task_basic_info info;
    //      mach_msg_type_number_t size = sizeof(info);
    //      kern_return_t kerr = task_info(mach_task_self(),
    //                                     TASK_BASIC_INFO,
    //                                     (task_info_t)&info,
    //                                     &size);
    //      if( kerr == KERN_SUCCESS ) {
    //        //    printf("Memory in use (in bytes): %lu", info.resident_size);
    //        return info.resident_size;
    //      } else {
    //        //    printf("Error with task_info(): %s", mach_error_string(kerr));
    //        return 0;
    //      }
    //    }
    
    virtual void runInBackground(const G3MContext* context){
      
      //      vm_size_t startM = report_memory();
      _buildings = CityGMLParser::parseLOD2Buildings2(_s); //SAX
      
      //      IXMLNode* xml = IFactory::instance()->createXMLNodeFromXML(_s);
      //      _buildings = CityGMLParser::parseLOD2Buildings2(xml); //DOM
      //      delete xml;
      
      
      //      vm_size_t finalM = report_memory();
      //      printf("MEMORY USAGE DOM PARSING OF %lu buildings: %lu\n", _buildings.size(), (finalM - startM));
      
    }
    
    virtual void onPostExecute(const G3MContext* context){
      _listener->onBuildingsCreated(_buildings);
      if (_deleteListener){
        delete _listener;
      }
    }
  };
  
  
public:
  CityGMLParser_BufferDownloadListener(const IThreadUtils* threadUtils,
                                       CityGMLListener* listener,
                                       bool deleteListener) :
  _threadUtils(threadUtils),
  _listener(listener),
  _deleteListener(deleteListener)
  {
  }
  
  ~CityGMLParser_BufferDownloadListener(){
  }
  
  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    
    std::string s = buffer->getAsString();
    delete buffer;
    
    //More "expensive" way of parsing
    //    IXMLNode* xml = IFactory::instance()->createXMLNodeFromXML(s);
    //    std::vector<CityGMLBuilding*> buildings = CityGMLParser::parseLOD2Buildings2(xml);
    //    delete xml;
    
    _threadUtils->invokeAsyncTask(new ParsingTask(s, _listener, _deleteListener), true);
    
    //    std::vector<CityGMLBuilding*> buildings = CityGMLParser::parseLOD2Buildings2(s);
    //
    //    _listener->onBuildingsCreated(buildings);
    //    if (_deleteListener){
    //      delete _listener;
    //    }
  }
  
  void onError(const URL& url) {
    ILogger::instance()->logError("Error downloading \"%s\"", url._path.c_str());
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

const IThreadUtils* CityGMLParser::_threadUtils = NULL;
IDownloader* CityGMLParser::_downloader = NULL;

void CityGMLParser::parseFromURL(const URL& url,
                                 CityGMLListener* listener,
                                 bool deleteListener){
  
  if (_downloader == NULL){
    THROW_EXCEPTION("CityGMLParser not initialized");
  }
  
  _downloader->requestBuffer(url,
                             DownloadPriority::HIGHEST,
                             TimeInterval::fromHours(1),
                             true,
                             new CityGMLParser_BufferDownloadListener(_threadUtils,
                                                                      listener,
                                                                      deleteListener),
                             true);
}

//#import <mach/mach.h>
//
//// ...
//
//vm_size_t report_memory(void) {
//  struct task_basic_info info;
//  mach_msg_type_number_t size = sizeof(info);
//  kern_return_t kerr = task_info(mach_task_self(),
//                                 TASK_BASIC_INFO,
//                                 (task_info_t)&info,
//                                 &size);
//  if( kerr == KERN_SUCCESS ) {
////    printf("Memory in use (in bytes): %lu", info.resident_size);
//    return info.resident_size;
//  } else {
////    printf("Error with task_info(): %s", mach_error_string(kerr));
//    return 0;
//  }
//}

std::vector<CityGMLBuilding*> CityGMLParser::parseLOD2Buildings2(IXMLNode* cityGMLDoc) {
  
  ILogger::instance()->logInfo("CityGMLParser starting parse");
  
  std::vector<CityGMLBuilding*> buildings;
  
  //  vm_size_t startM = report_memory();
  
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
  
  ILogger::instance()->logInfo("CityGMLParser parse finished: %d buildings.", buildings.size());
  
  //  vm_size_t finalM = report_memory();
  //  printf("MEMORY USAGE DOM PARSING OF %lu buildings: %lu\n", buildings.size(), finalM - startM);
  
  return buildings;
}


std::vector<CityGMLBuilding*> CityGMLParser::parseLOD2Buildings2(const std::string& cityGMLString) {
  
  ILogger::instance()->logInfo("CityGMLParser starting parse");
  
  std::vector<CityGMLBuilding*> buildings;
  
  //  vm_size_t startM = report_memory();
  
  
  size_t pos = 0;
  const size_t length = cityGMLString.length();
  while (pos < length){
    IStringUtils::StringExtractionResult beginning = IStringUtils::extractSubStringBetween(cityGMLString, "bldg:Building gml:id=\"", "\"", pos);
    std::string name = beginning._string;
    if (beginning._endingPos == std::string::npos){
      break;
    }
    pos = beginning._endingPos + 1;
    
    
    
    size_t endPos = cityGMLString.find("</bldg:Building>", pos);
    
    //Reading surfaces
    std::vector<CityGMLBuildingSurface*> surfaces;
    while (true){
      IStringUtils::StringExtractionResult points = IStringUtils::extractSubStringBetween(cityGMLString,
                                                                                          "<gml:posList>", "</gml:posList>",
                                                                                          pos);
      
      if (points._endingPos == std::string::npos || points._endingPos >= endPos){
        break;
      }
      
      CityGMLBuildingSurfaceType type = WALL;
      size_t groundPos = cityGMLString.find("bldg:GroundSurface", pos);
      if (groundPos < points._endingPos){
        type = GROUND;
      } else{
        size_t roofPos = cityGMLString.find("bldg:RoofSurface", pos);
        if (roofPos < points._endingPos){
          type = ROOF;
        }
      }
      
      pos = points._endingPos +1 ;
      
      pos = cityGMLString.find("</bldg:boundedBy>", pos);
      
      std::vector<double> coors = IStringUtils::instance()->parseDoubles(points._string, " ");
      surfaces.push_back(CityGMLBuildingSurface::createFromArrayOfCityGMLWGS84Coordinates(coors, type));
      
    }
    
    CityGMLBuilding* nb = new CityGMLBuilding(name, 1, surfaces);
    buildings.push_back(nb);
    
  }
  ILogger::instance()->logInfo("CityGMLParser parse finished: %d buildings.", buildings.size());
  
  //  vm_size_t finalM = report_memory();
  //  printf("MEMORY USAGE DOM PARSING OF %lu buildings: %lu\n", buildings.size(), finalM - startM);
  
  return buildings;
}

