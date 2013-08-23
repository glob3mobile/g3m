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
class GPUProgramState;
class GLGlobalState;
class GEOSymbolizer;

class GEOFeature : public GEOObject {
private:
  const JSONBaseObject* _id;
  GEOGeometry*          _geometry;
  const JSONObject*     _properties;

public:

  GEOFeature(const JSONBaseObject* id,
             GEOGeometry* geometry,
             const JSONObject* properties);

  ~GEOFeature();
  
  const JSONObject* getProperties() const {
    return _properties;
  }

  const GEOGeometry* getGeometry() const {
    return _geometry;
  }

  void symbolize(const G3MRenderContext* rc,
                 const GEOSymbolizer*    symbolizer,
                 MeshRenderer*           meshRenderer,
                 ShapesRenderer*         shapesRenderer,
                 MarksRenderer*          marksRenderer,
                 GEOTileRasterizer*      geoTileRasterizer) const;

};

#endif
