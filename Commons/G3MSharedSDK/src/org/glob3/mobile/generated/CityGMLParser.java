package org.glob3.mobile.generated; 
//
//  CityGMLParser.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 30/3/16.
//
//

//
//  CityGMLParser.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 30/3/16.
//
//




public class CityGMLParser
{

  public static java.util.ArrayList<CityGMLBuilding> parseLOD2Buildings2(IXMLNode cityGMLDoc)
  {
  
    java.util.ArrayList<CityGMLBuilding> buildings = new java.util.ArrayList<CityGMLBuilding>();
  
    final java.util.ArrayList<IXMLNode> buildingsXML = cityGMLDoc.evaluateXPathAsXMLNodes("//*[local-name()='Building']");
    //      ILogger.instance().logInfo("N Buildings %d", buildingsXML.size());
    for (int i = 0; i < buildingsXML.size(); i++)
    {
  
      IXMLNode b = buildingsXML.get(i);
  
      String name = b.getAttribute("id");
      if (name == null)
      {
        name = new String("NO NAME");
      }
  
      //Name
  //    std::string name = "NO NAME";
  
  //    std::string* name = b->evaluateXPathAndGetTextContentAsText("//*[local-name()='name']/text()");
  
      java.util.ArrayList<CityGMLBuildingSurface> surfaces = new java.util.ArrayList<CityGMLBuildingSurface>();
  
      final java.util.ArrayList<IXMLNode> grounds = b.evaluateXPathAsXMLNodes("*[local-name()='boundedBy']//*[local-name()='GroundSurface']//*[local-name()='posList']");
      for (int i = 0; i < grounds.size(); i++)
      {
        String str = grounds.get(i).getTextContent();
        if (str != null)
        {
          //ILogger::instance()->logInfo("%s", str->c_str() );
  
          java.util.ArrayList<Double> coors = IStringUtils.instance().parseDoubles(str, " ");
          if (coors.size() % 3 != 0)
          {
            ILogger.instance().logError("Problem parsing wall coordinates.");
          }
  
          surfaces.add(CityGMLBuildingSurface.createFromArrayOfCityGMLWGS84Coordinates(coors, CityGMLBuildingSurfaceType.GROUND));
  
          str = null;
        }
      }
  
      final java.util.ArrayList<IXMLNode> walls = b.evaluateXPathAsXMLNodes("*[local-name()='boundedBy']//*[local-name()='WallSurface']//*[local-name()='posList']");
      for (int i = 0; i < walls.size(); i++)
      {
        String str = walls.get(i).getTextContent();
        if (str != null)
        {
          //ILogger::instance()->logInfo("%s", str->c_str() );
  
          java.util.ArrayList<Double> coors = IStringUtils.instance().parseDoubles(str, " ");
          if (coors.size() % 3 != 0)
          {
            ILogger.instance().logError("Problem parsing wall coordinates.");
          }
  
          surfaces.add(CityGMLBuildingSurface.createFromArrayOfCityGMLWGS84Coordinates(coors, CityGMLBuildingSurfaceType.WALL));
  
          str = null;
        }
      }
  
      final java.util.ArrayList<IXMLNode> roofs = b.evaluateXPathAsXMLNodes("*[local-name()='boundedBy']//*[local-name()='RoofSurface']//*[local-name()='posList']");
      for (int i = 0; i < roofs.size(); i++)
      {
        String str = roofs.get(i).getTextContent();
        if (str != null)
        {
          //ILogger::instance()->logInfo("%s", str->c_str() );
  
          java.util.ArrayList<Double> coors = IStringUtils.instance().parseDoubles(str, " ");
          if (coors.size() % 3 != 0)
          {
            ILogger.instance().logError("Problem parsing wall coordinates.");
          }
  
          surfaces.add(CityGMLBuildingSurface.createFromArrayOfCityGMLWGS84Coordinates(coors, CityGMLBuildingSurfaceType.ROOF));
  
          str = null;
        }
      }
  
      CityGMLBuilding nb = new CityGMLBuilding(name, 1, surfaces);
      name = null;
      buildings.add(nb);
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
  //    const std::vector<XMLNode> wallsXML = b.evaluateXPathAsXMLNodes("/bldg:Building/bldg:boundedBy/bldg:WallSurface/bldg:lod2MultiSurface//gml:posList");
  //    //         ILogger.instance().logInfo("N Walls %d", wallsXML.size());
  //    for (const XMLNode s : wallsXML) {
  //      const std::vector<Double> coor = s.getTextContentAsNumberArray(" ");
  //      const CityGMLBuildingSurface w = CityGLMBuildingWall.createFromArrayOfCityGMLWGS84Coordinates(coor,
  //                                                                                                    CityGMLBuildingSurfaceType.WALL);
  //      surfaces.add(w);
  //    }
  //
  //
  //    //Rooftops
  //    const std::vector<XMLNode> roofsXML = b.evaluateXPathAsXMLNodes("/bldg:Building/bldg:boundedBy/bldg:RoofSurface/bldg:lod2MultiSurface//gml:posList");
  //    //         ILogger.instance().logInfo("N Roofs %d", roofsXML.size());
  //    for (const XMLNode s : roofsXML) {
  //      const std::vector<Double> coor = s.getTextContentAsNumberArray(" ");
  //      const CityGMLBuildingSurface w = CityGLMBuildingWall.createFromArrayOfCityGMLWGS84Coordinates(coor,
  //                                                                                                    CityGMLBuildingSurfaceType.ROOF);
  //      surfaces.add(w);
  //    }
  //
  //    //Ground
  //    const std::vector<XMLNode> groundXML = b.evaluateXPathAsXMLNodes("/bldg:Building/bldg:boundedBy/bldg:GroundSurface/bldg:lod2MultiSurface//gml:posList");
  //    //         ILogger.instance().logInfo("N Roofs %d", groundXMLs.size());
  //    for (const XMLNode s : groundXML) {
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

}