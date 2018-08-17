package org.glob3.mobile.generated;import java.util.*;

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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IByteBuffer;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONArray;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOFeatureCollection;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOFeature;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOGeometry;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOObject* pvtParse(boolean showStatistics) const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOObject* toGEO(const JSONObject* jsonObject) const
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
	  ILogger.instance().logError("GEOJSON: Unkown type \"%s\"", type.c_str());
	  return null;
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOFeatureCollection* createFeaturesCollection(const JSONObject* jsonObject) const
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
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOFeature* createFeature(const JSONObject* jsonObject) const
  private GEOFeature createFeature(JSONObject jsonObject)
  {
  
	final JSONBaseObject jsId = JSONBaseObject.deepCopy(jsonObject.get("id"));
  
	final JSONObject jsGeometry = jsonObject.getAsObject("geometry");
	GEOGeometry geometry = createGeometry(jsGeometry);
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	final JSONObject jsProperties = jsonObject.getAsObject("properties");
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	JSONObject jsProperties = jsonObject.getAsObject("properties");
//#endif
	if (jsProperties != null)
	{
	  jsProperties = jsProperties.deepCopy();
	}
  
	_featuresCount++;
	return new GEOFeature(jsId, geometry, jsProperties);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOGeometry* createGeometry(const JSONObject* jsonObject) const
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
	  ILogger.instance().logError("Unknown geometry type \"%s\"", type.c_str());
	}
  
	return geo;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOGeometry* createLineStringGeometry(const JSONObject* jsonObject) const
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
  
	if (jsFirstCoordinate == null)
	{
	  if (jsCoordinates.getAsNumber(0) != null)
	  {
		// assumes flat format: [ lon, lat, lon, lat ... ]
		java.util.ArrayList<Geodetic2D> coordinates = createFlat2DCoordinates(jsCoordinates);
		if (coordinates != null)
		{
		  geo = new GEO2DLineStringGeometry(coordinates);
		  _lineStrings2DCount++;
		}
	  }
	}
	else
	{
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
	  else
	  {
		ILogger.instance().logError("Invalid coordinates dimensions=%d", dimensions);
		return null;
	  }
	}
  
	return geo;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOGeometry* createMultiLineStringGeometry(const JSONObject* jsonObject) const
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
	  java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray = new java.util.ArrayList<java.util.ArrayList<Geodetic2D*>*>();
  
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
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOGeometry* createPointGeometry(const JSONObject* jsonObject) const
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
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOGeometry* createPolygonGeometry(const JSONObject* jsonObject) const
  private GEOGeometry createPolygonGeometry(JSONObject jsonObject)
  {
	GEO2DPolygonData polygonData = parsePolygon2DData(jsonObject);
  
	return (polygonData == null) ? null : new GEO2DPolygonGeometry(polygonData);
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOGeometry* createMultiPolygonGeometry(const JSONObject* jsonObject) const
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
  
	java.util.ArrayList<GEO2DPolygonData> polygonsData = new java.util.ArrayList<GEO2DPolygonData*>();
	for (int i = 0; i < polygonsCoordinatesArrayCount; i++)
	{
	  final JSONArray jsCoordinatesArray = jsPolygonsCoordinatesArray.getAsArray(i);
  
	  polygonsData.add(parsePolygon2DData(jsCoordinatesArray));
	}
  
	_multiPolygon2DCount++;
  
	return new GEO2DMultiPolygonGeometry(polygonsData);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEO2DPolygonData* parsePolygon2DData(const JSONObject* jsonObject) const
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
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEO2DPolygonData* parsePolygon2DData(const JSONArray* jsCoordinatesArray) const
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
	if (jsFirstCoordinate == null)
	{
	  if (jsFirstCoordinates.getAsNumber(0) != null)
	  {
		// assumes flat format: [ lon, lat, lon, lat ... ]
		final JSONArray jsCoordinates = jsCoordinatesArray.getAsArray(0);
		java.util.ArrayList<Geodetic2D> coordinates = createFlat2DCoordinates(jsCoordinates);
  
		java.util.ArrayList<java.util.ArrayList<Geodetic2D>> holesCoordinatesArray = new java.util.ArrayList<java.util.ArrayList<Geodetic2D*>*>();
		for (int i = 1; i < coordinatesArrayCount; i++)
		{
		  final JSONArray jsHoleCoordinates = jsCoordinatesArray.getAsArray(i);
		  java.util.ArrayList<Geodetic2D> holeCoordinates = createFlat2DCoordinates(jsHoleCoordinates);
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
	}
	else
	{
	  final int dimensions = jsFirstCoordinate.size();
	  if (dimensions == 2)
	  {
		final JSONArray jsCoordinates = jsCoordinatesArray.getAsArray(0);
		java.util.ArrayList<Geodetic2D> coordinates = create2DCoordinates(jsCoordinates);
  
		java.util.ArrayList<java.util.ArrayList<Geodetic2D>> holesCoordinatesArray = new java.util.ArrayList<java.util.ArrayList<Geodetic2D*>*>();
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
	}
  
	return null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Geodetic2D*>* create2DCoordinates(const JSONArray* jsCoordinates) const
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
  
	java.util.ArrayList<Geodetic2D> coordinates = new java.util.ArrayList<Geodetic2D*>();
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
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Geodetic2D*>* createFlat2DCoordinates(const JSONArray* jsCoordinates) const
  private java.util.ArrayList<Geodetic2D> createFlat2DCoordinates(JSONArray jsCoordinates)
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
  
	java.util.ArrayList<Geodetic2D> coordinates = new java.util.ArrayList<Geodetic2D*>();
	for (int i = 0; i < coordinatesCount; i += 2)
	{
	  final double longitudeDegrees = jsCoordinates.getAsNumber(i + 0, 0.0);
	  final double latitudeDegrees = jsCoordinates.getAsNumber(i + 1, 0.0);
  
	  Geodetic2D coordinate = new Geodetic2D(Angle.fromDegrees(latitudeDegrees), Angle.fromDegrees(longitudeDegrees));
	  coordinates.add(coordinate);
	  _coordinates2DCount++;
	}
  
	return coordinates;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void showStatisticsToLogger() const
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
	  sb.addString(" Coordinates=");
	  sb.addLong(_coordinates2DCount);
	}
  
	if (_points2DCount > 0)
	{
	  sb.addString(" Points=");
	  sb.addLong(_points2DCount);
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
	  sb.addString(" Polygons=");
	  sb.addLong(_polygon2DCount);
	  if (_lineStringsInMultiLineString2DCount > 0)
	  {
		sb.addString(" (Holes=");
		sb.addLong(_holesLineStringsInPolygon2DCount);
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
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static GEOObject* parseJSON(const String& json, boolean showStatistics = true)
  public static GEOObject parseJSON(String json, boolean showStatistics)
  {
	GEOJSONParser parser = new GEOJSONParser(json, null);
	return parser.pvtParse(showStatistics);
  }
  public static GEOObject parseJSON(IByteBuffer json)
  {
	  return parseJSON(json, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static GEOObject* parseJSON(const IByteBuffer* json, boolean showStatistics = true)
  public static GEOObject parseJSON(IByteBuffer json, boolean showStatistics)
  {
	return parseJSON(json.getAsString(), showStatistics);
  }

  public static GEOObject parseBSON(IByteBuffer bson)
  {
	  return parseBSON(bson, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static GEOObject* parseBSON(const IByteBuffer* bson, boolean showStatistics = true)
  public static GEOObject parseBSON(IByteBuffer bson, boolean showStatistics)
  {
	GEOJSONParser parser = new GEOJSONParser("", bson);
	return parser.pvtParse(showStatistics);
  }

  public static GEOObject parse(JSONObject jsonObject)
  {
	  return parse(jsonObject, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static GEOObject* parse(const JSONObject* jsonObject, boolean showStatistics = true)
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
