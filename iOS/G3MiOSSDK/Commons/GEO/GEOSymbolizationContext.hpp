//
//  GEOSymbolizationContext.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

#ifndef __G3MiOSSDK__GEOSymbolizationContext__
#define __G3MiOSSDK__GEOSymbolizationContext__

class GEOSymbolizer;
class MeshRenderer;
class MarksRenderer;
class ShapesRenderer;
class GEOTileRasterizer;

class GEOSymbolizationContext {
private:
  const GEOSymbolizer* _symbolizer;

  MeshRenderer*      _meshRenderer;
  ShapesRenderer*    _shapesRenderer;
  MarksRenderer*     _marksRenderer;
  GEOTileRasterizer* _geoTileRasterizer;

public:
  GEOSymbolizationContext(const GEOSymbolizer* symbolizer,
                          MeshRenderer*        meshRenderer,
                          ShapesRenderer*      shapesRenderer,
                          MarksRenderer*       marksRenderer,
                          GEOTileRasterizer*   geoTileRasterizer) :
  _symbolizer(symbolizer),
  _meshRenderer(meshRenderer),
  _shapesRenderer(shapesRenderer),
  _marksRenderer(marksRenderer),
  _geoTileRasterizer(geoTileRasterizer)
  {

  }

  const GEOSymbolizer* getSymbolizer() const {
    return _symbolizer;
  }

  MeshRenderer* getMeshRenderer() const {
    return _meshRenderer;
  }

  ShapesRenderer* getShapesRenderer() const {
    return _shapesRenderer;
  }

  MarksRenderer* getMarksRenderer() const {
    return _marksRenderer;
  }

  GEOTileRasterizer* getGEOTileRasterizer() const {
    return _geoTileRasterizer;
  }

};

#endif
