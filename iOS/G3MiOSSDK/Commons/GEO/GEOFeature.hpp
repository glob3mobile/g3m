//
//  GEOFeature.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

#ifndef __G3MiOSSDK__GEOFeature__
#define __G3MiOSSDK__GEOFeature__

#include "GEOObject.hpp"


class GEOGeometry;
class JSONBaseObject;
class JSONObject;

class GEOFeature : public GEOObject {
private:
  const JSONBaseObject* _id;
  GEOGeometry*          _geometry;
  const JSONObject*     _properties;

public:

  GEOFeature(const JSONBaseObject* id,
             GEOGeometry* geometry,
             const JSONObject* properties) :
  _id(id),
  _geometry(geometry),
  _properties(properties)
  {

  }

  ~GEOFeature();

};

#endif
