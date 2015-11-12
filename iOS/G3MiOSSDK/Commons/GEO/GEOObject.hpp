//
//  GEOObject.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

#ifndef __G3MiOSSDK__GEOObject__
#define __G3MiOSSDK__GEOObject__

class GEORasterSymbolizer;
class ICanvas;
class GEORasterProjection;
class G3MRenderContext;
class GEOSymbolizer;
class MeshRenderer;
class ShapesRenderer;
class MarksRenderer;
class GEOVectorLayer;

#include "VectorStreamingRenderer.hpp"


class GEOObject {
public:
  virtual ~GEOObject() {
  }

  virtual void rasterize(const GEORasterSymbolizer* symbolizer,
                         ICanvas* canvas,
                         const GEORasterProjection* projection,
                         int tileLevel) const = 0;

  virtual void symbolize(const G3MRenderContext* rc,
                         const GEOSymbolizer*    symbolizer,
                         MeshRenderer*           meshRenderer,
                         ShapesRenderer*         shapesRenderer,
                         MarksRenderer*          marksRenderer,
                         GEOVectorLayer*         geoVectorLayer
                         ) const = 0 ;

  virtual long long getCoordinatesCount() const = 0;

  virtual GEOObject* deepCopy() const = 0;

  virtual long long createFeatureMarks(const VectorStreamingRenderer::VectorSet* vectorSet,
                                       const VectorStreamingRenderer::Node*      node) const = 0;

};

#endif
