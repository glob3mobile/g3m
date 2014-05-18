//
//  GEOFeatureCollection.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

#ifndef __G3MiOSSDK__GEOFeatureCollection__
#define __G3MiOSSDK__GEOFeatureCollection__

#include "GEOObject.hpp"

#include <vector>

class GEOFeature;
class GPUProgramState;
class GLGlobalState;
class GPUProgramState;
class GEOSymbolizer;

class GEOFeatureCollection : public GEOObject {
private:
  const std::vector<GEOFeature*> _features;

public:
  GEOFeatureCollection(const std::vector<GEOFeature*>& features) :
  _features(features)
  {
  }

  virtual ~GEOFeatureCollection();

  void symbolize(const G3MRenderContext* rc,
                 const GEOSymbolizer*    symbolizer,
                 MeshRenderer*           meshRenderer,
                 ShapesRenderer*         shapesRenderer,
                 MarksRenderer*          marksRenderer,
                 GEOTileRasterizer*      geoTileRasterizer) const;

  GEOFeature* get(int i) const{ return _features[i];}
  int size() const{ return _features.size();}

  void rasterize(const GEORasterSymbolizer* symbolizer,
                 ICanvas* canvas,
                 const GEORasterProjection* projection,
                 int tileLevel) const;

  long long getCoordinatesCount() const;

  const GEOFeatureCollection* deepCopy() const;

};

#endif
