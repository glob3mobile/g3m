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
//class GEOLineStringGeometry;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOMultiLineStringGeometry;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;

public class GEOJSONParser
{
  private final String _json;

  private GEOJSONParser(String json)
  {
	  _json = json;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOObject* pvtParse() const
  private GEOObject pvtParse()
  {
	JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(_json);
  
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
  
	return result;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOObject* toGEO(const JSONObject* jsonObject) const
  private GEOObject toGEO(JSONObject jsonObject)
  {
	int __GEO_AT_WORK;
  
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOFeatureCollection* createFeaturesCollection(const JSONObject* jsonObject) const
  private GEOFeatureCollection createFeaturesCollection(JSONObject jsonObject)
  {
	GEOFeatureCollection geo = new GEOFeatureCollection();
  
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
		  geo.addFeature(feature);
		}
	  }
	}
  
	return geo;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOFeature* createFeature(const JSONObject* jsonObject) const
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
	 "MultiPoint"
	 "Polygon"
	 "MultiPolygon"
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
	else
	{
	  ILogger.instance().logError("Unknown geometry type \"%s\"", type);
	}
  
	return geo;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOLineStringGeometry* createLineStringGeometry(const JSONObject* jsonObject) const
  private GEOLineStringGeometry createLineStringGeometry(JSONObject jsonObject)
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
  
	GEOLineStringGeometry geo = null;
  
	final int dimensions = jsCoordinates.getAsArray(0).size();
	if (dimensions == 2)
	{
	  java.util.ArrayList<Geodetic2D> coordinates = create2DCoordinates(jsCoordinates);
	  if (coordinates != null)
	  {
		geo = new GEO2DLineStringGeometry(coordinates);
	  }
	}
	/*
	 else if (dimensions >= 3) {
	 geo = new GEO3DLineStringGeometry(coordinates);
	 }
	 */
	else
	{
	  ILogger.instance().logError("Invalid coordinates dimensions=%d", dimensions);
	  return null;
	}
  
	return geo;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOMultiLineStringGeometry* createMultiLineStringGeometry(const JSONObject* jsonObject) const
  private GEOMultiLineStringGeometry createMultiLineStringGeometry(JSONObject jsonObject)
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
  
	GEOMultiLineStringGeometry geo = null;
  
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
		}
	  }
  
	  geo = new GEO2DMultiLineStringGeometry(coordinatesArray);
	}
	/*
	 else if (dimensions >= 3) {
	 geo = new GEO3DLineStringGeometry(coordinates);
	 }
	 */
	else
	{
	  ILogger.instance().logError("Invalid coordinates dimensions=%d", dimensions);
	  return null;
	}
  
	return geo;
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Geodetic2D*>* create2DCoordinates(const JSONArray* jsCoordinates) const
  private java.util.ArrayList<Geodetic2D> create2DCoordinates(JSONArray jsCoordinates)
  {
	//    std::vector<Geodetic2D*>* coordinates = new std::vector<Geodetic2D*>();
	//    for (int i = 0; i < coordinatesCount; i++) {
	//      const JSONArray* jsCoordinate = jsCoordinates->getAsArray(i);
	//
	//      const double latitudeDegrees  = jsCoordinate->getAsNumber(1, 0.0);
	//      const double longitudeDegrees = jsCoordinate->getAsNumber(0, 0.0);
	//
	//      Geodetic2D* coordinate = new Geodetic2D(Angle::fromDegrees(latitudeDegrees),
	//                                              Angle::fromDegrees(longitudeDegrees));
	//      coordinates->push_back( coordinate );
	//    }
  
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
	}
  
	return coordinates;
  }


  public static GEOObject parse(String json)
  {
	GEOJSONParser parser = new GEOJSONParser(json);
	return parser.pvtParse();
  }

  public static GEOObject parse(IByteBuffer json)
  {
	return parse(json.getAsString());
  }

}