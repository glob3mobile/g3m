//
//  GEOObject.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

#ifndef __G3MiOSSDK__GEOObject__
#define __G3MiOSSDK__GEOObject__

class G3MRenderContext;
class GEOSymbolizer;
class MeshRenderer;
class ShapesRenderer;
class MarksRenderer;
class GEOTileRasterizer;

#include "Disposable.hpp"


class GEOObject : public Disposable {
public:
  virtual ~GEOObject() {
    JAVA_POST_DISPOSE
  }

  virtual void symbolize(const G3MRenderContext* rc,
                         const GEOSymbolizer*    symbolizer,
                         MeshRenderer*           meshRenderer,
                         ShapesRenderer*         shapesRenderer,
                         MarksRenderer*          marksRenderer,
                         GEOTileRasterizer*      geoTileRasterizer) const = 0 ;
};

#endif
