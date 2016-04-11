//
//  BuildingDataParser.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 11/4/16.
//
//

#include "BuildingDataParser.hpp"

#include "DirectMesh.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"

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

Mesh* BuildingDataParser::createPointCloudMesh(const std::string& data, const Planet* planet){
  
  //{ "type": "Feature", "properties": { "OBJECTID": 1, "SOURCE_ID": 1, "Value": 72, "GiZScore": -1.341899, "GiPValue": 0.179629, "Gi_Bin": 0 }, "geometry": { "type": "Point", "coordinates": [ 8.406533595313695, 49.025119962101599 ] } },
  
  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(planet);
  int pointCounter = 0;
  size_t pos = 0;
  size_t dataL = data.length();
  while (pos < dataL){
    StringAndPos point = extractSubStringBetween(data, "\"coordinates\": [", " ] ", pos);
    if (point._endingPos == std::string::npos){
      break;
    }
    pos = point._endingPos +1 ;
    
    std::vector<double> vd = IStringUtils::instance()->parseDoubles(point._string, ", ");
    
    Geodetic3D g = Geodetic3D::fromDegrees(vd[1], vd[0], 2);
    
    vertices->add(g);
    
    if (pointCounter++ % 100 == 0){
      ILogger::instance()->logInfo("%d points parsed", pointCounter);
    }
  }
  
  DirectMesh* dm = new DirectMesh(GLPrimitive::points(),
                                  true,
                                  vertices->getCenter(),
                                  vertices->create(),
                                  1.0,
                                  2.0,
                                  new Color(Color::red()),
                                  NULL);
  
  delete vertices;
  
  return dm;
}

