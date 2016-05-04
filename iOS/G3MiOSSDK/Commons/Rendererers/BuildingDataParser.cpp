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
#include "FloatBufferBuilderFromColor.hpp"
#include "Color.hpp"
#include "ColorLegend.hpp"
#include "IStringUtils.hpp"
#include "PointCloudMesh.hpp"

void BuildingDataParser::includeDataInBuildingSet(const std::string& data,
                                                  const std::vector<CityGMLBuilding*>& buildings){
  
  ////PARSING
  
  size_t pos = 0;
  size_t dataL = data.length();
  while (pos < dataL){
    IStringUtils::StringExtractionResult name = IStringUtils::extractSubStringBetween(data, "\"osm_id\": \"", "\"", pos);
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
      
      IStringUtils::StringExtractionResult heatDem = IStringUtils::extractSubStringBetween(data, "\"Heat_Dem_1\": ", ",", pos);
      pos = heatDem._endingPos+1;
      double v = IStringUtils::instance()->parseDouble(heatDem._string);
      b->addNumericProperty(new CityGMLBuildingNumericProperty("Heat_Dem_1", v));
      
      IStringUtils::StringExtractionResult vol = IStringUtils::extractSubStringBetween(data, "\"Bui_Volu_1\": ", ",", pos);
      pos = vol._endingPos+1;
      v = IStringUtils::instance()->parseDouble(vol._string);
      b->addNumericProperty(new CityGMLBuildingNumericProperty("Bui_Volu_1", v));
      
      IStringUtils::StringExtractionResult qcl = IStringUtils::extractSubStringBetween(data, "\"QCL_1\": ", ",", pos);
      pos = qcl._endingPos+1;
      v = IStringUtils::instance()->parseDouble(qcl._string);
      b->addNumericProperty(new CityGMLBuildingNumericProperty("QCL_1", v));
      
      IStringUtils::StringExtractionResult som = IStringUtils::extractSubStringBetween(data, "\"SOMcluster\": ", ",", pos);
      pos = som._endingPos+1;
      v = IStringUtils::instance()->parseDouble(som._string);
      b->addNumericProperty(new CityGMLBuildingNumericProperty("SOMcluster", v));
      
      IStringUtils::StringExtractionResult field2 = IStringUtils::extractSubStringBetween(data, "\"Field2_12\": ", ",", pos);
      pos = field2._endingPos+1;
      v = IStringUtils::instance()->parseDouble(field2._string);
      b->addNumericProperty(new CityGMLBuildingNumericProperty("Field2_12", v));
      
    }
  }
}

Mesh* BuildingDataParser::createPointCloudMesh(const std::string& data, const Planet* planet, const ElevationData* elevationData){
  
  //{ "type": "Feature", "properties": { "OBJECTID": 1, "SOURCE_ID": 1, "Value": 72, "GiZScore": -1.341899, "GiPValue": 0.179629, "Gi_Bin": 0 }, "geometry": { "type": "Point", "coordinates": [ 8.406533595313695, 49.025119962101599 ] } },
  
  std::vector<ColorLegend::ColorAndValue*> legend;
  legend.push_back(new ColorLegend::ColorAndValue(Color::blue(), -2.0));
  legend.push_back(new ColorLegend::ColorAndValue(Color::red(), 2.0));
  ColorLegend cl(legend);
  
  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(planet);
  FloatBufferBuilderFromColor colors;
//  int pointCounter = 0;
  size_t pos = 0;
  size_t dataL = data.length();
  while (pos < dataL){
    
    IStringUtils::StringExtractionResult value = IStringUtils::extractSubStringBetween(data, "\"GiZScore\": ", ",", pos);
    if (value._endingPos == std::string::npos){
      break;
    }
    pos = value._endingPos +1 ;
    double v = IStringUtils::instance()->parseDouble(value._string);
    
    
    IStringUtils::StringExtractionResult point = IStringUtils::extractSubStringBetween(data, "\"coordinates\": [", " ] ", pos);
    if (point._endingPos == std::string::npos){
      break;
    }
    pos = point._endingPos +1 ;
    
    std::vector<double> vd = IStringUtils::instance()->parseDoubles(point._string, ", ");
    
    if (elevationData == NULL){
      Geodetic3D g = Geodetic3D::fromDegrees(vd[1], vd[0], 2);
      vertices->add(g);
    } else{
      double h = elevationData->getElevationAt(Angle::fromDegrees(vd[1]), Angle::fromDegrees(vd[0]));
      Geodetic3D g = Geodetic3D::fromDegrees(vd[1], vd[0], 2 + h);
      vertices->add(g);
    }
    
    Color color = cl.getColor(v);
    colors.add(color);
    
//    if (pointCounter++ % 100 == 0){
//      ILogger::instance()->logInfo("%d points parsed", pointCounter);
//    }
  }
  
  DirectMesh* dm = new DirectMesh(GLPrimitive::points(),
                                  true,
                                  vertices->getCenter(),
                                  vertices->create(),
                                  1.0,
                                  2.0,
                                  NULL, //new Color(Color::red()),
                                  colors.create());
  
  delete vertices;
  
  return dm;
}

Mesh* BuildingDataParser::createSolarRadiationMesh(const std::string& data, const Planet* planet, const ElevationData* elevationData){
  
  std::vector<ColorLegend::ColorAndValue*> legend;
  legend.push_back(new ColorLegend::ColorAndValue(Color::black(), 0.0));
  legend.push_back(new ColorLegend::ColorAndValue(Color::red(), 25.0));
  ColorLegend cl(legend);
  
  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(planet);
  FloatBufferBuilderFromColor colors;
  //  int pointCounter = 0;
  size_t pos = 0;
  size_t dataL = data.length();
  while (pos < dataL){
    
    IStringUtils::StringExtractionResult value = IStringUtils::extractSubStringBetween(data, "\"represente\": ", ",", pos);
    if (value._endingPos == std::string::npos){
      break;
    }
    pos = value._endingPos +1 ;
    double v = IStringUtils::instance()->parseDouble(value._string);
    
    
    IStringUtils::StringExtractionResult point = IStringUtils::extractSubStringBetween(data, "\"coordinates\": [", " ] ", pos);
    if (point._endingPos == std::string::npos){
      break;
    }
    pos = point._endingPos +1 ;
    
    std::vector<double> vd = IStringUtils::instance()->parseDoubles(point._string, ", ");
    
    if (elevationData == NULL){
      Geodetic3D g = Geodetic3D::fromDegrees(vd[1], vd[0], 2);
      vertices->add(g);
    } else{
      double h = elevationData->getElevationAt(Angle::fromDegrees(vd[1]), Angle::fromDegrees(vd[0]));
      Geodetic3D g = Geodetic3D::fromDegrees(vd[1], vd[0], 2 + h);
      vertices->add(g);
    }
    
    Color color = cl.getColor(v);
    colors.add(color);
    
    //    if (pointCounter++ % 100 == 0){
    //      ILogger::instance()->logInfo("%d points parsed", pointCounter);
    //    }
  }
  
  PointCloudMesh* pcm = new PointCloudMesh(true,
                                           vertices->getCenter(),
                                           vertices->create(),
                                           15.0,
                                           colors.create(),
                                           false,
                                           Color::blue());
  
  
//  DirectMesh* dm = new DirectMesh(GLPrimitive::points(),
//                                  true,
//                                  vertices->getCenter(),
//                                  vertices->create(),
//                                  1.0,
//                                  20.0,
//                                  NULL, //new Color(Color::red()),
//                                  colors.create());
  
  delete vertices;
  
  return pcm;
}

