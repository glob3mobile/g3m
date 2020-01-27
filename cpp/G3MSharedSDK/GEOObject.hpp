//
//  GEOObject.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

#ifndef __G3MiOSSDK__GEOObject__
#define __G3MiOSSDK__GEOObject__

#include "VectorStreamingRenderer.hpp"

class GEORasterSymbolizer;
class ICanvas;
class GEORasterProjection;
class GEOSymbolizer;
class MeshRenderer;
class ShapesRenderer;
class MarksRenderer;
class GEOVectorLayer;


class GEOObject {
protected:
  GEOObject();
  
public:
  virtual ~GEOObject();
  
  virtual void rasterize(const GEORasterSymbolizer* symbolizer,
                         ICanvas* canvas,
                         const GEORasterProjection* projection,
                         int tileLevel) const = 0;
  
  virtual void symbolize(const G3MRenderContext* rc,
                         const GEOSymbolizer*    symbolizer,
                         MeshRenderer*           meshRenderer,
                         ShapesRenderer*         shapesRenderer,
                         MarksRenderer*          marksRenderer,
                         GEOVectorLayer*         geoVectorLayer) const = 0 ;
  
  virtual int symbolize(const VectorStreamingRenderer::VectorSet* vectorSet,
                        const VectorStreamingRenderer::Node*      node) const = 0;
  
};

#endif
