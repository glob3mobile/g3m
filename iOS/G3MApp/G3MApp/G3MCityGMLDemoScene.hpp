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

class G3MCityGMLDemoScene : public G3MDemoScene {
private:
  long long _requestId;
  
  
  void colorBuildings(CityGMLBuildingColorProvider* cp);
  
  
  
protected:
  void rawActivate(const G3MContext* context);
  
  void rawSelectOption(const std::string& option,
                       int optionIndex);
  
public:
  
  
  std::vector<CityGMLBuilding*> _buildings;
  
  
//  GeoJSONDataBuildingColorPicker* _colorProvider;
  
  G3MCityGMLDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "CityGML", "", -1),
  _requestId(-1),
  _buildings(NULL)
  {
    _options.push_back("Random Colors");
    _options.push_back("Heat Demand");
    _options.push_back("Volume");
    _options.push_back("QCL");
    _options.push_back("SOM Cluster");
    _options.push_back("Field 2");
  }
  
  void deactivate(const G3MContext* context);
  
  ~G3MCityGMLDemoScene(){
  }
  
  
};

#endif /* G3MCityGMLDemoScene_hpp */
