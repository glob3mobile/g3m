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

//class GEOTileRasterizer_QuadTreeVisitor : public QuadTreeVisitor {
//private:
//  ICanvas*                   _canvas;
//  const GEORasterProjection* _projection;
//  const int                  _tileLevel;
//
//public:
//  GEOTileRasterizer_QuadTreeVisitor(ICanvas* canvas,
//                                    const GEORasterProjection* projection,
//                                    int tileLevel) :
//  _canvas(canvas),
//  _projection(projection),
//  _tileLevel(tileLevel)
//  {
//  }
//
//  bool visitElement(const Sector&           sector,
//                    const QuadTree_Content* content) const {
//    GEORasterSymbol* symbol = (GEORasterSymbol*) content;
//
//    symbol->rasterize(_canvas, _projection, _tileLevel);
//
//    return false;
//  }
//
//  void endVisit(bool aborted) const {
//  }
//};

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

  const int                    _tileLevel;

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
  _tileLevel(trc->_tile->_level),
  _canvas(NULL),
  _projection(NULL)
  {
  }

  bool visitElement(const Sector&           sector,
                    const QuadTree_Content* content) const {
    GEORasterSymbol* symbol = (GEORasterSymbol*) content;

    if (_canvas == NULL) {
      const int width  = _originalImage->getWidth();
      const int height = _originalImage->getHeight();

      const Tile*   tile     = _trc->_tile;
      const bool    mercator = _trc->_mercator;

      _canvas = _geoTileRasterizer->getCanvas(width, height);

      _canvas->drawImage(_originalImage, 0, 0);

      _projection = new GEORasterProjection(tile->_sector, mercator,
                                            width, height);
    }

    symbol->rasterize(_canvas, _projection, _tileLevel);

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
  //  if (_quadTree.isEmpty()) {
  //    listener->imageCreated(image);
  //    if (autodelete) {
  //      delete listener;
  //    }
  //  }
  //  else {
  //    const Tile*   tile     = trc._tile;
  //    const bool    mercator = trc._mercator;
  //
  //    const int width  = image->getWidth();
  //    const int height = image->getHeight();
  //
  //    GEORasterProjection* projection = new GEORasterProjection(tile->_sector, mercator,
  //                                                              width, height);
  //
  //    ICanvas* canvas = getCanvas(width, height);
  //
  //    canvas->drawImage(image, 0, 0);
  //
  //    _quadTree.acceptVisitor(tile->_sector,
  //                            GEOTileRasterizer_QuadTreeVisitor(canvas, projection, tile->_level));
  //
  //    canvas->createImage(listener, autodelete);
  //
  //    delete image;
  //
  //    delete projection;
  //  }

  const Tile* tile = trc._tile;
  _quadTree.acceptVisitor(tile->_sector,
                          GEOTileRasterizer_QuadTreeVisitor(this,
                                                            image,
                                                            &trc,
                                                            listener,
                                                            autodelete));
}
