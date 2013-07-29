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
  std::vector<GEOFeature*> _features;

public:
  GEOFeatureCollection(std::vector<GEOFeature*>& features) :
  _features(features)
  {

  }
  
//  void addFeature(GEOFeature* feature);

  virtual ~GEOFeatureCollection();

//  void render(const G3MRenderContext* rc,
//              const GLGlobalState& parentState, const GPUProgramState* parentProgramState,
//              const GEOSymbolizer* symbolizer);
  
  void symbolize(const G3MRenderContext* rc, const GEOSymbolizationContext& sc) const;

};

#endif
