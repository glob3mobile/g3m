//
//  BuildingDataParser.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 11/4/16.
//
//

#ifndef BuildingDataParser_hpp
#define BuildingDataParser_hpp

#include "CityGMLBuilding.hpp"
#include <vector>
#include <string>
#include "ColorLegend.hpp"

class BuildingDataParser{
  
  static size_t extractFeature(CityGMLBuilding* b,
                               const std::string& name, const std::string& nameInJSON,
                               const std::string& data, size_t pos);
  
public:
  
  static void includeDataInBuildingSet(const std::string& data,
                                       const std::vector<CityGMLBuilding*>& buildings);
  
  static Mesh* createPointCloudMesh(const std::string& data, const Planet* planet, const ElevationData* elevationData);
  
  static Mesh* createSolarRadiationMeshFromCSV(const std::string& data,
                                               const Planet* planet,
                                               const ElevationData* elevationData,
                                               const ColorLegend& colorLegend);
  
  static IFloatBuffer* createColorsForSolarRadiationMeshFromCSV(const std::string& data,
                                                                const ColorLegend& colorLegend);
  static IFloatBuffer* create0ColorsForSolarRadiationMeshFromCSV(const ColorLegend& colorLegend,
                                                                 int nVertices);
  
};

#endif /* BuildingDataParser_hpp */
