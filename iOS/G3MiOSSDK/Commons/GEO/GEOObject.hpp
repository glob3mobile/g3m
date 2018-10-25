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
private:
#ifdef C_CODE
  mutable const Sector* _sector;
#endif
#ifdef JAVA_CODE
  private Sector _sector;
#endif

protected:
  virtual const Sector* calculateSector() const = 0;

  GEOObject() :
  _sector(NULL)
  {

  }

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
                         GEOVectorLayer*         geoVectorLayer
                         ) const = 0 ;

  virtual long long getCoordinatesCount() const = 0;

  virtual GEOObject* deepCopy() const = 0;

  virtual long long createFeatureMarks(const VectorStreamingRenderer::VectorSet* vectorSet,
                                       const VectorStreamingRenderer::Node*      node) const = 0;

  const Sector* getSector() const;

};

#endif
