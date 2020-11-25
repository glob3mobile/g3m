package org.glob3.mobile.generated;
//
//  GEOJSONParser.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

//
//  GEOJSONParser.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//



//class IByteBuffer;
//class JSONObject;
//class JSONArray;
//class GEOObject;
//class GEOFeatureCollection;
//class GEOFeature;
//class GEOGeometry;
//class Geodetic2D;
//class Geodetic3D;
//class GEO2DPolygonData;
//class GEO3DPolygonData;


public class GEOJSONParser
{
  private final String _json;
  private final IByteBuffer _bson;

  // statistics
  private int _points2DCount;
  private int _points3DCount;
  private int _coordinates2DCount;
  private int _coordinates3DCount;
  private int _lineStrings2DCount;
  private int _multiLineStrings2DCount;
  private int _lineStringsInMultiLineString2DCount;
  private int _featuresCount;
  private int _featuresCollectionCount;
  private int _polygon2DCount;
  private int _polygon3DCount;
  private int _holesLineStringsInPolygon2DCount;
  private int _holesLineStringsInPolygon3DCount;
  private int _multiPolygon2DCount;

  private GEOJSONParser(String json, IByteBuffer bson)
  {
     _json = json;
     _bson = bson;
     _points2DCount = 0;
     _points3DCount = 0;
     _coordinates2DCount = 0;
     _coordinates3DCount = 0;
     _lineStrings2DCount = 0;
     _multiLineStrings2DCount = 0;
     _lineStringsInMultiLineString2DCount = 0;
     _featuresCount = 0;
     _featuresCollectionCount = 0;
     _polygon2DCount = 0;
     _polygon3DCount = 0;
     _holesLineStringsInPolygon2DCount = 0;
     _holesLineStringsInPolygon3DCount = 0;
     _multiPolygon2DCount = 0;

  }

  private GEOObject pvtParse(boolean showStatistics)
  {
    GEOObject result = null;
  
    final JSONBaseObject jsonBaseObject = (_bson == null) ? IJSONParser.instance().parse(_json) : BSONParser.parse(_bson);
  
    if (jsonBaseObject != null)
    {
      final JSONObject jsonObject = jsonBaseObject.asObject();
      if (jsonObject != null)
      {
        result = toGEO(jsonObject);
      }
      else
      {
        ILogger.instance().logError("Root object for GEOJSON has to be a JSONObject");
      }
  
      if (jsonBaseObject != null)
         jsonBaseObject.dispose();
  
      if (showStatistics)
      {
        showStatisticsToLogger();
      }
    }
  
    return result;
  }

  private GEOObject toGEO(JSONObject jsonObject)
  {
    final String type = jsonObject.getAsString("type", "");
    if (type.compareTo("FeatureCollection") == 0)
    {
      return createFeaturesCollection(jsonObject);
    }
    else if (type.compareTo("Feature") == 0)
    {
      return createFeature(jsonObject);
    }
    else
    {
      ILogger.instance().logError("GEOJSON: Unkown type \"%s\"", type);
      return null;
    }
  }

  private GEOFeatureCollection createFeaturesCollection(JSONObject jsonObject)
  {
    java.util.ArrayList<GEOFeature> features = new java.util.ArrayList<GEOFeature>();
  
    final JSONArray jsFeatures = jsonObject.getAsArray("features");
    if (jsFeatures != null)
    {
      final int featuresCount = jsFeatures.size();
      for (int i = 0; i < featuresCount; i++)
      {
        final JSONObject jsFeature = jsFeatures.getAsObject(i);
        if (jsFeature != null)
        {
          GEOFeature feature = createFeature(jsFeature);
          features.add(feature);
        }
      }
    }
  
    _featuresCollectionCount++;
    return new GEOFeatureCollection(features);
  }
  private GEOFeature createFeature(JSONObject jsonObject)
  {
  
    final JSONBaseObject jsID = JSONBaseObject.deepCopy(jsonObject.get("id"));
  
    final JSONObject jsGeometry = jsonObject.getAsObject("geometry");
    GEOGeometry geometry = createGeometry(jsGeometry);
  
    JSONObject jsProperties = jsonObject.getAsObject("properties");
    if (jsProperties != null)
    {
      jsProperties = jsProperties.deepCopy();
    }
  
    _featuresCount++;
    return new GEOFeature(jsID, geometry, jsProperties);
  }

