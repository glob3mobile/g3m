//
//  CityGMLBuilding.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

#include "CityGMLBuilding.hpp"

#include "CityGMLBuildingTessellator.hpp"

std::string CityGMLBuilding::getPropertiesDescription() const{
  
  std::string d = "";
  for (size_t i = 0; i < _numericProperties.size(); i++){
    d += _numericProperties[i]->_name + ": " + IStringUtils::instance()->toString(_numericProperties[i]->_value);
    if (i < _numericProperties.size() - 1){
      d += "\n";
    }
  }
  return d;
}

int CityGMLBuilding::checkWallsVisibility(const std::vector<CityGMLBuilding*> buildings){
  
  int nInvisibleWalls = 0;
  int size_1 = ((int)buildings.size()) - 1;
  for (int i = 0; i < size_1; i++){
    CityGMLBuilding* b1 = buildings[i];
    if (b1->markGroundAsNotVisible()){
      nInvisibleWalls++;
    }
    
    for (int j = i+1; j <= size_1; j++){
      CityGMLBuilding* b2 = buildings[j];
      if (CityGMLBuildingTessellator::areClose(b1, b2)){
        nInvisibleWalls += checkWallsVisibility(b1, b2);
      }
    }
    
  }
  return nInvisibleWalls;
}
