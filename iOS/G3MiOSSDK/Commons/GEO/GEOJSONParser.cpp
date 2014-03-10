//
//  GEOJSONParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

#include "GEOJSONParser.hpp"

#include "IByteBuffer.hpp"
#include "IJSONParser.hpp"
#include "JSONObject.hpp"
#include "JSONArray.hpp"

#include "GEOFeatureCollection.hpp"
#include "GEOFeature.hpp"
#include "GEOGeometry.hpp"
#include "GEO2DLineStringGeometry.hpp"
#include "GEO2DMultiLineStringGeometry.hpp"
#include "GEO2DPointGeometry.hpp"
#include "GEO2DPolygonGeometry.hpp"
#include "Geodetic2D.hpp"
#include "GEO2DPolygonData.hpp"
#include "GEO2DMultiPolygonGeometry.hpp"
#include "BSONParser.hpp"

GEOObject* GEOJSONParser::parseJSON(const IByteBuffer* json) {
  return parseJSON(json->getAsString());
}

GEOObject* GEOJSONParser::parseJSON(const std::string& json) {
  GEOJSONParser parser(json, NULL);
  return parser.pvtParse();
}

GEOObject* GEOJSONParser::parseBSON(const IByteBuffer* bson) {
  GEOJSONParser parser("", bson);
  return parser.pvtParse();
}

void GEOJSONParser::showStatistics() const {
  ILogger::instance()->logInfo("GEOJSONParser Statistics: Coordinates2D=%d, Points2D=%d, LineStrings2D=%d, MultiLineStrings2D=%d (LineStrings2D=%d), Polygons2D=%d (Holes=%d), MultiPolygons=%d, features=%d, featuresCollection=%d",
                               _coordinates2DCount,
                               _points2DCount,
                               _lineStrings2DCount,
                               _multiLineStrings2DCount,
                               _lineStringsInMultiLineString2DCount,
                               _polygon2DCount,
                               _holesLineStringsInPolygon2DCount,
                               _multiPolygon2DCount,
                               _featuresCount,
                               _featuresCollectionCount);
}

GEOObject* GEOJSONParser::pvtParse() const {
  const JSONBaseObject* jsonBaseObject = (_bson == NULL) ? IJSONParser::instance()->parse(_json) : BSONParser::parse(_bson);

  GEOObject* result = NULL;

  const JSONObject* jsonObject = jsonBaseObject->asObject();
  if (jsonObject != NULL) {
    result = toGEO(jsonObject);
  }
  else {
    ILogger::instance()->logError("Root object for GEOJSON has to be a JSONObject");
  }

  delete jsonBaseObject;

  showStatistics();

  return result;
}

std::vector<Geodetic2D*>* GEOJSONParser::create2DCoordinates(const JSONArray* jsCoordinates) const {
  if (jsCoordinates == NULL) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is not present");
    return NULL;
  }

  const int coordinatesCount = jsCoordinates->size();
  if (coordinatesCount == 0) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is empty");
    return NULL;
  }

  std::vector<Geodetic2D*>* coordinates = new std::vector<Geodetic2D*>();
  for (int i = 0; i < coordinatesCount; i++) {
    const JSONArray* jsCoordinate = jsCoordinates->getAsArray(i);

    const double latitudeDegrees  = jsCoordinate->getAsNumber(1, 0.0);
    const double longitudeDegrees = jsCoordinate->getAsNumber(0, 0.0);

    Geodetic2D* coordinate = new Geodetic2D(Angle::fromDegrees(latitudeDegrees),
                                            Angle::fromDegrees(longitudeDegrees));
    coordinates->push_back( coordinate );
    _coordinates2DCount++;
  }

  return coordinates;
}

GEOGeometry* GEOJSONParser::createPointGeometry(const JSONObject* jsonObject) const {
  const JSONArray* jsCoordinates = jsonObject->getAsArray("coordinates");
  if (jsCoordinates == NULL) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is not present");
    return NULL;
  }

  GEOGeometry* geo = NULL;
  
  const int dimensions = jsCoordinates->size();
  if (dimensions == 2) {
    const double latitudeDegrees  = jsCoordinates->getAsNumber(1, 0.0);
    const double longitudeDegrees = jsCoordinates->getAsNumber(0, 0.0);

    _points2DCount++;

    geo = new GEO2DPointGeometry( Geodetic2D::fromDegrees(latitudeDegrees, longitudeDegrees) );
  }
//  else if (dimensions == 3) {
//    const double latitudeDegrees  = jsCoordinates->getAsNumber(1, 0.0);
//    const double longitudeDegrees = jsCoordinates->getAsNumber(0, 0.0);
//    const double height           = jsCoordinates->getAsNumber(2, 0.0);
//  }
  else {
    ILogger::instance()->logError("Mandatory \"coordinates\" dimensions not supported %d", dimensions);
  }

  return geo;
}

