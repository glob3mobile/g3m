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

GEOObject* GEOJSONParser::parseJSON(const IByteBuffer* json,
                                    bool showStatistics) {
  return parseJSON(json->getAsString(), showStatistics);
}

GEOObject* GEOJSONParser::parseJSON(const std::string& json,
                                    bool showStatistics) {
  GEOJSONParser parser(json, NULL);
  return parser.pvtParse(showStatistics);
}

GEOObject* GEOJSONParser::parseBSON(const IByteBuffer* bson,
                                    bool showStatistics) {
  GEOJSONParser parser("", bson);
  return parser.pvtParse(showStatistics);
}

GEOObject* GEOJSONParser::parse(const JSONObject* jsonObject,
                                bool showStatistics) {
  GEOJSONParser parser("", NULL);
  GEOObject* result = parser.toGEO(jsonObject);
  if (showStatistics) {
    parser.showStatisticsToLogger();
  }
  return result;
}

void GEOJSONParser::showStatisticsToLogger() const {
  IStringBuilder* sb = IStringBuilder::newStringBuilder();

  sb->addString("GEOJSONParser Statistics:");

  if (_featuresCollectionCount > 0) {
    sb->addString(" FeaturesCollection=");
    sb->addLong(_featuresCollectionCount);
  }

  if (_featuresCount > 0) {
    sb->addString(" Features=");
    sb->addLong(_featuresCount);
  }

  if (_coordinates2DCount > 0) {
    sb->addString(" Coordinates=");
    sb->addLong(_coordinates2DCount);
  }

  if (_points2DCount > 0) {
    sb->addString(" Points=");
    sb->addLong(_points2DCount);
  }

  if (_lineStrings2DCount > 0) {
    sb->addString(" LineStrings=");
    sb->addLong(_lineStrings2DCount);
  }

  if (_multiLineStrings2DCount > 0) {
    sb->addString(" MultiLineStrings=");
    sb->addLong(_multiLineStrings2DCount);
    if (_lineStringsInMultiLineString2DCount > 0) {
      sb->addString(" (LineStrings=");
      sb->addLong(_lineStringsInMultiLineString2DCount);
      sb->addString(")");
    }
  }

  if (_polygon2DCount > 0) {
    sb->addString(" Polygons=");
    sb->addLong(_polygon2DCount);
    if (_lineStringsInMultiLineString2DCount > 0) {
      sb->addString(" (Holes=");
      sb->addLong(_holesLineStringsInPolygon2DCount);
      sb->addString(")");
    }
  }

  if (_multiPolygon2DCount > 0) {
    sb->addString(" MultiPolygons=");
    sb->addLong(_multiPolygon2DCount);
  }

  ILogger::instance()->logInfo( sb->getString() );

  delete sb;
}

GEOObject* GEOJSONParser::pvtParse(bool showStatistics) const {
  GEOObject* result = NULL;

  const JSONBaseObject* jsonBaseObject = (_bson == NULL) ? IJSONParser::instance()->parse(_json) : BSONParser::parse(_bson);

  if (jsonBaseObject != NULL) {
    const JSONObject* jsonObject = jsonBaseObject->asObject();
    if (jsonObject != NULL) {
      result = toGEO(jsonObject);
    }
    else {
      ILogger::instance()->logError("Root object for GEOJSON has to be a JSONObject");
    }

    delete jsonBaseObject;

    if (showStatistics) {
      showStatisticsToLogger();
    }
  }

  return result;
}

std::vector<Geodetic2D*>* GEOJSONParser::create2DCoordinates(const JSONArray* jsCoordinates) const {
  if (jsCoordinates == NULL) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is not present");
    return NULL;
  }

  const size_t coordinatesCount = jsCoordinates->size();
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

  const size_t dimensions = jsCoordinates->size();
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


std::vector<Geodetic2D*>* GEOJSONParser::createFlat2DCoordinates(const JSONArray* jsCoordinates) const {
  if (jsCoordinates == NULL) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is not present");
    return NULL;
  }

  const size_t coordinatesCount = jsCoordinates->size();
  if (coordinatesCount == 0) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is empty");
    return NULL;
  }

  std::vector<Geodetic2D*>* coordinates = new std::vector<Geodetic2D*>();
  for (int i = 0; i < coordinatesCount; i += 2) {
    const double longitudeDegrees = jsCoordinates->getAsNumber(i + 0, 0.0);
    const double latitudeDegrees  = jsCoordinates->getAsNumber(i + 1, 0.0);

    Geodetic2D* coordinate = new Geodetic2D(Angle::fromDegrees(latitudeDegrees),
                                            Angle::fromDegrees(longitudeDegrees));
    coordinates->push_back( coordinate );
    _coordinates2DCount++;
  }

  return coordinates;
}


