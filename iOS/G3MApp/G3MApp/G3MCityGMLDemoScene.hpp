//
//  G3MCityGMLDemoScene.hpp
//  G3MApp
//
//  Created by Jose Miguel SN on 29/3/16.
//  Copyright © 2016 Igo Software SL. All rights reserved.
//

#ifndef G3MCityGMLDemoScene_hpp
#define G3MCityGMLDemoScene_hpp

#include "G3MDemoScene.hpp"
#include <G3MiOSSDK/CityGMLBuildingColorProvider.hpp>
#include "G3MDemoModel.hpp"

class CityGMLBuilding;
class ElevationData;
class LabelImageBuilder;

class G3MCityGMLDemoScene : public G3MDemoScene {
private:
  long long _requestId;
  
  bool _useDEM;
  
  
  void colorBuildings(CityGMLBuildingColorProvider* cp);
  
  
  
  std::vector<CityGMLBuilding*> _buildings;
  std::vector<std::string> _cityGMLFiles;
  int _modelsLoadedCounter;
  
  LabelImageBuilder* _labelBuilder;
  
protected:
  void rawActivate(const G3MContext* context);
  
  void rawSelectOption(const std::string& option,
                       int optionIndex);
  
public:
  
  
  G3MCityGMLDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "CityGML", "", -1),
  _requestId(-1),
  _buildings(NULL),
  _modelsLoadedCounter(0),
  _useDEM(true)
  {
    _options.push_back("Random Colors");
    _options.push_back("Heat Demand");
    _options.push_back("Volume");
    _options.push_back("QCL");
    _options.push_back("SOM Cluster");
    _options.push_back("Field 2");
    
    _cityGMLFiles.push_back("file:///innenstadt_ost_4326_lod2.gml");
    _cityGMLFiles.push_back("file:///innenstadt_west_4326_lod2.gml");
//    _cityGMLFiles.push_back("file:///hagsfeld_4326_lod2.gml");
//    _cityGMLFiles.push_back("file:///durlach_4326_lod2_PART_1.gml");
//    _cityGMLFiles.push_back("file:///durlach_4326_lod2_PART_2.gml");
//    _cityGMLFiles.push_back("file:///hohenwettersbach_4326_lod2.gml");
//    _cityGMLFiles.push_back("file:///bulach_4326_lod2.gml");
//    _cityGMLFiles.push_back("file:///daxlanden_4326_lod2.gml");
//    _cityGMLFiles.push_back("file:///knielingen_4326_lod2_PART_1.gml");
//    _cityGMLFiles.push_back("file:///knielingen_4326_lod2_PART_2.gml");
//    _cityGMLFiles.push_back("file:///knielingen_4326_lod2_PART_3.gml");
  }
  
  void deactivate(const G3MContext* context);
  
  ~G3MCityGMLDemoScene(){
  }
  
  void loadCityModel(ElevationData* ed);
  
  void addBuildings(const std::vector<CityGMLBuilding*>& buildings, const ElevationData* ed);
  
  void requestPointCloud(ElevationData* ed);
  void createPointCloud(ElevationData* ed, const std::string& pointCloudDescriptor);
  
  
};

#endif /* G3MCityGMLDemoScene_hpp */
