//
//  GEOTileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#include "GEOTileRasterizer.hpp"

#include "GEORasterSymbol.hpp"
#include "Tile.hpp"
#include "IImageListener.hpp"
#include "IImage.hpp"
#include "ICanvas.hpp"
#include "Color.hpp"
#include "GFont.hpp"

#include "IStringBuilder.hpp"
#include "GEORasterProjection.hpp"

void GEOTileRasterizer::addSymbol(const GEORasterSymbol* symbol) {
  const Sector* sector = symbol->getSector();

  if (sector == NULL) {
    //    ILogger::instance()->logError("Symbol %s has not sector, can't symbolize",
    //                                  symbol->description().c_str());
    delete symbol;
  }
  else {
    const bool added = _quadTree.add(*sector, symbol);
    if (added) {
      notifyChanges();
    }
    else {
      delete symbol;
    }
  }
}

class GEOTileRasterizer_QuadTreeVisitor : public QuadTreeVisitor {
private:
  ICanvas*                   _canvas;
  const GEORasterProjection* _projection;
  const int                  _tileLevel;

public:
  GEOTileRasterizer_QuadTreeVisitor(ICanvas* canvas,
                                    const GEORasterProjection* projection,
                                    int tileLevel) :
  _canvas(canvas),
  _projection(projection),
  _tileLevel(tileLevel)
  {
  }

  bool visitElement(const Sector& sector,
                    const void*   element) const {
    GEORasterSymbol* symbol = (GEORasterSymbol*) element;

    symbol->rasterize(_canvas, _projection, _tileLevel);

    return false;
  }

  void endVisit(bool aborted) const {
  }

};

void GEOTileRasterizer::rawRasterize(const IImage* image,
                                     const TileRasterizerContext& trc,
                                     IImageListener* listener,
                                     bool autodelete) const {
  const Tile*   tile     = trc._tile;
  const bool    mercator = trc._mercator;

  const int width  = image->getWidth();
  const int height = image->getHeight();

  GEORasterProjection* projection = new GEORasterProjection(tile->getSector(), mercator,
                                                            width, height);

  ICanvas* canvas = getCanvas(width, height);

  canvas->drawImage(image, 0, 0);

  _quadTree.acceptVisitor(tile->getSector(),
                          GEOTileRasterizer_QuadTreeVisitor(canvas, projection, tile->getLevel()));

  canvas->createImage(listener, autodelete);
  
  delete image;
  
  delete projection;
}