  private GEOGeometry createGeometry(JSONObject jsonObject)
  {
    if (jsonObject == null)
    {
      return null;
    }
  
    final String type = jsonObject.getAsString("type", "");
  
    GEOGeometry geo = null;
  
    /*
     "Point"
     - TODO --> "MultiPoint"
     "LineString"
     "MultiLineString"
     "Polygon"
     "MultiPolygon"
     - TODO --> "GeometryCollection"
     */
  
    if (type.compareTo("Point") == 0)
    {
      geo = createPointGeometry(jsonObject);
    }
  //  else if (type.compare("MultiPoint") == 0) {
  //    geo = createMultiPointPointGeometry(jsonObject);
  //  }
    else if (type.compareTo("LineString") == 0)
    {
      geo = createLineStringGeometry(jsonObject);
    }
    else if (type.compareTo("MultiLineString") == 0)
    {
      geo = createMultiLineStringGeometry(jsonObject);
    }
    else if (type.compareTo("Polygon") == 0)
    {
      geo = createPolygonGeometry(jsonObject);
    }
    else if (type.compareTo("MultiPolygon") == 0)
    {
      geo = createMultiPolygonGeometry(jsonObject);
    }
    else
    {
      ILogger.instance().logError("Unknown geometry type \"%s\"", type);
    }
  
    return geo;
  }
  private GEOGeometry createLineStringGeometry(JSONObject jsonObject)
  {
  
    final JSONArray jsCoordinates = jsonObject.getAsArray("coordinates");
    if (jsCoordinates == null)
    {
      ILogger.instance().logError("Mandatory \"coordinates\" attribute is not present");
      return null;
    }
  
    final int coordinatesCount = jsCoordinates.size();
    if (coordinatesCount == 0)
    {
      ILogger.instance().logError("Mandatory \"coordinates\" attribute is empty");
      return null;
    }
  
    GEOGeometry geo = null;
  
    final JSONArray jsFirstCoordinate = jsCoordinates.getAsArray(0);
    final int dimensions = jsFirstCoordinate.size();
    if (dimensions == 2)
    {
      java.util.ArrayList<Geodetic2D> coordinates = create2DCoordinates(jsCoordinates);
      if (coordinates != null)
      {
        geo = new GEO2DLineStringGeometry(coordinates);
        _lineStrings2DCount++;
      }
    }
  //  else if (dimensions == 3) {
  //    std::vector<Geodetic3D*>* coordinates = create3DCoordinates(jsCoordinates);
  //    if (coordinates != NULL) {
  //      geo = new GEO3DLineStringGeometry(coordinates);
  //      _lineStrings3DCount++;
  //    }
  //  }
    else
    {
      ILogger.instance().logError("Invalid coordinates dimensions=%d", dimensions);
    }
  
    return geo;
  }
  private GEOGeometry createMultiLineStringGeometry(JSONObject jsonObject)
  {
  
    final JSONArray jsCoordinatesArray = jsonObject.getAsArray("coordinates");
    if (jsCoordinatesArray == null)
    {
      ILogger.instance().logError("Mandatory \"coordinates\" attribute is not present");
      return null;
    }
  
    final int coordinatesArrayCount = jsCoordinatesArray.size();
    if (coordinatesArrayCount == 0)
    {
      ILogger.instance().logError("Mandatory \"coordinates\" attribute is empty");
      return null;
    }
  
    final JSONArray jsFirstCoordinates = jsCoordinatesArray.getAsArray(0);
    if (jsFirstCoordinates == null)
    {
      ILogger.instance().logError("Invalid format for first \"coordinates\" element");
      return null;
    }
    final int firstCoordinatesCount = jsFirstCoordinates.size();
    if (firstCoordinatesCount == 0)
    {
      ILogger.instance().logError("Invalid format for first \"coordinates\" element");
      return null;
    }
  
    GEOGeometry geo = null;
  
    final int dimensions = jsFirstCoordinates.getAsArray(0).size();
    if (dimensions == 2)
    {
      java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray = new java.util.ArrayList<java.util.ArrayList<Geodetic2D>>();
  
      for (int i = 0; i < coordinatesArrayCount; i++)
      {
        final JSONArray jsCoordinates = jsCoordinatesArray.getAsArray(i);
        java.util.ArrayList<Geodetic2D> coordinates = create2DCoordinates(jsCoordinates);
        if (coordinates != null)
        {
          coordinatesArray.add(coordinates);
          _lineStringsInMultiLineString2DCount++;
        }
      }
  
      geo = new GEO2DMultiLineStringGeometry(coordinatesArray);
      _multiLineStrings2DCount++;
    }
  //  else if (dimensions == 3) {
  //    std::vector<std::vector<Geodetic3D*>*>* coordinatesArray = new std::vector<std::vector<Geodetic3D*>*>();
  //
  //    for (int i = 0; i < coordinatesArrayCount; i++) {
  //      const JSONArray* jsCoordinates = jsCoordinatesArray->getAsArray(i);
  //      std::vector<Geodetic3D*>* coordinates = create3DCoordinates(jsCoordinates);
  //      if (coordinates != NULL) {
  //        coordinatesArray->push_back( coordinates );
  //        _lineStringsInMultiLineString3DCount++;
  //      }
  //    }
  //
  //    geo = new GEO3DMultiLineStringGeometry(coordinatesArray);
  //    _multiLineStrings3DCount++;
  //  }
    else
    {
      ILogger.instance().logError("Invalid coordinates dimensions=%d", dimensions);
    }
  
    return geo;
  }
  private GEOGeometry createPointGeometry(JSONObject jsonObject)
  {
    final JSONArray jsCoordinates = jsonObject.getAsArray("coordinates");
    if (jsCoordinates == null)
    {
      ILogger.instance().logError("Mandatory \"coordinates\" attribute is not present");
      return null;
    }
  
    GEOGeometry geo = null;
  
    final int dimensions = jsCoordinates.size();
    if (dimensions == 2)
    {
      final double latitudeDegrees = jsCoordinates.getAsNumber(1).value();
      final double longitudeDegrees = jsCoordinates.getAsNumber(0).value();
  
      _points2DCount++;
  
      geo = new GEO2DPointGeometry(Geodetic2D.fromDegrees(latitudeDegrees, longitudeDegrees));
    }
    else if (dimensions == 3)
    {
      final double latitudeDegrees = jsCoordinates.getAsNumber(1).value();
      final double longitudeDegrees = jsCoordinates.getAsNumber(0).value();
      final double height = jsCoordinates.getAsNumber(2).value();
  
      _points3DCount++;
  
      geo = new GEO3DPointGeometry(Geodetic3D.fromDegrees(latitudeDegrees, longitudeDegrees, height));
    }
    else
    {
      ILogger.instance().logError("Mandatory \"coordinates\" dimensions not supported %d", dimensions);
    }
  
    return geo;
  }
  private GEOGeometry createPolygonGeometry(JSONObject jsonObject)
  {
    final JSONArray jsCoordinatesArray = jsonObject.getAsArray("coordinates");
    if ((jsCoordinatesArray == null) || (jsCoordinatesArray.size() == 0))
    {
      ILogger.instance().logError("Mandatory \"coordinates\" attribute is not present");
      return null;
    }
  
    final JSONArray jsOuterRingArray = jsCoordinatesArray.getAsArray(0);
    if ((jsOuterRingArray == null) || (jsOuterRingArray.size() == 0))
    {
      ILogger.instance().logError("Mandatory \"coordinates\" first ring is not present");
      return null;
    }
  
    final JSONArray jsFirstCoordinate = jsOuterRingArray.getAsArray(0);
    final int dimensions = jsFirstCoordinate.size();
    if (dimensions == 2)
    {
      GEO2DPolygonData polygonData = parsePolygon2DData(jsCoordinatesArray);
      return (polygonData == null) ? null : new GEO2DPolygonGeometry(polygonData);
    }
    else if (dimensions == 3)
    {
      GEO3DPolygonData polygonData = parsePolygon3DData(jsCoordinatesArray);
      return (polygonData == null) ? null : new GEO3DPolygonGeometry(polygonData);
    }
  
    ILogger.instance().logError("Invalid coordinates dimensions=%d", dimensions);
    return null;
  }
  private GEOGeometry createMultiPolygonGeometry(JSONObject jsonObject)
  {
    final JSONArray jsPolygonsCoordinatesArray = jsonObject.getAsArray("coordinates");
    if (jsPolygonsCoordinatesArray == null)
    {
      ILogger.instance().logError("Mandatory \"coordinates\" attribute is not present");
      return null;
    }
  
    final int polygonsCoordinatesArrayCount = jsPolygonsCoordinatesArray.size();
    if (polygonsCoordinatesArrayCount == 0)
    {
      ILogger.instance().logError("Mandatory \"coordinates\" attribute is empty");
      return null;
    }
  
    java.util.ArrayList<GEO2DPolygonData> polygonsData = new java.util.ArrayList<GEO2DPolygonData>();
    for (int i = 0; i < polygonsCoordinatesArrayCount; i++)
    {
      final JSONArray jsCoordinatesArray = jsPolygonsCoordinatesArray.getAsArray(i);
  
      polygonsData.add(parsePolygon2DData(jsCoordinatesArray));
    }
  
    _multiPolygon2DCount++;
  
    return new GEO2DMultiPolygonGeometry(polygonsData);
  }

