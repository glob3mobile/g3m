//
//  GEOShapeSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

#ifndef __G3MiOSSDK__GEOShapeSymbol__
#define __G3MiOSSDK__GEOShapeSymbol__

#include "GEOSymbol.hpp"
class Shape;

class GEOShapeSymbol : public GEOSymbol {
private:
  mutable Shape* _shape;

public:
  GEOShapeSymbol(Shape* shape) :
  _shape(shape)
  {

  }

  ~GEOShapeSymbol();

  bool symbolize(const G3MRenderContext* rc,
                 const GEOSymbolizer*    symbolizer,
                 MeshRenderer*           meshRenderer,
                 ShapesRenderer*         shapesRenderer,
                 MarksRenderer*          marksRenderer,
                 GEOTileRasterizer*      geoTileRasterizer) const;

};

#endif