GEOGeometry* GEOJSONParser::createLineStringGeometry(const JSONObject* jsonObject) const {

  const JSONArray* jsCoordinates = jsonObject->getAsArray("coordinates");
  if (jsCoordinates == NULL) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is not present");
    return NULL;
  }

  const size_t coordinatesCount = jsCoordinates->size();
  if (coordinatesCount == 0) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is empty");
    return NULL;
  }

  GEOGeometry* geo = NULL;

  const JSONArray* jsFirstCoordinate = jsCoordinates->getAsArray(0);

  if (jsFirstCoordinate == NULL) {
    if (jsCoordinates->getAsNumber(0) != NULL) {
      // assumes flat format: [ lon, lat, lon, lat ... ]
      std::vector<Geodetic2D*>* coordinates = createFlat2DCoordinates(jsCoordinates);
      if (coordinates != NULL) {
        geo = new GEO2DLineStringGeometry(coordinates);
        _lineStrings2DCount++;
      }
    }
  }
  else {
    const size_t dimensions = jsFirstCoordinate->size();
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
  }

  return geo;
}

GEOGeometry* GEOJSONParser::createMultiLineStringGeometry(const JSONObject* jsonObject) const {

  const JSONArray* jsCoordinatesArray = jsonObject->getAsArray("coordinates");
  if (jsCoordinatesArray == NULL) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is not present");
    return NULL;
  }

  const size_t coordinatesArrayCount = jsCoordinatesArray->size();
  if (coordinatesArrayCount == 0) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is empty");
    return NULL;
  }

  const JSONArray* jsFirstCoordinates = jsCoordinatesArray->getAsArray(0);
  if (jsFirstCoordinates == NULL) {
    ILogger::instance()->logError("Invalid format for first \"coordinates\" element");
    return NULL;
  }
  const size_t firstCoordinatesCount = jsFirstCoordinates->size();
  if (firstCoordinatesCount == 0) {
    ILogger::instance()->logError("Invalid format for first \"coordinates\" element");
    return NULL;
  }

  GEOGeometry* geo = NULL;

  const size_t dimensions = jsFirstCoordinates->getAsArray(0)->size();
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
  const size_t coordinatesArrayCount = jsCoordinatesArray->size();
  if (coordinatesArrayCount == 0) {
    ILogger::instance()->logError("Mandatory \"coordinates\" attribute is empty");
    return NULL;
  }

  const JSONArray* jsFirstCoordinates = jsCoordinatesArray->getAsArray(0);
  if (jsFirstCoordinates == NULL) {
    ILogger::instance()->logError("Invalid format for first \"coordinates\" element");
    return NULL;
  }
  const size_t firstCoordinatesCount = jsFirstCoordinates->size();
  if (firstCoordinatesCount == 0) {
    ILogger::instance()->logError("Invalid format for first \"coordinates\" element");
    return NULL;
  }

  const JSONArray* jsFirstCoordinate = jsFirstCoordinates->getAsArray(0);
  if (jsFirstCoordinate == NULL) {
    if (jsFirstCoordinates->getAsNumber(0) != NULL) {
      // assumes flat format: [ lon, lat, lon, lat ... ]
      const JSONArray* jsCoordinates = jsCoordinatesArray->getAsArray(0);
      std::vector<Geodetic2D*>* coordinates = createFlat2DCoordinates(jsCoordinates);

      std::vector<std::vector<Geodetic2D*>*>* holesCoordinatesArray = new std::vector<std::vector<Geodetic2D*>*>();
      for (int i = 1; i < coordinatesArrayCount; i++) {
        const JSONArray* jsHoleCoordinates = jsCoordinatesArray->getAsArray(i);
        std::vector<Geodetic2D*>* holeCoordinates = createFlat2DCoordinates(jsHoleCoordinates);
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
  }
  else {
    const size_t dimensions = jsFirstCoordinate->size();
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
  }

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

  const size_t polygonsCoordinatesArrayCount = jsPolygonsCoordinatesArray->size();
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
    const size_t featuresCount = jsFeatures->size();
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