  private GEO2DPolygonData parsePolygon2DData(JSONArray jsCoordinatesArray)
  {
    final int coordinatesArrayCount = jsCoordinatesArray.size();
    if (coordinatesArrayCount == 0)
    {
      ILogger.instance().logError("Mandatory \"coordinates\" attribute is empty");
      return null;
    }
  
    final JSONArray jsFirstCoordinates = jsCoordinatesArray.getAsArray(0);
    if (jsFirstCoordinates == null)
    {
      ILogger.instance().logError("Invalid format for first \"coordinates\" element");
      return null;
    }
    final int firstCoordinatesCount = jsFirstCoordinates.size();
    if (firstCoordinatesCount == 0)
    {
      ILogger.instance().logError("Invalid format for first \"coordinates\" element");
      return null;
    }
  
    final JSONArray jsFirstCoordinate = jsFirstCoordinates.getAsArray(0);
    final int dimensions = jsFirstCoordinate.size();
    if (dimensions == 2)
    {
      final JSONArray jsCoordinates = jsCoordinatesArray.getAsArray(0);
      java.util.ArrayList<Geodetic2D> coordinates = create2DCoordinates(jsCoordinates);
  
      java.util.ArrayList<java.util.ArrayList<Geodetic2D>> holesCoordinatesArray = new java.util.ArrayList<java.util.ArrayList<Geodetic2D>>();
      for (int i = 1; i < coordinatesArrayCount; i++)
      {
        final JSONArray jsHoleCoordinates = jsCoordinatesArray.getAsArray(i);
        java.util.ArrayList<Geodetic2D> holeCoordinates = create2DCoordinates(jsHoleCoordinates);
        if (holeCoordinates != null)
        {
          holesCoordinatesArray.add(holeCoordinates);
          _holesLineStringsInPolygon2DCount++;
        }
      }
  
      if (holesCoordinatesArray.size() == 0)
      {
        holesCoordinatesArray = null;
        holesCoordinatesArray = null;
      }
  
      _polygon2DCount++;
      return new GEO2DPolygonData(coordinates, holesCoordinatesArray);
    }
  
    ILogger.instance().logError("Invalid coordinates dimensions=%d", dimensions);
    return null;
  }
  private GEO3DPolygonData parsePolygon3DData(JSONArray jsCoordinatesArray)
  {
    final int coordinatesArrayCount = jsCoordinatesArray.size();
    if (coordinatesArrayCount == 0)
    {
      ILogger.instance().logError("Mandatory \"coordinates\" attribute is empty");
      return null;
    }
  
    final JSONArray jsFirstCoordinates = jsCoordinatesArray.getAsArray(0);
    if (jsFirstCoordinates == null)
    {
      ILogger.instance().logError("Invalid format for first \"coordinates\" element");
      return null;
    }
    final int firstCoordinatesCount = jsFirstCoordinates.size();
    if (firstCoordinatesCount == 0)
    {
      ILogger.instance().logError("Invalid format for first \"coordinates\" element");
      return null;
    }
  
    final JSONArray jsFirstCoordinate = jsFirstCoordinates.getAsArray(0);
    final int dimensions = jsFirstCoordinate.size();
    if (dimensions == 3)
    {
      final JSONArray jsCoordinates = jsCoordinatesArray.getAsArray(0);
      java.util.ArrayList<Geodetic3D> coordinates = create3DCoordinates(jsCoordinates);
  
      java.util.ArrayList<java.util.ArrayList<Geodetic3D>> holesCoordinatesArray = new java.util.ArrayList<java.util.ArrayList<Geodetic3D>>();
      for (int i = 1; i < coordinatesArrayCount; i++)
      {
        final JSONArray jsHoleCoordinates = jsCoordinatesArray.getAsArray(i);
        java.util.ArrayList<Geodetic3D> holeCoordinates = create3DCoordinates(jsHoleCoordinates);
        if (holeCoordinates != null)
        {
          holesCoordinatesArray.add(holeCoordinates);
          _holesLineStringsInPolygon3DCount++;
        }
      }
  
      if (holesCoordinatesArray.size() == 0)
      {
        holesCoordinatesArray = null;
        holesCoordinatesArray = null;
      }
  
      _polygon3DCount++;
      return new GEO3DPolygonData(coordinates, holesCoordinatesArray);
    }
  
    ILogger.instance().logError("Invalid coordinates dimensions=%d", dimensions);
    return null;
  }