GEOGeometry* GEOJSONParser::createLineStringGeometry(const JSONObject* jsonObject) const {

  const JSONArray* jsCoordinates = jsonObject->getAsArray("coordinates");
  if (jsCoordinates == NULL) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is not present");
    return NULL;
  }

  const int coordinatesCount = jsCoordinates->size();
  if (coordinatesCount == 0) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is empty");
    return NULL;
  }

  GEOGeometry* geo = NULL;

  const int dimensions = jsCoordinates->getAsArray(0)->size();
  if (dimensions == 2) {
    std::vector<Geodetic2D*>* coordinates = create2DCoordinates(jsCoordinates);
    if (coordinates != NULL) {
      geo = new GEO2DLineStringGeometry(coordinates);
      _lineStrings2DCount++;
    }
  }
  else {
    ILogger::instance()->logError("Invalid coordinates dimensions=%d", dimensions);
    return NULL;
  }

  return geo;
}

GEOGeometry* GEOJSONParser::createMultiLineStringGeometry(const JSONObject* jsonObject) const {

  const JSONArray* jsCoordinatesArray = jsonObject->getAsArray("coordinates");
  if (jsCoordinatesArray == NULL) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is not present");
    return NULL;
  }

  const int coordinatesArrayCount = jsCoordinatesArray->size();
  if (coordinatesArrayCount == 0) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is empty");
    return NULL;
  }

  const JSONArray* jsFirstCoordinates = jsCoordinatesArray->getAsArray(0);
  if (jsFirstCoordinates == NULL) {
    ILogger::instance()->logError("Invalid format for first \"coordinates\" element");
    return NULL;
  }
  const int firstCoordinatesCount = jsFirstCoordinates->size();
  if (firstCoordinatesCount == 0) {
    ILogger::instance()->logError("Invalid format for first \"coordinates\" element");
    return NULL;
  }

  GEOGeometry* geo = NULL;

  const int dimensions = jsFirstCoordinates->getAsArray(0)->size();
  if (dimensions == 2) {
    std::vector<std::vector<Geodetic2D*>*>* coordinatesArray = new std::vector<std::vector<Geodetic2D*>*>();

    for (int i = 0; i < coordinatesArrayCount; i++) {
      const JSONArray* jsCoordinates = jsCoordinatesArray->getAsArray(i);
      std::vector<Geodetic2D*>* coordinates = create2DCoordinates(jsCoordinates);
      if (coordinates != NULL) {
        coordinatesArray->push_back( coordinates );
        _lineStringsInMultiLineString2DCount++;
      }
    }

    geo = new GEO2DMultiLineStringGeometry(coordinatesArray);
    _multiLineStrings2DCount++;
  }
  else {
    ILogger::instance()->logError("Invalid coordinates dimensions=%d", dimensions);
    return NULL;
  }

  return geo;
}


GEO2DPolygonData* GEOJSONParser::parsePolygon2DData(const JSONArray* jsCoordinatesArray) const {
  const int coordinatesArrayCount = jsCoordinatesArray->size();
  if (coordinatesArrayCount == 0) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is empty");
    return NULL;
  }

  const JSONArray* jsFirstCoordinates = jsCoordinatesArray->getAsArray(0);
  if (jsFirstCoordinates == NULL) {
    ILogger::instance()->logError("Invalid format for first \"coordinates\" element");
    return NULL;
  }
  const int firstCoordinatesCount = jsFirstCoordinates->size();
  if (firstCoordinatesCount == 0) {
    ILogger::instance()->logError("Invalid format for first \"coordinates\" element");
    return NULL;
  }

  const int dimensions = jsFirstCoordinates->getAsArray(0)->size();
  if (dimensions == 2) {
    const JSONArray* jsCoordinates = jsCoordinatesArray->getAsArray(0);
    std::vector<Geodetic2D*>* coordinates = create2DCoordinates(jsCoordinates);

    std::vector<std::vector<Geodetic2D*>*>* holesCoordinatesArray = new std::vector<std::vector<Geodetic2D*>*>();
    for (int i = 1; i < coordinatesArrayCount; i++) {
      const JSONArray* jsHoleCoordinates = jsCoordinatesArray->getAsArray(i);
      std::vector<Geodetic2D*>* holeCoordinates = create2DCoordinates(jsHoleCoordinates);
      if (holeCoordinates != NULL) {
        holesCoordinatesArray->push_back( holeCoordinates );
        _holesLineStringsInPolygon2DCount++;
      }
    }

    if (holesCoordinatesArray->size() == 0) {
      delete holesCoordinatesArray;
      holesCoordinatesArray = NULL;
    }

    _polygon2DCount++;
    return new GEO2DPolygonData(coordinates, holesCoordinatesArray);
  }

  ILogger::instance()->logError("Invalid coordinates dimensions=%d", dimensions);
  return NULL;
}


