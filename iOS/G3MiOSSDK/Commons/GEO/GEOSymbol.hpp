//
//  GEOSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

#ifndef __G3MiOSSDK__GEOSymbol__
#define __G3MiOSSDK__GEOSymbol__

#include <vector>

class G3MRenderContext;
class GEOSymbolizer;
class MeshRenderer;
class ShapesRenderer;
class MarksRenderer;
class GEOTileRasterizer;


class GEOSymbol {
public:
  virtual ~GEOSymbol() {
  }

  virtual bool symbolize(const G3MRenderContext* rc,
                         const GEOSymbolizer*    symbolizer,
                         MeshRenderer*           meshRenderer,
                         ShapesRenderer*         shapesRenderer,
                         MarksRenderer*          marksRenderer,
                         GEOTileRasterizer*      geoTileRasterizer) const = 0 ;

};

#endif
