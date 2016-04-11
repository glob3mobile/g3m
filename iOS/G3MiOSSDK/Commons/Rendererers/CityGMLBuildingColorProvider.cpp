//
//  CityGMLBuildingColorProvider.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/4/16.
//
//

#include "CityGMLBuildingColorProvider.hpp"

#include "CityGMLBuilding.hpp"

Color RandomBuildingColorPicker::getColor(CityGMLBuilding* building) const{
  double x = (int)(IMathUtils::instance()->nextRandomDouble() * 10.0e6);
  
  return Color::fromRGBA255((int)(IMathUtils::instance()->nextRandomDouble() * 10.0e6) % 256,
                     (int)(IMathUtils::instance()->nextRandomDouble() * 10.0e6) % 256,
                     (int)(IMathUtils::instance()->nextRandomDouble() * 10.0e6) % 256,
                     255);
}

Color BuildingDataColorProvider::getColor(CityGMLBuilding* building) const{
  double value = building->getNumericProperty(_propertyName);
  if (ISNAN(value)){
    return Color::gray();
  }
  return _legend->getColor(value);
}