  private java.util.ArrayList<Geodetic2D> create2DCoordinates(JSONArray jsCoordinates)
  {
    if (jsCoordinates == null)
    {
      ILogger.instance().logError("Mandatory \"coordinates\" attribute is not present");
      return null;
    }
  
    final int coordinatesCount = jsCoordinates.size();
    if (coordinatesCount == 0)
    {
      ILogger.instance().logError("Mandatory \"coordinates\" attribute is empty");
      return null;
    }
  
    java.util.ArrayList<Geodetic2D> coordinates = new java.util.ArrayList<Geodetic2D>();
    for (int i = 0; i < coordinatesCount; i++)
    {
      final JSONArray jsCoordinate = jsCoordinates.getAsArray(i);
  
      final double latitudeDegrees = jsCoordinate.getAsNumber(1).value();
      final double longitudeDegrees = jsCoordinate.getAsNumber(0).value();
  
      Geodetic2D coordinate = new Geodetic2D(Angle.fromDegrees(latitudeDegrees), Angle.fromDegrees(longitudeDegrees));
      coordinates.add(coordinate);
      _coordinates2DCount++;
    }
  
    return coordinates;
  }
  private java.util.ArrayList<Geodetic3D> create3DCoordinates(JSONArray jsCoordinates)
  {
    if (jsCoordinates == null)
    {
      ILogger.instance().logError("Mandatory \"coordinates\" attribute is not present");
      return null;
    }
  
    final int coordinatesCount = jsCoordinates.size();
    if (coordinatesCount == 0)
    {
      ILogger.instance().logError("Mandatory \"coordinates\" attribute is empty");
      return null;
    }
  
    java.util.ArrayList<Geodetic3D> coordinates = new java.util.ArrayList<Geodetic3D>();
    for (int i = 0; i < coordinatesCount; i++)
    {
      final JSONArray jsCoordinate = jsCoordinates.getAsArray(i);
  
      final double latitudeDegrees = jsCoordinate.getAsNumber(1).value();
      final double longitudeDegrees = jsCoordinate.getAsNumber(0).value();
      final double height = jsCoordinate.getAsNumber(2).value();
  
      Geodetic3D coordinate = new Geodetic3D(Angle.fromDegrees(latitudeDegrees), Angle.fromDegrees(longitudeDegrees), height);
      coordinates.add(coordinate);
      _coordinates3DCount++;
    }
  
    return coordinates;
  }

