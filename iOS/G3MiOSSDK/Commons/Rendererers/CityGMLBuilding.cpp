//
//  CityGMLBuilding.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

#include "CityGMLBuilding.hpp"

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
