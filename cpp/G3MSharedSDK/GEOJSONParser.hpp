//
//  GEOJSONParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

#ifndef __G3MiOSSDK__GEOJSONParser__
#define __G3MiOSSDK__GEOJSONParser__

#include <string>
#include <vector>

class IByteBuffer;
class JSONObject;
class JSONArray;
class GEOObject;
class GEOFeatureCollection;
class GEOFeature;
class GEOGeometry;
class Geodetic2D;
class Geodetic3D;
class GEO2DPolygonData;
class GEO3DPolygonData;


class GEOJSONParser {
private:
  const std::string  _json;
  const IByteBuffer* _bson;

  // statistics
  mutable int _points2DCount;
  mutable int _points3DCount;
  mutable int _coordinates2DCount;
  mutable int _coordinates3DCount;
  mutable int _lineStrings2DCount;
  mutable int _multiLineStrings2DCount;
  mutable int _lineStringsInMultiLineString2DCount;
  mutable int _featuresCount;
  mutable int _featuresCollectionCount;
  mutable int _polygon2DCount;
  mutable int _polygon3DCount;
  mutable int _holesLineStringsInPolygon2DCount;
  mutable int _holesLineStringsInPolygon3DCount;
  mutable int _multiPolygon2DCount;

  GEOJSONParser(const std::string& json,
                const IByteBuffer* bson) :
  _json(json),
  _bson(bson),
  _points2DCount(0),
  _points3DCount(0),
  _coordinates2DCount(0),
  _coordinates3DCount(0),
  _lineStrings2DCount(0),
  _multiLineStrings2DCount(0),
  _lineStringsInMultiLineString2DCount(0),
  _featuresCount(0),
  _featuresCollectionCount(0),
  _polygon2DCount(0),
  _polygon3DCount(0),
  _holesLineStringsInPolygon2DCount(0),
  _holesLineStringsInPolygon3DCount(0),
  _multiPolygon2DCount(0)
  {

  }

  GEOObject* pvtParse(bool showStatistics) const;

  GEOObject* toGEO(const JSONObject* jsonObject) const;

  GEOFeatureCollection* createFeaturesCollection(const JSONObject* jsonObject) const;
  GEOFeature*           createFeature(const JSONObject* jsonObject) const;

  GEOGeometry* createGeometry(const JSONObject* jsonObject) const;
  GEOGeometry* createLineStringGeometry(const JSONObject* jsonObject) const;
  GEOGeometry* createMultiLineStringGeometry(const JSONObject* jsonObject) const;
  GEOGeometry* createPointGeometry(const JSONObject* jsonObject) const;
  GEOGeometry* createPolygonGeometry(const JSONObject* jsonObject) const;
  GEOGeometry* createMultiPolygonGeometry(const JSONObject* jsonObject) const;

  GEO2DPolygonData* parsePolygon2DData(const JSONArray* jsCoordinatesArray) const;
  GEO3DPolygonData* parsePolygon3DData(const JSONArray* jsCoordinatesArray) const;

  std::vector<Geodetic2D*>* create2DCoordinates(const JSONArray* jsCoordinates) const;
  std::vector<Geodetic3D*>* create3DCoordinates(const JSONArray* jsCoordinates) const;

  void showStatisticsToLogger() const;

public:

  static GEOObject* parseJSON(const std::string& json,
                              bool showStatistics = true);
  static GEOObject* parseJSON(const IByteBuffer* json,
                              bool showStatistics = true);
  
  static GEOObject* parseBSON(const IByteBuffer* bson,
                              bool showStatistics = true);

  static GEOObject* parse(const JSONObject* jsonObject,
                          bool showStatistics = true);

};

#endif
