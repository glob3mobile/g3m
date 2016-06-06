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

  private static int extractFeature(CityGMLBuilding b, String name, String nameInJSON, String data, int pos)
  {
    IStringUtils.StringExtractionResult heatDem = IStringUtils.extractSubStringBetween(data, "\"" + nameInJSON + "\": ", ",", pos);
    if (heatDem._endingPos == String.npos)
    {
      return heatDem._endingPos;
    }
    pos = heatDem._endingPos+1;
    double v = IStringUtils.instance().parseDouble(heatDem._string);
    b.addNumericProperty(new CityGMLBuildingNumericProperty(name, v));
    return pos;
  }


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

  public static Mesh createSolarRadiationMeshFromCSV(String data, Planet planet, ElevationData elevationData, ColorLegend colorLegend)
  {
  
    java.util.ArrayList<String> lines = IStringUtils.instance().splitLines(data);
  
    java.util.ArrayList<Geodetic3D> points = new java.util.ArrayList<Geodetic3D>();
  
  
  
    for (int i = 0; i < lines.size(); i++)
    {
      //    ILogger::instance()->logInfo(lines[i]);
      java.util.ArrayList<Double> vs = IStringUtils.instance().parseDoubles(lines.get(i), ",");
      if (vs.size() < 3)
      {
        continue;
      }
  
      Geodetic3D g = new Geodetic3D(Geodetic3D.fromDegrees(vs.get(1), vs.get(0), vs.get(2)));
      points.add(g);
  
    }
  
  
    double minH = points.get(0)._height;
    if (elevationData != null)
    {
      for (int i = 0; i < points.size(); i++)
      {
        if (points.get(i)._height < minH)
        {
          minH = points.get(i)._height;
        }
      }
    }
  
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
    for (int i = 0; i < points.size(); i++)
    {
  
      Geodetic2D g2D = points.get(i).asGeodetic2D();
  
      double h = points.get(i)._height;
  
  ///#warning REMOVE THIS IS JUST FOR TECHNOLOGY PARK
  //    double scale = 1.4;
  //    h = h + scale * (h - minH);
  
      if (elevationData != null)
      {
        h = h - minH + elevationData.getElevationAt(g2D);
      }
  
      vertices.add(new Geodetic3D(g2D, h));
      if (points.get(i) != null)
         points.get(i).dispose();
    }
  
    IFloatBuffer colors = create0ColorsForSolarRadiationMeshFromCSV(colorLegend, (int)vertices.size() / 3);
  
    PointCloudMesh pcm = new PointCloudMesh(true, vertices.getCenter(), vertices.create(), 10.0, colors, true, Color.blue());
  
    ILogger.instance().logInfo("Created point cloud of %d points.", vertices.size() / 3);
  
    if (vertices != null)
       vertices.dispose();
  
    return pcm;
  
  }

  public static IFloatBuffer createColorsForSolarRadiationMeshFromCSV(String data, ColorLegend colorLegend)
  {
  
    java.util.ArrayList<Double> vs = IStringUtils.instance().parseDoubles(data, ",");
  
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  
    java.util.ArrayList<Geodetic3D> points = new java.util.ArrayList<Geodetic3D>();
  
    for (int i = 0; i < vs.size(); i++)
    {
      Color color = colorLegend.getColor(vs.get(i));
      colors.add(color);
    }
  
    return colors.create();
  }
  public static IFloatBuffer create0ColorsForSolarRadiationMeshFromCSV(ColorLegend colorLegend, int nVertices)
  {
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
    java.util.ArrayList<Geodetic3D> points = new java.util.ArrayList<Geodetic3D>();
  
    Color color = colorLegend.getColor(0);
    for (int i = 0; i < nVertices; i++)
    {
      colors.add(color);
    }
  
    return colors.create();
  }

}