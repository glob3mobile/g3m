//
//  GEOGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#ifndef __G3MiOSSDK__GEOGeometry__
#define __G3MiOSSDK__GEOGeometry__

#include "GEOObject.hpp"

#include <vector>
class GEOSymbol;
class GEOFeature;

class GEOGeometry : public GEOObject {
private:
  mutable GEOFeature* _feature;

protected:
  virtual std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const = 0;

public:
  GEOGeometry() :
  _feature(NULL)
  {

  }

  virtual ~GEOGeometry() {
#ifdef JAVA_CODE
  super.dispose();
#endif
  }

  void setFeature(GEOFeature* feature) const;

  const GEOFeature* getFeature() const {
    return _feature;
  }

  void symbolize(const G3MRenderContext* rc,
                 const GEOSymbolizer*    symbolizer,
                 MeshRenderer*           meshRenderer,
                 ShapesRenderer*         shapesRenderer,
                 MarksRenderer*          marksRenderer,
                 GEOVectorLayer*         geoVectorLayer) const;

  virtual GEOGeometry* deepCopy() const = 0;

  virtual long long createFeatureMarks(const VectorStreamingRenderer::VectorSet* vectorSet,
                                       const VectorStreamingRenderer::Node*      node) const {
    return 0;
  }

};

#endif
