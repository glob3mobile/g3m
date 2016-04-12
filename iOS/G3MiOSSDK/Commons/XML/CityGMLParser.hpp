//
//  CityGMLParser.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 30/3/16.
//
//

#ifndef CityGMLParser_hpp
#define CityGMLParser_hpp

#include "IXMLNode.hpp"
#include "CityGMLBuilding.hpp"

#include <vector>
#include <string>

class MarksRenderer;
class MeshRenderer;

class CityGMLListener{
public:
  virtual void onBuildingsCreated(const std::vector<CityGMLBuilding*>& buildings) = 0;
  
  virtual void onError() = 0;
};

class CityGMLParser{
  
public:
  static std::vector<CityGMLBuilding*> parseLOD2Buildings2(IXMLNode* cityGMLDoc);
  
  static void addLOD2MeshAndMarksFromFile(const std::string& url,
                                          IDownloader* downloader,
                                          const Planet* planet,
                                          MeshRenderer* meshRenderer,
                                          MarksRenderer* marksRenderer,
                                          CityGMLListener* listener,
                                          bool deleteListener,
                                          ElevationData* elevationData);
  
};

#endif /* CityGMLParser_hpp */
