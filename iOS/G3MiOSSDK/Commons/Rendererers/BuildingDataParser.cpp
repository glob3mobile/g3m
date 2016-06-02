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

size_t BuildingDataParser::extractFeature(CityGMLBuilding* b,
                                          const std::string& name, const std::string& nameInJSON,
                                          const std::string& data, size_t pos){
  IStringUtils::StringExtractionResult heatDem = IStringUtils::extractSubStringBetween(data, "\"" + nameInJSON + "\": ", ",", pos);
  if (heatDem._endingPos == std::string::npos){
    return heatDem._endingPos;
  }
  pos = heatDem._endingPos+1;
  double v = IStringUtils::instance()->parseDouble(heatDem._string);
  b->addNumericProperty(new CityGMLBuildingNumericProperty(name, v));
  return pos;
}

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
      
      pos = extractFeature(b, "GHG Emissions", "GHG_Emis_1", data, pos);
      pos = extractFeature(b, "Heat Demand", "Heat_Dem_1", data, pos);
      pos = extractFeature(b, "Building Volume", "Bui_Volu_1", data, pos);
      pos = extractFeature(b, "Demographic Clusters (SOM)", "SOMcluster", data, pos);
      pos = extractFeature(b, "Demographic Clusters (k-Means)", "Field2_12", data, pos);
      
//      IStringUtils::StringExtractionResult heatDem = IStringUtils::extractSubStringBetween(data, "\"Heat_Dem_1\": ", ",", pos);
//      pos = heatDem._endingPos+1;
//      double v = IStringUtils::instance()->parseDouble(heatDem._string);
//      b->addNumericProperty(new CityGMLBuildingNumericProperty("Heat_Dem_1", v));
//      
//      IStringUtils::StringExtractionResult vol = IStringUtils::extractSubStringBetween(data, "\"Bui_Volu_1\": ", ",", pos);
//      pos = vol._endingPos+1;
//      v = IStringUtils::instance()->parseDouble(vol._string);
//      b->addNumericProperty(new CityGMLBuildingNumericProperty("Bui_Volu_1", v));
//      
//      IStringUtils::StringExtractionResult qcl = IStringUtils::extractSubStringBetween(data, "\"QCL_1\": ", ",", pos);
//      pos = qcl._endingPos+1;
//      v = IStringUtils::instance()->parseDouble(qcl._string);
//      b->addNumericProperty(new CityGMLBuildingNumericProperty("QCL_1", v));
//      
//      IStringUtils::StringExtractionResult som = IStringUtils::extractSubStringBetween(data, "\"SOMcluster\": ", ",", pos);
//      pos = som._endingPos+1;
//      v = IStringUtils::instance()->parseDouble(som._string);
//      b->addNumericProperty(new CityGMLBuildingNumericProperty("SOMcluster", v));
//      
//      IStringUtils::StringExtractionResult field2 = IStringUtils::extractSubStringBetween(data, "\"Field2_12\": ", ",", pos);
//      pos = field2._endingPos+1;
//      v = IStringUtils::instance()->parseDouble(field2._string);
//      b->addNumericProperty(new CityGMLBuildingNumericProperty("Field2_12", v));
      
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

Mesh* BuildingDataParser::createSolarRadiationMeshFromCSV(const std::string& data,
                                                          const Planet* planet,
                                                          const ElevationData* elevationData,
                                                          const ColorLegend& colorLegend){
  
  std::vector<std::string> lines = IStringUtils::instance()->splitLines(data);
  
  std::vector<Geodetic3D*> points;
  
  
  
  for (size_t i = 0; i < lines.size(); i++) {
    //    ILogger::instance()->logInfo(lines[i]);
    std::vector<double> vs = IStringUtils::instance()->parseDoubles(lines[i], ",");
    if (vs.size() < 3){
      continue;
    }
    
    Geodetic3D* g = new Geodetic3D( Geodetic3D::fromDegrees(vs[1], vs[0], vs[2]));
    points.push_back(g);
    
  }
  
  
  double minH = points[0]->_height;
  if (elevationData != NULL){
    for (size_t i = 0; i < points.size(); i++) {
      if (points[i]->_height < minH){
        minH = points[i]->_height;
      }
    }
  }
  
  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(planet);
  for (size_t i = 0; i < points.size(); i++) {
    
    Geodetic2D g2D = points[i]->asGeodetic2D();
    
    double h = points[i]->_height;
    
//#warning REMOVE THIS IS JUST FOR TECHNOLOGY PARK
//    double scale = 1.4;
//    h = h + scale * (h - minH);

    if (elevationData != NULL){
      h = h - minH + elevationData->getElevationAt(g2D);
    }
    
    vertices->add(Geodetic3D(g2D, h));
    delete points[i];
  }

  IFloatBuffer* colors = create0ColorsForSolarRadiationMeshFromCSV(colorLegend, (int)vertices->size() / 3);
  
  PointCloudMesh* pcm = new PointCloudMesh(true,
                                           vertices->getCenter(),
                                           vertices->create(),
                                           10.0,
                                           colors,
                                           true,
                                           Color::blue());
  
  ILogger::instance()->logInfo("Created point cloud of %d points.", vertices->size() / 3);
  
  delete vertices;
  
  return pcm;
  
}

IFloatBuffer* BuildingDataParser::createColorsForSolarRadiationMeshFromCSV(const std::string& data,
                                                                           const ColorLegend& colorLegend){
  
  std::vector<double> vs = IStringUtils::instance()->parseDoubles(data, ",");
  
  FloatBufferBuilderFromColor colors;
  
  std::vector<Geodetic3D*> points;
  
  for (size_t i = 0; i < vs.size(); i++) {
    Color color = colorLegend.getColor(vs[i]);
    colors.add(color);
  }
  
  return colors.create();
}

IFloatBuffer* BuildingDataParser::create0ColorsForSolarRadiationMeshFromCSV(const ColorLegend& colorLegend, int nVertices){
  FloatBufferBuilderFromColor colors;
  std::vector<Geodetic3D*> points;
  
  Color color = colorLegend.getColor(0);
  for (size_t i = 0; i < nVertices; i++) {
    colors.add(color);
  }
  
  return colors.create();
}