  private void showStatisticsToLogger()
  {
    IStringBuilder sb = IStringBuilder.newStringBuilder();
  
    sb.addString("GEOJSONParser Statistics:");
  
    if (_featuresCollectionCount > 0)
    {
      sb.addString(" FeaturesCollection=");
      sb.addLong(_featuresCollectionCount);
    }
  
    if (_featuresCount > 0)
    {
      sb.addString(" Features=");
      sb.addLong(_featuresCount);
    }
  
    if (_coordinates2DCount > 0)
    {
      sb.addString(" Coordinates2=");
      sb.addLong(_coordinates2DCount);
    }
  
    if (_coordinates3DCount > 0)
    {
      sb.addString(" Coordinates3=");
      sb.addLong(_coordinates3DCount);
    }
  
    if (_points2DCount > 0)
    {
      sb.addString(" Points2=");
      sb.addLong(_points2DCount);
    }
  
    if (_points3DCount > 0)
    {
      sb.addString(" Points3=");
      sb.addLong(_points3DCount);
    }
  
    if (_lineStrings2DCount > 0)
    {
      sb.addString(" LineStrings=");
      sb.addLong(_lineStrings2DCount);
    }
  
    if (_multiLineStrings2DCount > 0)
    {
      sb.addString(" MultiLineStrings=");
      sb.addLong(_multiLineStrings2DCount);
      if (_lineStringsInMultiLineString2DCount > 0)
      {
        sb.addString(" (LineStrings=");
        sb.addLong(_lineStringsInMultiLineString2DCount);
        sb.addString(")");
      }
    }
  
    if (_polygon2DCount > 0)
    {
      sb.addString(" Polygons2=");
      sb.addLong(_polygon2DCount);
      if (_holesLineStringsInPolygon2DCount > 0)
      {
        sb.addString(" (Holes=");
        sb.addLong(_holesLineStringsInPolygon2DCount);
        sb.addString(")");
      }
    }
  
    if (_polygon3DCount > 0)
    {
      sb.addString(" Polygons3=");
      sb.addLong(_polygon3DCount);
      if (_holesLineStringsInPolygon3DCount > 0)
      {
        sb.addString(" (Holes=");
        sb.addLong(_holesLineStringsInPolygon3DCount);
        sb.addString(")");
      }
    }
  
    if (_multiPolygon2DCount > 0)
    {
      sb.addString(" MultiPolygons=");
      sb.addLong(_multiPolygon2DCount);
    }
  
    ILogger.instance().logInfo(sb.getString());
  
    if (sb != null)
       sb.dispose();
  }


