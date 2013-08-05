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
#include "GEOLineStringGeometry.hpp"
#include "GEO2DLineStringGeometry.hpp"
#include "GEOMultiLineStringGeometry.hpp"
#include "GEO2DMultiLineStringGeometry.hpp"
#include "GEO2DPointGeometry.hpp"

#include "Geodetic2D.hpp"

GEOObject* GEOJSONParser::parse(const IByteBuffer* json) {
  return parse(json->getAsString());
}

GEOObject* GEOJSONParser::parse(const std::string& json) {
  GEOJSONParser parser(json);
  return parser.pvtParse();
}

void GEOJSONParser::showStatistics() const {
  ILogger::instance()->logInfo("GEOJSONParser Statistics: Coordinates2D=%d, Points2D=%d, LineStrings2D=%d, MultiLineStrings2D=%d (LineStrings2D=%d), features=%d, featuresCollection=%d",
                               _coordinates2DCount,
                               _points2DCount,
                               _lineStrings2DCount,
                               _multiLineStrings2DCount,
                               _lineStringsInMultiLineString2DCount,
                               _featuresCount,
                               _featuresCollectionCount);
}

GEOObject* GEOJSONParser::pvtParse() const {
  const JSONBaseObject* jsonBaseObject =  IJSONParser::instance()->parse(_json);

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

GEOPointGeometry* GEOJSONParser::createPointGeometry(const JSONObject* jsonObject) const {
  const JSONArray* jsCoordinates = jsonObject->getAsArray("coordinates");
  if (jsCoordinates == NULL) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is not present");
    return NULL;
  }

  GEOPointGeometry* geo = NULL;
  
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

GEOLineStringGeometry* GEOJSONParser::createLineStringGeometry(const JSONObject* jsonObject) const {

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

  GEOLineStringGeometry* geo = NULL;

  const int dimensions = jsCoordinates->getAsArray(0)->size();
  if (dimensions == 2) {
    std::vector<Geodetic2D*>* coordinates = create2DCoordinates(jsCoordinates);
    if (coordinates != NULL) {
      geo = new GEO2DLineStringGeometry(coordinates);
      _lineStrings2DCount++;
    }
  }
  /*
   else if (dimensions >= 3) {
   geo = new GEO3DLineStringGeometry(coordinates);
   }
   */
  else {
    ILogger::instance()->logError("Invalid coordinates dimensions=%d", dimensions);
    return NULL;
  }

  return geo;
}

GEOMultiLineStringGeometry* GEOJSONParser::createMultiLineStringGeometry(const JSONObject* jsonObject) const {

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

  GEOMultiLineStringGeometry* geo = NULL;

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
  /*
   else if (dimensions >= 3) {
   geo = new GEO3DLineStringGeometry(coordinates);
   }
   */
  else {
    ILogger::instance()->logError("Invalid coordinates dimensions=%d", dimensions);
    return NULL;
  }

  return geo;
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

   "MultiPoint"
   "Polygon"
   "MultiPolygon"
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
