//
//  CityGMLParser.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 30/3/16.
//
//

#include "CityGMLParser.hpp"

#include "ILogger.hpp"
#include "IStringUtils.hpp"

std::vector<CityGMLBuilding*> CityGMLParser::parseLOD2Buildings2(IXMLDocument* cityGMLDoc) {
  
  std::vector<CityGMLBuilding*> buildings;
  
  const std::vector<IXMLDocument*> buildingsXML = cityGMLDoc->evaluateXPathAsXMLDocuments("//*[local-name()='Building']");
  //      ILogger.instance().logInfo("N Buildings %d", buildingsXML.size());
  for (size_t i = 0; i < buildingsXML.size(); i++) {
    
    IXMLDocument* b = buildingsXML[i];
    
    //Name
//    std::string name = "NO NAME";
    
//    std::string* name = b->evaluateXPathAndGetTextContentAsText("//*[local-name()='name']/text()");
    
    std::vector<CityGMLBuildingSurface*> surfaces;
    
    const std::vector<IXMLDocument*> walls = b->evaluateXPathAsXMLDocuments("*[local-name()='boundedBy']//*[local-name()='posList']");
    for (int i = 0; i < walls.size(); i++) {
      std::string* str = walls[i]->getTextContent();
      if (str != NULL){
        //ILogger::instance()->logInfo("%s", str->c_str() );
        
        std::vector<double> coors = IStringUtils::instance()->parseDoubles(*str, " ");
        if (coors.size() % 3 != 0){
          ILogger::instance()->logError("Problem parsing wall coordinates.");
        }
        
        surfaces.push_back(CityGMLBuildingSurface::createFromArrayOfCityGMLWGS84Coordinates(coors, WALL));
      }
    }
    
    CityGMLBuilding* nb = new CityGMLBuilding("NO NAME", 1, surfaces);
    buildings.push_back(nb);
//    try {
//      name = b.evaluateXPathAndGetTextContentAsText("/bldg:Building/gml:name/text()");
//      //            building = new CityGMLBuilding(name);
//    }
//    catch (const Exception e) {
//      //            ILogger.instance().logError("No name for building");
//      //            building = new CityGMLBuilding("NO NAME");
//      
//      //ID
//      try {
//        name = b.getAttributeAsText("gml:id");
//        //               building = new CityGMLBuilding(id);
//      }
//      catch (const Exception e2) {
//        ILogger.instance().logError("No ID for building");
//        //               building = new CityGMLBuilding("NO NAME");
//      }
//    }
//    
//    //RoofType
//    int roofType = -1;
//    try {
//      roofType = b.evaluateXPathAndGetTextContentAsInteger("/bldg:Building/bldg:roofType/text()");
//      //            building.setRoofTypeCode(roofType);
//    }
//    catch (const Exception e) {
//      //            ILogger.instance().logError("No roof type for building");
//    }
//    
//    const std::vector<CityGMLBuildingSurface> surfaces = new std::vector<CityGMLBuildingSurface>();
//    
//    //Walls
//    const std::vector<XMLDocument> wallsXML = b.evaluateXPathAsXMLDocuments("/bldg:Building/bldg:boundedBy/bldg:WallSurface/bldg:lod2MultiSurface//gml:posList");
//    //         ILogger.instance().logInfo("N Walls %d", wallsXML.size());
//    for (const XMLDocument s : wallsXML) {
//      const std::vector<Double> coor = s.getTextContentAsNumberArray(" ");
//      const CityGMLBuildingSurface w = CityGLMBuildingWall.createFromArrayOfCityGMLWGS84Coordinates(coor,
//                                                                                                    CityGMLBuildingSurfaceType.WALL);
//      surfaces.add(w);
//    }
//    
//    
//    //Rooftops
//    const std::vector<XMLDocument> roofsXML = b.evaluateXPathAsXMLDocuments("/bldg:Building/bldg:boundedBy/bldg:RoofSurface/bldg:lod2MultiSurface//gml:posList");
//    //         ILogger.instance().logInfo("N Roofs %d", roofsXML.size());
//    for (const XMLDocument s : roofsXML) {
//      const std::vector<Double> coor = s.getTextContentAsNumberArray(" ");
//      const CityGMLBuildingSurface w = CityGLMBuildingWall.createFromArrayOfCityGMLWGS84Coordinates(coor,
//                                                                                                    CityGMLBuildingSurfaceType.ROOF);
//      surfaces.add(w);
//    }
//    
//    //Ground
//    const std::vector<XMLDocument> groundXML = b.evaluateXPathAsXMLDocuments("/bldg:Building/bldg:boundedBy/bldg:GroundSurface/bldg:lod2MultiSurface//gml:posList");
//    //         ILogger.instance().logInfo("N Roofs %d", groundXMLs.size());
//    for (const XMLDocument s : groundXML) {
//      const std::vector<Double> coor = s.getTextContentAsNumberArray(" ");
//      const CityGMLBuildingSurface w = CityGLMBuildingWall.createFromArrayOfCityGMLWGS84Coordinates(coor,
//                                                                                                    CityGMLBuildingSurfaceType.GROUND);
//      surfaces.add(w);
//    }
//    
//    const CityGMLBuilding building = new CityGMLBuilding(name,
//                                                         roofType, surfaces);
//    
//    buildings.add(building);
  }
  
  return buildings;
}

