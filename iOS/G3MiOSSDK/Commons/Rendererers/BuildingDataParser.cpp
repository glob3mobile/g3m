//
//  BuildingDataParser.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 11/4/16.
//
//

#include "BuildingDataParser.hpp"

void BuildingDataParser::includeDataInBuildingSet(const std::string& data,
                                                  const std::vector<CityGMLBuilding*>& buildings){
  
  ////PARSING
  
  size_t pos = 0;
  size_t dataL = data.length();
  while (pos < dataL){
    StringAndPos name = extractSubStringBetween(data, "\"osm_id\": \"", "\"", pos);
    if (name._endingPos == std::string::npos){
      return;
    }
    pos = name._endingPos +1 ;
    
    CityGMLBuilding* b = NULL;
    for (int i = 0; i < buildings.size(); i++){
      if (buildings[i]->_name == name._string){
        b = buildings[i];
      }
    }
    
    if (b != NULL){
      
      StringAndPos heatDem = extractSubStringBetween(data, "\"Heat_Dem_1\": ", ",", pos);
      pos = heatDem._endingPos+1;
      double v = IStringUtils::instance()->parseDouble(heatDem._string);
      b->addNumericProperty(new CityGMLBuildingNumericProperty("Heat_Dem_1", v));
      
      StringAndPos vol = extractSubStringBetween(data, "\"Bui_Volu_1\": ", ",", pos);
      pos = vol._endingPos+1;
      v = IStringUtils::instance()->parseDouble(vol._string);
      b->addNumericProperty(new CityGMLBuildingNumericProperty("Bui_Volu_1", v));
      
      StringAndPos qcl = extractSubStringBetween(data, "\"QCL_1\": ", ",", pos);
      pos = qcl._endingPos+1;
      v = IStringUtils::instance()->parseDouble(qcl._string);
      b->addNumericProperty(new CityGMLBuildingNumericProperty("QCL_1", v));
      
      StringAndPos som = extractSubStringBetween(data, "\"SOMcluster\": ", ",", pos);
      pos = som._endingPos+1;
      v = IStringUtils::instance()->parseDouble(som._string);
      b->addNumericProperty(new CityGMLBuildingNumericProperty("SOMcluster", v));
      
      StringAndPos field2 = extractSubStringBetween(data, "\"Field2_12\": ", ",", pos);
      pos = field2._endingPos+1;
      v = IStringUtils::instance()->parseDouble(field2._string);
      b->addNumericProperty(new CityGMLBuildingNumericProperty("Field2_12", v));
      
    }
  }
}

