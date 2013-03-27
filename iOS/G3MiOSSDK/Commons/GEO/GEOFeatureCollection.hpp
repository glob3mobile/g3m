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

class GEOFeatureCollection : public GEOObject {
private:
  std::vector<GEOFeature*> _features;

public:
  void addFeature(GEOFeature* feature);

  virtual ~GEOFeatureCollection();

  void render(const G3MRenderContext* rc,
              const GLState& parentState,
              const GEOSymbolizer* symbolizer);

};

#endif
