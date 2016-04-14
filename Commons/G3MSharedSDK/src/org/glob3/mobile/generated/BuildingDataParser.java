package org.glob3.mobile.generated; 
//
//  BuildingDataParser.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 11/4/16.
//
//

//
//  BuildingDataParser.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 11/4/16.
//
//



public class BuildingDataParser
{

//  class StringExtractionResult{
//    
//  public:
//    std::string _string;
//    size_t _endingPos;
//    
//    StringExtractionResult(std::string string,
//                 size_t endingPos):
//    _string(string),
//    _endingPos(endingPos){}
//  };
//  
//  static StringExtractionResult extractSubStringBetween(const std::string& string,
//                                              const std::string& startTag,
//                                              const std::string& endTag,
//                                              const size_t startPos){
//    
//    size_t pos1 = string.find(startTag, startPos) + startTag.length();
//    size_t pos2 = string.find(endTag, pos1);
//    
//    if (pos1 == std::string::npos || pos2 == std::string::npos || pos1 < startPos || pos2 < startPos){
//      return StringExtractionResult("", std::string::npos);
//    }
//    
//    std::string str = string.substr(pos1, pos2-pos1);
//    
//    return StringExtractionResult(str, pos2 + endTag.length());
//  }
//  

  public static void includeDataInBuildingSet(String data, java.util.ArrayList<CityGMLBuilding> buildings)
  {
  
    ////PARSING
  
    int pos = 0;
    int dataL = data.length();
    while (pos < dataL)
    {
      IStringUtils.StringExtractionResult name = IStringUtils.extractSubStringBetween(data, "\"osm_id\": \"", "\"", pos);
      if (name._endingPos == String.npos)
      {
        return;
      }
      pos = name._endingPos +1;
  
      CityGMLBuilding b = null;
      for (int i = 0; i < buildings.size(); i++)
      {
        if (buildings.get(i)._name.equals(name._string))
        {
          b = buildings.get(i);
        }
      }
  
      if (b != null)
      {
  
        IStringUtils.StringExtractionResult heatDem = IStringUtils.extractSubStringBetween(data, "\"Heat_Dem_1\": ", ",", pos);
        pos = heatDem._endingPos+1;
        double v = IStringUtils.instance().parseDouble(heatDem._string);
        b.addNumericProperty(new CityGMLBuildingNumericProperty("Heat_Dem_1", v));
  
        IStringUtils.StringExtractionResult vol = IStringUtils.extractSubStringBetween(data, "\"Bui_Volu_1\": ", ",", pos);
        pos = vol._endingPos+1;
        v = IStringUtils.instance().parseDouble(vol._string);
        b.addNumericProperty(new CityGMLBuildingNumericProperty("Bui_Volu_1", v));
  
        IStringUtils.StringExtractionResult qcl = IStringUtils.extractSubStringBetween(data, "\"QCL_1\": ", ",", pos);
        pos = qcl._endingPos+1;
        v = IStringUtils.instance().parseDouble(qcl._string);
        b.addNumericProperty(new CityGMLBuildingNumericProperty("QCL_1", v));
  
        IStringUtils.StringExtractionResult som = IStringUtils.extractSubStringBetween(data, "\"SOMcluster\": ", ",", pos);
        pos = som._endingPos+1;
        v = IStringUtils.instance().parseDouble(som._string);
        b.addNumericProperty(new CityGMLBuildingNumericProperty("SOMcluster", v));
  
        IStringUtils.StringExtractionResult field2 = IStringUtils.extractSubStringBetween(data, "\"Field2_12\": ", ",", pos);
        pos = field2._endingPos+1;
        v = IStringUtils.instance().parseDouble(field2._string);
        b.addNumericProperty(new CityGMLBuildingNumericProperty("Field2_12", v));
  
      }
    }
  }

  public static Mesh createPointCloudMesh(String data, Planet planet, ElevationData elevationData)
  {
  
    //{ "type": "Feature", "properties": { "OBJECTID": 1, "SOURCE_ID": 1, "Value": 72, "GiZScore": -1.341899, "GiPValue": 0.179629, "Gi_Bin": 0 }, "geometry": { "type": "Point", "coordinates": [ 8.406533595313695, 49.025119962101599 ] } },
  
    java.util.ArrayList<ColorLegend.ColorAndValue> legend = new java.util.ArrayList<ColorLegend.ColorAndValue>();
    legend.add(new ColorLegend.ColorAndValue(Color.blue(), -2.0));
    legend.add(new ColorLegend.ColorAndValue(Color.red(), 2.0));
    ColorLegend cl = new ColorLegend(legend);
  
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  //  int pointCounter = 0;
    int pos = 0;
    int dataL = data.length();
    while (pos < dataL)
    {
  
      IStringUtils.StringExtractionResult value = IStringUtils.extractSubStringBetween(data, "\"GiZScore\": ", ",", pos);
      if (value._endingPos == String.npos)
      {
        break;
      }
      pos = value._endingPos +1;
      double v = IStringUtils.instance().parseDouble(value._string);
  
  
      IStringUtils.StringExtractionResult point = IStringUtils.extractSubStringBetween(data, "\"coordinates\": [", " ] ", pos);
      if (point._endingPos == String.npos)
      {
        break;
      }
      pos = point._endingPos +1;
  
      java.util.ArrayList<Double> vd = IStringUtils.instance().parseDoubles(point._string, ", ");
  
      if (elevationData == null)
      {
        Geodetic3D g = Geodetic3D.fromDegrees(vd.get(1), vd.get(0), 2);
        vertices.add(g);
      }
      else
      {
        double h = elevationData.getElevationAt(Angle.fromDegrees(vd.get(1)), Angle.fromDegrees(vd.get(0)));
        Geodetic3D g = Geodetic3D.fromDegrees(vd.get(1), vd.get(0), 2 + h);
        vertices.add(g);
      }
  
      Color color = cl.getColor(v);
      colors.add(color);
  
  //    if (pointCounter++ % 100 == 0){
  //      ILogger::instance()->logInfo("%d points parsed", pointCounter);
  //    }
    }
  
    DirectMesh dm = new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(), 1.0, 2.0, null, colors.create()); //new Color(Color::red()),
  
    if (vertices != null)
       vertices.dispose();
  
    return dm;
  }

}