GEO2DPolygonData* GEOJSONParser::parsePolygon2DData(const JSONObject* jsonObject) const {
  const JSONArray* jsCoordinatesArray = jsonObject->getAsArray("coordinates");
  if (jsCoordinatesArray == NULL) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is not present");
    return NULL;
  }

  return parsePolygon2DData(jsCoordinatesArray);
}



GEOGeometry* GEOJSONParser::createPolygonGeometry(const JSONObject* jsonObject) const {
  GEO2DPolygonData* polygonData = parsePolygon2DData(jsonObject);

  return (polygonData == NULL) ? NULL : new GEO2DPolygonGeometry(polygonData);
}

GEOGeometry* GEOJSONParser::createMultiPolygonGeometry(const JSONObject* jsonObject) const {
  const JSONArray* jsPolygonsCoordinatesArray = jsonObject->getAsArray("coordinates");
  if (jsPolygonsCoordinatesArray == NULL) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is not present");
    return NULL;
  }

  const int polygonsCoordinatesArrayCount = jsPolygonsCoordinatesArray->size();
  if (polygonsCoordinatesArrayCount == 0) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is empty");
    return NULL;
  }

  std::vector<GEO2DPolygonData*>* polygonsData = new std::vector<GEO2DPolygonData*>();
  for (int i = 0; i < polygonsCoordinatesArrayCount; i++) {
    const JSONArray* jsCoordinatesArray = jsPolygonsCoordinatesArray->getAsArray(i);

    polygonsData->push_back( parsePolygon2DData(jsCoordinatesArray) );
  }

  _multiPolygon2DCount++;

  return new GEO2DMultiPolygonGeometry(polygonsData);
}

GEOGeometry* GEOJSONParser::createGeometry(const JSONObject* jsonObject) const {
  if (jsonObject == NULL) {
    return NULL;
  }

  const std::string type = jsonObject->getAsString("type", "");

  GEOGeometry* geo = NULL;

  /*
   "LineString"
   "MultiLineString"
   "Point"
   "Polygon"

   "MultiPolygon"

   "MultiPoint"
   "GeometryCollection"
   */

  if (type.compare("LineString") == 0) {
    geo = createLineStringGeometry(jsonObject);
  }
  else if (type.compare("MultiLineString") == 0) {
    geo = createMultiLineStringGeometry(jsonObject);
  }
  else if (type.compare("Point") == 0) {
    geo = createPointGeometry(jsonObject);
  }
  else if (type.compare("Polygon") == 0) {
    geo = createPolygonGeometry(jsonObject);
  }
  else if (type.compare("MultiPolygon") == 0) {
    geo = createMultiPolygonGeometry(jsonObject);
  }
  else {
    ILogger::instance()->logError("Unknown geometry type \"%s\"", type.c_str());
  }

  return geo;
}

GEOFeature* GEOJSONParser::createFeature(const JSONObject* jsonObject) const {

  const JSONBaseObject* jsId = JSONBaseObject::deepCopy( jsonObject->get("id") );

  const JSONObject* jsGeometry = jsonObject->getAsObject("geometry");
  GEOGeometry* geometry = createGeometry(jsGeometry);

#ifdef C_CODE
  const JSONObject* jsProperties = jsonObject->getAsObject("properties");
#endif
#ifdef JAVA_CODE
  JSONObject jsProperties = jsonObject.getAsObject("properties");
#endif
  if (jsProperties != NULL) {
    jsProperties = jsProperties->deepCopy();
  }

  _featuresCount++;
  return new GEOFeature(jsId, geometry, jsProperties);
}

GEOFeatureCollection* GEOJSONParser::createFeaturesCollection(const JSONObject* jsonObject) const {
  std::vector<GEOFeature*> features;

  const JSONArray* jsFeatures = jsonObject->getAsArray("features");
  if (jsFeatures != NULL) {
    const int featuresCount = jsFeatures->size();
    for (int i = 0; i < featuresCount; i++) {
      const JSONObject* jsFeature = jsFeatures->getAsObject(i);
      if (jsFeature != NULL) {
        GEOFeature* feature = createFeature(jsFeature);
        features.push_back(feature);
      }
    }
  }

  _featuresCollectionCount++;
  return new GEOFeatureCollection(features);
}

GEOObject* GEOJSONParser::toGEO(const JSONObject* jsonObject) const {
  const std::string type = jsonObject->getAsString("type", "");
  if (type.compare("FeatureCollection") == 0) {
    return createFeaturesCollection(jsonObject);
  }
  else if (type.compare("Feature") == 0) {
    return createFeature(jsonObject);
  }
  else {
    ILogger::instance()->logError("GEOJSON: Unkown type \"%s\"", type.c_str());
    return NULL;
  }
}
