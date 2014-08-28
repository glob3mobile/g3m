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
#include "TileRasterizerContext.hpp"


void GEOTileRasterizer::initialize(const G3MContext* context) {
}

void GEOTileRasterizer::clear() {
  _quadTree.clear();
  notifyChanges();
}

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
  const GEOTileRasterizer*     _geoTileRasterizer;
#ifdef C_CODE
  mutable const IImage*        _originalImage;
#endif
#ifdef JAVA_CODE
  private IImage _originalImage;
#endif
  const TileRasterizerContext* _trc;
  IImageListener*              _listener;
  bool                         _autodelete;

  mutable ICanvas*             _canvas;
  mutable GEORasterProjection* _projection;

public:
  GEOTileRasterizer_QuadTreeVisitor(const GEOTileRasterizer* geoTileRasterizer,
                                    const IImage* originalImage,
                                    const TileRasterizerContext* trc,
                                    IImageListener* listener,
                                    bool autodelete) :
  _geoTileRasterizer(geoTileRasterizer),
  _originalImage(originalImage),
  _trc(trc),
  _listener(listener),
  _autodelete(autodelete),
  _canvas(NULL),
  _projection(NULL)
  {
  }

  bool visitElement(const Sector&           sector,
                    const QuadTree_Content* content) const {
    GEORasterSymbol* symbol = (GEORasterSymbol*) content;

    const Tile* tile = _trc->_tile;

    if (_canvas == NULL) {
      const int width  = _originalImage->getWidth();
      const int height = _originalImage->getHeight();

      _canvas = _geoTileRasterizer->getCanvas(width, height);

      _canvas->drawImage(_originalImage, 0, 0, _originalImage->getWidth(), _originalImage->getHeight());

      _projection = new GEORasterProjection(tile->_sector,
                                            tile->_mercator,
                                            width, height);
    }

    symbol->rasterize(_canvas, _projection, tile->_level);

    return false;
  }

  void endVisit(bool aborted) const {
    if (_canvas == NULL) {
      _listener->imageCreated(_originalImage);
      if (_autodelete) {
        delete _listener;
      }
    }
    else {
      _canvas->createImage(_listener, _autodelete);

      delete _originalImage;
      _originalImage = NULL;

      delete _projection;
    }
  }
};

void GEOTileRasterizer::rawRasterize(const IImage* image,
                                     const TileRasterizerContext& trc,
                                     IImageListener* listener,
                                     bool autodelete) const {
  _quadTree.acceptVisitor(trc._tile->_sector,
                          GEOTileRasterizer_QuadTreeVisitor(this,
                                                            image,
                                                            &trc,
                                                            listener,
                                                            autodelete));
}
