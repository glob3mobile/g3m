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

GeoJSONDataBuildingColorPicker::GeoJSONDataBuildingColorPicker(const std::string& data,
                                                               const ColorLegend* colorLegend,
                                                               BUILDING_PROPERTY prop):
_legend(colorLegend), _activeProperty(prop){
  
  ////PARSING
  
  size_t pos = 0;
  size_t dataL = data.length();
  while (pos < dataL){
    StringAndPos name = extractSubStringBetween(data, "\"osm_id\": \"", "\"", pos);
    if (name._endingPos == std::string::npos){
      return;
    }
    pos = name._endingPos +1 ;
    
    BuildingProperties bp;
    
    
    StringAndPos heatDem = extractSubStringBetween(data, "\"Heat_Dem_1\": ", ",", pos);
    pos = heatDem._endingPos+1;
    bp._heatDem = IStringUtils::instance()->parseDouble(heatDem._string);
    
    StringAndPos vol = extractSubStringBetween(data, "\"Bui_Volu_1\": ", ",", pos);
    pos = vol._endingPos+1;
    bp._volume = IStringUtils::instance()->parseDouble(vol._string);
    
    StringAndPos qcl = extractSubStringBetween(data, "\"QCL_1\": ", ",", pos);
    pos = qcl._endingPos+1;
    bp._qcl = IStringUtils::instance()->parseDouble(qcl._string);
    
    StringAndPos som = extractSubStringBetween(data, "\"SOMcluster\": ", ",", pos);
    pos = som._endingPos+1;
    bp._som = IStringUtils::instance()->parseDouble(som._string);
    
    StringAndPos field2 = extractSubStringBetween(data, "\"Field2_12\": ", ",", pos);
    pos = field2._endingPos+1;
    bp._field2 = IStringUtils::instance()->parseDouble(field2._string);
    
    _heatDemand.insert(std::pair<std::string, BuildingProperties>(name._string, bp));
  }
}


Color GeoJSONDataBuildingColorPicker::getColor(CityGMLBuilding* building) const{
  const std::string name = building->_name; //OSM ID
  if (_heatDemand.count(name) == 1){
    
    double value;
    switch (_activeProperty) {
      case HEAT_DEMAND:
        value = _heatDemand.at(name)._heatDem;
        break;
      case VOLUME:
        value = _heatDemand.at(name)._volume;
        break;
      case SOM:
        value = _heatDemand.at(name)._som;
        break;
      case QCL:
        value = _heatDemand.at(name)._qcl;
        break;
      case FIELD2:
        value = _heatDemand.at(name)._field2;
        break;
      default:
        break;
    }
    
    return _legend->getColor(value);
  } else{
    //ILogger::instance()->logError("Cannot create color for " + name);
    return Color::gray();
  }
  
}
