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

class CityGMLBuildingColorProvider: public RCObject{
public:
  virtual Color getColor(CityGMLBuilding* building) const = 0;
};

class RandomBuildingColorPicker: public CityGMLBuildingColorProvider{
  
public:
  
  RandomBuildingColorPicker(){}
  
  Color getColor(CityGMLBuilding* building) const;
};

class GeoJSONDataBuildingColorPicker: public CityGMLBuildingColorProvider{
  
  const ColorLegend* _legend;
  
  class BuildingProperties{
  public:
    double _heatDem;
    double _volume;
    double _qcl;
    double _som;
    double _field2;
  };
  
  std::map<std::string, BuildingProperties> _heatDemand;
  
  class StringAndPos{
    
  public:
    std::string _string;
    size_t _endingPos;
    
    StringAndPos(std::string string,
                 size_t endingPos):
    _string(string),
    _endingPos(endingPos){}
  };
  
public:
  
  
  enum BUILDING_PROPERTY{
    HEAT_DEMAND,
    VOLUME,
    QCL,
    SOM,
    FIELD2
  };
  
  BUILDING_PROPERTY _activeProperty;
  
  static StringAndPos extractSubStringBetween(const std::string& string,
                                              const std::string& startTag,
                                              const std::string& endTag,
                                              const size_t startPos){
    
    size_t pos1 = string.find(startTag, startPos) + startTag.length();
    size_t pos2 = string.find(endTag, pos1);
    
    if (pos1 == std::string::npos || pos2 == std::string::npos || pos1 < startPos || pos2 < startPos){
      return StringAndPos("", std::string::npos);
    }
    
    std::string str = string.substr(pos1, pos2-pos1);
    
    return StringAndPos(str, pos2 + endTag.length());
  }
  
  GeoJSONDataBuildingColorPicker(const std::string& data,
                                 const ColorLegend* colorLegend,
                                 BUILDING_PROPERTY prop);
  
  ~GeoJSONDataBuildingColorPicker(){
    delete _legend;
  }
  
  void setLegend(ColorLegend* legend){
    delete _legend;
    _legend = legend;
  }
  
  Color getColor(CityGMLBuilding* building) const;
  
};

#endif /* CityGMLBuildingColorProvider_hpp */
