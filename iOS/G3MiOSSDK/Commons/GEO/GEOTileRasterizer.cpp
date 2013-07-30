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
  
public:
  GEOTileRasterizer_QuadTreeVisitor(ICanvas* canvas,
                                    const GEORasterProjection* projection) :
  _canvas(canvas),
  _projection(projection)
  {
  }

  bool visitElement(const Sector& sector,
                    const void*   element) const {
    GEORasterSymbol* symbol = (GEORasterSymbol*) element;

    symbol->rasterize(_canvas, _projection);

    return false;
  }

  void endVisit(bool aborted) const {
  }

};

void GEOTileRasterizer::rasterize(IImage* image,
                                  const Tile* tile,
                                  bool mercator,
                                  IImageListener* listener,
                                  bool autodelete) const {

  const int width  = image->getWidth();
  const int height = image->getHeight();

  GEORasterProjection* projection = new GEORasterProjection(tile->getSector(), mercator,
                                                            width, height);

  ICanvas* canvas = getCanvas(width, height);

  canvas->drawImage(image, 0, 0);

//  canvas->setFillColor(Color::yellow());

//  canvas->setLineColor(Color::white());
//  canvas->setLineWidth(1);
//  canvas->strokeRectangle(0, 0, width, height);


  _quadTree.acceptVisitor(tile->getSector(),
                          GEOTileRasterizer_QuadTreeVisitor(canvas, projection));

  canvas->createImage(listener, autodelete);

  delete image;

  delete projection;
}
