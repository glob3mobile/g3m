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

class G3MContext;
class IByteBuffer;

class JSONObject;
class JSONArray;

class GEOObject;
class GEOFeatureCollection;
class GEOFeature;
class GEOGeometry;
class GEOLineStringGeometry;
class GEOMultiLineStringGeometry;

#include <vector>
class Geodetic2D;

class GEOJSONParser {
private:
  const std::string _json;
  const G3MContext* _context;

  GEOJSONParser(const G3MContext* context,
                const std::string& json) :
  _json(json),
  _context(context)
  {

  }

  GEOObject* pvtParse() const;

  GEOObject* toGEO(const JSONObject* jsonObject) const;

  GEOFeatureCollection* createFeaturesCollection(const JSONObject* jsonObject) const;
  GEOFeature*           createFeature(const JSONObject* jsonObject) const;

  GEOGeometry*                createGeometry(const JSONObject* jsonObject) const;
  GEOLineStringGeometry*      createLineStringGeometry(const JSONObject* jsonObject) const;
  GEOMultiLineStringGeometry* createMultiLineStringGeometry(const JSONObject* jsonObject) const;


  std::vector<Geodetic2D*>* create2DCoordinates(const JSONArray* jsCoordinates) const;

public:

  static GEOObject* parse(const G3MContext* context,
                          const std::string& json);

  static GEOObject* parse(const G3MContext* context,
                          const IByteBuffer* json);
  
};

#endif
