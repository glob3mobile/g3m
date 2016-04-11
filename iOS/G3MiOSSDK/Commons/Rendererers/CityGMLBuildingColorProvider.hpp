//
//  CityGMLBuildingColorProvider.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/4/16.
//
//

#ifndef CityGMLBuildingColorProvider_hpp
#define CityGMLBuildingColorProvider_hpp

#include "Color.hpp"
#include "ColorLegend.hpp"
#include "RCObject.hpp"
#include "IStringUtils.hpp"
#include <map>


class CityGMLBuilding;

class CityGMLBuildingColorProvider{
public:
  virtual ~CityGMLBuildingColorProvider(){}
  virtual Color getColor(CityGMLBuilding* building) const = 0;
};

class RandomBuildingColorPicker: public CityGMLBuildingColorProvider{
  
public:
  
  RandomBuildingColorPicker(){}
  
  Color getColor(CityGMLBuilding* building) const;
};

class BuildingDataColorProvider: public CityGMLBuildingColorProvider{
  
  const ColorLegend* _legend;
  const std::string _propertyName;
  
  
public:

  
  BuildingDataColorProvider(const std::string& propertyName,
                          const ColorLegend* colorLegend):
  _propertyName(propertyName),
  _legend(colorLegend){}
  
  Color getColor(CityGMLBuilding* building) const;
  
};

#endif /* CityGMLBuildingColorProvider_hpp */