  public static GEOObject parseJSON(String json)
  {
     return parseJSON(json, true);
  }
  public static GEOObject parseJSON(String json, boolean showStatistics)
  {
    GEOJSONParser parser = new GEOJSONParser(json, null);
    return parser.pvtParse(showStatistics);
  }
  public static GEOObject parseJSON(IByteBuffer json)
  {
     return parseJSON(json, true);
  }
  public static GEOObject parseJSON(IByteBuffer json, boolean showStatistics)
  {
    return parseJSON(json.getAsString(), showStatistics);
  }

  public static GEOObject parseBSON(IByteBuffer bson)
  {
     return parseBSON(bson, true);
  }
  public static GEOObject parseBSON(IByteBuffer bson, boolean showStatistics)
  {
    GEOJSONParser parser = new GEOJSONParser("", bson);
    return parser.pvtParse(showStatistics);
  }

  public static GEOObject parse(JSONObject jsonObject)
  {
     return parse(jsonObject, true);
  }
  public static GEOObject parse(JSONObject jsonObject, boolean showStatistics)
  {
    GEOJSONParser parser = new GEOJSONParser("", null);
    GEOObject result = parser.toGEO(jsonObject);
    if (showStatistics)
    {
      parser.showStatisticsToLogger();
    }
    return result;
  }

}