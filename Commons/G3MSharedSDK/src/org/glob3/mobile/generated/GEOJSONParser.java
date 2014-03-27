package org.glob3.mobile.generated; 
//
//  GEOJSONParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

//
//  GEOJSONParser.hpp
//  G3MiOSSDK
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
//class GEO2DPolygonData;


public class GEOJSONParser
{
  private final String _json;
  private final IByteBuffer _bson;

  // statistics
  private int _points2DCount;
  private int _coordinates2DCount;
  private int _lineStrings2DCount;
  private int _multiLineStrings2DCount;
  private int _lineStringsInMultiLineString2DCount;
  private int _featuresCount;
  private int _featuresCollectionCount;
  private int _polygon2DCount;
  private int _holesLineStringsInPolygon2DCount;
  private int _multiPolygon2DCount;

  private GEOJSONParser(String json, IByteBuffer bson)
  {
     _json = json;
     _bson = bson;
     _points2DCount = 0;
     _coordinates2DCount = 0;
     _lineStrings2DCount = 0;
     _multiLineStrings2DCount = 0;
     _lineStringsInMultiLineString2DCount = 0;
     _featuresCount = 0;
     _featuresCollectionCount = 0;
     _polygon2DCount = 0;
     _holesLineStringsInPolygon2DCount = 0;
     _multiPolygon2DCount = 0;

  }

  private GEOObject pvtParse()
  {
    final JSONBaseObject jsonBaseObject = (_bson == null) ? IJSONParser.instance().parse(_json) : BSONParser.parse(_bson);
  
    GEOObject result = null;
  
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
  
    showStatistics();
  
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
  
    final JSONBaseObject jsId = JSONBaseObject.deepCopy(jsonObject.get("id"));
  
    final JSONObject jsGeometry = jsonObject.getAsObject("geometry");
    GEOGeometry geometry = createGeometry(jsGeometry);
  
    JSONObject jsProperties = jsonObject.getAsObject("properties");
    if (jsProperties != null)
    {
      jsProperties = jsProperties.deepCopy();
    }
  
    _featuresCount++;
    return new GEOFeature(jsId, geometry, jsProperties);
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
     "LineString"
     "MultiLineString"
     "Point"
     "Polygon"
  
     "MultiPolygon"
  
     "MultiPoint"
     "GeometryCollection"
     */
  
    if (type.compareTo("LineString") == 0)
    {
      geo = createLineStringGeometry(jsonObject);
    }
    else if (type.compareTo("MultiLineString") == 0)
    {
      geo = createMultiLineStringGeometry(jsonObject);
    }
    else if (type.compareTo("Point") == 0)
    {
      geo = createPointGeometry(jsonObject);
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
  
    final int dimensions = jsCoordinates.getAsArray(0).size();
    if (dimensions == 2)
    {
      java.util.ArrayList<Geodetic2D> coordinates = create2DCoordinates(jsCoordinates);
      if (coordinates != null)
      {
        geo = new GEO2DLineStringGeometry(coordinates);
        _lineStrings2DCount++;
      }
    }
    else
    {
      ILogger.instance().logError("Invalid coordinates dimensions=%d", dimensions);
      return null;
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
    else
    {
      ILogger.instance().logError("Invalid coordinates dimensions=%d", dimensions);
      return null;
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
      final double latitudeDegrees = jsCoordinates.getAsNumber(1, 0.0);
      final double longitudeDegrees = jsCoordinates.getAsNumber(0, 0.0);
  
      _points2DCount++;
  
      geo = new GEO2DPointGeometry(Geodetic2D.fromDegrees(latitudeDegrees, longitudeDegrees));
    }
  //  else if (dimensions == 3) {
  //    const double latitudeDegrees  = jsCoordinates->getAsNumber(1, 0.0);
  //    const double longitudeDegrees = jsCoordinates->getAsNumber(0, 0.0);
  //    const double height           = jsCoordinates->getAsNumber(2, 0.0);
  //  }
    else
    {
      ILogger.instance().logError("Mandatory \"coordinates\" dimensions not supported %d", dimensions);
    }
  
    return geo;
  }
  private GEOGeometry createPolygonGeometry(JSONObject jsonObject)
  {
    GEO2DPolygonData polygonData = parsePolygon2DData(jsonObject);
  
    return (polygonData == null) ? null : new GEO2DPolygonGeometry(polygonData);
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

  private GEO2DPolygonData parsePolygon2DData(JSONObject jsonObject)
  {
    final JSONArray jsCoordinatesArray = jsonObject.getAsArray("coordinates");
    if (jsCoordinatesArray == null)
    {
      ILogger.instance().logError("Mandatory \"coordinates\" attribute is not present");
      return null;
    }
  
    return parsePolygon2DData(jsCoordinatesArray);
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
  
    final int dimensions = jsFirstCoordinates.getAsArray(0).size();
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
  
      final double latitudeDegrees = jsCoordinate.getAsNumber(1, 0.0);
      final double longitudeDegrees = jsCoordinate.getAsNumber(0, 0.0);
  
      Geodetic2D coordinate = new Geodetic2D(Angle.fromDegrees(latitudeDegrees), Angle.fromDegrees(longitudeDegrees));
      coordinates.add(coordinate);
      _coordinates2DCount++;
    }
  
    return coordinates;
  }

  private void showStatistics()
  {
    ILogger.instance().logInfo("GEOJSONParser Statistics: Coordinates2D=%d, Points2D=%d, LineStrings2D=%d, MultiLineStrings2D=%d (LineStrings2D=%d), Polygons2D=%d (Holes=%d), MultiPolygons=%d, features=%d, featuresCollection=%d", _coordinates2DCount, _points2DCount, _lineStrings2DCount, _multiLineStrings2DCount, _lineStringsInMultiLineString2DCount, _polygon2DCount, _holesLineStringsInPolygon2DCount, _multiPolygon2DCount, _featuresCount, _featuresCollectionCount);
  }


  public static GEOObject parseJSON(String json)
  {
    GEOJSONParser parser = new GEOJSONParser(json, null);
    return parser.pvtParse();
  }
  public static GEOObject parseJSON(IByteBuffer json)
  {
    return parseJSON(json.getAsString());
  }

  public static GEOObject parseBSON(IByteBuffer bson)
  {
    GEOJSONParser parser = new GEOJSONParser("", bson);
    return parser.pvtParse();
  }

}